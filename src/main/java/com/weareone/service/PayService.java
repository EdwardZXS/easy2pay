package com.weareone.service;

import com.alibaba.fastjson.JSONObject;
import com.weareone.common.Constans;
import com.weareone.common.HttpUtils;
import com.weareone.common.XMLUtils;
import com.weareone.pojo.PayResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wang.linqiao on 2016/11/9.
 */
@Service
public class PayService {
    private Logger logger = LoggerFactory.getLogger(PayService.class);

    //此处配好数据源后，加事务
    public PayResponse pay2winxi(String device_info, String order_no, String attach, String goods_tag, String spbill_create_ip) {

        Map<String, Object> param = new TreeMap<>();
        try {

            String appid = Constans.WEI_XI_MAP.get("appid");
            if (StringUtils.isEmpty(appid)) {
                logger.error("应用ID为空，无法生成！");
                return PayResponse.Error();
            }
            param.put("appid", appid);

            String mch_id = Constans.WEI_XI_MAP.get("mch_id");
            if (StringUtils.isEmpty(mch_id)) {
                logger.error("商户号为空，无法生成！");
                return PayResponse.Error();
            }
            param.put("mch_id", appid);

            if (StringUtils.isNotEmpty(device_info)) {
                param.put("device_info", device_info);
            }

            try {
                //生成随机串
                String randStr = UUID.randomUUID().toString().replace("-", "");
                param.put("nonce_str", randStr);
            } catch (Exception e) {
                logger.error("随机数处理失败", e);
                return PayResponse.Error();
            }

            if (StringUtils.isNotEmpty(Constans.WEI_XI_MAP.get("sign_type"))) {
                param.put("sign_type", Constans.WEI_XI_MAP.get("sign_type"));
            }

            String body = Constans.WEI_XI_MAP.get("body");
            if (StringUtils.isEmpty(body)) {
                logger.error("商品描述为空，无法生成！");
                return PayResponse.Error();
            }

            if (StringUtils.isEmpty(order_no)) {
                logger.error("商品订单号为空，无法生成！");
                return PayResponse.Error();
            }
            param.put("out_trade_no", order_no);

            //根据订单号查询出该订单的商品详情，也可以由前台传过来，自行选择
            List prodoucts = new ArrayList();
            //products = orderMapper.queryById(order_no).getProducts
            if (prodoucts != null && !prodoucts.isEmpty()) {
                param.put("detail", JSONObject.toJSONString(prodoucts));
            }

            if (StringUtils.isNotEmpty(attach)) {
                param.put("attach", attach);
            }

            String fee_type = Constans.WEI_XI_MAP.get("fee_type");
            if (StringUtils.isNotEmpty(fee_type)) {
                param.put("fee_type", fee_type);
            }

            //根据订单号可得到总金额
            param.put("total_fee", "999");

            //获取终端ip
            param.put("spbill_create_ip", spbill_create_ip);

            //根据订单号获取订单相关时间
            String time_start = "20091225091010";
            String time_expire = "20091227091010";
            if (StringUtils.isNotEmpty(time_start)) {
                param.put("time_start", time_start);
            }
            if (StringUtils.isNotEmpty(time_expire)) {
                param.put("time_expire", time_expire);
            }

            if (StringUtils.isNotEmpty(goods_tag)) {
                param.put("goods_tag", goods_tag);
            }

            String notify_url = Constans.WEI_XI_MAP.get("notify_url");
            if (StringUtils.isEmpty(notify_url)) {
                logger.error("回调地址为空，无法生成！");
                return PayResponse.Error();
            }
            param.put("notify_url", notify_url);

            String trade_type = Constans.WEI_XI_MAP.get("trade_type");
            if (StringUtils.isEmpty(trade_type)) {
                logger.error("交易类型为空，无法生成！");
                return PayResponse.Error();
            }
            param.put("trade_type", trade_type);

            //生成签名
            StringBuilder sBuilder = new StringBuilder();
            for (Map.Entry entry : param.entrySet()) {
                sBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            String stringSignTemp = sBuilder.substring(0, sBuilder.length() - 1);
            String sign = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
            if (StringUtils.isEmpty(sign)) {
                logger.error("生成签名失败！");
                return PayResponse.Error();
            }
            param.put("sign",sign);

            //发送请求
            Map<String, String> map = HttpUtils.httpsRequestToXML(Constans.WEI_XI_MAP.get("prepay_url"), "POST", XMLUtils.map2xmlBody(param,"xml").replace("__", "_").replace("<![CDATA[", "").replace("]]>", ""));

            //从结果中取出预支付单号
            String return_code = map.get("return_code");
            if(StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")){
                String return_msg = map.get("return_msg");
                if(StringUtils.isNotBlank(return_msg) && !return_msg.equals("OK")) {
                    return PayResponse.Error();
                }
            }else{
                return PayResponse.Error();
            }
            String prepay_Id = map.get("prepay_id");
            return PayResponse.OK(prepay_Id);

        } catch (Exception e) {
            logger.error("生成预支付单失败", e);
            return PayResponse.Error();
        }
    }
}
