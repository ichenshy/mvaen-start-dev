package com.chen.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.chen.apiclientsdk.model.User;
import com.chen.apiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * api客户端
 *
 * @author CSY
 * @date 2023/07/21
 */
public class ApiClient {
    private String accessKey;
    private String secretKey;


    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get("http://localhost:8123/api/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post("http://localhost:8123/api/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/user/")
                .addHeaders(getHeadsMap(json))
                .body(json)
                .execute();
        int status = httpResponse.getStatus();
        System.out.println("响应状态码：" + status);
        String body = httpResponse.body();
        System.out.println("body：" + body);
        return body;
    }

    private Map<String, String> getHeadsMap(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("noce", RandomUtil.randomNumbers(4));
        map.put("body", body);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 100));
        map.put("sign", SignUtils.genSign(body, secretKey));
        return map;
    }
}
