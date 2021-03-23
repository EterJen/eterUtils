package per.eter.utils.string;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    /**
     * 判断字符串的编码
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode[] = new String[]{
                "UTF-8",
                "ISO-8859-1",
                "GB2312",
                "GBK",
                "GB18030",
                "Big5",
                "Unicode",
                "ASCII"
        };
        for (int i = 0; i < encode.length; i++) {
            try {
                if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {
                    return encode[i];
                }
            } catch (Exception ex) {
            }
        }

        return "";
    }

    public static String changeEncode(String str ,String encode) throws UnsupportedEncodingException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(str)) {
            String encoding = getEncoding(str);
            return new String(str.getBytes(encoding), encode);
        } else {
            return "";
        }
    }
}
