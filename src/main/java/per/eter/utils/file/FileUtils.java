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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;

import static java.nio.file.StandardOpenOption.READ;

@Slf4j
@Component
public class FileUtils {
    @Resource
    RestTemplate restTemplate;
    private static final int BUFFER_LENGTH = 1024 * 16;
    @Value("${file.operation.host}")
    public String fileOpHost;

    public static String fileSeparator;

    @Value("${file.operation.fileSeparator:/}")
    public void setFileSeparator(String fileSeparator) {
        FileUtils.fileSeparator = fileSeparator;
    }

    @Value("${app.workspace}")
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

    public String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decryptByBase64(String base64, String filePath) {
        if (base64 == null && filePath == null) {
            return "生成文件失败，请给出相应的数据。";
        }
        try {
            Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "指定路径下生成文件成功！";
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
            targetFile.mkdirs();
            //            throw new Exception("存放的目标路径：[" + target + "] 不存在...");
        }

        // 获取源文件夹下的文件夹或文件
        File[] resourceFiles = resourceFile.listFiles();

        for (File file : resourceFiles) {

            File file1 = new File(targetFile.getAbsolutePath() + per.eter.utils.file.FileUtils.fileSeparator + resourceFile.getName());
            // 复制文件
            if (file.isFile()) {
                System.out.println("文件" + file.getName());
                // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                // 这样 在 B 中 就存在 A
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File targetFile1 = new File(file1.getAbsolutePath() + per.eter.utils.file.FileUtils.fileSeparator + file.getName());
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
        downloadName = URLEncoder.encode(downloadName, "UTF-8");
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


    public static File createNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
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


    public Map<String, SimpFile> remoteUpload(String appWorkSpace, MultipartFile[] multipartFiles, String relativePathPrefix) throws IOException {
        String tempWorkSpace = appWorkSpace + SimpFile.commonSeparator + "temp";

        Calendar currentCalendar = Calendar.getInstance();
        if (null == relativePathPrefix) {
            relativePathPrefix = "year" + currentCalendar.get(Calendar.YEAR) + SimpFile.commonSeparator + "month" + (currentCalendar.get(Calendar.MONTH) + 1) + SimpFile.commonSeparator + "day" + currentCalendar.get(Calendar.DAY_OF_MONTH);
        } else {
            relativePathPrefix = relativePathPrefix + SimpFile.commonSeparator + "year" + currentCalendar.get(Calendar.YEAR) + SimpFile.commonSeparator + "month" + (currentCalendar.get(Calendar.MONTH) + 1) + SimpFile.commonSeparator + "day" + currentCalendar.get(Calendar.DAY_OF_MONTH);
        }


        Map<String, SimpFile> simpleFiles = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE.toString());
        //headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();

        ArrayList<File> tempfiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

            String tempName = "file" + currentCalendar.getTimeInMillis();
            File file = createNewFile(tempWorkSpace + SimpFile.commonSeparator + tempName);
            multipartFile.transferTo(file);
            tempfiles.add(file);

            form.add("files", new FileSystemResource(file));
            SimpFile simpFile = new SimpFile();
            simpFile.setRelativePath(relativePathPrefix + SimpFile.commonSeparator + tempName + fileExt);
            simpFile.setPath(appWorkSpace + simpFile.getRelativePath());
            simpFile.setOriginalFilename(originalFilename);
            simpleFiles.put(tempName, simpFile);
        }

        form.add("simpleFilesJsonStr", JSON.toJSON(simpleFiles));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fileOpHost + FileOpController.localUploadUri, httpEntity, String.class);

        for (File tempfile : tempfiles) {
            tempfile.delete();
        }


        ResultInfo<SimpFile> resultInfo = JSON.parseObject(responseEntity.getBody(), new TypeReference<ResultInfo<SimpFile>>() {
        });
        Map<String, SimpFile> beanMap = resultInfo.getBeanMap();
        Map<String, SimpFile> result = new HashMap<>();
        for (SimpFile value : beanMap.values()) {
            result.put(value.getOriginalFilename(), value);
        }
        return result;


    }

    public Map<String, SimpFile> remoteUpload(MultipartFile[] multipartFiles, String relativePathPrefix) throws IOException {
        return remoteUpload(this.appWorkSpace, multipartFiles, relativePathPrefix);
    }

    public Map<String, SimpFile> remoteUpload(SimpFile[] simpFiles, String relativePathPrefix) throws IOException {
        String tempWorkSpace = appWorkSpace + SimpFile.commonSeparator + "remoteUploadTemp";

        Calendar currentCalendar = Calendar.getInstance();
        relativePathPrefix = relativePathPrefix + SimpFile.commonSeparator + currentCalendar.get(Calendar.YEAR) + "_" + (currentCalendar.get(Calendar.MONTH) + 1) + "_" + currentCalendar.get(Calendar.DAY_OF_MONTH);


        Map<String, SimpFile> simpleFiles = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();

        for (SimpFile simpFile : simpFiles) {
            form.add("files", new FileSystemResource(new File(simpFile.getPath())));
            simpFile.setRelativePath(relativePathPrefix + SimpFile.commonSeparator + simpFile.getUuidName() + simpFile.getNameSuffix());
            simpFile.setPath(appWorkSpace + simpFile.getRelativePath());
            simpleFiles.put(simpFile.getOriginalFilename(), simpFile);
        }

        form.add("simpleFilesJsonStr", JSON.toJSON(simpleFiles));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(fileOpHost + FileOpController.localUploadUri, httpEntity, String.class);


        ResultInfo<SimpFile> resultInfo = JSON.parseObject(responseEntity.getBody(), new TypeReference<ResultInfo<SimpFile>>() {
        });
        return resultInfo.getBeanMap();
    }

    public void remoteRead(SimpFile simpFile, HttpServletResponse response) throws Exception {
        restTemplate.execute(fileOpHost + FileOpController.localReadUri + "/" + simpFile.getRelativePath(), HttpMethod.GET, null, clientHttpResponse -> {

            String downloadName = StringUtils.changeEncode(simpFile.getDownloadName(), "UTF-8");
            log.info("下载文件名称: {} ", downloadName);
            downloadName = URLEncoder.encode(downloadName, "UTF-8");
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

    public void imgRead(SimpFile simpFile, HttpServletResponse response) throws Exception {
        Path video = Paths.get(appWorkSpace, simpFile.getRelativePath());

        int length = (int) Files.size(video);
        int start = 0;
        int end = length - 1;
        int contentLength = end - start + 1;

        response.reset();
        response.setBufferSize(BUFFER_LENGTH);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, end, length));
        response.setHeader("Content-Length", String.format("%s", contentLength));
        response.setContentType(Files.probeContentType(video));

        /*开关*/
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "public");
        /*校验*/
        response.setDateHeader("Last-Modified", Files.getLastModifiedTime(video).toMillis());


        int bytesRead;
        int bytesLeft = contentLength;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH);

        try (SeekableByteChannel input = Files.newByteChannel(video, READ);
             OutputStream output = response.getOutputStream()) {

            input.position(start);

            while ((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0) {
                buffer.clear();
                output.write(buffer.array(), 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
                bytesLeft -= bytesRead;
            }
        }

    }

    public SimpFile copy(SimpFile simpFile) throws IOException {
        SimpFile descSimpFile = new SimpFile();
        FileChannel input = null;
        FileChannel output = null;
        Calendar currentCalendar = Calendar.getInstance();


        try {
            input = new FileInputStream(new File(appWorkSpace + simpFile.getPath())).getChannel();
            String relativePath = simpFile.getRelativePath() + SimpFile.commonSeparator + "year" + currentCalendar.get(Calendar.YEAR) + SimpFile.commonSeparator + "month" + (currentCalendar.get(Calendar.MONTH) + 1) + SimpFile.commonSeparator + "day" + currentCalendar.get(Calendar.DAY_OF_MONTH) + SimpFile.commonSeparator + "file" + currentCalendar.getTimeInMillis();
            descSimpFile.setRelativePath(relativePath);
            descSimpFile.setOriginalFilename(simpFile.getOriginalFilename());
            String destPath = appWorkSpace + relativePath;
            File file = makeSureFileExists(destPath);
            output = new FileOutputStream(file).getChannel();
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            log.error("error occur while copy{}", e.getMessage());
        } finally {
            input.close();
            output.close();
        }
        return descSimpFile;

    }
}
