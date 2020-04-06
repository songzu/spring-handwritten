package com.songzuedu.spring.demo.action;


import com.songzuedu.spring.demo.service.IModifyService;
import com.songzuedu.spring.demo.service.IQueryService;
import com.songzuedu.spring.framework.annotation.SZAutowired;
import com.songzuedu.spring.framework.annotation.SZController;
import com.songzuedu.spring.framework.annotation.SZRequestMapping;
import com.songzuedu.spring.framework.annotation.SZRequestParam;
import com.songzuedu.spring.framework.webmvc.SZModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>公布接口url</p>
 *
 * @author gengen.wang
 **/
@SZController
@SZRequestMapping("/web")
public class MyAction {

    @SZAutowired
    IQueryService queryService;
    @SZAutowired
    IModifyService modifyService;

    @SZRequestMapping("/query.json")
    public SZModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @SZRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @SZRequestMapping("/add*.json")
    public SZModelAndView add(HttpServletRequest request, HttpServletResponse response,
                              @SZRequestParam("name") String name, @SZRequestParam("addr") String addr) {
        String result = null;
        try {
            result = modifyService.add(name, addr);
            return out(response, result);
        } catch (Exception e) {
//			e.printStackTrace();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("detail", e.getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", ""));
            return new SZModelAndView("500", model);
        }

    }

    @SZRequestMapping("/remove.json")
    public SZModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @SZRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @SZRequestMapping("/edit.json")
    public SZModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                               @SZRequestParam("id") Integer id,
                               @SZRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }

    @SZRequestMapping("/throwException.json")
    public SZModelAndView throwException(HttpServletRequest request, HttpServletResponse response,
                                         @SZRequestParam("param") String param) {
        String result = null;
        try {
            result = modifyService.throwException(param);
            return out(response, result);
        } catch (Exception e) {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("detail", e.getCause().getMessage());
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", ""));
            return new SZModelAndView("500", model);
        }

    }


    private SZModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}