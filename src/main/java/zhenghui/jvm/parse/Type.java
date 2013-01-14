package zhenghui.jvm.parse;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: ����4:43
 * ��ʾ�޷�����.������jvm specs��涨��u1,u2,u4,u8
 */
public class Type {
    /**
     * ��Ӧ��16���Ʊ�ʾֵ
     */
    private String value;

    Type(String value) {
        this.value = value;
    }

    /**
     * ת����ʮ����Integer
     *
     * @return
     */
    public int getDecimalInteger() {
        return Integer.parseInt(value, 16);
    }

    /**
     * ת����ʮ����Float
     *
     * @return
     */
    public float getDecimalFloat(){
        return Float.intBitsToFloat(getDecimalInteger());
    }

    /**
     * ת����ʮ����Long
     *
     * @return
     */
    public long getDecimalLong(){
        return Long.parseLong(value,16);
    }

    /**
     * ת����ʮ����Long
     *
     * @return
     */
    public double getDecimalDouble(){
        return Double.longBitsToDouble(getDecimalLong());
    }

    /**
     * ��ȡ��Ӧ��16���Ʊ�ʾֵ
     *
     * @return
     */
    public String getValue() {
        return value;
    }
}
