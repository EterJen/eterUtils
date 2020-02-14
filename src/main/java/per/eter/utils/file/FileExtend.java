package per.eter.utils.file;

import java.io.File;
import java.net.URI;

public class FileExtend  extends File {

    public FileExtend(String pathname) {
        super(pathname);
    }

    public FileExtend(String parent, String child) {
        super(parent, child);
    }

    public FileExtend(File parent, String child) {
        super(parent, child);
    }

    public FileExtend(URI uri) {
        super(uri);
    }

    private String webOutName;

    public String getWebOutName() {
        return webOutName;
    }

    public void setWebOutName(String webOutName) {
        this.webOutName = webOutName;
    }
}
