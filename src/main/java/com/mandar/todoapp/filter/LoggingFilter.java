package com.mandar.todoapp.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);

        long startTimeInMillis = System.currentTimeMillis();

        filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);

        long timeTakenInMillis = System.currentTimeMillis() - startTimeInMillis;
        String requestBody = getRequestFromByteArray(contentCachingRequestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getRequestFromByteArray(contentCachingResponseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

        log.info("Filtering logs: METHOD: {} \r\nREQUEST URI: {} \r\nREQUEST BODY: {} \r\nRESPONSE BODY: {} \r\nRESPONSE CODE: {} \r\n"
                + "TIME TAKEN: {} ", request.getMethod(), request.getRequestURI(), requestBody, responseBody, response.getStatus(), timeTakenInMillis);

        contentCachingResponseWrapper.copyBodyToResponse();
    }

    private String getRequestFromByteArray(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException" + e, e);
        }
        return "";
    }
}
