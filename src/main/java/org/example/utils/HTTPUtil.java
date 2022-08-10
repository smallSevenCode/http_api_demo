package org.example.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具类
 *
 * @author: 苦瓜不苦
 * @date: 2021/6/9 10:57
 **/
public class HTTPUtil {

    // 机构标识
    public final static String insId = "INS15***00001";
    // 操作员
    public final static String operId = "wup***est";


    /**
     * post请求
     *
     * @param url 请求地址
     * @param map 参与加密的请求参数
     * @throws Exception
     */
    public static void post(String url, Map<String, Object> map) throws Exception {
        post(url, map, new HashMap<>(), null);
    }


    /**
     * post请求
     *
     * @param url      请求地址
     * @param map      参与加密的请求参数
     * @param fileList 文件参数
     * @throws Exception
     */
    public static void post(String url, Map<String, Object> map, List<File> fileList) throws Exception {
        post(url, map, new HashMap<>(), fileList);
    }


    /**
     * post请求
     *
     * @param url      请求地址
     * @param map      参与加密的请求参数
     * @param formMap  非加密的请求参数
     * @param fileList 文件参数
     * @throws Exception
     */
    public static void post(String url, Map<String, Object> map, Map<String, Object> formMap, List<File> fileList) throws Exception {
        String jsonBody = JSON.toJSONString(map);
        // 签名信息
        String sign = RSAHelperUtil.generateSign(jsonBody);
        // 加密数据
        String encrypt = RSAHelperUtil.encrypt(jsonBody);
        // 加密后的请求参数
        formMap = formMap == null ? new HashMap<>() : formMap;
        formMap.put("insId", insId);
        formMap.put("operId", operId);
        formMap.put("sign", sign);
        formMap.put("encrypt", encrypt);
        // 发送http请求
        HttpRequest request = HttpRequest.post(url).form(formMap);
        // 文件
        if (fileList != null && fileList.size() > 0) {
            File[] files = fileList.toArray(new File[0]);
            request.form("file", files);
        }
        String body = request.execute().body();
        // 获取返回参数
        JSONObject jsonObject = JSON.parseObject(body);
        // 解密
        encrypt = jsonObject.getString("encrypt");
        String dataResult = RSAHelperUtil.decrypt(encrypt);
        System.err.println("数据结果 >>>>>> " + dataResult);
        // 验签
        sign = jsonObject.getString("sign");
        Boolean verifySign = RSAHelperUtil.verifySign(dataResult, sign);
        System.err.println("验签结果 >>>>>> " + verifySign);
    }

}
