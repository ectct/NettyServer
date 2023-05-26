package org.e.controller;


import com.alibaba.fastjson.JSONObject;
import org.e.entity.User;
import org.e.service.UserService;
import org.e.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    public JSONObject select(String uri, JSONObject json) {
        switch (uri) {
            case "/login":
                return login(json);
            default:
                return null;
        }
    }

    public JSONObject login(JSONObject json) {
        String username = json.getString("username");
        String password = json.getString("password");
        User user = userService.getByUsername(username);
        JSONObject jsonObject = new JSONObject();
        System.out.println(user);
        if (user==null || !password.equals(user.getPassword())) {
            status(jsonObject, false, "账号或密码错误");
        } else {
            String token = JWTUtil.getJwtToken(user.getId());
            json.put("cookie", token);
            status(jsonObject);
        }

        return jsonObject;
    }

    private static void status(JSONObject jsonObject) {
        status(jsonObject,true, "success");
    }

    private static void status(JSONObject jsonObject, Boolean success, String msg) {
        jsonObject.put("success", success);
        jsonObject.put("message", msg);
    }


}
