package per.eter.utils.poitl.demo;

import per.eter.utils.poitl.PoitlTemplate;

import java.util.HashMap;

public class PoitlTest {
    public static void main(String[] args) {
        HashMap<String, Object> dataMode = new HashMap<>();
        dataMode.put("qfr","測試");
        dataMode.put("qfwh","测试数据");
        dataMode.put("gksxblack","黑色测试");
        dataMode.put("gksxred","红色测试");

        dataMode.put("d1","红色测试");
        dataMode.put("d3","红色测红色测试红色测试红色测试红色测试红色测试红色测试红色测试红色测试红色测试红色测试红色测试红色测试试");
        dataMode.put("d2","红色测试");
        dataMode.put("d4","红色测试");

        PoitlTemplate poitlTemplate = new PoitlTemplate("d:\\jxls\\docTemplate.docx", "d:\\jxls\\docOut.docx", dataMode);
        poitlTemplate.generate();
    }
}
