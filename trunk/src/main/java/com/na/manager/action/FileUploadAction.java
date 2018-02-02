package com.na.manager.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.util.ImageUtils;

/**
 * 文件上传接口.
 * Created by Sunny on 2017/6/5 0005.
 */
@Controller
@Auth(isPublic = true)
public class FileUploadAction {
    final private static Logger logger = LoggerFactory.getLogger(FileUploadAction.class);

    //服务器地址
    @Value("${aws.file.url}")
    private String awsFileUrl;

    //文件上传根目录
    @Value("${aws.bucketName}")
    private String awsBucketName;

    //头像上传目录
    @Value("${aws.head-portrait}")
    private String awsHeadPortrait;


    public FileUploadAction(){
//        this.awsFileUrl = PropertiesUtil.getPropertiesValueByKey("aws.file.url");
//        this.awsBucketName = PropertiesUtil.getPropertiesValueByKey("aws.bucketName");
//        this.awsHeadPortrait = PropertiesUtil.getPropertiesValueByKey("aws.head-portrait");
    }

    @RequestMapping("/uploadPhoto")
    @ResponseBody
    public NaResponse uploadPhoto(@RequestParam("Filedata")MultipartFile file, HttpServletResponse response) throws Exception{
        try {
            String url = uploadFileUrl(file);
            return NaResponse.createSuccess(url);
        }catch (Exception e){
        	logger.error(e.getMessage(),e);
            return NaResponse.createError("file.upload.has.no.safe");
        }
    }

    private String uploadFileUrl(@RequestParam("Filedata") MultipartFile file) throws IOException, InterruptedException {
        String suffix = ImageUtils.securityCheck(file);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        File tempFile = File.createTempFile(uuid,suffix);

        try(FileOutputStream fileOutputStream = new FileOutputStream(tempFile);){
            ImageUtils.saveAsJPEG(file.getInputStream(),1.0f,fileOutputStream,suffix);
        }

        AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new ClasspathPropertiesFileCredentialsProvider("/aws.properties"))
                .build();
        String fileName = uuid+"."+suffix;

        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(client).build();
        PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsBucketName+"/"+this.awsHeadPortrait, fileName, tempFile);
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        putObjectRequest.withAccessControlList(accessControlList);
        Upload upload = transferManager.upload(putObjectRequest);
        upload.waitForCompletion();
        logger.info("文件上传成功：{}",uuid);
        transferManager.shutdownNow();
        return String.format("%s/%s/%s/%s", this.awsFileUrl, this.awsBucketName, this.awsHeadPortrait,fileName);
    }


    @RequestMapping("/uploadPhotos")
    @ResponseBody
    public NaResponse uploadPhotos(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("Filedata");
            List<String> urls = new ArrayList<>();
            for (MultipartFile file:files) {
                urls.add(uploadFileUrl(file));
            }
            return NaResponse.createSuccess(urls);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return NaResponse.createError("file.upload.has.no.safe");
        }
    }
}
