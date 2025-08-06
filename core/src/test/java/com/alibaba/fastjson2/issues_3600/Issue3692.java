package com.alibaba.fastjson2.issues_3600;

import com.alibaba.fastjson2.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author 张治保
 * @since 2025/8/4
 */
public class Issue3692 {
    private final HashSet<String> list;
    {
        list = new HashSet<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");
    }

    @Test
    public void objectArray() {
        Object[] data = new Object[]{list};
        String jsonString = encode(data);
        Object[] list1 = decode(jsonString, Object.class);
        Assertions.assertArrayEquals(data, list1);
    }

    @Test
    public void testList() {
        List data = new ArrayList<>();
        data.add(list);
        String jsonString = encode(data);
        List list1 = decode(jsonString, Object.class);
        Assertions.assertEquals(data, list1);
        System.out.println(1);
    }

    @Test
    public void map() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("list", list);
        String jsonString = encode(data);
        Map map = decode(jsonString, Object.class);
        Assertions.assertEquals(data, map);
    }

    static <T> T decode(String s, Type type) {
        s = s == null || s.isEmpty() ? null : s;
        return JSON.parseObject(s,
                type,
                JSONReader.autoTypeFilter(true, (Class<?>) null),
//                JSONReader.Feature.SupportAutoType,
                JSONReader.Feature.FieldBased,
                JSONReader.Feature.IgnoreAutoTypeNotMatch,
                JSONReader.Feature.UseNativeObject
        );
    }

    static String encode(Object object) {
        // 要避免String类型 再次被包装
        if (object instanceof String) {
            return (String) object;
        }
        return JSON.toJSONString(
                object,
                JSONWriter.Feature.WriteClassName,
                JSONWriter.Feature.IgnoreErrorGetter,
                JSONWriter.Feature.FieldBased,
                JSONWriter.Feature.ReferenceDetection
        );
    }
}
