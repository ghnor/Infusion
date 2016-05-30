package com.freer.infusion.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.freer.infusion.R;
import com.freer.infusion.config.AppConfig;
import com.freer.infusion.entity.SocketEntity;

/**
 * Created by 2172980000774 on 2016/5/11.
 */
public class DialogManger {

    /**
     * 主界面,床位信息设置弹窗
     */
    public interface OnMainPopupOkListener {
        public void onMessage(SocketEntity data);
    }

    public static PopupWindow getMainPopupWindow(Context context, final SocketEntity data,
                                                 final OnMainPopupOkListener onMainPopupOkListener) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup contentView = (ViewGroup) layoutInflater.inflate(R.layout.popupwindow_main, null);
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

        final AppCompatEditText deviceNum = (AppCompatEditText) contentView.findViewById(R.id.device_number);
        final AppCompatEditText bedNum = (AppCompatEditText) contentView.findViewById(R.id.bed_number);
        final AppCompatEditText lowerSpeed = (AppCompatEditText) contentView.findViewById(R.id.lower_speed);
        final AppCompatEditText upperSpeed = (AppCompatEditText) contentView.findViewById(R.id.upper_speed);
        final AppCompatEditText amount = (AppCompatEditText) contentView.findViewById(R.id.amount);
        deviceNum.setFocusable(false);
        deviceNum.setBackgroundDrawable(null);
        if (AppConfig.getInstance().getMode() == 0) {
            bedNum.setFocusable(false);
            bedNum.setBackgroundDrawable(null);
        }

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

                data.BedId = nBedNum;
                data.LowLimitSpeed = nLowerSpeed;
                data.TopLimitSpeed = nUpperSpeed;
                data.ClientAction = nAmount;

                onMainPopupOkListener.onMessage(data);
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
                .setCancelable(false)
                .setMessage(context.getResources().getString(R.string.app_quit))
                .setNegativeButton(context.getResources().getString(R.string.app_cancel), null)
                .setPositiveButton(context.getResources().getString(R.string.app_ok), onClickListener)
                .create();
    }

    /**
     * 重试连接对话框
     * @param context
     * @param onRetryListener
     * @param onQuitListener
     * @return
     */
    public static AlertDialog getRetryDialog(Context context,
                                             DialogInterface.OnClickListener onRetryListener,
                                             DialogInterface.OnClickListener onQuitListener) {
        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(context.getResources().getString(R.string.app_retry_title))
                .setNegativeButton(context.getResources().getString(R.string.app_retry_action), onRetryListener)
                .setPositiveButton(context.getResources().getString(R.string.app_ok), onQuitListener)
                .create();
    }

    /**
     * 进度条对话框
     * @param context
     * @return
     */
    public static Dialog getProgressDialog(Context context) {

        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(R.layout.dialog_progress)
                .create();
    }

}