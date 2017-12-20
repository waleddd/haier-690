package com.dituhui.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by by on 2017/12/19.
 */
public class SaasSignUtils {

    private static String SIGN_PARAM_NAME = "sign";

    public static String signRequest(Map<String, String> params, String secret) {
        try {
            // 第一步：参数排序
            String[] keys = params.keySet().toArray(new String[0]);
            Arrays.sort(keys);

            // 第二步：把所有参数名和参数值串在一起
            StringBuilder query = new StringBuilder();
            for (String key : keys) {
                String value = params.get(key);
                if (StringUtils.isNotEmpty(key) && !StringUtils.equalsIgnoreCase(key, SIGN_PARAM_NAME)
                        && StringUtils.isNotEmpty(value)) {
                    query.append(key).append(value);
                }
            }

            // 第三步：使用MD5/HMAC加密
            byte[] encrpted = HmacUtils.hmacMd5(secret.getBytes("UTF-8"), query.toString().getBytes("UTF-8"));
            // 第四步：把二进制转化为大写的十六进制
            return Hex.encodeHexString(encrpted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
