package com.lzydmc.bookstore.filter;

import com.lzydmc.bookstore.common.ApiRestResponse;
import com.lzydmc.bookstore.common.Constant;
import com.lzydmc.bookstore.exception.BookStoreExceptionEnum;
import com.lzydmc.bookstore.model.pojo.User;
import com.lzydmc.bookstore.service.UserService;
import org.apache.catalina.Session;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminFilter implements Filter {

    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpSession session = httpServletRequest.getSession();
        User currentUser = (User) session.getAttribute(Constant.BOOKSTORE_USER);
        if (currentUser == null){
            PrintWriter out = new HttpServletResponseWrapper(
                    (HttpServletResponse) response).getWriter();
            out.write("{\n"
                    + "    \"status\": 10007,\n"
                    + "    \"msg\": \"NEED_LOGIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
            return;
        }
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            chain.doFilter(request,response);
        }else {
            PrintWriter out = new HttpServletResponseWrapper(
                    (HttpServletResponse) response).getWriter();
            out.write("{\n"
                    + "    \"status\": 10009,\n"
                    + "    \"msg\": \"NEED_ADMIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
        }

    }

    @Override
    public void destroy() {

    }
}
