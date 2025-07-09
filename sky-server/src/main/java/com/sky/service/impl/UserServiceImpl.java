package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    public static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User weChatLogin(UserLoginDTO userLoginDTO) {
        //Call the wechat API service to obtain the current user's openid
        HashMap map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        //Each time WeChat sends a login request,
        //it includes a request code (js_code), which can only be used once
        map.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WECHAT_LOGIN_URL, map);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openId = jsonObject.getString("openid");

        //Check whether the openid is null
        if (openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //Determine whether the current client is a new user
        User user = userMapper.getByOpenid(openId);

        //If so, automatically complete the registration
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        // return the user object
        return user;
    }
}
