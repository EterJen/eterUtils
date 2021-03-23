package per.eter.utils.datetime.intf;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface MonthNum2Str {
    public String monthNum2Str(int monthOfYear);
    public String monthNum2Str(String  monthOfYear);
}
