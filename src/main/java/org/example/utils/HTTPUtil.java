package org.example.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.pojo.Account;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP请求工具类
 *
 * @author 苦瓜不苦
 * @date 2021/6/9 10:57
 **/
public class HTTPUtil {


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
        // 账户配置
        Account account = new Account();
        // 签名信息
        String sign = RSAHelperUtil.generateSign(jsonBody);
        // 加密数据
        String encrypt = RSAHelperUtil.encrypt(jsonBody);
        // 加密后的请求参数
        formMap = Optional.ofNullable(formMap).orElse(new HashMap<>());
        formMap.put("insId", account.getInsId());
        formMap.put("operId", account.getOperId());
        formMap.put("sign", sign);
        formMap.put("encrypt", encrypt);
        // 发送http请求
        HttpRequest request = HttpRequest.post(url).form(formMap);
        // 文件
        if (CollUtil.isNotEmpty(fileList)) {
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
