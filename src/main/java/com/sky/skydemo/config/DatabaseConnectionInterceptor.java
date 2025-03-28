package com.sky.skydemo.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DatabaseConnectionInterceptor implements HandlerInterceptor {

    /**
     * 预处理请求，设置动态数据源并执行拦截逻辑。
     *
     * 该方法在请求处理之前被调用，主要用于根据请求头中的租户ID设置动态数据源，
     * 并执行一些拦截逻辑（如日志记录等）。如果返回true，请求将继续处理；如果返回false，请求将被中断。
     *
     * @param request  HttpServletRequest对象，表示客户端的HTTP请求
     * @param response HttpServletResponse对象，表示服务器的HTTP响应
     * @param handler  处理请求的处理器对象
     * @return boolean 返回true表示继续处理请求，返回false表示中断请求处理
     * @throws Exception 如果在处理过程中发生异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 假设租户ID通过请求头传递
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId != null) {
            // 根据租户ID设置动态数据源
            DynamicDataSourceContextHolder.setDataSourceKey(tenantId);
        }

        // 在这里编写拦截逻辑，例如检查数据库连接等
        System.out.println("Intercepting request to: " + request.getRequestURI());

        // 返回true表示继续处理请求，返回false表示中断请求处理
        return true;
    }

    /**
     * 在请求处理完成后执行清理操作。
     * 该方法用于清除当前线程中设置的数据源键，确保线程安全并避免数据源污染。
     *
     * @param request  HttpServletRequest对象，表示当前的HTTP请求
     * @param response HttpServletResponse对象，表示当前的HTTP响应
     * @param handler  处理当前请求的处理器对象
     * @param ex       在处理请求过程中抛出的异常，如果没有异常则为null
     * @throws Exception 如果在清理过程中发生错误，则抛出异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除当前线程的数据源
        DynamicDataSourceContextHolder.clearDataSourceKey();
    }
}