package com.sk.dididemo.bean;

/**
 * @desc 优惠券
 */
public class Coupon {
    public int businessType; //业务类型
    public int couponId; //优惠券模版
    public String description;//优惠卷描述
    public String expireTime;//过期时间
    public int faceValue;//面值。
    public String getTime;//有效时间
    public int id; //用户优惠券ID
    public String name;//代金券
    public int ruleId; //发放规则ID
    public int serviceType; //服务类型
    public int threshold; //门槛限制
    public int thresholdValue;//代金券
    public int type;//优惠券类型
    public Double discount; //折扣
    public String useCity;//优惠券可使用城市
    public int userId; //用户ID
    public int status; //该券是否可用 1可用 0无可用 2使用中（使用中表示 该订单中已绑定的优惠券）


}
