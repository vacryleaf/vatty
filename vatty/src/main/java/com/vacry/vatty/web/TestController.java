package com.vacry.vatty.web;

import org.apache.log4j.Logger;

import com.vacry.vatty.annotation.Action;
import com.vacry.vatty.annotation.Autowired;
import com.vacry.vatty.annotation.RequestMapping;
import com.vacry.vatty.base.BaseController;
import com.vacry.vatty.model.User;
import com.vacry.vatty.service.UserService;
import com.vacry.vatty.util.JsonUtil;
import com.vacry.vatty.vo.UserVO;

@Action(value = "/test")
public class TestController extends BaseController
{
    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/insert")
    public String insert(String jsonStr)
    {
        log.info(String.format("请求体为:%s", jsonStr));
        User user = JsonUtil.parseObject(jsonStr, User.class);
        userService.insert(user);
        return super.getResultJSONStr(true, "哦", "");
    }

    @RequestMapping(value = "/login")
    public String login(String jsonStr)
    {
        log.info(String.format("请求体为:%s", jsonStr));
        User user = JsonUtil.parseObject(jsonStr, User.class);
        UserVO userVO = userService.login(user.getUsername(), user.getPassword());
        return super.getResultJSONStr(true, userVO, "");
    }
}
