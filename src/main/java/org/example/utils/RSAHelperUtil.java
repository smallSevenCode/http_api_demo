package org.example.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import org.example.pojo.Account;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>
 * RSA加解密工具类
 * <p>
 * 密钥长度 2048 bits
 * 密钥格式 PKCS#8
 * 加密算法 RSA/ECB/PKCS1Padding
 * 签名算法 SHA1withRSA
 *
 * @author 苦瓜不苦
 * @date 2021/6/9 10:22
 **/
public class RSAHelperUtil {

    // 加密算法
    private final static String encrypt_arithmetic = "RSA/ECB/PKCS1Padding";
    // 签名算法
    private final static String sign_arithmetic = "SHA1withRSA";

    // 2048 bits 的RSA 密钥对
    private final static int key_bit = 2048;
    // 2048 bits 的RSA 密钥对
    private final static int reserve_bytes = 11;

    // 实例化私钥
    private static PrivateKey privateKey = null;
    // 实例化公钥
    private static PublicKey publicKey = null;


    static {
        try {
            Account account = new Account();
            privateKey = getPrivateKey(account.getPrivateKey());
            publicKey = getPublicKey(account.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实例化公钥
     *
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] publicKeyByte = base64Decoder.decodeBuffer(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }


    /**
     * 实例化私钥
     *
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] privateKeyByte = base64Decoder.decodeBuffer(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 公钥分段加密
     *
     * @param text 明文字符串
     * @return 加密密文
     * @throws Exception
     */
    public static String encrypt(String text) throws Exception {
        // 2048 bits 的RSA 密钥对, 最大加密明文大小计算公式:  2048 (bits) / 8 - 11(byte) = 245 byte
        int max_encrypt_block = key_bit / 8 - reserve_bytes;

        Cipher cipher = Cipher.getInstance(encrypt_arithmetic);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        int textBytesLength = textBytes.length;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 分段加密
        for (int offset = 0; offset < textBytesLength; offset += max_encrypt_block) {
            // 字节长度不能超过max_encrypt_block
            int block = textBytesLength - offset;
            if (block > max_encrypt_block) {
                block = max_encrypt_block;
            }
            // 得到分段加密结果
            byte[] encryptBlock = cipher.doFinal(textBytes, offset, block);
            // 追加结果
            outputStream.write(encryptBlock);
        }
        // 关闭流
        outputStream.flush();
        outputStream.close();
        // 获取加密字节
        byte[] encryptBytes = outputStream.toByteArray();
        // Base64编码
        return Base64Encoder.encode(encryptBytes);

    }


    /**
     * 私钥分段解密
     *
     * @param text 加密字符串
     * @return 加密明文
     * @throws Exception
     */
    public static String decrypt(String text) throws Exception {

        // 2048 bits 的RSA 密钥对, 最大解密明文大小计算公式:  2048 (bits) / 8 = 256 byte
        int max_decrypt_block = key_bit / 8;

        Cipher cipher = Cipher.getInstance(encrypt_arithmetic);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] textBytes = Base64Decoder.decode(text);
        int textBytesLength = textBytes.length;
        // 分段解密
        for (int offset = 0; offset < textBytesLength; offset += max_decrypt_block) {
            // 字节长度不能超过max_decrypt_block
            int block = Math.min(textBytesLength - offset, max_decrypt_block);
            // 得到分段加密结果
            byte[] encryptBlock = cipher.doFinal(textBytes, offset, block);
            // 追加结果
            outputStream.write(encryptBlock);
        }
        // 关闭流
        outputStream.flush();
        outputStream.close();
        // 获取数据
        return outputStream.toString();
    }


    /**
     * 生成签名
     *
     * @param text 明文数据
     * @return 签名的信息
     * @throws Exception
     */
    public static String generateSign(String text) throws Exception {
        // 实例化
        Signature signature = Signature.getInstance(sign_arithmetic);
        signature.initSign(privateKey);
        signature.update(text.getBytes(StandardCharsets.UTF_8));
        // 生成签名
        byte[] sign = signature.sign();
        // base64编码
        return Base64Encoder.encode(sign);
    }


    /**
     * 验证签名
     *
     * @param text     明文数据
     * @param signText 签名的信息
     * @return
     * @throws Exception
     */
    public static Boolean verifySign(String text, String signText) throws Exception {
        Signature signature = Signature.getInstance(sign_arithmetic);
        signature.initVerify(publicKey);
        signature.update(text.getBytes(StandardCharsets.UTF_8));
        // base64解码
        byte[] signByte = Base64Decoder.decode(signText);
        // 验证签名
        return signature.verify(signByte);
    }

}
