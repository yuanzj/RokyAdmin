package com.rokyinfo.service;

import com.rokyinfo.domain.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by yuanzhijian on 2017/1/8.
 */
public interface SysUserService extends UserDetailsService {

    /**
     * 根据用户名获取用户
     * @param userName
     * @return SysUser
     */
    public SysUser getSysUserByName(String userName);

}
