package per.eter.web.fileop.bigfile.chunkupload;


import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import per.eter.utils.file.SimpFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin
@Controller
@RequestMapping("/fileOperation/trustedRequest/chunkUpload")
public class ChunkUploadController {
    @Value("${app.workspace}")
    public String appWorkSpace;
    @Value("${app.bigFileRelativePath}")
    public String bigFileRelativePath;

    @PostMapping("/part")
    @ResponseBody
    public Result partUpload(HttpServletRequest request, HttpServletResponse response, String guid, Integer chunk, MultipartFile file, Integer chunks) {
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                if (chunk == null) chunk = 0;
                // 临时目录用来存放所有分片文件
                String tempFileDir = appWorkSpace + bigFileRelativePath + "temp" + File.separator + guid;
                File parentFileDir = new File(tempFileDir);
                if (!parentFileDir.exists()) {
                    parentFileDir.mkdirs();
                }
                // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
                File tempPartFile = new File(parentFileDir, guid + "_" + chunk + ".part");
                FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
            }

        } catch (Exception e) {
            return Result.failMessage(400, e.getMessage());
        }
        return Result.successMessage(200, "上次成功");
    }

    @RequestMapping("merge")
    @ResponseBody
    public Result mergeFile(String guid, String fileName) {
        // 得到 destTempFile 就是最终的文件
        try {
            String sname = fileName.substring(fileName.lastIndexOf("."));
            //时间格式化格式
            Date currentTime = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmssSSS");
            //获取当前时间并作为时间戳
            String timeStamp = simpleDateFormat.format(currentTime);
            //拼接新的文件名
            String newName = timeStamp + sname;
            String path = appWorkSpace + bigFileRelativePath;
            simpleDateFormat = new SimpleDateFormat("yyyy");
            String tmp = simpleDateFormat.format(currentTime) + File.separator;
            simpleDateFormat = new SimpleDateFormat("MM");
            tmp += simpleDateFormat.format(currentTime) + File.separator;
            simpleDateFormat = new SimpleDateFormat("dd");
            tmp += simpleDateFormat.format(currentTime) + File.separator;
            File parentFileDir = new File(path + "temp" + File.separator + guid);
            if (parentFileDir.isDirectory()) {
                File destTempFile = new File(path + tmp, newName);
                if (!destTempFile.exists()) {
                    //先得到文件的上级目录，并创建上级目录，在创建文件
                    destTempFile.getParentFile().mkdirs();
                    try {
                        destTempFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < parentFileDir.listFiles().length; i++) {
                    File partFile = new File(parentFileDir, guid + "_" + i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
                    //遍历"所有分片文件"到"最终文件"中
                    FileUtils.copyFile(partFile, destTempfos);
                    destTempfos.close();
                }
                // 删除临时目录中的分片文件
                FileUtils.deleteDirectory(parentFileDir);
                Result<SimpFile> result = Result.successMessage(200, "合并成功");
                SimpFile simpFile = new SimpFile();
                result.setData(simpFile);
                simpFile.setRelativePath(bigFileRelativePath + tmp + "/" + newName);
                return result;
            } else {
                return Result.failMessage(400, "没找到目录");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failMessage(400, e.getMessage());
        }

    }

}
