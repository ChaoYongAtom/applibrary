package org.wcy.android.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESOperator {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static final String sKey = "RUIYUNYUEJIAYUN1";
    private static final String ivParameter = "RUIYUNYUEJIAYUN2";

    // 加密
    public static String encrypt(String sSrc) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            StringBuilder stringBuilder = new StringBuilder("");
            for (int i = 0; i < encrypted.length; i++) {
                int v = encrypted[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return sSrc;
        }

    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {
        if (sSrc == null || sSrc.equals("")) {
            return null;
        }
        sSrc = sSrc.toUpperCase();
        int length = sSrc.length() / 2;
        char[] hexChars = sSrc.toCharArray();
        byte[] encrypted1 = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            encrypted1[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return sSrc;
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void main(String[] args) {
        try {

            // 需要加密的字串
            String cSrc = "{'operatorAccount':'kfs_01','operatorPwd':'1'}";

            System.out.println("加密前:" + cSrc);
            // 加密
            String enString = AESOperator.encrypt(cSrc);
            System.out.println("加密后:" + enString);

            // 解密
            String DeString = AESOperator.decrypt(enString);
            System.out.println("解密后:" + DeString);
        } catch (Exception e) {
            e.getMessage();
        }
    }

}