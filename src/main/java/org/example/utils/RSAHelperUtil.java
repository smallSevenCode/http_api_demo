package org.example.utils;

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
import java.util.Base64;

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
    private final static String ENCRYPT_ARITHMETIC = "RSA/ECB/PKCS1Padding";
    // 签名算法
    private final static String SIGN_ARITHMETIC = "SHA1withRSA";

    // 2048 bits 的RSA 密钥对
    private final static int KEY_BIT = 2048;
    // 2048 bits 的RSA 密钥对
    private final static int RESERVE_BYTES = 11;

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
     */
    public static String encrypt(String text) {
        try {
            // 2048 bits 的RSA 密钥对, 最大加密明文大小计算公式:  2048 (bits) / 8 - 11(byte) = 245 byte
            int maxEncryptBlock = KEY_BIT / 8 - RESERVE_BYTES;

            Cipher cipher = Cipher.getInstance(ENCRYPT_ARITHMETIC);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            int textBytesLength = textBytes.length;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // 分段加密
            for (int offset = 0; offset < textBytesLength; offset += maxEncryptBlock) {
                // 字节长度不能超过max_encrypt_block
                int block = textBytesLength - offset;
                if (block > maxEncryptBlock) {
                    block = maxEncryptBlock;
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
            byte[] encode = Base64.getEncoder().encode(encryptBytes);
            return new String(encode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 私钥分段解密
     *
     * @param text 加密字符串
     * @return 加密明文
     */
    public static String decrypt(String text) {
        try {
            // 2048 bits 的RSA 密钥对, 最大解密明文大小计算公式:  2048 (bits) / 8 = 256 byte
            int maxDecryptBlock = KEY_BIT / 8;

            Cipher cipher = Cipher.getInstance(ENCRYPT_ARITHMETIC);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] textBytes = Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8));
            int textBytesLength = textBytes.length;
            // 分段解密
            for (int offset = 0; offset < textBytesLength; offset += maxDecryptBlock) {
                // 字节长度不能超过max_decrypt_block
                int block = Math.min(textBytesLength - offset, maxDecryptBlock);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成签名
     *
     * @param text 明文数据
     * @return 签名的信息
     */
    public static String generateSign(String text) {
        try {
            // 实例化
            Signature signature = Signature.getInstance(SIGN_ARITHMETIC);
            signature.initSign(privateKey);
            signature.update(text.getBytes(StandardCharsets.UTF_8));
            // 生成签名
            byte[] sign = signature.sign();
            // base64编码
            return new String(Base64.getEncoder().encode(sign), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 验证签名
     *
     * @param text     明文数据
     * @param signText 签名的信息
     * @return
     */
    public static Boolean verifySign(String text, String signText) {
        try {
            Signature signature = Signature.getInstance(SIGN_ARITHMETIC);
            signature.initVerify(publicKey);
            signature.update(text.getBytes(StandardCharsets.UTF_8));
            // base64解码
            byte[] signByte = Base64.getDecoder().decode(signText.getBytes(StandardCharsets.UTF_8));
            // 验证签名
            return signature.verify(signByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
