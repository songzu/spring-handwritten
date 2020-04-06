package com.songzuedu.spring;


import com.songzuedu.spring.demo.action.MyAction;
import com.songzuedu.spring.framework.context.SZApplicationContext;


/**
 * <p></p>
 *
 * @author gengen.wang
 **/
public class TestApp {

    public static void main(String[] args) {
        SZApplicationContext context = new SZApplicationContext("classpath:application.properties");
        try {
            Object object = context.getBean(MyAction.class);
            System.out.println(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
