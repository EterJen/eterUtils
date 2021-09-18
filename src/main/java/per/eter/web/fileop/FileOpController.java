package per.eter.web.fileop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import per.eter.utils.file.FileUtils;
import per.eter.utils.file.SimpFile;
import per.eter.utils.http.RequestTemplate;
import per.eter.web.common.ResultInfo;
import per.eter.web.fileop.bigfile.chunkupload.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/fileOperation/trustedRequest")
public class FileOpController {

    @Autowired
    FileUtils fileUtils;
    @Autowired
    RequestTemplate requestTemplate;
    @Autowired
    PseudostreamingService pseudostreamingService;
    @Autowired
    FileOpService fileOpService;
    @Value("${file.operation.host}")
    public String fileOpHost;

    public static String localFile2StringUri = "/fileOperation/trustedRequest/file2String";
    public static String localString2FileUri = "/fileOperation/trustedRequest/string2File";
    public static String localReadUri = "/fileOperation/trustedRequest/localRead";
    public static String imgReadUri = "/fileOperation/trustedRequest/imgRead";
    public static String localUploadUri = "/fileOperation/trustedRequest/localUpload";
    public static String remoteReadUri = "/fileOperation/trustedRequest/remoteRead";

    @RequestMapping("/string2File")
    public SimpFile string2File(@RequestBody SimpFile simpFile) throws Exception {
        return fileOpService.string2File(simpFile);
    }

    @RequestMapping("/file2String")
    public SimpFile file2String(@RequestBody SimpFile simpFile) throws Exception {
        return fileOpService.file2String(simpFile);
    }

    @RequestMapping("/localDownload")
    public void localDownload(@RequestBody SimpFile simpFile, HttpServletResponse response) throws Exception {
        FileUtils.localDownload(simpFile, response);
    }

    @RequestMapping("/localRead/**")
    public void localRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileUtils.localRead(request, response);
    }

    @RequestMapping("/localRead4Base64Str")
    @ResponseBody
    public Result localRead4Base64Str(@RequestBody SimpFile simpFileQueryBean)throws Exception  {
        String filePath = fileUtils.appWorkSpace + simpFileQueryBean.getRelativePath();
        String content = fileUtils.encryptToBase64(filePath);

        SimpFile simpFile = new SimpFile();
        simpFile.setStringContent(content);

        return Result.success(200,simpFile);
    }



    @RequestMapping("/processRequest")
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        pseudostreamingService.processRequest(request, response);
    }

    @RequestMapping("/remoteRead/**")
    public void remoteRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpFile simpFile = new SimpFile();
        String servletPath = request.getServletPath();
        simpFile.setRelativePath(servletPath.replace(FileOpController.remoteReadUri, ""));
        simpFile.setDownloadUrl(fileOpHost + FileOpController.localReadUri);

        fileUtils.remoteRead(simpFile, response);
    }



    @RequestMapping("/remoteUpload")
    public Map<String, SimpFile> remoteUpload(@RequestParam("files") MultipartFile[] multipartFiles, @RequestParam("relativePathPrefix") String relativePathPrefix) throws Exception {
        return fileUtils.remoteUpload(multipartFiles, relativePathPrefix);
    }
    @RequestMapping("/copy")
    public SimpFile copy(SimpFile simpFile) throws Exception {
        return fileUtils.copy(simpFile);
    }

    @RequestMapping("/remoteDownload")
    public void remoteDownload(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        SimpFile simpFile = new SimpFile();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            String key = stringEntry.getKey();
            String[] value = stringEntry.getValue();
            org.apache.commons.beanutils.BeanUtils.setProperty(simpFile, key, value[0]);
        }
        fileUtils.remoteRead(simpFile, response);
    }
    @RequestMapping("/imgRead/**")
    public void imgRead(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpFile simpFile = new SimpFile();
        String servletPath = request.getServletPath();
        simpFile.setRelativePath(servletPath.replace(FileOpController.imgReadUri, ""));

        fileUtils.imgRead(simpFile, response);
    }


    @RequestMapping("/localUpload")
    public ResultInfo<SimpFile> localUpload(@RequestParam("files") MultipartFile[] files, @RequestParam("simpleFilesJsonStr") String simpleFilesJsonStr) throws Exception {
        Map<String, SimpFile> simpleFiles = JSON.parseObject(simpleFilesJsonStr, new TypeReference<Map<String, SimpFile>>() {
        });
        return fileOpService.localUpload(files, simpleFiles);
    }

}
