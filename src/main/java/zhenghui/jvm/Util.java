package zhenghui.jvm;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-11
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    /**
     * 16进制转换成字符串
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
        }
        try {
            return new String(baKeyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 传入的flag中是否有对应的tag
     * @param flag
     * @param tag
     * @return
     */
    public static boolean hasTag(int flag,int tag){
        return (flag & tag) == tag;
    }

    public static void main(String [] args) throws Exception{
       System.out.println(Util.toStringHex("546573742E6A617661"));
    }
}
