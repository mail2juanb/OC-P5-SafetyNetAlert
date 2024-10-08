package com.oc_P5.SafetyNetAlerts.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * Executed before actual handler is executed
     **/
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        //log.info("[preHandle][" + request + "]" + "[" + request.getMethod() + "]" + request.getRequestURI() + getParameters(request));
        log.info("[preHandle] Incoming request [{}] [{}]{}", request, request.getMethod(), request.getRequestURI());
        log.debug("[preHandle] Request parameters: {}", getParameters(request));
        return true;
    }

    /**
     * Executed before after handler is executed
     **/
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        //log.info("[postHandle][" + request + "]");
        log.info("[postHandle] Request completed with status : {}", response.getStatus());
    }

    /**
     * Executed after complete request is finished
     **/
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
//        if (ex != null)
//            ex.printStackTrace();
//        log.info("[afterCompletion][" + request + "][exception: " + ex + "]");
        if (ex != null) {
            log.error("[afterCompletion] Exception occurred: ", ex);
        } else {
            log.info("[afterCompletion] Request successfully handled [{}]", request.getRequestURI());
        }
    }

    private String getParameters(final HttpServletRequest request) {
        final StringBuffer posted = new StringBuffer();
        final Enumeration<?> e = request.getParameterNames();
        if (e != null)
            posted.append("?");
        while (e != null && e.hasMoreElements()) {
            if (posted.length() > 1)
                posted.append("&");
            final String curr = (String) e.nextElement();
            posted.append(curr)
                    .append("=");
            posted.append(request.getParameter(curr));
        }

        final String ip = request.getHeader("X-FORWARDED-FOR");
        final String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (!StringUtils.isBlank(ipAddr))
            posted.append("&_psip=" + ipAddr);
        return posted.toString();
    }

    private String getRemoteAddr(final HttpServletRequest request) {
        final String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}