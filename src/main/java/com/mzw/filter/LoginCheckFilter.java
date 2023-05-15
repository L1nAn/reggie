package com.mzw.filter;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/17
 */

import com.alibaba.fastjson.JSON;
import com.mzw.common.BaseContext;
import com.mzw.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检测用户是否已经完成登陆
 */
//filterName：过滤器名称
    //urlPatterns:要拦截（过滤）的路径
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {//过滤器要实现Filter这个接口
    //专门用来路径比较的，路径匹配器，支持通配符，进行路径比较
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;//因为上面是ServletRequest类，所以要进行强转对象
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();//  /backend/index.html

        log.info("拦截到请求：{}", request.getRequestURI());//测试是否可以拦截到信息
        String[] urls = new String[]{//这是把不需要处理的请求放进这个数组里面
                "/employee/login",//登录时是这个路径，所以这个路径不需要检查，因为人家登录的时候，你干嘛过滤
                "/employee/logout",//退出，也没必要检查
                //静态资源，静态资源想看就看，但是里面的数据是不能看的
                "/backend/**",//backend里面的静态资源，不用处理，直接放行，下面也是
                "/front/**",
                "/user/sendMsg",//移动端发送短信，所以需要放行，要不然一点击发送短信就会跳到登录界面
                "/user/login"//移动端登陆，人家本来就是登陆的，所以也要放行
        };
        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);//放行
            return;
        }
        //4-1、判断登陆状态，如果已经登陆，则直接放行，这个是后台员工登录
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户以登陆， 用户id为：{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
            log.info("线程id为：{}", id);

            filterChain.doFilter(request, response);
            return;
        }
        //4-2、判断移动端登陆状态，如果已经登陆，则直接放行,直接复制的上面的
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录， 用户id为：{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果,通过输出流的方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行，本次请求只有一个路径，所以匹配到直接返回true就行
     *
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
