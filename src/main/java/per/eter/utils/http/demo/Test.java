package per.eter.utils.http.demo;

import per.eter.utils.http.RequestTemplate;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        RequestTemplate httpClientTool = new RequestTemplate();
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("queryDate", "2020-01-04");
        objectObjectHashMap.put("name", "沈永良");

        String s = httpClientTool.jsonClient("http://172.17.12.1/third/trustedRequest/toDoTask", objectObjectHashMap);
        System.out.println(s);
    }
}
