package com.ggbond.defectdetection.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

/**
 * 图像处理工具类
 * <p>
 * Author: 19461
 * Date: 2024/2/17
 */
@Slf4j
public class ImgUtil {


    public static boolean isImage(MultipartFile file) {
        // 获取文件的MIME类型
        String contentType = file.getContentType();

        log.info("文件类型为{}",contentType);

        // 判断是否为图片类型
        return contentType != null && contentType.startsWith("image/");
    }

    public static String imageToBase64ByPath(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            return "";
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] imageData = new byte[(int) file.length()];
        fis.read(imageData);
        return Base64.getEncoder().encodeToString(imageData);
    }

    public static String generateRandomName() {
        UUID uuid = UUID.randomUUID();
        String randomName = uuid.toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String time=formatter.format(LocalDateTime.now()) ;

        randomName=randomName.replace('-','_')+"_"+time+".jpeg";
        return randomName;
    }
    public static void saveImageToFile(String base64Image, String filePath) throws IOException {
        String folderPath="";
        if(filePath.contains("\\")){
            folderPath= filePath.substring(0,filePath.lastIndexOf("\\"));
        }else{
            folderPath= filePath.substring(0,filePath.lastIndexOf("/"));
        }
        File folder = new File(folderPath);

        if(!folder.exists()){
            folder.mkdirs();
        }
        // 将Base64编码的图片字符串解码为字节数组
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//        log.info(filePath);
        File outputFile = new File(filePath);
        OutputStream outputStream=new FileOutputStream(filePath);
        outputStream.write(imageBytes);

        outputStream.flush();
        outputStream.close();
    }

    public static String convertToJPGBase64(MultipartFile image) throws Exception {

        if(!isImage(image)){
            throw new Exception();
        }

        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建一个新的BufferedImage，指定宽度、高度和颜色模式
        BufferedImage jpegImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 将原始图片绘制到新的BufferedImage上
        jpegImage.createGraphics().drawImage(originalImage, 0, 0, null);

        //saveImage(jpegImage)  ##存储图片
        // 将BufferedImage转为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(jpegImage, "jpeg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageBytes = baos.toByteArray();

        // 将字节数组转为Base64字符串
        String base64String = java.util.Base64.getEncoder().encodeToString(imageBytes);

        return base64String;
    }
}