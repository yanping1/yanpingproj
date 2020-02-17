package com.dkha.common.signature;

import com.dkha.common.http.HttpUtil;
import com.dkha.common.validate.UtilValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author Spring
 * @Since 2019/9/26 22:14
 * @Description 接口签名校验
 */

public class SignatureUtil {

    public static final Logger logger = LoggerFactory.getLogger(SignatureUtil.class);

    public static final String HMAC_SHA1_ALGORITHM  = "HmacSHA1";

    /**
     * 校验签名
     * @param request
     * @param secretKey 秘钥
     * @param time 请求时间
     * @param signature 请求过来的签名
     * @return
     */
    public static boolean validateSignature(HttpServletRequest request, String secretKey, String time, String signature) {
        if (signature(request, secretKey, time).equals(signature)) {
            return true;
        }
        return false;
    }

    /**
     * 生成签名
     * @param request
     * @param secretKey 秘钥
     * @param time 请求时间
     * @return
     *
     * 签名生成算法如下
     * StringToSing = HTTP-Verb +”\n”+      //http请求的动作
     *
     *             Content-MD5+”\n”+     //http请求的MD5值
     *
     *             Content-Type+”\n”+     //http请求的类型
     *
     *             Date+”\n”+            //http请求时间
     *
     *             CanonicalizdResource；  //http请求资源--当前版本不适用该字段做为签名
     *
     * 签名生成算法
     * Signature = Base64( HMAC-SHA1( AccessSecret, UTF-8-Encoding-Of(StringToSign)) )
     *
     * https://help.aliyun.com/knowledge_detail/106305.html
     * https://blog.csdn.net/huanggang028/article/details/9004322
     */
    public static String signature(HttpServletRequest request, String secretKey, String time) {
        //获取请求方法
        String requestMethod = request.getMethod();
        String requestData = "";
        try {
            requestData = HttpUtil.getRequestPostBytes(request);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        String  contentType = request.getContentType();
        String waitSignStr = requestMethod + "\n" + (UtilValidate.isEmpty(requestData) ? "" : md5Encode(requestData) + "\n") + contentType + "\n" + time;
        try {
            waitSignStr = new String(waitSignStr.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        String cipherText = hamcsha1(waitSignStr, secretKey);
        cipherText = base64Encode(cipherText);
        return cipherText;
    }


    /**
     * HMAC_SHA1 签名
     * @param data 消息
     * @param secretKey 签名密钥
     * @return
     */
    public static String hamcsha1(String data, String secretKey) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5 加密
     * @param data
     * @return
     */
    public static String md5Encode(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes());
    }

    public static String base64Encode(String data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.getBytes());
    }

    /**
     * 字节数组转16进制
     * @param b 字节数组
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
