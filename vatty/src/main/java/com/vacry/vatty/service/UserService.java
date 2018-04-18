package com.vacry.vatty.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vacry.vatty.dto.UserDTO;
import com.vacry.vatty.model.User;
import com.vacry.vatty.vo.UserVO;

public interface UserService
{
    public void insert(User user);

    public void update(User user);

    public Long countAll();

    public List<UserVO> findAll(UserDTO dto);

    public UserVO login(@Param("username") String username, @Param("password") String password);
}
