package org.siu.rukawa.datasource.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @Author Siu
 * @Date 2020/3/26 16:30
 * @Version 0.0.1
 */
@UtilityClass
public class PropertiesUtil {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static JSONObject loadJsonPropertiesFromRemoteServer(String api) {
        Object object = REST_TEMPLATE.getForObject(api, Object.class);
        if (object instanceof HashMap) {
            HashMap<String, Object> ret = (HashMap<String, Object>) object;
            if (ret.get("data") != null) {
                //String s = JSON.toJSONString(ret.get("data"));
                return (JSONObject) JSON.toJSON(ret.get("data"));
            }
        }
        return null;
    }


}
