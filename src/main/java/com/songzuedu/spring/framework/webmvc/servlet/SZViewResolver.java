package com.songzuedu.spring.framework.webmvc.servlet;


import com.songzuedu.spring.framework.webmvc.SZView;

import java.io.File;
import java.util.Locale;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class SZViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFX = ".html";

    private File templateRootDir;

    public SZViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public SZView resolveViewName(String viewName, Locale locale) {
        if ((viewName == null) || "".equals(viewName.trim())) {
            return null;
        }

        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new SZView(templateFile);
    }

}
