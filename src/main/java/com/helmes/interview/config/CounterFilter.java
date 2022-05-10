package com.helmes.interview.config;


import com.helmes.interview.util.CounterStorage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/users/*")
public class CounterFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        CounterStorage.increment();
        chain.doFilter(request, response);
    }
}
