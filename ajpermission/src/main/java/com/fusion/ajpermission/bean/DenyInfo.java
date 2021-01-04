package com.fusion.ajpermission.bean;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description 被取消授权的信息
 */

public class DenyInfo {

    private int requestCode;
    private final List<String> deniedList;

    public DenyInfo(int requestCode, List<String> list) {
        this.requestCode = requestCode;
        deniedList = list;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
