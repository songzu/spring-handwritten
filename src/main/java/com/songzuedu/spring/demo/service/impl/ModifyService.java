package com.songzuedu.spring.demo.service.impl;


import com.songzuedu.spring.demo.service.IModifyService;
import com.songzuedu.spring.framework.annotation.SZService;

/**
 * <p>增删改业务</p>
 *
 * @author gengen.wang
 **/
@SZService
public class ModifyService implements IModifyService {

    /**
     * 增加
     */
    public String add(String name, String addr) {
        return "modifyService add,name=" + name + ",addr=" + addr;
    }

    /**
     * 修改
     */
    public String edit(Integer id, String name) {
        return "modifyService edit,id=" + id + ",name=" + name;
    }

    /**
     * 删除
     */
    public String remove(Integer id) {
        return "modifyService id=" + id;
    }

    @Override
    public String throwException(String param) throws Exception {
        throw new Exception("故意抛出异常！！");
    }

}
