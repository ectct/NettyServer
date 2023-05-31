package org.e.controller;


import com.alibaba.fastjson.JSONObject;
import org.e.entity.Questionnaire;
import org.e.entity.User;
import org.e.entity.UserInfo;
import org.e.service.QuestionnaireService;
import org.e.service.UserInfoService;
import org.e.service.UserService;
import org.e.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    QuestionnaireService questionnaireService;

    public JSONObject select(String uri, JSONObject json) {
        switch (uri) {
            case "/login":
                return login(json);
            case "/register":
                return register(json);
            //发送传感器数据
            case "/sensor":
                return sensor(json);
            //订阅传感器
            case "/subscribe":
                return subscribe(json);
            //用户数据
            case "/user/add":
                return setUserInfo(json);
            case "/user/get":
                return getUserInfo(json);
            case "/questionnaire":
                return questionnaire(json);
            default:
                return null;
        }
    }

    private JSONObject questionnaire(JSONObject input) {
        Integer id = JWTUtil.getMemberIdByJwtToken(input.getString("cookie"));
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(id);
        questionnaire.setMood(input.getString("mood"));
        questionnaire.setAppetite(input.getString("appetite"));
        questionnaire.setSport(input.getString("sport"));
        if (questionnaireService.getById(id) != null) {
            questionnaireService.updateById(questionnaire);
        } else {
            questionnaireService.save(questionnaire);
        }
        JSONObject res = new JSONObject();
        status(res);
        return res;
    }

    private JSONObject getUserInfo(JSONObject input) {

        Integer id = JWTUtil.getMemberIdByJwtToken(input.getString("cookie"));
        UserInfo userInfo = userInfoService.getById(id);
        JSONObject res = (JSONObject) JSONObject.toJSON(userInfo);
        if (res==null) {
            res = new JSONObject();
            status(res, false, "无信息");
            return res;
        }
        status(res);
        return res;
    }

    private JSONObject setUserInfo(JSONObject input) {
        UserInfo userInfo = new UserInfo();
        JSONObject credentials = (JSONObject) input.get("credentials");
        Integer id = JWTUtil.getMemberIdByJwtToken(input.getString("cookie"));
        userInfo.setId(id);
        userInfo.setName(credentials.getString("name"));
        userInfo.setAge(Integer.valueOf(credentials.getString("age")));
        userInfo.setSex(credentials.getString("sex"));
        userInfo.setHeight(Double.valueOf(credentials.getString("height")));
        userInfo.setWeight(Double.valueOf((credentials.getString("weight"))));
        if (userInfoService.getById(id) != null) {
            userInfoService.updateById(userInfo);
        } else {
            userInfoService.save(userInfo);
        }
        JSONObject res = new JSONObject();
        status(res);
        return res;
    }

    private JSONObject subscribe(JSONObject input) {
        Integer userId = JWTUtil.getMemberIdByJwtToken(input.getString("cookie"));
        String string = input.getString("subscribe");
        List<Integer> subscribe = JSONObject.parseArray(string, Integer.class);
        userService.updateSenor(userId, subscribe);
        JSONObject res = new JSONObject();
        status(res);
        return res;
    }

    private JSONObject login(JSONObject json) {
        String username = json.getString("username");
        String password = json.getString("password");
        User user = userService.getByUsername(username);
        JSONObject jsonObject = new JSONObject();
        System.out.println(user);
        if (user==null || !password.equals(user.getPassword())) {
            status(jsonObject, false, "账号或密码错误");
        } else {
            String token = JWTUtil.getJwtToken(user.getId());
            jsonObject.put("cookie", token);
            status(jsonObject);
        }
        return jsonObject;
    }

    private JSONObject register(JSONObject input) {
        User user = new User();
        user.setUsername(input.getString("username"));
        user.setPassword(input.getString("password"));
        user.setPhone(input.getString("phone"));
        user.setMail(input.getString("mail"));
        JSONObject res = new JSONObject();
        try {
            userService.save(user);
            status(res);
        } catch (DuplicateKeyException e){
            status(res, false, "用户名或邮箱已占用");
        }
        return res;
    }

    private JSONObject sensor(JSONObject input) {
        Integer id = JWTUtil.getMemberIdByJwtToken(input.getString("cookie"));
        JSONObject res = userService.getSensorById(id);
        status(res);
        return res;
    }

    private static void status(JSONObject jsonObject) {
        status(jsonObject,true, "success");
    }

    private static void status(JSONObject jsonObject, Boolean success, String msg) {
        jsonObject.put("success", success);
        jsonObject.put("message", msg);
    }


}
