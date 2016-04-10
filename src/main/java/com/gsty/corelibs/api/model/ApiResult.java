package com.gsty.corelibs.api.model;

/**
 * 非列表数据接口的model
 */
public class ApiResult<T> {

    private int errcode;
    private String errmsg;
    private String apiName;
    /**
     * 泛型，根据的接口返回不同对象
     */
    private T extraMap;


    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public T getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(T extraMap) {
        this.extraMap = extraMap;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


}
