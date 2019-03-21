package pers.mrwangx.netdisk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileExistsException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.mrwangx.netdisk.pojo.FileProperties;
import pers.mrwangx.netdisk.service.NetDiskService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/********
 *
 * @author WHX
 * @description
 * @date Created in 2018/12/4 20:12
 *
 *********/
@CrossOrigin
@Controller
@RequestMapping(value = {"/netdisk"})
public class NetDisk implements CommandLineRunner {

    private String DIR_PATH;

    @Resource
    private NetDiskService netDiskService;

    @RequestMapping(path = {"", "/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String index() {
        return "/main.html";
    }

    @RequestMapping(path = {"/files"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String getFileProps(@RequestBody String reqData) {
        int status = 200;
        String msg = null;
        Object data = null;
        try {
            JSONObject reqJson = JSON.parseObject(reqData);
            List<FileProperties> flist = netDiskService.getFilePList(DIR_PATH, reqJson.getString("path"));
            msg = "success";
            data = flist;
        } catch (FileNotFoundException e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        }
        return netDiskService.getReturnData(status, msg, data);
    }

    @RequestMapping(path = "/mkdir", method = {RequestMethod.POST, RequestMethod.GET}, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String mkdir(@RequestBody String reqData) {
        int status = 200;
        String msg = null;
        try {
            JSONObject reqJson = JSON.parseObject(reqData);
            netDiskService.createDir(DIR_PATH, reqJson.getString("path"), reqJson.getString("dirname"));
            msg = "success mkdir";
        } catch (FileExistsException e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        }
        return netDiskService.getReturnData(status, msg, null);
    }

    /**
     * @param mfile
     * @return
     */
    @RequestMapping(path = {"/upload"}, method = {RequestMethod.POST})
    public ModelAndView upload(@RequestParam(name = "file") MultipartFile mfile, @RequestParam String path) {
        ModelAndView mv = new ModelAndView();
        try {
            netDiskService.fileUpload(path, DIR_PATH, mfile, mv);
            mv.setViewName("/WEB-INF/jsp/message.jsp");
        } catch (Exception e) {
            mv.addObject("msg", "服务器出了点错误");
            mv.addObject("description", e.getMessage());
            mv.addObject("url", "/netdisk/index");
            mv.setViewName("/WEB-INF/jsp/message.jsp");
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * @param mfile
     * @param path
     * @return
     */
    @RequestMapping(path = {"/uploadfile"}, method = {RequestMethod.POST}, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String uploadFile(@RequestParam(name = "file") MultipartFile mfile, @RequestParam String path) {
        int status = 200;
        String msg = null;
        try {
            netDiskService.fileUpload(path, DIR_PATH, mfile);
            msg = "上传文件成功";
        } catch (IOException e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        }
        return netDiskService.getReturnData(status, msg, null);
    }

    @RequestMapping(path = {"/deldir"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String delDir(@RequestBody String reqData) {
        int status = 200;
        String msg = null;
        try {
            JSONObject reqJson = JSON.parseObject(reqData);
            netDiskService.del(DIR_PATH, reqJson.getString("path"), reqJson.getString("name"));
        } catch (IOException e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        }
        return netDiskService.getReturnData(status, msg, null);
    }

    @RequestMapping(path = {"/loginout"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String loginOut(HttpSession session) {
        int status = 200;
        String msg = null;
        try {
            session.removeAttribute("islogin");
        } catch (Exception e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        }
        return netDiskService.getReturnData(status, msg, null);
    }


    @Override
    public void run(String... args) throws Exception {
        this.DIR_PATH = args[0];
    }
}
