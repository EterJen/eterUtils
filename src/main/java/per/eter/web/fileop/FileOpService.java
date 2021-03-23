package per.eter.web.fileop;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import per.eter.utils.file.SimpFile;
import per.eter.web.common.ResultInfo;

import java.io.*;
import java.util.Map;

import static per.eter.utils.file.FileUtils.makeSureDirExists;
import static per.eter.utils.file.FileUtils.makeSureFileExists;

@Service
public class FileOpService {
    public ResultInfo<SimpFile> localUpload(MultipartFile[] files, Map<String, SimpFile> simpleFiles) {
        ResultInfo<SimpFile> result = new ResultInfo<SimpFile>();
        for (MultipartFile file : files) {
            SimpFile simpFile = simpleFiles.get(file.getOriginalFilename());
            try {
                File dest = makeSureFileExists(simpFile.getPath());
                file.transferTo(dest);
            } catch (IOException e) {
                simpFile.setFileOperationException(e);
                simpFile.setFileOperationResult(SimpFile.FileOperationResult.exception);
                e.printStackTrace();
            }
        }
        result.setBeanMap(simpleFiles);
        return result;
    }

    public SimpFile file2String(SimpFile simpFile) {
        String content = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(simpFile.getPath());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, simpFile.getStringContentEncoding());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                content += line;
                content += "\r\n";
            }
        } catch (Exception e) {
            simpFile.setFileOperationResult(SimpFile.FileOperationResult.exception);
            simpFile.setFileOperationException(e);
        } finally {
            simpFile.setStringContent(content);
            return simpFile;
        }
    }

    public SimpFile string2File(SimpFile simpFile) {
        try {
            File file = makeSureFileExists(simpFile.getPath());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, simpFile.getStringContentEncoding());
            outputStreamWriter.write(simpFile.getStringContent());
            outputStreamWriter.flush();
            fileOutputStream.close();
            outputStreamWriter.close();
        } catch (Exception e) {
            simpFile.setFileOperationResult(SimpFile.FileOperationResult.exception);
            simpFile.setFileOperationException(e);
        } finally {
            return simpFile;
        }
    }


}
