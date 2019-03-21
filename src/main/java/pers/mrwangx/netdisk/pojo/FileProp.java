package pers.mrwangx.netdisk.pojo;

/********
 *
 * @author WHX
 * @description
 * @date Created in 2018/12/4 20:52
 *
 *********/
public class FileProp {

    private String url;
    private String size;
    private String target;
    private String cls;

    public FileProp(String url, long size, String target, String cls) {
        this.url = url;
        setSize(size);
        this.target = target;
        this.cls = cls;
    }

    public FileProp(String url, String size, String target, String cls) {
        this.url = url;
        this.size = size;
        this.target = target;
        this.cls = cls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(long size) {
        if (size > 0 && size < 1024) {
            this.size = size + "Byte";
        } else if (size >= 1024 && size < 1048576) {
            this.size = String.format("%.2f", size / 1024.00) + " KB";
        } else if (size >= 8192 && size < 1073741824) {
            this.size = String.format("%.2f", size / 1048576.00) + " MB";
        } else {
            this.size = String.format("%.2f", size / 1073741824.00) + " GB";
        }
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }
}
