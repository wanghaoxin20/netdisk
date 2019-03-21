package pers.mrwangx.netdisk.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pers.mrwangx.netdisk.filter.LoginFilter;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/9 21:56
 *****/
@SuppressWarnings("unchecked")
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    //过滤器
    @Bean
    public FilterRegistrationBean loginFilterRegister() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoginFilter());
        filterRegistrationBean.addUrlPatterns("/netdisk/*");
        filterRegistrationBean.addUrlPatterns("/NetDisk/*");
        filterRegistrationBean.setName("loginFilter");
        return filterRegistrationBean;
    }

}
