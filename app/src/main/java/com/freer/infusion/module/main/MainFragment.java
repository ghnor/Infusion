package com.freer.infusion.module.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.freer.infusion.R;
import com.freer.infusion.base.BaseFragment;
import com.freer.infusion.entity.SocketEntity;
import com.freer.infusion.module.service.SocketService;
import com.freer.infusion.util.DialogManger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/5/15.
 */
public class MainFragment extends BaseFragment {

    private MainListAdapter mMainListAdapter;

    private SocketService.SocketBinder mSocketBinder = null;

    private PopupWindow mPopupWindow;

    private Dialog mProgressDialog;

    public void setData(List<SocketEntity> dataList) {
        if (mMainListAdapter == null) {
            mMainListAdapter = new MainListAdapter(getActivity());
        }
        mMainListAdapter.setData(dataList);
    }

    public void setBinder(SocketService.SocketBinder socketBinder) {
        this.mSocketBinder = socketBinder;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListViewCompat listViewCompat = (ListViewCompat) rootView.findViewById(R.id.list);
        listViewCompat.setAdapter(mMainListAdapter = new MainListAdapter(getActivity()));

        mProgressDialog = DialogManger.getProgressDialog(getActivity());

        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow = DialogManger.getMainPopupWindow(getActivity(),
                        (SocketEntity) mMainListAdapter.getItem(position),
                        new DialogManger.OnMainPopupOkListener() {
                            @Override
                            public void onMessage(SocketEntity data) {
                                if (mSocketBinder == null) {
                                    return;
                                }
                                mProgressDialog.show();
                                if (mSocketBinder.sendMessage(data)) {
                                    mProgressDialog.dismiss();
                                    mPopupWindow.dismiss();
                                    mPopupWindow = null;
                                }
                            }
                        });
            }
        });

        return rootView;
    }
}
