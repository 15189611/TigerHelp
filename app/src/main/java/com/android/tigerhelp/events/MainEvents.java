package com.android.tigerhelp.events;

/**
 * Created by huangTing on 2016/10/10.
 */
public class MainEvents {
    /***
     * 选择地址
     */
    public static final int SELECT_ADDRESS = 1;
    /***
     * 数据
     */
    private Object obj;
    /***
     * 类别
     */
    private int type;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
