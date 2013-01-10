package zhenghui.jvm.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public class ClassLoader {

    /**
     * load class 生成对应的16进制的字符串
     * @param filePath
     * @return
     * @throws Exception
     */
    public String loadClass(String filePath) throws Exception {
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(filePath);
        int i;
        StringBuilder sb = new StringBuilder();
        while ((i = fileInputStream.read()) != -1) {
            sb.append(Integer.toHexString(i));
        }

        fileInputStream.close();
        return sb.toString();
    }

    public static void main(String[] args) throws Exception{
        ClassLoader cl = new ClassLoader();
        System.out.println(cl.loadClass("d:\\Test.class"));
    }
}
