package com.freer.infusion.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.freer.infusion.R;
import com.freer.infusion.entity.SocketEntity;

/**
 * Created by 2172980000774 on 2016/5/11.
 */
public class DialogManger {

    /**
     * 主界面,床位信息设置弹窗
     */
    public interface OnMainPopupOkListener {
        public void onMessage(int deviceNum, int bedNum, int lowerSpeed, int upperSpeed, int amount);
    }

    public static PopupWindow getMainPopupWindow(Context context, SocketEntity data,
                                                 final OnMainPopupOkListener onMainPopupOkListener) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contentView = layoutInflater.inflate(R.layout.popupwindow_main, null);
        View parentView = new View(context);
        final PopupWindow mainPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 实例化一个ColorDrawable做背景
        ColorDrawable dw = new ColorDrawable(ContextCompat.getColor(context, R.color.white));
        mainPopupWindow.setBackgroundDrawable(dw);
        //必须设置不然不能点击
        mainPopupWindow.setFocusable(true);
        //设置点击外部可以关闭popupWindows
        mainPopupWindow.setOutsideTouchable(true);
        mainPopupWindow.setAnimationStyle(R.style.SlideBottom);
        mainPopupWindow.showAtLocation(parentView,
                Gravity.BOTTOM, 0, 0);

        // 设置窗口背景变暗
        final Window window = ((AppCompatActivity) context).getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        window.setAttributes(lp);
        mainPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                window.setAttributes(lp);
            }
        });

        final EditText deviceNum = (EditText) contentView.findViewById(R.id.device_number);
        final EditText bedNum = (EditText) contentView.findViewById(R.id.bed_number);
        final EditText lowerSpeed = (EditText) contentView.findViewById(R.id.lower_speed);
        final EditText upperSpeed = (EditText) contentView.findViewById(R.id.upper_speed);
        final EditText amount = (EditText) contentView.findViewById(R.id.amount);

        if (data != null) {
            deviceNum.setText(data.UxName);
            bedNum.setText(String.valueOf(data.BedId));
            lowerSpeed.setText(String.valueOf(data.LowLimitSpeed));
            upperSpeed.setText(String.valueOf(data.TopLimitSpeed));
            amount.setText(String.valueOf(data.ClientAction));
        }

        contentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPopupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nDeviceNum = Integer.valueOf(deviceNum.getText().toString());
                int nBedNum = Integer.valueOf(bedNum.getText().toString());
                int nLowerSpeed = Integer.valueOf(lowerSpeed.getText().toString());
                int nUpperSpeed = Integer.valueOf(upperSpeed.getText().toString());
                int nAmount = Integer.valueOf(amount.getText().toString());
                onMainPopupOkListener.onMessage(nDeviceNum,
                        nBedNum,
                        nLowerSpeed,
                        nUpperSpeed,
                        nAmount);
            }
        });

        return mainPopupWindow;
    }

    /**
     * 确认退出APP对话框
     * @param context
     * @param onClickListener
     * @return
     */
    public static AlertDialog getQuitDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        return new AlertDialog.Builder(context)
//                .setTitle(context.getResources().getString(R.string.app_quit))
                .setMessage(context.getResources().getString(R.string.app_quit))
                .setNegativeButton(context.getResources().getString(R.string.app_cancel), null)
                .setPositiveButton(context.getResources().getString(R.string.app_ok), onClickListener)
                .create();
    }



}
