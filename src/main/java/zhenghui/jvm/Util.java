package zhenghui.jvm;

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
    public static String toStringHex(String s) throws Exception {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
        }
        return new String(baKeyword, "utf-8");
    }

    public static void main(String [] args) throws Exception{
       System.out.println(Util.toStringHex("546573742E6A617661"));
    }
}
