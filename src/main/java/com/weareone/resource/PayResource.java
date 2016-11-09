package com.weareone.resource;

import com.alibaba.fastjson.JSONObject;
import com.weareone.common.HttpUtils;
import com.weareone.pojo.PayResponse;
import com.weareone.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wang.linqiao on 2016/11/9.
 */
@RestController
@RequestMapping("/pay")
@Api(value = "/pay", description = "App支付相关接口")
public class PayResource {

    @Autowired
    private PayService payService;

    /**
     * 请求生成微信的预支付单
     *
     * @param device_info
     * @param order_no
     * @return
     */
    @RequestMapping("/winxi/{device_info}/{body}/{order_no}/{attach}/{goods_tag}")
    @ApiOperation(value = "请求生成微信的预支付单", notes = "", httpMethod = "POST", response = Map.class)
    public String pay2winxi(@ApiParam(value = "设备号",required = false) @PathVariable("device_info") String device_info, @ApiParam("订单号") @PathVariable("order_no") String order_no,
                            @ApiParam("附加说明") @PathVariable("attach") String attach, @ApiParam("商品标记，代金券或立减优惠功能的参数") @PathVariable("goods_tag") String goods_tag, @ApiIgnore HttpServletRequest request) {
        PayResponse result = payService.pay2winxi(device_info, order_no, attach, goods_tag, HttpUtils.getIpAddr(request));
        return JSONObject.toJSONString(result);
    }
}
