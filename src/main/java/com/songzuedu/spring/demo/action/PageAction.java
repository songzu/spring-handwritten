package com.songzuedu.spring.demo.action;

import com.songzuedu.spring.demo.service.IQueryService;
import com.songzuedu.spring.framework.annotation.SZAutowired;
import com.songzuedu.spring.framework.annotation.SZController;
import com.songzuedu.spring.framework.annotation.SZRequestMapping;
import com.songzuedu.spring.framework.annotation.SZRequestParam;
import com.songzuedu.spring.framework.webmvc.SZModelAndView;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
@SZController
@SZRequestMapping("/")
public class PageAction {

    @SZAutowired
    IQueryService queryService;

    @SZRequestMapping("/first.html")
    public SZModelAndView query(@SZRequestParam("name") String name) {
        if (StringUtils.isBlank(name)){
            throw new RuntimeException("请求参数不能为空！");
        }

        String result = queryService.query(name);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", name);
        model.put("data", result);
        model.put("token", "123456");
        return new SZModelAndView("first.html", model);
    }

    @SZRequestMapping()
    public SZModelAndView index() {

        return new SZModelAndView("index.html", new HashMap<>());
    }

}
