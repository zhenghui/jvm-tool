package zhenghui.jvm.parse;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: 下午4:43
 * 表示无符号数.类似在jvm specs里规定的u1,u2,u4,u8
 */
public class Type {
    /**
     * 对应的16进制表示值
     */
    private String value;

    Type(String value) {
        this.value = value;
    }

    /**
     * 转换成十进制Integer
     *
     * @return
     */
    public int getDecimalInteger() {
        return Integer.parseInt(value, 16);
    }

    /**
     * 转换成十进制Float
     *
     * @return
     */
    public float getDecimalFloat(){
        return Float.intBitsToFloat(getDecimalInteger());
    }

    /**
     * 转换成十进制Long
     *
     * @return
     */
    public long getDecimalLong(){
        return Long.parseLong(value,16);
    }

    /**
     * 转换成十进制Long
     *
     * @return
     */
    public double getDecimalDouble(){
        return Double.longBitsToDouble(getDecimalLong());
    }

    /**
     * 获取对应的16进制表示值
     *
     * @return
     */
    public String getValue() {
        return value;
    }
}
