package com.songzuedu.spring.framework.webmvc.servlet;

import com.songzuedu.spring.framework.annotation.SZController;
import com.songzuedu.spring.framework.annotation.SZRequestMapping;
import com.songzuedu.spring.framework.context.SZApplicationContext;
import com.songzuedu.spring.framework.webmvc.SZHandlerMapping;
import com.songzuedu.spring.framework.webmvc.SZModelAndView;
import com.songzuedu.spring.framework.webmvc.SZView;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p></p>
 *
 * @author gengen.wang
 **/
//Servlet 只是作为一个 MVC 的启动入口
@Slf4j
public class SZDispatcherServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private SZApplicationContext context;

    //SZHandlerMapping 最核心的设计，也是最经典的
    private List<SZHandlerMapping> handlerMappings = new ArrayList<>();

    private Map<SZHandlerMapping, SZHandlerAdapter> handlerAdapters = new HashMap<>();

    private List<SZViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("init()方法调用");
        //1、初始化ApplicationContext
        context = new SZApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        initStrategies(context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            this.doDispatcher(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("<font size='25' color='red'>500 Exception</font><br/>Details:<br/>" +
                    Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll("\\s", "\r\n") +
                    "<br/><font color='blue'><i>Copyright@GupaoEDU</i></font>");
            e.printStackTrace();
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1、通过从request中拿到URL，去匹配一个HandlerMapping
        SZHandlerMapping handler = getHandler(req);

        if (handler == null) {
            processDispatchResult(req, resp, new SZModelAndView("404"));
            return;
        }

        //2、准备调用前的参数
        SZHandlerAdapter ha = getHandlerAdapter(handler);

        //3、真正的调用方法,返回ModelAndView存储了要传页面上的值，和页面模板的名称
        SZModelAndView mv = ha.handle(req, resp, handler);

        //这一步才是真正的输出
        processDispatchResult(req, resp, mv);

    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, SZModelAndView mv) throws Exception {
        //把给我的ModleAndView变成一个HTML、OuputStream、json、freemark、veolcity
        //ContextType
        if (mv == null) {
            return;
        }

        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (SZViewResolver viewResolver : this.viewResolvers) {
            SZView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    private SZHandlerAdapter getHandlerAdapter(SZHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        SZHandlerAdapter ha = this.handlerAdapters.get(handler);
        if (ha.supports(handler)) {
            return ha;
        }
        return null;
    }

    private SZHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url.replace(contextPath, "").replaceAll("/+", "/");

        for (SZHandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            //如果没有匹配上继续下一个匹配
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }

        return null;
    }

    //初始化策略
    protected void initStrategies(SZApplicationContext context) {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);


        //handlerMapping，必须实现
        initHandlerMappings(context);
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);


        //初始化视图转换器，必须实现
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(SZApplicationContext context) {
    }

    private void initViewResolvers(SZApplicationContext context) {
        //拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i++) {
            //这里主要是为了兼容多模板，所以模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所以还是搞了个List

            this.viewResolvers.add(new SZViewResolver(templateRoot));
        }

    }

    private void initRequestToViewNameTranslator(SZApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(SZApplicationContext context) {
    }

    private void initHandlerAdapters(SZApplicationContext context) {
        //把一个requet请求变成一个handler，参数都是字符串的，自动配到handler中的形参
        //他要拿到HandlerMapping才能干活
        //就意味着，有几个HandlerMapping就有几个HandlerAdapter
        for (SZHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new SZHandlerAdapter());
        }
    }

    private void initHandlerMappings(SZApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            try {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(SZController.class)) {
                    continue;
                }

                String baseUrl = "";
                //获取Controller的url配置
                if (clazz.isAnnotationPresent(SZRequestMapping.class)) {
                    SZRequestMapping requestMapping = clazz.getAnnotation(SZRequestMapping.class);
                    baseUrl = requestMapping.value();
                }
                //获取Method的url配置
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    //没有加RequestMapping注解的直接忽略
                    if (!method.isAnnotationPresent(SZRequestMapping.class)) {
                        continue;
                    }
                    //映射URL
                    SZRequestMapping requestMapping = method.getAnnotation(SZRequestMapping.class);
                    //  /demo/query
                    //  (//demo//query)
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);

                    this.handlerMappings.add(new SZHandlerMapping(pattern, controller, method));
                    log.info("Mapped " + regex + "," + method);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initThemeResolver(SZApplicationContext context) {
    }

    private void initLocaleResolver(SZApplicationContext context) {
    }

    private void initMultipartResolver(SZApplicationContext context) {
    }
}
