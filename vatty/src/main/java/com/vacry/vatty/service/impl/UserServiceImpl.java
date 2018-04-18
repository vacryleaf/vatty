package com.vacry.vatty.service.impl;

import java.util.List;

import com.vacry.vatty.annotation.Autowired;
import com.vacry.vatty.annotation.Service;
import com.vacry.vatty.dto.UserDTO;
import com.vacry.vatty.mapper.UserMapper;
import com.vacry.vatty.model.User;
import com.vacry.vatty.service.UserService;
import com.vacry.vatty.vo.UserVO;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserMapper mapper;

    public void insert(User user)
    {
        mapper.insert(user);
    }

    public UserVO login(String username, String password)
    {
        UserVO user = mapper.login(username, password);
        return user;
    }

    public void update(User user)
    {
        mapper.update(user);
    }

    public Long countAll()
    {
        Long count = mapper.countAll();
        return count;
    }

    public List<UserVO> findAll(UserDTO dto)
    {
        List<UserVO> list = mapper.findAll(dto);
        return list;
    }
}