package per.eter.utils.poitl;

import com.deepoove.poi.XWPFTemplate;

import java.io.FileOutputStream;
import java.io.IOException;

public class PoitlTemplate {
    private  String templatePath;
    private  String outPath;
    private  Object dataModel;

    public void generate() {
        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(dataModel);
        try {
            FileOutputStream out = new FileOutputStream(outPath);
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PoitlTemplate(String templatePath, String outPath, Object dataModel) {
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

    public Object getDataModel() {
        return dataModel;
    }

    public void setDataModel(Object dataModel) {
        this.dataModel = dataModel;
    }
}
