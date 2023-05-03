package com.renzf.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.renzf.domain.ResponseResult;
import com.renzf.enums.AppHttpCodeEnum;
import com.renzf.exception.SystemException;
import com.renzf.service.UploadService;
import com.renzf.utils.PathUtils;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
@Service
public class OssUploadService implements UploadService {

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        // TODO判断文件类型或者文件大小
        //获取文件原始名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
        if(!originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果判断通过上传到oss
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = upLoadOss(img,filePath);

        return ResponseResult.okResult(url);
    }

    @Test
    private String upLoadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "S6zBQo89kTvriFQwOPGcdWJj5Kyznit1tr6SxLJl";
        String secretKey = "mDQvHn3Gzo7HXlWJZjoQ77sqwz2YKybrpJJAG7ky";
        String bucket = "renzf";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rtkjcxmoo.bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
