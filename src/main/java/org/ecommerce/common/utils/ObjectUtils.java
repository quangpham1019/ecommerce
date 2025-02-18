package org.ecommerce.common.utils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ObjectUtils {

    public static <T> Map<String, Object> convertToMap(T object) {
        Map<String, Object> map = new HashMap<>();
        if (object == null) {
            return map;
        }

        BeanWrapper wrapper = new BeanWrapperImpl(object);
        for (PropertyDescriptor propertyDescriptor : wrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = wrapper.getPropertyValue(propertyName);

            // Exclude null values and "class" property to avoid unnecessary entries
            if (!"class".equals(propertyName) && Objects.nonNull(propertyValue)) {
                map.put(propertyName, propertyValue);
            }
        }
        return map;
    }
}
