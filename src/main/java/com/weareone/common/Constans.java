package com.weareone.common;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化静态值表
 * Created by wang.linqiao on 2016/11/9.
 */
@Component
public class Constans {
    public static Map<String,String> WEI_XI_MAP = new HashMap<>();

    public static void init(){
        WEI_XI_MAP.put("appid","*****");
        WEI_XI_MAP.put("mch_id","*****");
        WEI_XI_MAP.put("body","大表哥游戏充值");
        WEI_XI_MAP.put("sign_type","MD5");
        WEI_XI_MAP.put("fee_type","CNY");
        WEI_XI_MAP.put("notify_url","http://localhost/pay/winxi/rollback");
        WEI_XI_MAP.put("trade_type","APP");
        WEI_XI_MAP.put("prepay_url","https://api.mch.weixin.qq.com/pay/unifiedorder");
    }
}
