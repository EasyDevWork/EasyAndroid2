package com.easy.utils;

import android.util.Log;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RsaUtils {
    public static final String PUBLIC_KEY = "public_key";
    public static final String PRIVATE_KEY = "private_key";

    public static Map<String, Key> geration() {
        KeyPairGenerator keyPairGenerator;
        try {
            Map<String, Key> map = new HashMap<>();
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            X509EncodedKeySpec xks = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(xks);
            map.put(PUBLIC_KEY, publicKey);

            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            PKCS8EncodedKeySpec pks = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = kf.generatePrivate(pks);
            map.put(PRIVATE_KEY, privateKey);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(Key privKey, String content) {
        //开始解密
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] cipherText = cipher.doFinal(content.getBytes());
            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void test() {
        Map<String, Key> keyMap = geration();
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            Key pubKey = keyMap.get(PUBLIC_KEY);
            Key privKey = keyMap.get(PRIVATE_KEY);

            String input = "!!!hello world!!!";
            Log.d("cipher: ", "原始:" + input);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            //加密后的东西
            Log.d("cipher: ", "加密后:" + new String(cipherText));
            //开始解密
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(cipherText);
            Log.d("cipher: ", "解密后:" + new String(plainText));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
