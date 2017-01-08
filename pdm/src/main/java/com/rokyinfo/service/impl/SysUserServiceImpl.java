package com.rokyinfo.service.impl;


import com.rokyinfo.dao.SysUserMapper;
import com.rokyinfo.domain.SysUser;
import com.rokyinfo.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanzhijian on 2017/1/8.
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SysUser mSysUser = getSysUserByName(userName);
        if (mSysUser == null) {
            throw new UsernameNotFoundException(userName);
        }

        //定义权限集合
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        boolean booleanStatus = true;
        if(mSysUser.getStatus() == 0){
            booleanStatus = false;
        }
        User user = new User(mSysUser.getUsername(),mSysUser.getPassword(),booleanStatus,true,true, true, grantedAuthorities);
        return user;
    }

    @Override
    public SysUser getSysUserByName(String userName) {
        return userMapper.getSysUserByName(userName);
    }
}
