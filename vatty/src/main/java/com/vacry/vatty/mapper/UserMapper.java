package com.vacry.vatty.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.vacry.vatty.dto.UserDTO;
import com.vacry.vatty.model.User;
import com.vacry.vatty.vo.UserVO;

@Mapper
public interface UserMapper
{
    void insert(User user);

    void update(User user);

    Long countAll();

    List<UserVO> findAll(UserDTO dto);

    UserVO login(@Param("username") String username, @Param("password") String password);
}