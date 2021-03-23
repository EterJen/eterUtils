package per.eter.utils.system;

public class SystmeUtils {
    /**
     * @description: 判断运行的系统是不是linux
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}
