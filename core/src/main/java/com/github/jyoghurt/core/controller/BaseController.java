package com.github.jyoghurt.core.controller;


import com.github.jyoghurt.core.exception.BaseAccidentException;
import com.github.jyoghurt.core.exception.BaseErrorException;
import com.github.jyoghurt.core.result.HttpResultEntity;
import com.github.jyoghurt.core.result.HttpResultHandle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 控制器基类
 *
 * @author JiangYingxu
 */
public class BaseController {
    public static String EXCEPTION = "Exception";
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    static {
        //定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

    }

    @ExceptionHandler
    public HttpResultEntity<?> exceptionHandler(HttpServletRequest request, Exception ex) {
        request.setAttribute(EXCEPTION, ex);
        if (ex instanceof BaseAccidentException) {
            if (((BaseAccidentException) ex).getLogFlag()) {
                logger.error(ex.getMessage() + "\n method:★{}★\n parameterValues : ★{}★", request.getContextPath(),
                        WebUtils.getParametersStartingWith(request, null).toString(), ex);
            } else {
                logger.warn(ex.getMessage() + "\n method:★{}★\n parameterValues : ★{}★", request.getContextPath(),
                        WebUtils.getParametersStartingWith(request, null).toString(), ex);
            }
            return HttpResultHandle.getErrorResult(((BaseAccidentException) ex).getErrorCode().replace("ERROR_",
                    StringUtils.EMPTY), ex.getMessage());
        }

        if (ex instanceof BaseErrorException) {
            logger.error(ex.getMessage() + "\n method:★{}★\n parameterValues : ★{}★", request.getContextPath(),
                    WebUtils.getParametersStartingWith(request, null).toString(), ex);
            return HttpResultHandle.getErrorResult();
        }
        logger.error("uncaught  exception,\n method:★{}★\n parameterValues : ★{}★", request.getContextPath(),
                WebUtils.getParametersStartingWith(request, null).toString(), ex);
        return HttpResultHandle.getErrorResult();
    }


    public HttpResultEntity<?> getSuccessResult() {
        return HttpResultHandle.getSuccessResult();
    }


    public HttpResultEntity<?> getSuccessResult(Object result) {
        return HttpResultHandle.getSuccessResult(result);
    }


    public static HttpResultEntity<?> getErrorResult() {
        return HttpResultHandle.getErrorResult();
    }


    public static HttpResultEntity<?> getErrorResult(BaseAccidentException e) {
        if (null == e) {
            return getErrorResult();
        }
        logger.warn(e.getMessage(), e.getCause());
        if (null == e.getExceptionBody()) {
            return getErrorResult();
        }
        return HttpResultHandle.getErrorResult(e.getErrorCode(), e.getExceptionBody().getMessage());
    }

    public static HttpResultEntity<?> getErrorResult(Object result) {
        return HttpResultHandle.getErrorResult(result);
    }

}
