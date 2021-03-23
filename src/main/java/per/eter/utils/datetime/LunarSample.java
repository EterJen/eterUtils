package per.eter.utils.datetime;

import com.nlf.calendar.Lunar;

public class LunarSample {
    public static void main(String[] args){
        //今天
        //Lunar date = new Lunar();

        //指定阴历的某一天
        Lunar date = new Lunar(2021,1,1);
        System.out.println(date.toFullString());
        System.out.println(date.getSolar().toFullString());
    }
}
