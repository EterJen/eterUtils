package per.eter.utils.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import per.eter.utils.string.StringUtils;
import per.eter.utils.system.SystmeUtils;
import per.eter.web.common.ResultInfo;
import per.eter.web.fileop.FileOpController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Component
public class FileUtils {
    @Resource
    RestTemplate restTemplate;

    @Value("${file.operation.host}")
    public String fileOpHost;

    @Value("${app.workSpace}")
    public String appWorkSpace;

    public static void download(FileExtend file, HttpServletResponse response) throws IOException {
        String outFileName = file.getWebOutName();
        String encoding = StringUtils.getEncoding(outFileName);
        outFileName = new String(outFileName.getBytes(encoding), "UTF-8");
        outFileName = URLEncoder.encode(outFileName, "UTF-8");
        outFileName = outFileName.replaceAll("\\+", "%20");
        response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + outFileName);
        response.setContentType("application/force-download;charset=utf-8");


        InputStream inputStream = new FileInputStream(file);
        int available = inputStream.available();

        BufferedInputStream inBuffer = new BufferedInputStream(inputStream);
        BufferedOutputStream outBuffer = new BufferedOutputStream(response.getOutputStream());
        byte[] buf = new byte[1024]; //自定义的字节缓冲区
        while ((available = inBuffer.read(buf)) != -1) { //返回的是数组中的个数，如读完或读满，则返回-1
            outBuffer.write(buf, 0, available);
        }

        outBuffer.flush();
        outBuffer.close();
        inBuffer.close();
    }

    /**
     * 复制文件
     *
     * @param resource
     * @param target
     */
    public static void copyFile(File resource, File target) throws Exception {
        // 输入流 --> 从一个目标读取数据
        // 输出流 --> 向一个目标写入数据

        long start = System.currentTimeMillis();

        // 文件输入流并进行缓冲
        FileInputStream inputStream = new FileInputStream(resource);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        // 文件输出流并进行缓冲
        FileOutputStream outputStream = new FileOutputStream(target);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        // 缓冲数组
        // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
        byte[] bytes = new byte[1024 * 2];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            bufferedOutputStream.write(bytes, 0, len);
        }
        // 刷新输出缓冲流
        bufferedOutputStream.flush();
        //关闭流
        bufferedInputStream.close();
        bufferedOutputStream.close();
        inputStream.close();
        outputStream.close();

        long end = System.currentTimeMillis();

        System.out.println("耗时：" + (end - start) / 1000 + " s");

    }

    /**
     * 复制文件夹
     *
     * @param resource 源路径
     * @param target   目标路径
     */
    public static void copyFolder(String resource, String target) throws Exception {

        File resourceFile = new File(resource);
        if (!resourceFile.exists()) {
            throw new Exception("源目标路径：[" + resource + "] 不存在...");
        }
        File targetFile = new File(target);
        if (!targetFile.exists()) {
            throw new Exception("存放的目标路径：[" + target + "] 不存在...");
        }

        // 获取源文件夹下的文件夹或文件
        File[] resourceFiles = resourceFile.listFiles();

        for (File file : resourceFiles) {

            File file1 = new File(targetFile.getAbsolutePath() + File.separator + resourceFile.getName());
            // 复制文件
            if (file.isFile()) {
                System.out.println("文件" + file.getName());
                // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                // 这样 在 B 中 就存在 A
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                copyFile(file, targetFile1);
            }
            // 复制文件夹
            if (file.isDirectory()) {// 复制源文件夹
                String dir1 = file.getAbsolutePath();
                // 目的文件夹
                String dir2 = file1.getAbsolutePath();
                copyFolder(dir1, dir2);
            }
        }

    }

    public static String webResourceFilePath(String resourceLocation) throws FileNotFoundException {
        String srcPath = ResourceUtils.getURL(resourceLocation).getPath();
        if (SystmeUtils.isWindows()) {
            srcPath = srcPath.substring(1);
        }
        return srcPath;
    }


    public SimpFile remoteFile2String(SimpFile simpFile) throws Exception {
        ResponseEntity<SimpFile> responseEntity = restTemplate.postForEntity(fileOpHost + FileOpController.localFile2StringUri, simpFile, SimpFile.class);
        return responseEntity.getBody();
    }


    public SimpFile remoteString2File(SimpFile simpFile) throws Exception {
        ResponseEntity<SimpFile> responseEntity = restTemplate.postForEntity(fileOpHost + FileOpController.localString2FileUri, simpFile, SimpFile.class);
        return responseEntity.getBody();
    }

    public static void localDownload(SimpFile simpFile, HttpServletResponse response) throws Exception {
        String downloadName = StringUtils.changeEncode(simpFile.getDownloadName(), "UTF-8");
        log.info("下载文件名称: {} ", downloadName);
        downloadName = downloadName.replaceAll("\\+", "%20");
        response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + downloadName);
        response.setContentType("application/force-download;charset=utf-8");

        InputStream inputStream = new FileInputStream(simpFile.getPath());
        int available = inputStream.available();
        log.info("下载文件大小: {} ", available);

        BufferedInputStream inBuffer = new BufferedInputStream(inputStream);
        BufferedOutputStream outBuffer = new BufferedOutputStream(response.getOutputStream());
        byte[] buf = new byte[1024]; //自定义的字节缓冲区
        while ((available = inBuffer.read(buf)) != -1) { //返回的是数组中的个数，如读完或读满，则返回-1
            outBuffer.write(buf, 0, available);
        }

        outBuffer.flush();
        outBuffer.close();
        inBuffer.close();
    }

    public void localRead(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String servletPath = request.getServletPath();
        InputStream inputStream = new FileInputStream(appWorkSpace + servletPath.replace(FileOpController.localReadUri, ""));
        int available = inputStream.available();
        log.info("下载文件大小: {} ", available);

        BufferedInputStream inBuffer = new BufferedInputStream(inputStream);
        BufferedOutputStream outBuffer = new BufferedOutputStream(response.getOutputStream());

        byte[] buf = new byte[1024]; //自定义的字节缓冲区
        while ((available = inBuffer.read(buf)) != -1) { //返回的是数组中的个数，如读完或读满，则返回-1
            outBuffer.write(buf, 0, available);
        }

        outBuffer.flush();
        outBuffer.close();
        inBuffer.close();
    }

    public static void makeSureDirExists(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static File makeSureFileExists(String filePath) throws IOException {
        String substring = filePath.substring(0, filePath.lastIndexOf(SimpFile.getCommonSeparator()));
        makeSureDirExists(substring);
        File file = new File(filePath);
        file.createNewFile();
        return file;
    }

    public static FileType fileType(SimpFile simpFile) throws IOException {
        String path = simpFile.getPath();
        String suffix = path.substring(path.lastIndexOf(".") + 1, path.length());
        FileType fileType = FileType.NOMATCH;
        switch (suffix) {
            case "pdf":
            case "PDF":
                fileType = FileType.PDF;
                break;
            case "txt":
            case "TXT":
                fileType = FileType.TXT;
                break;
            case "JPG":
            case "JPEG":
            case "GIF":
            case "PNG":
            case "BMP":
            case "PCX":
            case "TGA":
            case "PSD":
            case "TIFF":

            case "jpg":
            case "jpeg":
            case "gif":
            case "png":
            case "bmp":
            case "pcx":
            case "tga":
            case "psd":
            case "tiff":
                fileType = FileType.IMAGE;
                break;
            default:
                fileType = FileType.AUDIO;
                break;
        }
        return fileType;
    }

    public static void main(String[] args) throws IOException {
        SimpFile simpFile = new SimpFile();
        simpFile.setPath("C:/workspack/svn/data/jyjcms/articleAtt/new/2020_1_20/02e10dd2-f60f-4211-9ab3-525cede957ff.txt");
        FileType fileType = fileType(simpFile);
    }


    public Map<String, SimpFile> remoteUpload(MultipartFile[] multipartFiles, String relativePathPrefix) throws IOException {
        String tempWorkSpace = appWorkSpace + SimpFile.commonSeparator + "remoteUploadTemp";

        Calendar currentCalendar = Calendar.getInstance();
        relativePathPrefix = relativePathPrefix + SimpFile.commonSeparator + currentCalendar.get(Calendar.YEAR) + "_" + (currentCalendar.get(Calendar.MONTH) + 1) + "_" + currentCalendar.get(Calendar.DAY_OF_MONTH);


        Map<String, SimpFile> simpleFiles = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();

        ArrayList<File> tempfiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

            File file = makeSureFileExists(tempWorkSpace + SimpFile.commonSeparator + originalFilename);
            tempfiles.add(file);
            multipartFile.transferTo(file);

            form.add("files", new FileSystemResource(file));
            SimpFile simpFile = new SimpFile();
            simpFile.setRelativePath(relativePathPrefix + SimpFile.commonSeparator + UUID.randomUUID() + fileExt);
            simpFile.setPath(appWorkSpace + simpFile.getRelativePath());
            simpleFiles.put(originalFilename, simpFile);
        }

        form.add("simpleFilesJsonStr", JSON.toJSON(simpleFiles));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fileOpHost + FileOpController.localUploadUri, httpEntity, String.class);

        for (File tempfile : tempfiles) {
            tempfile.delete();
        }


        ResultInfo<SimpFile> resultInfo = JSON.parseObject(responseEntity.getBody(), new TypeReference<ResultInfo<SimpFile>>() {
        });
        return resultInfo.getBeanMap();

    }

    public void remoteRead(SimpFile simpFile, HttpServletResponse response) throws Exception {
        restTemplate.execute(fileOpHost + FileOpController.localReadUri + simpFile.getRelativePath(), HttpMethod.GET, null, clientHttpResponse -> {
            String downloadName = StringUtils.changeEncode(simpFile.getDownloadName(), "UTF-8");
            log.info("下载文件名称: {} ", downloadName);
            downloadName = downloadName.replaceAll("\\+", "%20");
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + downloadName);
            response.setContentType("application/force-download;charset=utf-8");

            ServletOutputStream outputStream = response.getOutputStream();
            InputStream body = clientHttpResponse.getBody();
            StreamUtils.copy(body, outputStream);
            body.close();
            outputStream.close();
            return null;
        });
    }
}
