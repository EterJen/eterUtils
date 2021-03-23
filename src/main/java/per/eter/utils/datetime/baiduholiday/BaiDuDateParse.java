package per.eter.utils.datetime.baiduholiday;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaiDuDateParse {
    public  Map<Date, DayStatusEnum> holidayByList(String year){
        Date date = new Date();
        date.setTime(Long.parseLong("1612492672210"));
        System.out.println(date);
        String httpUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+year+"&resource_id=6018&format=json";
        String query = "2020年10月";
        httpUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+query+"&co=&resource_id=39043&t=1612492672210&ie=utf8&oe=gbk&cb=op_aladdin_callback,jQuery110202589086671148326_1612405512200&format=json&tn=wisetpl&_=1612405512730";
        //httpUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=2022年6月&co=&resource_id=6018&t=d21eb6a7-5de6-4d3f-9b06-09e14e5877e3&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110209630343350406516_1544930242767&_=1544930242772";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        Map<Date,DayStatusEnum> dateMap = new HashMap<Date,DayStatusEnum>();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "GBK"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                //sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
            int i1 = result.indexOf("(");
            int i2 = result.lastIndexOf(")");
            result = result.substring(i1+1, i2);
            //int i1 = result.indexOf("\"almanac\":[");
            //int i2 = result.lastIndexOf("\"almanac#num#baidu\"");
            //result = result.substring(i1+1, i2);
            // i1 = result.indexOf("[")-1;
            // i2 = result.lastIndexOf("]")+1;
            //result = result.substring(i1+1, i2);
            //result = "{date:" + result + "}";
            JSONObject jsonObject = JSONObject.parseObject(result);
            String s = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
            System.out.println(s);
            if (true) {
                return null;
            }
            JSONArray holidayJsonArr = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("holiday");
            List<String> holidays = holidayJsonArr.stream().map(h -> {
                JSONObject hJsonObj = (JSONObject) h;
                JSONArray list = hJsonObj.getJSONArray("list");
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        JSONObject job = list.getJSONObject(i);   // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        String sts = (String)job.get("status");
                        if (sts.equals("1")) {
                            dateMap.put(DateUtils.getDateByString((String)job.get("date"), "yyyy-MM-dd"),DayStatusEnum.HOLIDAYS);
                        }else {
                            dateMap.put(DateUtils.getDateByString((String)job.get("date"), "yyyy-MM-dd"),DayStatusEnum.WORKDAY);
                        }
                    }
                }
                return "1";
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateMap;
    }
}

