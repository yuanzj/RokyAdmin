package com.rokyinfo.dao;

import com.rokyinfo.domain.SysUser;
import org.apache.ibatis.annotations.*;

/**
 * Created by yuanzhijian on 2017/1/8.
 */
@Mapper
public interface SysUserMapper {

    /**
     * 根据用户名获取用户
     * @param userName
     * @return SysUser
     */
    @Results({
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "status", column = "status")
    })
    @Select("SELECT username, password, status FROM sys_user WHERE username = #{name}")
    public SysUser getSysUserByName(@Param("name") String userName);

}
