package com.yunhui.mapper;

import com.yunhui.bean.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-05-13 19:54
 */
public interface UserMapper {

    User selectByPK(@Param("userId") int userId);

    Long insertSelective(User user);
}
