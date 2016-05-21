package com.freer.infusion.module.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.freer.infusion.R;
import com.freer.infusion.config.AppConfig;
import com.freer.infusion.entity.SocketEntity;
import com.freer.infusion.model.SocketDataModel;
import com.freer.infusion.model.SocketDataProcess;
import com.freer.infusion.util.JsonUtils;
import com.freer.infusion.util.NetUtils;
import com.freer.infusion.util.ScreenLockLocation;
import com.freer.infusion.util.ThreadManager;
import com.google.gson.FieldAttributes;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by 2172980000774 on 2016/5/12.
 */
public class SocketService extends Service {

    /* handler消息标志 */
    private static final int HANDLER_DEBUG = 0; //Debug数据
    private static final int HANDLER_SERVER = 1; //服务器数据
    private static final int HANDLER_LOCAL = 2; //本地数据
    private static final int HANDLER_ERROR = 3; //扫描服务器失败

    private static final String CHECK_CONN = "conn {\"cate\":8,\"d\":[]}\r\n"; //连接确认
    private static final String HEART_BEAT = "live {\"cate\":9,\"d\":[]}\r\n"; //心跳检测

    private static final long HEART_BEAT_RATE = 5 * 1000; //心跳检测间隔时间
    private static final long CONN_CHECK_RATE = 30 * 1000; //扫描服务器超时时间

    public static String HOST = null; //服务器IP地址
    public static int PORT = 2020; //服务器端口号

    public static final String MESSAGE_ACTION="message_action";
    public static final String HEART_BEAT_ACTION="heart_beat_action";
    public static final String CONN_CHECK_ACTION = "conn_check_action";
    public static final String CONN_ERROR_ACTION = "conn_error_action";

    private boolean mIsSend = true; //是否要继续发送心跳

    private ReadThread mReadThread;

    private LocalBroadcastManager mLocalBroadcastManager;

    private WeakReference<Socket> mSocket;

    private SocketDataProcess mSocketDataProcess = new SocketDataProcess();
    private SocketDataModel mFollowModel = new SocketDataModel();
    private SocketDataModel mAllModel = new SocketDataModel();

    private ScreenLockLocation mScreenLockLocation;

    /** For Heart Beat */
    private long sendTime = 0L;
    private Handler mHeartBeatHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (!mIsSend) {
                return;
            }
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = sendMsg(null);//就发送一个心跳数据过去 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHeartBeatHandler.removeCallbacks(heartBeatRunnable);
                    mReadThread.release();
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                }
            }
            mHeartBeatHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 更新主界面数据接口
     */
    public interface IReceiveMessage {
        void receiveMessage(List<SocketEntity> followBedList, List<SocketEntity> allBedList);
    }
    private IReceiveMessage mIReceiveMessage;

    public class SocketBinder extends Binder {
        public boolean sendMessage(SocketEntity message) {
            return sendMsg(message);
        }

        public void setOnReveiveMessage(IReceiveMessage iReceiveMessage) {
            mIReceiveMessage = iReceiveMessage;
        }

        public void startWork() {
            String ip = AppConfig.getInstance().getServerIp();
            if (ip != null && !ip.equals("")) {
                new Thread(new CheckConnRunnable(ip)).start();
            } else {
                scanIpSegment();
            }

        }

        public void setIpPort(String ip, int port) {
            HOST = ip;
            PORT = port;
//            new InitSocketThread().start();
            scanIpSegment();
        }
    }
    public SocketBinder mSocketBinder;

    @Override
    public IBinder onBind(Intent arg0) {
        return mSocketBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mScreenLockLocation = new ScreenLockLocation(this);
        mScreenLockLocation.start();

        startForeground(1,
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Infusion")
                        .setContentText("infusion")
                        .setOngoing(true)
                        .build());

        mSocketBinder = new SocketBinder();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SocketService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("SocketService", "onDestroy");
        stopForeground(true);
        stopWork();
        mScreenLockLocation.stop();
        super.onDestroy();
    }

    public boolean sendMsg(SocketEntity msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                // 如果为null，说明是心跳检测，否则就是本地主动设置的数据
                String message =
                        msg == null ?
                                HEART_BEAT : "{\"cate\":0,\"d\":["+msg.toString()+"]}\r\n";
                os.write(message.getBytes());
                os.flush();
                sendTime = System.currentTimeMillis(); //每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (msg != null) { //确认是本地主动修改发送给服务器的数据
            Message message = new Message();
            message.what = HANDLER_LOCAL;
            message.obj = msg.toString();
            mMsgHandler.sendMessage(message);
        }
        return true;
    }

    private void initSocket() { // 初始化Socket
        try {
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<Socket>(so);
            mReadThread = new ReadThread(so);
            mReadThread.start();
            mHeartBeatHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在遍历ip之后，第一次连接调用
     * @param socket
     */
    private void initSocket(Socket socket) {
        mSocket = new WeakReference<Socket>(socket);
        mReadThread = new ReadThread(socket);
        mReadThread.start();
        mHeartBeatHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE); // 初始化成功后，就准备发送心跳包
    }

    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk != null && !sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }

    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<Socket> weakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            weakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(weakSocket);
        }

        @Override
        public void run() {
            super.run();
            Socket socket = weakSocket.get();
            receiveMsg(socket, isStart);
        }
    }

    public void receiveMsg(Socket socket, boolean isStart) {
        if (null != socket) {
            try {
                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[1024 * 4];
                int length = 0;
                while (!socket.isClosed() && !socket.isInputShutdown()
                        && isStart && ((length = is.read(buffer)) != -1)) {
                    if (length > 0) {
                        String message = new String(Arrays.copyOf(buffer,
                                length)).trim();
                        System.out.println("收到Socket服务器回复");
                        //收到服务器过来的消息，就通过Broadcast发送出去
                        if(message.contains("\"cate\":9,\"d\":")){//处理心跳回复
                            System.out.println("当前回复为心跳检测");
//                            Intent intent=new Intent(HEART_BEAT_ACTION);
//                            mLocalBroadcastManager.sendBroadcast(intent);
                        }else{
                            System.out.println("当前回复为其他回复");
                            //其他消息回复
                            Message msg = new Message();
                            msg.what = HANDLER_SERVER;
                            msg.obj = message;
                            mMsgHandler.sendMessage(msg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过回调的方式通知activity
     * 通过handler从子线程传递数据到主线程中
     */
    private Handler mMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_SERVER) { //收到服务器传递的数据
                mSocketDataProcess.processData((String) msg.obj);
                mFollowModel.setData(mSocketDataProcess.getFollowBed());
                mAllModel.setData(mSocketDataProcess.getAllBed());
                //考虑一下，因为ReceiveMessage需要在成功绑定之后才能获取实例
                //那么，如果在成功绑定之前，当前service就已经运行到这里
                //就会出现数据丢失的情况
                if (mIReceiveMessage != null) {
                    mIReceiveMessage.receiveMessage(mFollowModel.getData(), mAllModel.getData());
                }
            } else if (msg.what == HANDLER_LOCAL) { //收到本地需要修改的数据
                SocketEntity socketEntity = JsonUtils.fromJson((String) msg.obj, SocketEntity.class);
                mFollowModel.setDataNoAdd(socketEntity);
                mAllModel.setDataNoAdd(socketEntity);
                //考虑一下，因为ReceiveMessage需要在成功绑定之后才能获取实例
                //那么，如果在成功绑定之前，当前service就已经运行到这里
                //就会出现数据丢失的情况
                if (mIReceiveMessage != null) {
                    mIReceiveMessage.receiveMessage(mFollowModel.getData(), mAllModel.getData());
                }
            } else if (msg.what == HANDLER_DEBUG) {
                System.out.println(msg.obj.toString());
            } else if (msg.what == HANDLER_ERROR) {
                if (HOST == null) { //在扫描开始30秒后，结束到消息，判断此时是否有扫描结果
                    Intent intent=new Intent(CONN_ERROR_ACTION);
                    mLocalBroadcastManager.sendBroadcast(intent);
                }
            }

        }
    };

    /**
     * 跟服务器确认首次连接
     * @param ip
     * @return
     */
    public Socket checkConn(String ip) {
        if (AppConfig.getInstance().isDebug()) {
            Message msg = new Message();
            msg.what = HANDLER_DEBUG;
            msg.obj = "尝试跟服务器socket通信" + ip;
            mMsgHandler.sendMessage(msg);
        }
        Socket socket = null;
        try {
            socket = new Socket();
            socket.setSoTimeout(300);
            socket.connect(new InetSocketAddress(ip, PORT), 300);
            //获取输出流
            OutputStream os = socket.getOutputStream();
            //获取输入流
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024 * 4];
            int length = 0;

            //发送连接确认消息
            if (!socket.isClosed() && !socket.isOutputShutdown()) {
                String message = CHECK_CONN;
                os.write(message.getBytes());
                os.flush(); //刷新输出流，使Server马上收到该字符串
            }
            //接受连接确认消息
            if (!socket.isClosed() && !socket.isInputShutdown()
                    && ((length = is.read(buffer)) != -1)) {
                if (length > 0) {
                    String message = new String(Arrays.copyOf(buffer, length)).trim();
                    if (message.contains("{\"cate\":8,\"d\":")) {
                        return socket;
                    }
                }
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (SocketTimeoutException e) {
            System.out.println("socket服务器响应超时--->"+ip);
        } catch (ConnectException e) {
            System.out.println("socket服务器请求超时--->"+ip);
        } catch (InterruptedIOException e) {
            System.out.println("socket连接超时--->"+ip);
        } catch (UnknownHostException e) {
            System.out.println("socket连接错误，尝试连接一个不存在的端口--->"+ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *跟服务器确认首次连接
     */
    class CheckConnRunnable implements Runnable {

        private String currentIp = null;
        private int ipSuff = 0; //ip后缀

        public CheckConnRunnable(String ip) {
            this(ip, 0);
        }

        public CheckConnRunnable(String ip, int ipSuff) {
            this.currentIp = ip;
            this.ipSuff = ipSuff;
        }

        @Override
        public void run() {
            System.out.println("建立线程尝试连接" + currentIp);
            if (ipSuff == 1) { // 在ip后缀为1，即扫描开始后30秒，向消息队列发送一次消息
                Message msgarg = new Message();
                msgarg.what = HANDLER_ERROR;
                mMsgHandler.sendMessageDelayed(msgarg, CONN_CHECK_RATE);
            }
            try {
                Socket socket = null;
                //向服务器发送验证信息,如果验证通过...
                if ((socket = checkConn(currentIp)) != null) {
                    if (AppConfig.getInstance().isDebug()) {
                        Message msg = new Message();
                        msg.what = HANDLER_DEBUG;
                        msg.obj = "Socket通信成功"+currentIp;
                        mMsgHandler.sendMessage(msg);
                    }
                    Intent intent = new Intent(CONN_CHECK_ACTION);
                    mLocalBroadcastManager.sendBroadcast(intent);
                    AppConfig.getInstance().setServerIp(currentIp);
                    socket.setSoTimeout(0);
                    HOST = currentIp;
                    initSocket(socket);
                } else {
                    if (AppConfig.getInstance().isDebug()) {
                        Message msg = new Message();
                        msg.what = HANDLER_DEBUG;
                        msg.obj = "Socket通信失败"+currentIp;
                        mMsgHandler.sendMessage(msg);
                    }
                    if (ipSuff == 0) { //连接本地保存的服务器地址失败，开始遍历IP进行扫描
                        scanIpSegment();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 扫描局域网IP段
     */
    public void scanIpSegment(){
        System.out.println("开始遍历局域网Ip段");
        String locAddrPref = NetUtils.getLocAddrIndex(); //获取本地ip前缀，比如192.168.1.
        if(locAddrPref == null || locAddrPref.equals("")) {
            return;
        }
        for (int suff = 1; suff < 256; suff++) {//创建256个线程分别去ping
            System.out.println("向线程池添加线程" + locAddrPref + suff);
            ThreadManager.getInstance().addTask(
                    new CheckConnRunnable(locAddrPref+String.valueOf(suff), suff));
        }
    }

    public void stopWork() {
        mIsSend = false;
        ThreadManager.getInstance().closeThreadPool(); // 关闭线程池
        if (mReadThread != null) {
            mReadThread.release();
        }
        releaseLastSocket(mSocket);
    }
}