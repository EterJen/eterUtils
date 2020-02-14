package per.eter.utils.jxls;

import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JxlsUtils {
    static{
        //添加自定义指令（可覆盖jxls原指令）
        //合并单元格(模板已经做过合并单元格操作的单元格无法再次合并)
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object>
            model) throws IOException{
        Context context = PoiTransformer.createInitialContext();
        if (model != null) {
            for (Map.Entry<String, Object> entry : model.entrySet()){
                context.putVar(entry.getKey(), entry.getValue());
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(is, os);
        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        //设置静默模式，不报警告
        evaluator.getJexlEngine().setSilent(true);
        //函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("jx", new JxlsUtils());    //添加自定义功能
        evaluator.getJexlEngine().setFunctions(funcs);
        //必须要这个，否者表格函数统计会错乱
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        is.close();
        os.close();
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws FileNotFoundException, IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws Exception {
        File template = getTemplate(templatePath);
        if(template != null){
            exportExcel(new FileInputStream(template), os, model);
        } else {
            throw new Exception("Excel 模板未找到。");
        }
    }

    public static void exportExcel(String templatePath, String outPath, Map<String, Object> model) throws Exception {
        exportExcel(new FileInputStream(templatePath), new FileOutputStream(outPath), model);
    }

    //获取jxls模版文件
    public static File getTemplate(String path){
        File template = new File(path);
        if(template.exists()){
            return template;
        }
        return null;
    }




}
