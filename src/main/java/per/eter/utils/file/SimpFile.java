package per.eter.utils.file;

public class SimpFile {
    /*本地属性*/
    private String path;
    private String dir;

    /*业务属性*/
    public static String commonSeparator = "/";
    private String relativePath;
    private String stringContent;
    private String originalFilename;
    private String stringContentEncoding = "UTF-8";

    /*数据库存储属性*/
    private String name;

    /*下载属性*/
    private String downloadUrl;
    private String downloadName;

    /*上传属性*/
    private String uploadUrl;
    private String uploadName;
    private FileOperationResult fileOperationResult = FileOperationResult.success;
    private Exception fileOperationException;

    public static enum FileOperationResult {
        success,
        exception;
        private FileOperationResult() {
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getStringContent() {
        return stringContent;
    }

    public void setStringContent(String stringContent) {
        this.stringContent = stringContent;
    }

    public String getStringContentEncoding() {
        return stringContentEncoding;
    }

    public void setStringContentEncoding(String stringContentEncoding) {
        this.stringContentEncoding = stringContentEncoding;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }


    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public static String getCommonSeparator() {
        return commonSeparator;
    }

    public static void setCommonSeparator(String commonSeparator) {
        SimpFile.commonSeparator = commonSeparator;
    }

    public FileOperationResult getFileOperationResult() {
        return fileOperationResult;
    }

    public void setFileOperationResult(FileOperationResult fileOperationResult) {
        this.fileOperationResult = fileOperationResult;
    }

    public Exception getFileOperationException() {
        return fileOperationException;
    }

    public void setFileOperationException(Exception fileOperationException) {
        this.fileOperationException = fileOperationException;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
}
