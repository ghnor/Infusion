package com.freer.infusion.entity;

import java.util.List;

/**
 * Created by 2172980000774 on 2016/5/12.
 */
public class DataEntity {

    public int cate;
    public List<SocketEntity> d;

    @Override
    public String toString() {
        return "{" +
                "cate=" + cate +
                ", d=" + d +
                '}';
    }
}
