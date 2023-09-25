package com.atguigu.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public String app_id = "9021000128623979";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCq1V+85ok7KjMTLU7fZjHMBAJ/+SiVci9uWUithYFUzzhdP7oKHJs1lIRgpgMTe+1jr0xrflkP1QIA0m+is709EK9PQXZlZ95BWldxV4tLuKernhb+TBNaqG0qPAMWQHsqVP9S9xIhDkjYy4q38k77e5CtMDdA3oCJJ9PsI1DVH1iC/eWG6VEYuW2gEW8+c9L2khNVw/7wJCzI2ZiAc1AzoQTzUe5jQ5CkhMQU9Lt58jNtECndBHZA68npG1QQDvXTMAiyRImvutcH+tnnhM6wvfutCIpDKBSPoaUlLHnx2QT8Ta5pETacKjrUSK+ToHeaiDQJj8Vq4y09VwUiy/oxAgMBAAECggEAOx83T+/VBIXZQUTH7Ca5CytEFjgLpJTs91qs/Wib1tcUYalBYGiACq4Ilk49+EtGX952KymB9ym31ML/Yaak0nJoyZwVNokY8TMr2gz7J0yTsqA2wvFfLVvm09wLEXNlbMQXzQW233vLGERxELIqRXyBGH78UFXw1AbVn/6k5eyt4tPya57uyPMd5Tc9Enzs60zACj14RqW5u2AWSe6qTAHW/wupnwhEGQdKWKP8Pa8BeUnissQHhLrD5tt3c5rWotQh0np7iKiwx7U7vApCTtQmqZbJa4eDotHp+Vnt9I7YKJAF5SyERrDKDIC6iDNvWPEChTCDBFrYRalwwF5UAQKBgQDym5Y7yeQiOBQVOoK49Isbhj88zpmFz016AE4I2x2X3h3hDYxXfloNDzWn90avaEeaFdB86bIttswSP/myt/9FRRxcLEolzuYp6T70oVVsuCAZ08iOaMjIl4k/SfJMs2guSts8hMLTrnJq0jzH3Z2ZU+5STCDDZeHrRLT4ABFTcQKBgQC0Q4IyKF3iBproFmZ+SSoEt9pYfIQ7kVjmlASBQTVkPQphewji2+gLk6U8jjUTGsLL5zXJ0LIvhN9LpgX2XjWcd8mlhL6Ox0kqCjPqllWBlcKawT+tz68pXAYmLvpM3VGN6Pz8xqDJoGD++yn5JnDxqZ4kn9a7n8jq1HvrUnAywQKBgByU+fEXSvpBExShLQ9vOAhJG6AlUu7xylXqPUdL0UO4FlsSRot0RlWtigu81SYLKgxcL5DX03cRRx+LS/ajOL7HcpzlVuL3aiRb51/YtgC1/Iouaj1k2OrQ94luQ86HgGmbxhJuLanUroQpOqHtWQjcW4J0l5ONqD0Yfh2NnHzxAoGBAI7yYdJolFpipoCZEtneJwXqzakRxMJ/9iGTCyd8kFxW016N9HQBsrdLc+LbWlc2B3cueEb7yfFIytHwbBX8i0Ly8uLgq0QJ4IkH4jeK02+ZVOLnkBnC8cI4iV/KLQ67CqsgMU46LNL27TQF9DjxNOtOeGvOnLfYc7lUmpf3naWBAoGAQQ4eckzNqD9zdooN0gxxwmuUgmcf0B7icqcM4NIu7QE9etNfbaY5WBR8pk3Yp/+uf9MfT2moi8rzGRfbPNBZPR/UDZLau5DV7cOA+IITY4n3F3e4HOA0Wt1tG6LOKbO1hGZ3E8ha9RfZM41ai09BvRjY2EtQ9p2xhE5jGDFUASk=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgD88n5EdA3IFjdfPOTw4RXjB08MHllXAct6/wyeughysJ2tTRONkPuL4rJOfm7I2/PBkl5FkwDqdbQ5t/cHIoJygquOj8R6puHZ05Jw2adBZCSpQcqxknA06FDDdmVGT6MBlK0vqslfOkmv2EzErGrSy4VwvSPU7oO0fejUuHbdoaUz3oAYDpaC8FZjLs1nHrQUNVIcz+YthayD4RtVqscU8b3gNSyL1La1jQwubz+Vrhtb9vPNqVDtMVISSF27llnZfvV8/i8IeNR2P16mII6OySV42SjMCcZfg0AhbLRUf2IMZQBpjH00S0BhG9ORwcG+QjU3cqAEq9SFtjVIDKwIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    public String notify_url="http://13183857754.gnway.cc/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    public String return_url = "http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "UTF-8";

    //订单超时时间
    private String timeout = "5m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    public String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
