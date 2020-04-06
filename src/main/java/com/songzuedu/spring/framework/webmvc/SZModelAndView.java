package com.songzuedu.spring.framework.webmvc;

import java.util.Map;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZModelAndView {

    private String viewName;

    private Map<String, ?> model;

    public SZModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public SZModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

}
