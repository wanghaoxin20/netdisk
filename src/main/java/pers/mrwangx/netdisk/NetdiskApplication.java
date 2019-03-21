package pers.mrwangx.netdisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class NetdiskApplication {

    public static void main(String[] args) {
        Map<String, String> cmds = new HashMap<>();
        String[] myargs = new String[3];
        boolean isRun = false;
        if (args.length == 0) {
            System.out.println("参数为空");
            System.out.println("-u   \t网盘的用户名");
            System.out.println("-P   \t网盘的密码,默认为系统生成");
            System.out.println("-path\t网盘的本地文件夹位置");
        } else if (args.length % 2 == 1) {
            System.out.println("参数不规范");
            System.out.println("-u   \t网盘的用户名");
            System.out.println("-P   \t网盘的操作密码,默认为系统生成");
            System.out.println("-path\t网盘的本地文件夹位置");
        } else {
            for (int i = 0; i < args.length / 2; i++) {
                cmds.put(args[2 * i], args[2 * i + 1]);
            }
            String path = cmds.get("-path");
            String un = cmds.get("-u");
            String pwd = cmds.get("-P");
            if (path == null) {
                System.out.println("-path\t需指定网盘的文件夹位置");
            } else {
                File file = new File(path);
                if (!file.exists()) {
                    System.out.println("指定目录不存在");
                } else if (!file.isDirectory()) {
                    System.out.println("指定目录不能为文件");
                } else {
                    myargs[0] = path;
                    boolean next = false;

                    if (un == null) {
                        myargs[1] = "admin";
                        next = true;
                    } else if (un.matches("^\\s+|$")) {
                        System.out.println("用户名不能为空");
                    } else {
                        myargs[1] = un;
                        next = true;
                    }

                    if (next) {
                        if (pwd == null) {
                            String uuid = UUID.randomUUID().toString();
                            myargs[2] = uuid.replaceAll("-", "").substring(0, 6);
                            System.out.println("网盘用户名:" + myargs[1]);
                            System.out.println("网盘操作密码:" + myargs[2]);
                            isRun = true;
                        } else if (pwd.matches("^\\s+|$")) {
                            System.out.println("密码不能为空");
                        } else if (pwd.length() < 6) {
                            System.out.println("密码必须大于等于6位");
                        } else {
                            myargs[2] = pwd;
                            System.out.println("网盘用户名:" + myargs[1]);
                            System.out.println("网盘操作密码:" + myargs[2]);
                            isRun = true;
                        }
                    }

                }
            }
        }
        if (isRun) {
            SpringApplication.run(NetdiskApplication.class, myargs);
        }
    }

}
