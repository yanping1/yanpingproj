package com.dkha.common.fileupload;

import com.alibaba.fastjson.JSONObject;
import com.dkha.common.validate.UtilValidate;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:
 * @Description: (please write your description)
 * @author: {开发人的姓名}
 * @date:
 * @: 成都电科惠安
 */
@Component
@PropertySource({"classpath:uploadFile.properties"})
public class MinioUtil {

    public static final Logger logger = LoggerFactory.getLogger(MinioUtil.class);

    @Value("${minio_url}")
    private String minio_url;
    @Value("${minio_name}")
    private String minio_name;

    @Value("${minio_pass}")
    private String minio_pass;

    @Value("${minio_bucketName}")
    private String minio_bucketName;


    /**
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadImage
     * @Description:上传图片
     */
    public JSONObject uploadImage(InputStream inputStream, String fileName, String suffix) throws Exception {
        return upload(inputStream, fileName, suffix, "image/jpeg");
    }

    /**
     * 上传图片
     *
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     */
    public JSONObject uploadImage(InputStream inputStream, String suffix) throws Exception {
        return upload(inputStream, null, suffix, "image/jpeg");
    }

    /**
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadVideo
     * @Description:上传视频
     */
    public JSONObject uploadVideo(InputStream inputStream, String fileName, String suffix) throws Exception {
        return upload(inputStream, fileName, suffix, "video/mp4");
    }


    /**
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadVideo
     * @Description:上传文件
     */
    public JSONObject uploadFile(InputStream inputStream, String fileName, String suffix) throws Exception {
        return upload(inputStream, fileName, suffix, "application/octet-stream");
    }
    public JSONObject uploadFile(InputStream inputStream,String parentdir, String fileName, String suffix) throws Exception {
        return upload(inputStream, parentdir,fileName, suffix, "application/octet-stream");
    }
    /**
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     * @Title: uploadVideo
     * @Description:上传文件
     */
    public JSONObject uploadFiles(InputStream inputStream, String folder,String fileName, String suffix) throws Exception {
        return uploads(inputStream, folder,fileName, suffix, "application/octet-stream");
//        return upload(inputStream, fileName, suffix, "audio/mp3");
    }
    /**
     * 上传字符串大文本内容
     *
     * @param str
     * @return
     * @throws Exception
     * @Title: uploadString
     * @Description:描述方法
     */
    public JSONObject uploadString(String str, String fileName) throws Exception {
        if (!StringUtils.isEmpty(str)) {
            return new JSONObject();
        }
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return upload(inputStream, fileName, null, "text/html");
    }


    /**
     * @return
     * @throws Exception
     * @Title: upload
     * @Description:上传主功能
     */
    private JSONObject upload(InputStream inputStream, String fileName, String suffix, String contentType) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(minio_bucketName);
        if (isExist) {
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储文件。
            minioClient.makeBucket(minio_bucketName);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        String objectName = ymd + "/" + (UtilValidate.isEmpty(fileName) ? getUniqueFileName() : fileName) + (suffix != null ? suffix : "");
        minioClient.putObject(minio_bucketName, objectName, inputStream, contentType);
        String url = minioClient.getObjectUrl(minio_bucketName, objectName);
        map.put("flag", "0");
        map.put("mess", "上传成功");
        map.put("url", url);
        map.put("fullName", objectName);
        map.put("path", objectName);
        return map;
    }
    /**
     * @return
     * @throws Exception
     * @Title: upload
     * @Description:上传主功能
     */
    private JSONObject upload(InputStream inputStream, String parentpath, String fileName, String suffix, String contentType) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(minio_bucketName);
        if (isExist) {
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储文件。
            minioClient.makeBucket(minio_bucketName);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        String objectName = ymd +"/"+parentpath+ "/" + (UtilValidate.isEmpty(fileName) ? getUniqueFileName() : fileName) + (suffix != null ? suffix : "");
        minioClient.putObject(minio_bucketName, objectName, inputStream, contentType);
        String url = minioClient.getObjectUrl(minio_bucketName, objectName);
        map.put("flag", "0");
        map.put("mess", "上传成功");
        map.put("url", url);
        map.put("fullName", objectName);
        map.put("path", objectName);
        return map;
    }
    /**
     * @return
     * @throws Exception
     * @Title: upload
     * @Description:上传主功能
     */
    private JSONObject uploads(InputStream inputStream,String folder, String fileName, String suffix, String contentType) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(minio_bucketName);
        if (isExist) {
//                System.out.println("Bucket already exists.");
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储文件。
            minioClient.makeBucket(minio_bucketName);
        }

        String objectName = folder + "/" + (UtilValidate.isEmpty(fileName) ? getUniqueFileName() : fileName) + (suffix != null ? suffix : "");
        minioClient.putObject(minio_bucketName, objectName, inputStream, contentType);
        String url = minioClient.getObjectUrl(minio_bucketName, objectName);
        map.put("flag", "0");
        map.put("mess", "上传成功");
        map.put("url", url);
        map.put("fullName", objectName);
//        map.put("path", minio_bucketName + "/" + objectName);
        map.put("path", objectName);
        return map;
    }
    /**
     * 获取上传对象信息
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public ObjectStat objectStat(String bucketName, String objectName) throws Exception {
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        ObjectStat objectStat = minioClient.statObject(bucketName, objectName);
        return objectStat;
    }

    /**
     * 删除多个
     *
     * @param bucketName
     * @param objectNames
     * @return
     * @throws Exception
     */
    public JSONObject removeObjects(String bucketName, List<String> objectNames) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        for (Result<DeleteError> errorResult : minioClient.removeObjects(bucketName, objectNames)) {
            DeleteError error = errorResult.get();
            System.out.println("Failed to remove '" + error.objectName() + "'. Error:" + error.message());
            map.put("results", error);
        }
//        Iterable<Result<DeleteError>> results =minioClient.removeObjects(bucketName,objectNames);
//        map.put("results",results);
        return map;
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public JSONObject removeObject(String bucketName, String objectName) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        minioClient.removeObject(bucketName, objectName);
        map.put("results", "ok");
        return map;
    }

    /**
     * 根据存储箱和前缀查询对象集合
     *
     * @param bucketName
     * @param prefix     文件对象前缀可以不传）
     * @return
     */
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix) {
        Iterable<Result<Item>> myObjects = new ArrayList<>();
        try {
            MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
            // Check whether 'mybucket' exists or not.
            boolean found = minioClient.bucketExists(bucketName);
            if (found) {
                //如果prefix为null或者‘’
                if ("".equals(prefix) || prefix == null) {
                    myObjects = minioClient.listObjects(bucketName);
                } else {
                    //前缀查询
                    myObjects = minioClient.listObjects(bucketName, prefix);
                }
            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e);
        }
        return myObjects;
    }

    /**
     * 根据桶和前缀输出里面所有的信息
     *
     * @param bucketName
     * @param prefix
     */
    public void printObjectsMsg(String bucketName, String prefix) {
        Iterable<Result<Item>> results = listObjects(bucketName, prefix);
        for (Result<Item> result : results) {
            Item item = null;
            try {
                item = result.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info(item.lastModified() + ", " + item.size() + ", " + item.objectName());
        }
    }


    /**
     * 以流的形式下载文件
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @return
     */
    public void downloadFile(String bucketName, String objectName, HttpServletResponse response, String fileName) {
        try {
            MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
            InputStream inputStream = minioClient.getObject(bucketName, objectName);
            OutputStream os = response.getOutputStream();
            BufferedInputStream br = new BufferedInputStream(inputStream);
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("application/octet-stream");
//            response.setHeader("Access-Control-Allow-Origin", "localhost:8001");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
            OutputStream out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            br.close();
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 获取唯一文件名
     *
     * @return
     */
    public String getUniqueFileName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 通过图片路径获取桶名称
     *
     * @param fileUrl
     * @return 桶名称
     */
    public String getBucketNameByUrl(String fileUrl) {
        String[] splitArray = fileUrl.split("/");
        return splitArray[3];
    }



    /**
     * 通过图片路径获取对象名称/文件名称
     *
     * @param fileUrl
     * @return
     */
    public String getObjectNameByUrl(String fileUrl) {
        String[] splitArray = fileUrl.split(getBucketNameByUrl(fileUrl));
        String splitResult = splitArray[1].replaceFirst("/", "");
        //临时地址处理
        if (splitResult.contains("?")) {
            String[] split = splitResult.split("\\?");
            return split[0];
        }
        return splitResult;
    }

    /**
     * 获取文件名称 包括后缀
     *
     * @param fileUrl
     * @return
     */
    public String getFileName(String fileUrl) {
        String[] split = fileUrl.split("/");
        return split[split.length - 1];
    }

    /**
     * 截取minio有效的url 将问号后面的所有内容去掉
     *
     * @param minioUrl
     * @return
     */
    public String interceptValidUrl(String minioUrl) {
        if (UtilValidate.isNotEmpty(minioUrl)) {
            return minioUrl.substring(0, minioUrl.indexOf("?"));
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        MinioClient minioClient = new MinioClient("http://10.51.10.201:9001", "sunseaiot", "sunseaiot");
////
////      String minio_bucketName="middleware";
////        boolean isExist = minioClient.bucketExists(minio_bucketName);
////        if (isExist) {
////        } else {
////            // 创建一个名为asiatrip的存储桶，用于存储文件。
////            minioClient.makeBucket(minio_bucketName);
////        }
//
////        MinioClient minioClient = new MinioClient("http://10.51.10.201:9001", "sunseaiot", "sunseaiot");
//        List<Bucket> buckets = minioClient.listBuckets();
//        for (Bucket bucket : buckets) {
//            Iterable<Result<Item>> objects = minioClient.listObjects(bucket.name(), null);
//            for (Result<Item> result : objects) {
//                Item item = result.get();
//                System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
//                minioClient.removeObject(bucket.name(), item.objectName());
//            }
//        }
//    }
//    public  List<Bucket> listBuckets(MinioClient minioClient) {
//        List<Bucket> listBuckets = null;
//        try {
//            listBuckets = minioClient.listBuckets();
//            for (Bucket bucket : listBuckets) {
//                minioClient.removeBucket(bucket.name());
//            }
//        } catch(Exception e){}
//        return listBuckets;
//    }

}
