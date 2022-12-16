package org.example;

import org.example.utils.HTTPUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 主方法
 *
 * @author 苦瓜不苦
 * @date 2022/12/16 17:39
 **/
public class MainApi {


    private final static String URL = "http://127.0.0.1:8080/xlzxop/v2/***.do";


    /**
     * 发送请求
     * 替换Account类中的公钥、私钥、机构标识、操作员
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("xxx", "xxx");
        paramMap.put("yyy", "yyy");
        paramMap.put("zzz", "zzz");
        HTTPUtil.post(URL, paramMap);
    }

}
