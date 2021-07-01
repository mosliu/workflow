package net.liuxuan.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.UUID;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 文件相关工具
 * @date 2021-06-18
 **/
@Slf4j
public class FileUtils {
    public static File makeParentFolder(String fileName) {
        // 获取文件的原名称，生成一个UUID的文件名称
        String uuidFileName = createFileName(fileName);
        // 构建文件路径
//        StringBuilder sb = new StringBuilder(System.getProperties().getProperty("user.home"));

//        String absolutePath = new File(".").getAbsolutePath();

        String absolutePath = FileUtils.class.getResource("/").getPath();

        StringBuilder sb = new StringBuilder(absolutePath);
        sb.append("/imagesCache/").append(uuidFileName);
        log.info("生成缓存文件 地址是:  {}", sb.toString());
        // 生成文件
        File originalFile = new File(sb.toString());
        //判断文件父目录是否存在
        if (!originalFile.getParentFile().exists()) {
            // 创建文件目录
            originalFile.getParentFile().mkdir();
        }
        return originalFile;
    }

    /**
     * 根据原始文件名称，生成uuid文件名称
     *
     * @param originalFileName
     * @return
     */
    public static String createFileName(String originalFileName) {
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return UUID.randomUUID().toString().toLowerCase().replace("-", "") + "." + suffix;
    }

}
