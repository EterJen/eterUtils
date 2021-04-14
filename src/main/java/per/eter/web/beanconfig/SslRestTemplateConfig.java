package per.eter.web.beanconfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
/*
使用的地方注入bean替换RestTemplate
  @Bean
    @Primary
    public RestTemplate restTemplate() {
        SslRestTemplateConfig sslRestTemplateConfig = new SslRestTemplateConfig();
        RestTemplate restTemplate = sslRestTemplateConfig.restTemplate();
        return restTemplate;
    }
* */

@Slf4j
public class SslRestTemplateConfig {
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }

    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        /*ssl 允许*/
        try {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory =
                    new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            CloseableHttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(10 * 1000);
            factory.setReadTimeout(30 * 1000);
            return factory;
        } catch (Exception e) {
            log.error("创建HttpsRestTemplate失败", e);
            throw new RuntimeException("创建HttpsRestTemplate失败", e);
        }


    }

}
