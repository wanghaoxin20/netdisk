package pers.mrwangx.netdisk.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/****
 * 登录过滤器
 * @author:MrWangx
 * @description
 * @Date 2019/3/9 21:37
 *****/
public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getSession().getAttribute("islogin") == null) {
            httpServletResponse.sendRedirect("/user/lg");
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

}
