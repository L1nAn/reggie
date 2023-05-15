package com.mzw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzw.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
