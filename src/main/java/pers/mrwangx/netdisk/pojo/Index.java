package pers.mrwangx.netdisk.pojo;

/********
 *
 * @author WHX
 * @description
 * @date Created in 2018/12/5 17:06
 *
 *********/
public class Index {

    private String name;
    private String path;

    public Index(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
