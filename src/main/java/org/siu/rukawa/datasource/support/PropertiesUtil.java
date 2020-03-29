package org.siu.rukawa.datasource.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.siu.rukawa.datasource.autoconfigure.properties.CustomPointcut;
import org.siu.rukawa.datasource.autoconfigure.properties.CustomPointcutProperty;
import org.siu.rukawa.datasource.core.aop.custom.matcher.ExpressionMatcher;
import org.siu.rukawa.datasource.core.aop.custom.matcher.Matcher;
import org.siu.rukawa.datasource.core.aop.custom.matcher.RegexMatcher;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    /**
     * 自定义切点转换
     *
     * @param customPointcut
     * @return
     */
    public static List<Matcher> toMatchers(Map<String, CustomPointcutProperty> customPointcut) {
        List<Matcher> matchers = new LinkedList<>();
        customPointcut.forEach((k, v) -> {
            if (CustomPointcut.EXPRESSION == v.getType()) {
                for (String m : v.getMatchers()) {
                    matchers.add(new ExpressionMatcher(m, v.getDs()));
                }
            }
            if (CustomPointcut.REGEXP == v.getType()) {
                int size = v.getMatchers().size();
                matchers.add(new RegexMatcher(v.getMatchers().toArray(new String[size]), v.getDs()));

            }


        });

        return matchers;
    }


}
