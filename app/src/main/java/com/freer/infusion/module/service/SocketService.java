package com.freer.infusion.module.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.freer.infusion.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by 2172980000774 on 2016/5/12.
 */
public class SocketService extends Service {

//    private static final String CHECK_CONN = "{\"cate\":0,\"d\":[{\"LogId\":0,\"RFId\":\"03\",\"UxId\":\"04\",\"UxName\":\"0304\",\"BedId\":\"28\",\"\n" +
//            "CurrSpeed\":0,\"TopLimitSpeed\":0,\"LowLimitSpeed\":0,\"DropAmount\":0,\"WorkingState\":7\n" +
//            ",\"WarnProcess\":10,\"RealProcess\":0,\"ClientAction\":0,\"State\":1,\"Key\":null,\"LastRea\n" +
//            "lProcess\":0,\"StopWarningCount\":0,\"LimitWarningCount\":0,\"SettingSpeed\":0,\"Battery\n" +
//            "Capacity\":0,\"WarningMSG\":null,\"TempBedID\":\"\",\"IsWorkingStartUx\":0}]}";
    private static final String CHECK_CONN = "{\"cate\":8,\"d\":[]}";
    private static final String HEART_BEAT = "{\"cate\":9,\"d\":[]}";

    private static final long HEART_BEAT_RATE = 5 * 1000;

    public static String HOST = "192.168.1.113";
    public static int PORT = 2020;

    public static final String MESSAGE_ACTION="org.feng.message_ACTION";
    public static final String HEART_BEAT_ACTION="org.feng.heart_beat_ACTION";

    private ReadThread mReadThread;

    private LocalBroadcastManager mLocalBroadcastManager;

    private WeakReference<Socket> mSocket;

    private String locAddress;//存储本机ip，例：本地ip ：192.168.1.

    private Runtime run = Runtime.getRuntime();//获取当前运行环境，来执行ping，相当于windows的cmd

    private Process proc = null; //命令行进程

    private String ping = "ping -c 1 -w 0.5 " ;//其中 -c 1为发送的次数，-w 表示发送后等待响应的时间

    private int j;//存放ip最后一位地址 0-255

    // For heart Beat
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = sendMsg(HEART_BEAT);//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHandler.removeCallbacks(heartBeatRunnable);
                    mReadThread.release();
                    releaseLastSocket(mSocket);
                    new InitSocketThread().start();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    private long sendTime = 0L;

    public interface IReceiveMessage {
        public void receiveMessage(String message);
    }
    private IReceiveMessage mIReceiveMessage;

    public class SocketBinder extends Binder {
        public boolean sendMessage(String message) {
            return sendMsg(message);
        }

        public void setOnReveiveMessage(IReceiveMessage iReceiveMessage) {
            mIReceiveMessage = iReceiveMessage;
        }

        public void setIpPort(String ip, int port) {
            HOST = ip;
            PORT = port;
            new InitSocketThread().start();
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

        NotificationManagerCompat.from(this).notify(1,
                new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Infusion")
                    .setContentText("infusion")
                    .setOngoing(true)
                    .build());

        mSocketBinder = new SocketBinder();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
//        new InitSocketThread().start();
//        scanIpSegment();
    }

    public boolean sendMsg(String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        Socket soc = mSocket.get();
        try {
            if (!soc.isClosed() && !soc.isOutputShutdown()) {
                OutputStream os = soc.getOutputStream();
                String message = msg + "\r\n";
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
        return true;
    }

    private void initSocket() {//初始化Socket
        try {
            Socket so = new Socket(HOST, PORT);
            mSocket = new WeakReference<Socket>(so);
            mReadThread = new ReadThread(so);
            mReadThread.start();
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
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
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
    }

    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (!sk.isClosed()) {
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
                        //收到服务器过来的消息，就通过Broadcast发送出去
                        if(message.contains("\"cate\":9,")){//处理心跳回复
                            Intent intent=new Intent(HEART_BEAT_ACTION);
                            mLocalBroadcastManager.sendBroadcast(intent);
                        }else{
                            //其他消息回复
                            Intent intent=new Intent(MESSAGE_ACTION);
                            intent.putExtra("message", message);
                            mLocalBroadcastManager.sendBroadcast(intent);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跟服务器确认首次连接
     * @param ip
     * @return
     */
    public boolean checkConn(String ip, Socket socket) {

        try {
            socket = new Socket(ip, PORT);

            //发送连接确认消息
            if (!socket.isClosed() && !socket.isOutputShutdown()) {
                OutputStream os = socket.getOutputStream();
                String message = CHECK_CONN;
                os.write(message.getBytes());
                os.flush(); //刷新输出流，使Server马上收到该字符串
            }

            //接受连接确认消息
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024 * 4];
            int length = 0;
            if (!socket.isClosed() && !socket.isInputShutdown()
                    && ((length = is.read(buffer)) != -1)) {
                if (length > 0) {
                    String message = new String(Arrays.copyOf(buffer, length)).trim();
                    if (message.contains("cate") && message.contains("8")) {
                        return true;
                    }
                }
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void scanIpSegment(){

        locAddress = getLocAddrIndex();//获取本地ip前缀

        if(locAddress.equals("")){
            return ;
        }

        for ( int i = 0; i < 256; i++) {//创建256个线程分别去ping

            j = i ;

            new Thread(new Runnable() {

                public void run() {

                    String p = SocketService.this.ping + locAddress + SocketService.this.j ;

                    String currentIp = locAddress+ SocketService.this.j;

                    System.out.println("建立线程尝试连接" + currentIp);

                    try {
                        proc = run.exec(p);

                        int result = proc.waitFor();
                        if (result == 0) {
                            System.out.println("ping连接成功" + currentIp);

                            Socket socket = null;

                            //向服务器发送验证信息,如果验证通过...
                            if (checkConn(currentIp, socket)) {
                                HOST = currentIp;
                                initSocket(socket);
                            }

                        } else {

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        proc.destroy();
                    }
                }
            }).start();
        }
    }

    //获取IP前缀
    public String getLocAddrIndex(){

        String str = getLocIpAddress();

        if(!str.equals("")){
            return str.substring(0, str.lastIndexOf(".")+1);
        }
        return null;
    }

    //获取本地ip地址
    public String getLocIpAddress(){

        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = (NetworkInterface) en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = (InetAddress) address.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ScanIpAddress", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return "";
    }
}
