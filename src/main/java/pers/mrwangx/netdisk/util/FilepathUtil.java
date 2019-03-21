package pers.mrwangx.netdisk.util;

import org.apache.commons.io.FilenameUtils;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/1/16 0:36
 *****/
public class FilepathUtil {

    public static String normalPath(String path) {
        if (path == null) { //path为null，默认为我的网盘下的目录
            path = "/";
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }
//        path = path.replaceAll("/+", "/");
//        if (path.endsWith("/") && !path.equals("/")) { //规范路径
//            path = path.replaceAll("/$", "");
//        }
        return FilenameUtils.normalize(path);
    }


}
