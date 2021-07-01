package net.liuxuan.security.controller;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-18
 **/
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    /**
     * 头像上传：200px * 200px
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/avatar")
    public CommonResponseDto uploadAvatar(MultipartFile file) throws IOException {
        if (file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename())) {
            CommonResponseDto.fail("头像不能为空");
        }
        String contentType = file.getContentType();
        if (!contentType.contains("")) {
            CommonResponseDto.fail("头像格式不能为空");
        }
        String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG)$";
        Matcher matcher = Pattern.compile(reg).matcher(file.getOriginalFilename());
        // 校验 图片的后缀名 是否符合要求
        if (matcher.find()) {
            Map<String, String> map = uploadFile(file, 200, 200);
            return CommonResponseDto.success(map);
        }
        return CommonResponseDto.fail("头像格式不正确,只可以上传[ JPG , JPEG , PNG ]中的一种");
    }


    /**
     * 图片压缩并且上传 七牛云 OSS
     *
     * @param file
     * @param height
     * @param width
     * @return Map<String, String>
     * @author Wang Chen Chen<932560435@qq.com>
     * @date 2019/12/13 23:06
     */
    private Map<String, String> uploadFile(MultipartFile file, int width, int height) throws IOException {
        // 获取一个空文件
        File targetFile = FileUtils.makeParentFolder(file.getOriginalFilename());
        // 因为是spring boot 打包以后是jar包，所以需要用ClassPathResource来获取classpath下面的水印图片
        ClassPathResource classPathResource = new ClassPathResource("images/watermark.png");
        String path = this.getClass().getResource("/").getPath();
//        log.info(path);
        InputStream watermark;
        File watermarkFilePath = new File(path + "/images/watermark.png");
        if (watermarkFilePath == null) {
            watermark = null;
        } else {
            watermark = new FileInputStream(watermarkFilePath);
        }
        if (classPathResource != null) {
            watermark = classPathResource.getInputStream();
        }


        // 图片压缩以后读到空文件中
        Thumbnails.of(file.getInputStream())
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(watermark), 0.5f)
                .outputQuality(0.8f)
                .size(width, height).toFile(targetFile);
        // 把压缩好的图片上传到 七牛云
//        file.transferTo(targetFile);
//        String url = qiniuUtils.upload(new FileInputStream(targetFile), targetFile.getName());
        Map<String, String> result = new HashMap<>(2);
        String url = "/uploads/" + targetFile.getName();
        result.put("url", url);
        result.put("fileName", targetFile.getName());
        log.info("uploadFile( {} , {} , {} , {} );", file.getOriginalFilename(), file.getSize(), file.getContentType(), targetFile.getName(), url);
        // 删除压缩文件
//        targetFile.delete();
        return result;
    }

    /**
     * 根据url 删除图片
     * @param url
     * @return
     * @throws IOException
     */
    @DeleteMapping("/delete")
    public CommonResponseDto deleteImages(String url) throws IOException {
        if (StringUtils.isNotEmpty(url)) {
            String fileKey = url.substring((url.lastIndexOf("/") + 1), url.length());
            log.info("deleteImages url: {}  fileKey: {}", url, fileKey);
//            qiniuUtils.delete(fileKey);
        }
        return CommonResponseDto.success();
    }



}
