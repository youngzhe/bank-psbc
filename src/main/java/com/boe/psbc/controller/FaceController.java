/**
 * Copyright (C), 2015-2020
 * FileName: FaceController
 * Author:   yangz
 * Date:     2020/8/16 13:29
 * Description: 人脸识别接口类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.boe.psbc.controller;

import com.boe.psbc.FaceParam;
import com.boe.psbc.service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/face")
@Slf4j
public class FaceController {

    @Autowired
    private FaceService faceService;

    /**
     * 模拟服务端将调用的第三方人脸识别接口
     * @param file1 图片文件
     * @param faceParam 人脸识别接口的参数
     * @return
     */
    @PostMapping("/register")
    public Map<String,Object> faceRegister(MultipartFile file1, FaceParam faceParam){
        log.info("获取的参数图片名={}",file1.getOriginalFilename());
        log.info("获取的参数对象={}",faceParam);
        HashMap<String, Object> map = new HashMap<>();
        map.put("faceParam",faceParam);
        return map;
    }

    /**
     * 摄像头终端上报图片，调用服务端接口
     * @param file
     * @return
     */
    @PostMapping("/match")
    public Map<String,Object> faceMatch(MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        try {
            Object o = faceService.faceMatch(file);
//            Object o = faceService.faceMatchTest(file);
            map.put("code",200);
            map.put("message","SUCCESS");
            map.put("data",o);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",500);
            map.put("message","FAIL");
            map.put("data",e.getMessage());
        }
        return map;
    }



}
