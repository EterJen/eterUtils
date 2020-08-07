package per.eter.web.fileop.bigfile.chunkupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author ys
 * @topic
 * @date 2020/3/8 13:00
 */
//@SpringBootApplication
//@ComponentScan(basePackages = {"per.eter"})
public class FileUpApplication implements CommandLineRunner {
    @Bean
    public MyCommandRunner errorPageFilter() {
        return new MyCommandRunner();
    }


    public static void main(String[] args) {
        SpringApplication.run(FileUpApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World!");
    }
}

class MyCommandRunner implements CommandLineRunner {


    @Override
    public void run(String... args) {
        try {
//            Runtime.getRuntime().exec("firefox http://172.17.12.1:8081/jyjcms/chunkUpload.html");
            Runtime.getRuntime().exec("firefox http://172.17.12.1:8081/jyjcms/fileOperation/trustedRequest/processRequest?video=/bigFile/2020/07/09//094144640.mp4");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}