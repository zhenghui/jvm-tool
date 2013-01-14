package zhenghui.jvm;

import zhenghui.jvm.parse.BaseInfoParse;
import zhenghui.jvm.parse.ConstantPoolParse;

/**
 *
 * User: zhenghui
 * Date: 13-1-10
 * Time: ÏÂÎç2:19
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ClassLoader cl = new ClassLoader();
        String code = cl.loadClass("d:\\Test.class");
        BaseInfoParse baseInfoParse = new BaseInfoParse(code);
        for(String str : baseInfoParse.pareBaseInfo()){
            System.out.println(str);
        }
        ConstantPoolParse constantPoolParse = new ConstantPoolParse(code);
        ConstantPoolResult result = constantPoolParse.pareContantPool();
        for(String str : result.getStrs()){
            System.out.println(str);
        }
        System.out.println(result.getHandle()/2);
    }
}
