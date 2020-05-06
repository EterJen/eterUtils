package per.eter.utils.bean;

import per.eter.utils.string.MyStringTool;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtils {
    private void map2Bean(final Map<String, Object> map, final Object descBean) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                org.apache.commons.beanutils.BeanUtils.setProperty(descBean, MyStringTool.lineToHump(entry.getKey()), entry.getValue());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
