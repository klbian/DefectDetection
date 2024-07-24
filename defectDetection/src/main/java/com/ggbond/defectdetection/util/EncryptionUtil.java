package com.ggbond.defectdetection.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Author: 19461
 * Date: 2024/3/2
 */
@Slf4j
public class EncryptionUtil {
    private static final String DES_KEY = "MyKey123"; // DES密钥，必须为8位
    private static final String fillment="0000000000";

    public static String encrypt(String input) {
        if(input==null){
            return null;
        }
        if(input.length()==6){
            input.concat(fillment);
        }else{
            log.warn("错误的密码长度");
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(DES_KEY.getBytes(StandardCharsets.UTF_8), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String input) {
        if(input==null){
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(DES_KEY.getBytes(StandardCharsets.UTF_8), "DES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(input);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8).replace(fillment,"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}