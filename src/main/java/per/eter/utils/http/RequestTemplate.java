package per.eter.utils.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by moye on 2018/8/29.
 */
@Component
public class RequestTemplate {

    @Resource
    RestTemplate restTemplate;

    /**
     * get
     *
     * @param url         请求地址
     * @param param       参数
     * @param returnClass 返回类型
     * @return
     */
    public <T> T get(String url, Class<T> returnClass, Map<String, ?> param) {
        return restTemplate.getForObject(url, returnClass, param);
    }

    /**
     * post
     *
     * @param url         请求地址
     * @param param       参数
     * @param returnClass 返回类型
     * @param header      自定义的头信息
     * @return
     */
    public <E> E post(String url, E param, Class<E> returnClass, Map<String, String> header) {
        HttpHeaders headers = new HttpHeaders();
        header.forEach((o1, o2) -> headers.set(o1, o2));
        HttpEntity<E> httpEntity = new HttpEntity<E>(param, headers);
        return restTemplate.postForObject(url, httpEntity, returnClass);
    }

    public <E> E client(String url, Class<E> returnClass, HttpMethod method, Map<String, String> header, String params) {
        HttpHeaders headers = new HttpHeaders();
        /*默认表单提交方案*/
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        header.forEach((o1, o2) -> headers.set(o1, o2));
        HttpEntity<String> requestEntity = new HttpEntity<String>(params, headers);
        ResponseEntity<E> exchange = restTemplate.exchange(url, method, requestEntity, returnClass);
        return exchange.getBody();
    }


    public String jsonClient(String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(params, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url, request, String.class);
        return entity.getBody();
    }



    public String jsonClient(String url, Object params) {
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> entity = restTemplate.postForEntity(url, params, String.class);
        return entity.getBody();
    }

    /**
     * post
     *
     * @param url         请求地址
     * @param param       参数
     * @param returnClass 返回类型
     * @return
     */
    public <E> E postByDefault(String url, E param, Class<E> returnClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        HttpEntity<E> httpEntity = new HttpEntity<E>(param, headers);
        return restTemplate.postForObject(url, httpEntity, returnClass);
    }

 /*   public String client(String url, HttpMethod method, MultiValueMap<String, String> params){
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }*/
}
