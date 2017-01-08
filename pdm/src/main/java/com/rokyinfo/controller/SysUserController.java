package com.rokyinfo.controller;

import com.rokyinfo.domain.SysUser;
import com.rokyinfo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by yuanzhijian on 2017/1/8.
 */
@RestController
@RequestMapping(value="/users")
public class SysUserController {

    @Autowired
    private SysUserService mSysUserService;

    static Map<Long, SysUser> users = Collections.synchronizedMap(new HashMap<Long, SysUser>());

    @RequestMapping(value="/", method= RequestMethod.GET)
    public List<SysUser> getUserList() {
        // 处理"/users/"的GET请求，用来获取用户列表
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
        List<SysUser> r = new ArrayList<SysUser>(users.values());
        return r;
    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    public String postUser(@ModelAttribute SysUser sysUser) {
        // 处理"/users/"的POST请求，用来创建User
        // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
//        users.put(sysUser.getId(), sysUser);
        return "success";
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public SysUser getUser(@PathVariable Long id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        return mSysUserService.getSysUserByName("admin");
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @ModelAttribute SysUser sysUser) {
        // 处理"/users/{id}"的PUT请求，用来更新User信息
        SysUser u = users.get(id);
//        u.setName(sysUser.getName());
//        u.setAge(sysUser.getAge());
        users.put(id, u);
        return "success";
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        // 处理"/users/{id}"的DELETE请求，用来删除User
        users.remove(id);
        return "success";
    }

}
