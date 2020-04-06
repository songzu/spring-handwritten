package com.songzuedu.spring.demo.service;


/**
 * <p>增删改业务</p>
 *
 * @author gengen.wang
 **/
public interface IModifyService {

    /**
     * 增加
     */
    String add(String name, String addr);

    /**
     * 修改
     */
    String edit(Integer id, String name);

    /**
     * 删除
     */
    String remove(Integer id);

    /**
     * 抛异常
     *
     * @return
     */
    String throwException(String param) throws Exception;

}
