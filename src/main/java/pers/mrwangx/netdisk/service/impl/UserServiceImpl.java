package pers.mrwangx.netdisk.service.impl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import pers.mrwangx.netdisk.service.UserService;

import javax.servlet.http.HttpSession;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/9 20:08
 *****/
@Service
public class UserServiceImpl implements UserService, CommandLineRunner {

    private String UN;
    private String PWD;


    @Override
    public boolean isLogin(HttpSession session) {
        return session.getAttribute("islogin") != null;
    }

    @Override
    public boolean login(String username, String password) {
        return UN.equals(username) && PWD.equals(password);
    }

    @Override
    public void run(String... args) throws Exception {
        this.UN = args[1];
        this.PWD = args[2];
    }
}
