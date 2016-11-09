package com.weareone.pojo;

/**
 * Created by wang.linqiao on 2016/11/9.
 */
public class PayResponse {
    private String statusCode; //200 成功  501 失败
    private Object data;  //返回的数据

    public PayResponse(String statusCode, Object data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static PayResponse Error(){
        return new PayResponse("501","生成微信预支单失败！");
    }
    public static PayResponse OK(Object data){
        return new PayResponse("200",data);
    }
}
