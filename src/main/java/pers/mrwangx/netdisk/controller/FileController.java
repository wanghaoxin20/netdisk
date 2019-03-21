package pers.mrwangx.netdisk.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/18 1:35
 *****/
@Controller
@RequestMapping(value = {"/NetDisk"})
public class FileController implements CommandLineRunner {

    private String DIR_PATH;

    @RequestMapping(value = {"", "/"})
    public String getFile(@RequestParam String filename, HttpServletResponse response) {
        File file = new File(FilenameUtils.normalize(DIR_PATH + "/" + filename));
        if (file.exists()) {
            try {
                response.setHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s",URLEncoder.encode(FilenameUtils.getName(filename), "UTF-8"), "utf-8"));
                FileInputStream fin = new FileInputStream(file);
                int length = 0;
                byte[] data = new byte[1024];
                while ( (length  = fin.read(data, 0, data.length)) != -1) {
                    response.getOutputStream().write(data, 0, length);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public void run(String... args) throws Exception {
        this.DIR_PATH = args[0];
    }
}
