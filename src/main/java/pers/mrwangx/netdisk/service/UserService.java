package pers.mrwangx.netdisk.service;

import javax.servlet.http.HttpSession;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/9 20:07
 *****/
public interface UserService {

    boolean isLogin(HttpSession session);

    boolean login(String username, String password);

}
