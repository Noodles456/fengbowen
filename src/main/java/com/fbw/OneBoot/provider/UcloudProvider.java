package com.fbw.OneBoot.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.BucketAuthorization;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileBucketLocalAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.fbw.OneBoot.exception.CustomizeException;
import com.fbw.OneBoot.exception.ErrorCodeImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;
@Service
public class UcloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;
    @Value("${ucloud.ufile.region}")
    private String region;
    @Value("${ucloud.ufile.suffix}")
    private String suffix;
    @Value("${ucloud.ufile.expires}")
    private Integer expires;

    public String upload(InputStream fileStream, String mimeType,String fileName) {
    File file = new File("your file path");
String generatedFileName="";
String []filePaths=fileName.split("\\.");
if(filePaths.length>1){
    generatedFileName= UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
}
else{
    throw new CustomizeException(ErrorCodeImpl.FILE_UPLOAD_FAIL);
}
    try {
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);
        ObjectConfig config = new ObjectConfig(region, suffix);

        PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                .putObject(fileStream, mimeType)
                .nameAs(generatedFileName)
                .toBucket(bucketName)
                .setOnProgressListener((long bytesWritten, long contentLength)->{
                })
                .execute();
                if(response!=null&&response.getRetCode()==0){
                    String url = UfileClient.object(objectAuthorization, config)
                            .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName,expires)
                            .createUrl();
                    return url;
                }else{
throw new CustomizeException(ErrorCodeImpl.FILE_UPLOAD_FAIL);
                }
    } catch (UfileClientException e) {
        e.printStackTrace();
        throw new CustomizeException(ErrorCodeImpl.FILE_UPLOAD_FAIL);
    } catch (UfileServerException e) {
        e.printStackTrace();
        throw new CustomizeException(ErrorCodeImpl.FILE_UPLOAD_FAIL);
    }

}
}
