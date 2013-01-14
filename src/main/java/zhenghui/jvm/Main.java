package zhenghui.jvm;

/**
 *
 * User: zhenghui
 * Date: 13-1-10
 * Time: обнГ2:19
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ClassLoader cl = new ClassLoader();
        ConstantPoolParse parse = new ConstantPoolParse(cl.loadClass("d:\\Test.class"));
        for(String str : parse.pareBaseInfo()){
            System.out.println(str);
        }
        for(String str : parse.pareContantPool()){
            System.out.println(str);
        }
    }
}
