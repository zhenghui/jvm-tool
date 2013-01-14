package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: ÏÂÎç2:38
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    private int a = 20;

    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        int a = 0x0001;
        int b = 0x0010;
        int c = 0x0020;
        int d = 0x0200;
        System.out.println((0x0021 & a) == a);
        System.out.println((0x0021 & b) == b);
        System.out.println((0x0021 & c) == c);
        System.out.println((0x0021 & d) == d);

    }
}
