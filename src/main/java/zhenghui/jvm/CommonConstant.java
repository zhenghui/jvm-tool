package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: 下午7:22
 *
 */
public final class CommonConstant {
    public static final  String JVM_MAGIC_NUM = "CAFEBABE";

    /**
     * 一个字节需要两位表示
     */
    public static final int TWO = 2;

    public static final String BLANK = "     ";

    public static final int ACC_PUBLIC        = 0x0001; // class, inner, field, method
    public static final int ACC_PRIVATE       = 0x0002; //        inner, field, method
    public static final int ACC_PROTECTED     = 0x0004; //        inner, field, method
    public static final int ACC_STATIC        = 0x0008; //        inner, field, method
    public static final int ACC_FINAL         = 0x0010; // class, inner, field, method
    public static final int ACC_SUPER         = 0x0020; // class
    public static final int ACC_SYNCHRONIZED  = 0x0020; //                      method
    public static final int ACC_VOLATILE      = 0x0040; //               field
    public static final int ACC_BRIDGE        = 0x0040; //                      method
    public static final int ACC_TRANSIENT     = 0x0080; //               field
    public static final int ACC_VARARGS       = 0x0080; //                      method
    public static final int ACC_NATIVE        = 0x0100; //                      method
    public static final int ACC_INTERFACE     = 0x0200; // class, inner
    public static final int ACC_ABSTRACT      = 0x0400; // class, inner,        method
    public static final int ACC_STRICT        = 0x0800; //                      method
    public static final int ACC_SYNTHETIC     = 0x1000; // class, inner, field, method
    public static final int ACC_ANNOTATION    = 0x2000; // class, inner
    public static final int ACC_ENUM          = 0x4000; // class, inner, field
    public static final int ACC_MODULE        = 0x8000; // class, inner, field, method
}
