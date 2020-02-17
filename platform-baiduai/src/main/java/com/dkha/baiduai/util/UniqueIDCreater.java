package com.dkha.baiduai.util;

import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * 生成唯一编码
 */
public class UniqueIDCreater {

    static SnowFlake snowFlake = new SnowFlake(2,2);

    public static long generateID() {
        //TODO 后期增加分布式ID生成器
        return snowFlake.nextId();
    }

    /**
     * 随机生成数字
     * @return
     */
    public static String generatePhoneCode() {
        Random random = new Random(System.currentTimeMillis());
        int randomNum = random.nextInt(9999-1000)+1000;
        return String.valueOf(randomNum);
    }

    /**
     * 随机生成用户账号
     * @return
     */
    public static String generateUserName() {
        String uuid = UUID.randomUUID().toString();
        String[] userNames = ShortText(uuid);
        return "zz_"+userNames[0];
    }

    public static String[] ShortText(String string){
        String key = "djfkdjkfhskfdsk《》？？#￥￥";                 //自己定义生成MD5加密字符串前的混合KEY
        String[] chars = new String[]{          //要使用生成URL的字符
                "a","b","c","d","e","f","g","h",
                "i","j","k","l","m","n","o","p",
                "q","r","s","t","u","v","w","x",
                "y","z","0","1","2","3","4","5",
                "6","7","8","9","A","B","C","D",
                "E","F","G","H","I","J","K","L",
                "M","N","O","P","Q","R","S","T",
                "U","V","W","X","Y","Z"
        };

        String hex = Encript.md5(key + string);
        int hexLen = hex.length();
        int subHexLen = hexLen / 8;
        String[] ShortStr = new String[4];

        for (int i = 0; i < subHexLen; i++) {
            String outChars = "";
            int j = i + 1;
            String subHex = hex.substring(i * 8, j * 8);
            long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);

            for (int k = 0; k < 6; k++) {
                int index = (int) (Long.valueOf("0000003D", 16) & idx);
                outChars += chars[index];
                idx = idx >> 5;
            }
            ShortStr[i] = outChars;
        }

        return ShortStr;
    }

}


class Encript {
    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    /**把inputString加密*/
    public static String md5(String inputStr){
        return encodeByMD5(inputStr);
    }

    /**
     * 验证输入的password是否正确
     * @param password 真正的password（加密后的真password）
     * @param inputString 输入的字符串
     * @return 验证结果，boolean类型
     */
    public static boolean authenticatePassword(String password,String inputString){
        if(password.equals(encodeByMD5(inputString))){
            return true;
        }else{
            return false;
        }
    }

    /**对字符串进行MD5编码*/
    private static String encodeByMD5(String originString){
        if (originString!=null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完毕摘要计算
                byte[] results = md5.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String result = byteArrayToHexString(results);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 轮换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    //将一个字节转化成十六进制形式的字符串
    private static String byteToHexString(byte b){
        int n = b;
        if(n<0)
            n=256+n;
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
