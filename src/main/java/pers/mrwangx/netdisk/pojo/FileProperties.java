package pers.mrwangx.netdisk.pojo;

import com.alibaba.fastjson.annotation.JSONField;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/1/15 0:58
 *****/
public class FileProperties {

    private String name;
    @JSONField(name = "isFile")
    private boolean isFile;
    private String size;
    private String filetype;

    public FileProperties(String name, boolean isFile, long size, String filetype) {
        this.name = name;
        this.isFile = isFile;
        setSize(size);
        this.filetype = filetype;
    }

    public FileProperties(String name, boolean isFile, String size, String filetype) {
        this.name = name;
        this.isFile = isFile;
        this.size = size;
        this.filetype = filetype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
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

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
}
