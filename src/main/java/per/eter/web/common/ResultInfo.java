package per.eter.web.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultInfo<P> {
    private ResultType resultType = ResultType.success;
    private String message = "操作成功";
    private P bean;
    private List<P> beanList;
    private Map<String, P> beanMap;
    private Map<String, Object> additionalInfo;

    private Exception exception;


    public ResultInfo() {
    }


    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Map<String, Object> getAdditionalInfo() {
        return this.additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public P getBean() {
        return this.bean;
    }

    public void setBean(P bean) {
        this.bean = bean;
    }

    public List<P> getBeanList() {
        return this.beanList;
    }

    public void setBeanList(List<P> beanList) {
        this.beanList = beanList;
    }

    public Map<String, P> getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(Map<String, P> beanMap) {
        this.beanMap = beanMap;
    }

    public static enum ResultType {
        success,
        exception,
        fail,
        error;
        private ResultType() {
        }
    }
}
