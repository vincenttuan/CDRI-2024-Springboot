package com.example.demo.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class HstsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            // 將標頭資訊傳遞給瀏覽器, 因此瀏覽器將會根據此標頭進行保護
        	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
            httpServletResponse.setHeader("X-Frame-Options", "DENY");
            httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
            httpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");
        }
        chain.doFilter(request, response);
    }

}

