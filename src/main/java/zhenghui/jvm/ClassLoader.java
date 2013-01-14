package zhenghui.jvm;

import java.io.File;
import java.io.FileInputStream;

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
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public String loadClass(String filePath) throws Exception {
        File file = new File(filePath);
        if(!file.exists()){
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(filePath);
        int i;
        StringBuilder sb = new StringBuilder();
        while ((i = fileInputStream.read()) != -1) {
            sb.append(String.format("%02X", i));
        }
        fileInputStream.close();
        return sb.toString();
    }

}
