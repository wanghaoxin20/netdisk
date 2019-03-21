package pers.mrwangx.netdisk.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.mrwangx.netdisk.pojo.FileProp;
import pers.mrwangx.netdisk.pojo.FileProperties;
import pers.mrwangx.netdisk.pojo.Index;
import pers.mrwangx.netdisk.service.NetDiskService;
import pers.mrwangx.netdisk.util.FilepathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/********
 *
 * @author WHX
 * @description
 * @date Created in 2018/12/5 14:42
 *
 *********/
@Service
public class NetDiskServiceImpl implements NetDiskService {

    @Override
    public Map<String, FileProp> showIndex(String path, String NETDISK_PATH, ModelAndView mv) throws UnsupportedEncodingException, Exception {
        Map<String, FileProp> dirs = new TreeMap<>();
        if (path == null) { //path为null，默认为我的网盘下的目录
            path = "/";
        }
        File file = new File(NETDISK_PATH + path); //获取访问的文件
        if (!file.exists()) {
            mv.addObject("msg", "访问目录不存在");
            mv.addObject("description", "您输入的文件地址有错,请检查后再输入");
            mv.addObject("url", "/netdisk/index");
            mv.setViewName("/WEB-INF/jsp/message.jsp");
        } else {
            path = path.replaceAll("/+", "/");
            if (path.endsWith("/") && !path.equals("/")) { //规范路径
                path = path.replaceAll("/$", "");
            }
            File fcd = new File(path);
            Stack<Index> indexStack = new Stack<>();
            List<Index> indexList = new ArrayList<>();
            while (fcd != null) { //获取所有父文件夹的path
                String fname = fcd.getName();
                if (fname.matches("(^\\s+$)|(^$)")) { //如果文件夹名字为空
                    fname = "/";
                }
                indexStack.push(new Index(fname, fcd.getPath().replaceAll("\\\\", "/"))); //替换符号
                fcd = fcd.getParentFile();
            }
            while (!indexStack.empty()) { //出栈并赋值给list
                indexList.add(indexStack.pop());
            }
            mv.addObject("previous", "/netdisk/index?path=" + path.substring(0, path.lastIndexOf("/") + 1));
            if (file.isDirectory()) { //如果为文件夹
                for (File f : file.listFiles()) {
                    FileProp fp = null;
                    if (f.isDirectory()) {
                        fp = new FileProp("/netdisk/index?path=" + (path + "/" + URLEncoder.encode(f.getName(), "utf-8")).replaceAll("/+", "/"), "文件夹", "_self", "label label-danger");
                    } else {
                        fp = new FileProp("/NetDisk" + path + "/" + f.getName(), f.length(), "_blank", "label label-success");
                    }
                    dirs.put(f.getName(), fp);
                }
            }

            mv.addObject("path", path.replaceAll("/+", "/")); //路径信息
            mv.addObject("index", indexList); //全部索引信息
            mv.addObject("dirs", dirs); //文件信息

            mv.setViewName("/WEB-INF/jsp/ndindex.jsp");
        }
        return dirs;
    }


    @Override
    public void fileUpload(String savepath, String NETDISK_PATH, MultipartFile mfile, ModelAndView mv) throws Exception {
        if (mfile != null && !mfile.getOriginalFilename().matches("(^\\s+$)|(^$)")) {
            try {
                File file = new File(NETDISK_PATH + savepath, mfile.getOriginalFilename());
                mfile.transferTo(file);
                mv.addObject("msg", "文件上传成功");
                mv.addObject("description", mfile.getOriginalFilename() + "已经成功上传");
            } catch (Exception e) {
                e.printStackTrace();
                mv.addObject("msg", "文件上传出错了");
                mv.addObject("description", e.getMessage());
            }
        } else {
            mv.addObject("msg", "文件不能为空");
            mv.addObject("description", "您必须选择一个文件再上传");
        }
        mv.addObject("url", "/netdisk/index?path=" + savepath);
    }

    @Override
    public void fileUpload(String savepath, String NETDISK_PATH, MultipartFile mfile) throws IOException {
        if (mfile != null && !mfile.getOriginalFilename().matches("(^\\s+$)|(^$)")) {
            try {
                File file = new File(NETDISK_PATH + savepath, mfile.getOriginalFilename());
                mfile.transferTo(file);
            } catch (IOException e) {
                throw new IOException("文件上传错误");
            }
        } else {
            throw new IOException("文件不能为空");
        }
    }


    @Override
    public List<FileProperties> getFilePList(String NETDISK_PATH, String path) throws FileNotFoundException, UnsupportedEncodingException {
        List<FileProperties> fileList = new ArrayList<>();
        path = FilepathUtil.normalPath(path);
        File file = new File(NETDISK_PATH + path); //获取访问的文件
        if (!file.exists()) {
            throw new FileNotFoundException("路径[" + path + "]不存在");
        } else {
            if (file.isDirectory()) { //如果为文件夹
                for (File f : file.listFiles()) {
                    FileProperties fp = null;
                    if (f.isDirectory()) {
                        fp = new FileProperties(f.getName(), false, "--", "文件夹");
                    } else {
                        fp = new FileProperties(f.getName(), true, f.length(), "文件");
                    }
                    fileList.add(fp);
                }
            }
            return fileList;
        }

    }

    @Override
    public void createDir(String NETDISK_PATH, String path, String dirname) throws FileExistsException {
        path = FilepathUtil.normalPath(path);
        File file = new File(NETDISK_PATH + path, dirname);
        if (dirname == null || dirname.matches("(^\\s+$)|(^$)")) {
            throw new FileExistsException("文件夹名不能为空");
        } else if (file.exists()) {
            throw new FileExistsException("文件夹已经存在");
        }
        {
            file.mkdir();
        }
    }

    @Override
    public String getReturnData(int status, String msg, Object data) {
        JSONObject rdata = new JSONObject();
        rdata.put("status", status);
        rdata.put("msg", msg);
        rdata.put("data", data);
        return rdata.toJSONString();
    }

    @Override
    public void del(String NETDISK_PATH, String path, String name) throws IOException {
        path = FilepathUtil.normalPath(path);
        File file = new File(NETDISK_PATH + path, name);
        if (!file.exists()) {
            throw new IOException("[" + NETDISK_PATH + path + "]不存在");
        } else {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                throw new IOException("文件删除错误!");
            }
        }
    }
}
