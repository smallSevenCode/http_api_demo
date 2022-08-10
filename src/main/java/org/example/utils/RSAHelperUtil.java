package org.example.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
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
 * @author: 苦瓜不苦
 * @date: 2021/6/9 10:22
 **/
public class RSAHelperUtil {


    // 公钥
    private final static String public_key = "MIIBIjANBgkq***EFAAOCAQ8AMIIBCgKCAQE***Fx6hhwSUxbM9Fa2l5g+***Zby+wlFF/p3TNupd5gDozEw680w+NFXLplrzPvQRnxTjjakY4V5PorhAIRtHz3O5o6qstiqyfYGSDUbEfPLzKjLVrXCv3kDT49d2PAD+03WVt34C3I9/ANrluDH+5BxYB2SiAi8tkpJLAHy7GZQKqxxo0KsaBoECVRCZehyW9w0+1TcqDBVguLYo/D5otk0COc3AqMULUwtqzsu8gYVLwQZr5LgvvFtcQm5E2Kp/54EeHt8PcjbVhpwInldt20WhOjBXjNftemz7JAO6KftWjzi+KzoZqQuRA27hqJeVtIL+5wIDAQAB";
    // 私钥
    private final static String private_key = "MIIEvAIBADA***0BAQEFAASCBKYwggSiAgEAAoI***K7HBX/cb/vOU4***uSGL6OLAq95zwabhRSqb528DQSE2GbDiMmJrWGAkhB1YnrY0zItt/7s4f6wwgZFFZaSDh6ZBWQaYmayzceEBhxiw5sRR90vHrmqbBiGMHZWxBHNv9a7hemcmoS5ou48T6Z3CxEkm3DfyPMKqxQag10ugSlTdUj3xzRIMH1J8y2DCRGPrGqpREA5aW+L+xf8t19UPqLhhKeprmbwiBbIaZk4IS+TJytxv3Flch2lRORxP1T8j+QRaxP1Pd2lSwrW2ZUmSW88881lXkuorKySz8Sl/hXxPwJdBV4kIEnCzvrXDRqBqk2p9AgMBAAECgf9hweysw0tnqH7EwEkJhQVdYGlene4HAFyEpRwLq8Pn1rp4UKawJMlTbbgc/zuETAf7QJamBPSuTP0svoVXUoY0e6c/JBSpf6ob7n3S3rXNwCS6VE++UU9WfueGYislDkPoCRIurg0C1pcZwj8dy3Ftdh7vQBA5Kq9I0OlgzCHHW3PEfSELDLnKFr8aJybtTm764Aq21mM4Mx6//g1yZ/n9TVEoVq6RnguZMVu/EVewPir4VENQx7r/vwF/DQ5W/LyITaVutQ/tjQ+McuuCEEqL9j8BFNIPTqNu5+EFjytMdGiVJXP7f+Ogmiy4QzsNNoS6I1HCYwQETsp223SPjQUCgYEA6mrNyTHBgqHx0ZuT3mjgNK6pJEASW4dGOFSDSG1g+zEwbJ5vQHOCXlgk3w2ANLwRhFP31kzlnhcNIh94IBz8zBL4uYhSmUgSzCxGa2+FXZ6CE1qHfzdZis2j9YAhcteu9j0pQP1YzDR7z65DA7KojcR5slN6AqOz5Y9+699W9hcCgYEAynKvpQblnPPexYIPG0f2F+O9fMxHjo4Bq15e3992gsJtRdcgI6rvBtNZ6r761aXhrb9111X924r6WPX+VfLix1FvnImUlfE7MZv0VqGFGUCFrVbsdETjGlW1W8s75TLHyW6xpwnhwnMmFPCq3IV/QJgQ81g78CRJwXbRVtUuFIsCgYAEYbSxAw4T7AUCHv9Xk+xE79LXi58MOO4WFXZSv0Cl0ViPXzLDKKWSL+GYjb8MvUsyhwNF7TAJkXK8ZYwwRXpwMhaWGTc6CD/ZsyYp0e7TPig+Rl3sAdPZpVhgOWHmrKNnlOMVhObJAl4iFqbIBenipH6F18ubCwZMzy7XJ9iPRwKBgQCd05MDQME4/xYpPB+y0QMV1MIgzNDsXeEHTGcCeR/XCORPkkrIA7acx6BN1d2POecaoSypV6y0v0A0onJJZzVM1jwA+XERBCN1pXNzJjCxYw/T9vQYIKw8F51vlp13LYN7kgSFiLqr8UE7CfTqPljabrKoQ51WtbOaU5sX6tIlyQKBgQDU/ZBa26vYqcEacje9WGXNSlf7L+HQKDPL1jx4Z7LXlDxIHbnpxD/Soqm1TAJkqUwuRBzrzK5xJhk1Vzh5p3p6BD3CCVqBg3KOPLh5aVI6vBletHV3Vhfxh5PvCaL9WeylPMyOqBI6aTiHQDelKIKAN+QRmue8mI3i9fhMaZMk1A\\=\\=";


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
            privateKey = getPrivateKey(private_key);
            publicKey = getPublicKey(public_key);
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
