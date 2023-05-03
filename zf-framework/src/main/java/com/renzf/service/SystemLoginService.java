package com.renzf.service;

import com.renzf.domain.ResponseResult;
import com.renzf.domain.entity.User;

public interface SystemLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
