package com.mango.mall.product;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.mall.product.entity.BrandEntity;
import com.mango.mall.product.service.BrandService;
import com.mango.mall.product.service.CategoryService;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallProductApplication.class)
class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Value("${spring.redis.host}")
    private String address;
    @Value("${spring.redis.password}")
    private String password;
    @Test
    void  testRedisssonClient(){
        System.out.println(redissonClient);
//        System.out.println(address);
//        System.out.println(password);
    }
//    @Test
//    void testRedisTemplate(){
//        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
//        forValue.set("h1","v1"+ UUID.randomUUID().toString());
//        System.out.println(forValue.get("h1"));
//    }

//    @Test
//    void contextLoads() {
////        BrandEntity brandEntity = new BrandEntity();
////        brandEntity.setBrandId(1L);
////        brandEntity.setName("huawei");
////        brandEntity.setLogo("xiaomi");
////        brandService.updateById(brandEntity);
//        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
//        list.forEach(System.out::println);
//    }

//    @Resource
//    CategoryService categoryService;
//
//    @Test
//    void testPath(){
//        Long[] path = categoryService.findCatelogPath(520L);
//        for (Long aLong : path) {
//            System.out.println(aLong.toString());
//        }
//    }

//    @Test
//    public void testUpload() throws FileNotFoundException {
//        String endpoint = "oss-accelerate.aliyuncs.com";
//        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "LTAI5tK97C5yYTWtEZ6znBRb";
//        String accessKeySecret = "RHj2mwUG1GWEt0GPTmgT7kloTfu8nV";
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "mangoskd-mall";
//        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "test/exampleobject.mp4";
//        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
//        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        String filePath= "E:\Steam\steamapps\workshop\content\431960\2772317094\[SURVIVE MORE] 突如消えた妻から届く寝取られビデオレター.[SplitIt].mp4";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        try {
//            InputStream inputStream = new FileInputStream(filePath);
//            // 创建PutObject请求。
//            ossClient.putObject(bucketName, objectName, inputStream);
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//        System.out.println("success!");
//    }

}
