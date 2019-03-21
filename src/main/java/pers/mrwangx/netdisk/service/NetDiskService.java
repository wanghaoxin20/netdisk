package pers.mrwangx.netdisk.service;

import org.apache.commons.io.FileExistsException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.mrwangx.netdisk.pojo.FileProp;
import pers.mrwangx.netdisk.pojo.FileProperties;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/********
 *
 * @author WHX
 * @description
 * @date Created in 2018/12/5 14:37
 *
 *********/
public interface NetDiskService {

    Map<String, FileProp> showIndex(String savepath, String NETDISK_PATH, ModelAndView mv) throws UnsupportedEncodingException, Exception;

    void fileUpload(String savepath, String NETDISK_PATH, MultipartFile mfile, ModelAndView mv) throws Exception;

    void fileUpload(String savepath, String NETDISK_PATH, MultipartFile mfile) throws IOException;

    List<FileProperties> getFilePList(String NETDISK_PATH, String path) throws FileNotFoundException, UnsupportedEncodingException;

    void createDir(String NETDISK_PATH, String path, String dirname) throws FileExistsException;

    String getReturnData(int status, String msg, Object data);

    void del(String NETDISK_PATH, String path, String name) throws IOException;

}
