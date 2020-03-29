package org.siu.rukawa.datasource.test.controller;

import lombok.extern.slf4j.Slf4j;

import org.siu.rukawa.datasource.test.param.Param;
import org.siu.rukawa.datasource.test.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 *
 * @Author Siu
 * @Date 2020/3/4 16:23
 * @Version 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/v1/api")
public class UserController {


    @Resource
    private UserService userService;


    @GetMapping("/get_ds_from_header")
    public Object header() {
        userService.getDsFromHeader();
        return "success";
    }

    @GetMapping("/get_ds_from_session")
    public Object session() {
        userService.getDsFromSession();
        return "success";
    }

    @GetMapping("/get_ds_from_param")
    public Object param(String ds) {
        userService.getDsFromParam(new Param(ds));
        return "success";
    }


}
