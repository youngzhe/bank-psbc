/**
 * Copyright (C), 2015-2020
 * FileName: FaceServiceImpl
 * Author:   yangz
 * Date:     2020/8/17 16:31
 * Description: 人脸匹配实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.boe.psbc.service.impl;

import com.boe.psbc.service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FaceServiceImpl implements FaceService {

    @Value("${face.match_url}")
    private String match_url;
    @Value("${face.up_file_path}")
    private String up_file_path;
//    @Value("${face.base_info}")
    private String base_info="{\"userid\":\"20208143\",\"username\":\"whanwu\",\"transcode\":\"5000003\",\"channeltype\":\"5000\"}";

    @Override
    public Object faceMatch(MultipartFile file) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        log.info("post请求的url" + match_url);
        HttpPost httpPost = new HttpPost(match_url);
        /*MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        //builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
        //builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        String originFileName = null;
        originFileName = file.getOriginalFilename();
//			log.info("上传图片的图片名"+fileName);
//			File file1 = new File("/下载/1200-560.jpg");
        builder.addBinaryBody("file1", file.getBytes(), ContentType.MULTIPART_FORM_DATA, originFileName);// 文件流
        builder.addTextBody("label", label);
        log.info("上传图的标签label" + label);*/
        MultipartEntityBuilder builder = convert2MultipartEntityBuilder(file);
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        log.info("第三方接口调用时，参数={}",entity);
        HttpResponse response = httpClient.execute(httpPost);// 执行提交
        HttpEntity responseEntity = response.getEntity();
        log.info("第三方接口调用结束，返回值={}",responseEntity);
        String data="";
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream content = responseEntity.getContent();
            byte[] bytes = toByteArray(content);
            data = new String(bytes, "UTF-8");
            log.info("第三方接口响应结果={}",data);
        }else {
            log.info("第三方接口响应码失败={}",response.getStatusLine().getStatusCode());
            data="请求出错";
        }
        return data;
    }

    @Override
    public Object faceMatchTest(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String result=up_file_path+"/"+originalFilename;
        File upFile = new File(result);
        file.transferTo(upFile);
        return result;
    }

    public MultipartEntityBuilder convert2MultipartEntityBuilder(MultipartFile file) throws IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        builder.addTextBody("requireID","facesearch");
        builder.addBinaryBody("file1", file.getBytes(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());
        builder.addTextBody("baseinfo",base_info);
        return builder;
    }

    /**
     * 将输入流转换为输出流
     *
     * @param input
     * @return
     * @throws IOException
     */
    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FileCopyUtils.copy(input, output);
        return output.toByteArray();
    }

}
