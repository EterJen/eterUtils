package per.eter.utils.jxls;

import java.util.Map;

public class JxlsTemplate {
    private  String templatePath;
    private  String outPath;
    private Map<String, Object> dataModel;

    public void generate() {
        try {
            JxlsUtils.exportExcel(templatePath,outPath,dataModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JxlsTemplate(String templatePath, String outPath, Map<String, Object> dataModel) {
        this.templatePath = templatePath;
        this.outPath = outPath;
        this.dataModel = dataModel;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public Map<String, Object> getDataModel() {
        return dataModel;
    }

    public void setDataModel(Map<String, Object> dataModel) {
        this.dataModel = dataModel;
    }
}
