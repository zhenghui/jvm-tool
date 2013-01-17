package zhenghui.jvm.parse;

import zhenghui.jvm.Util;

import java.util.HashMap;
import java.util.Map;

import static zhenghui.jvm.CommonConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: 下午7:36
 * 常量池解析器.
 * jvm specs
 * <url>http://docs.oracle.com/javase/specs/jvms/se5.0/html/ClassFile.doc.html</url>
 * 下面的所有注释都来自上面的jvm规范
 * <p/>
 * CONSTANT_InvokeDynamic CONSTANT_MethodHandle CONSTANT_MethodType 常量解析除外
 */
public class ConstantPoolParse {

    public static final int CONSTANT_Utf8 = 1;
    public static final int CONSTANT_Integer = 3;
    public static final int CONSTANT_Float = 4;
    public static final int CONSTANT_Long = 5;
    public static final int CONSTANT_Double = 6;
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_String = 8;
    public static final int CONSTANT_Fieldref = 9;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_InterfaceMethodref = 11;
    public static final int CONSTANT_NameAndType = 12;

    /**
     * class的十六进制字节码
     */
    private String code;

    public ConstantPoolParse(String code) {
        this.code = code;
    }

    /**
     * 存放所有常量池的信息.
     * 暂时只放了utf8 ,integer float double long 等基本类型数据
     * 因为后面的属性表中需要用到.just for study
     */
    public static final Map<Integer, String> constantPoolMap = new HashMap<Integer, String>();


    public ParseResult pareContantPool() throws Exception {
        //常量池容量计数器.注意,真实的计数是count-1个.因为这个count包括了计数为0的常量
        Type constant_pool_count = new Type(code.substring(8 * TWO, 10 * TWO));
        String[] strs = new String[constant_pool_count.getDecimalInteger()];
        strs[0] = "constant_pool_count :" + constant_pool_count.getDecimalInteger();
        int hand = 10 * TWO;//常量池的第一个常量是从第十个字节开始的(排除第0个常量)
        int tag_length = TWO;//所有的tag位都占用一个字节
        for (int idx = 1; idx < constant_pool_count.getDecimalInteger(); idx++) {
            //所有的常量类型都是以tag位开始的.
            Type tag = new Type(code.substring(hand, hand + tag_length));
            switch (tag.getDecimalInteger()) {
                //如果是utf-8编码的字符串
                case CONSTANT_Utf8:
                    //tag位后面两位是length位
                    //length开始的字节位置
                    int length_type_start = hand + tag_length;
                    //length 结束的字节位置 也是bytes开始的字节位置
                    int length_type_end = length_type_start + (2 * TWO);
                    Type length = new Type(code.substring(length_type_start, length_type_end));
                    int bytes_type_end = length_type_end + length.getDecimalInteger() * TWO;
                    Type bytes_utf8 = new Type(code.substring(length_type_end, bytes_type_end));
                    Constant_Type_Utf8 constant_type_utf8 = new Constant_Type_Utf8(length, bytes_utf8);
                    strs[idx] = "#" + idx + " = Utf8" + BLANK + constant_type_utf8.getValue();
                    //加上偏移量
                    hand += constant_type_utf8.getOffset() * TWO;
                    //放入到constantPoolMap中.
                    constantPoolMap.put(idx, Util.toStringHex(bytes_utf8.getValue()));
                    break;
                //如果是Constant_Type_Integer类型
                case CONSTANT_Integer:
                    //bytes的开始位置,hand 加tag位
                    int bytes_start = hand + tag_length;
                    //byte的结束位置.bytes占4位
                    int bytes_end = bytes_start + 4 * TWO;
                    Type bytes_integer = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_integer = new Constant_Type_Integer(bytes_integer);
                    strs[idx] = "#" + idx + " = Integer " + BLANK + constant_type_integer.getValue();
                    hand += constant_type_integer.getOffset() * TWO;
                    //放入到constantPoolMap中.
                    constantPoolMap.put(idx, bytes_integer.getDecimalInteger() + "");
                    break;
                case CONSTANT_Float:
                    //bytes的开始位置,hand 加tag位
                    bytes_start = hand + tag_length;
                    //byte的结束位置.bytes占4位
                    bytes_end = bytes_start + 4 * TWO;
                    Type bytes_float = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_float = new Constant_Type_Float(bytes_float);
                    strs[idx] = "#" + idx + " = Float " + BLANK + constant_type_float.getValue();
                    hand += constant_type_float.getOffset() * TWO;
                    //放入到constantPoolMap中.
                    constantPoolMap.put(idx, bytes_float.getDecimalFloat() + "f");
                    break;
                case CONSTANT_Long:
                    //bytes的开始位置,hand 加tag位
                    bytes_start = hand + tag_length;
                    //byte的结束位置.bytes占8位
                    bytes_end = bytes_start + 8 * TWO;
                    Type bytes_long = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_long = new Constant_Type_Long(bytes_long);
                    strs[idx] = "#" + idx + " = Float " + BLANK + constant_type_long.getValue();
                    hand += constant_type_long.getOffset() * TWO;
                    //放入到constantPoolMap中.
                    constantPoolMap.put(idx, bytes_long.getDecimalLong() + "l");
                    break;
                case CONSTANT_Double:
                    //bytes的开始位置,hand 加tag位
                    bytes_start = hand + tag_length;
                    //byte的结束位置.bytes占8位
                    bytes_end = bytes_start + 8 * TWO;
                    Type bytes_double = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_double = new Constant_Type_Double(bytes_double);
                    strs[idx] = "#" + idx + " = Float " + BLANK + constant_type_double.getValue();
                    hand += constant_type_double.getOffset() * TWO;
                    //放入到constantPoolMap中.
                    constantPoolMap.put(idx, bytes_double.getDecimalDouble() + "d");
                    break;
                case CONSTANT_Class:
                    //index开始的位置
                    int index_start = hand + tag_length;
                    //index占用两字节
                    int index_end = index_start + 2 * TWO;
                    Type index_class = new Type(code.substring(index_start, index_end));
                    Constant_Type constant_type_class = new Constant_Type_Class(index_class);
                    strs[idx] = "#" + idx + " = class " + BLANK + constant_type_class.getValue();
                    hand += constant_type_class.getOffset() * TWO;
                    break;
                case CONSTANT_String:
                    //index开始的位置
                    index_start = hand + tag_length;
                    //index占用两字节
                    index_end = index_start + 2 * TWO;
                    Type index_string = new Type(code.substring(index_start, index_end));
                    Constant_Type constant_type_string = new Constant_Type_String(index_string);
                    strs[idx] = "#" + idx + " = string " + BLANK + constant_type_string.getValue();
                    hand += constant_type_string.getOffset() * TWO;
                    break;
                case CONSTANT_Fieldref:
                    int class_index_start = hand + tag_length;
                    int class_index_end = class_index_start + 2 * TWO;
                    int name_index_end = class_index_end + 2 * TWO;
                    Type class_index = new Type(code.substring(class_index_start, class_index_end));
                    Type name_index = new Type(code.substring(class_index_end, name_index_end));
                    Constant_Type constant_type_fieldref = new Constant_Type_Fieldref(class_index, name_index);
                    strs[idx] = "#" + idx + " = fieldref " + BLANK + constant_type_fieldref.getValue();
                    hand += constant_type_fieldref.getOffset() * TWO;
                    break;
                case CONSTANT_Methodref:
                    class_index_start = hand + tag_length;
                    class_index_end = class_index_start + 2 * TWO;
                    name_index_end = class_index_end + 2 * TWO;
                    class_index = new Type(code.substring(class_index_start, class_index_end));
                    name_index = new Type(code.substring(class_index_end, name_index_end));
                    Constant_Type constant_type_methodref = new Constant_Type_Methodref(class_index, name_index);
                    strs[idx] = "#" + idx + " = methodref " + BLANK + constant_type_methodref.getValue();
                    hand += constant_type_methodref.getOffset() * TWO;
                    break;
                case CONSTANT_InterfaceMethodref:
                    class_index_start = hand + tag_length;
                    class_index_end = class_index_start + 2 * TWO;
                    name_index_end = class_index_end + 2 * TWO;
                    class_index = new Type(code.substring(class_index_start, class_index_end));
                    name_index = new Type(code.substring(class_index_end, name_index_end));
                    Constant_Type constant_type_interfacemethodref = new Constant_Type_InterfaceMethodref(class_index, name_index);
                    strs[idx] = "#" + idx + " = interfacemethodref " + BLANK + constant_type_interfacemethodref.getValue();
                    hand += constant_type_interfacemethodref.getOffset() * TWO;
                    break;
                //Constant_Type_NameAndType
                case CONSTANT_NameAndType:
                    class_index_start = hand + tag_length;
                    class_index_end = class_index_start + 2 * TWO;
                    int desc_index_end = class_index_end + 2 * TWO;
                    class_index = new Type(code.substring(class_index_start, class_index_end));
                    Type desc_index = new Type(code.substring(class_index_end, desc_index_end));
                    Constant_Type constant_type_nameandtype = new Constant_Type_NameAndType(class_index, desc_index);
                    strs[idx] = "#" + idx + " = nameandtype " + BLANK + constant_type_nameandtype.getValue();
                    hand += constant_type_nameandtype.getOffset() * TWO;
                    break;
                default:
                    ;
            }
        }
        ParseResult result = new ParseResult();
        result.setStrs(strs);
        result.setHandle(hand);
        return result;
    }

    /**
     * 常量池类型基类
     */
    abstract class Constant_Type {
        /**
         * 该常量的显示内容
         *
         * @return
         */
        public abstract String getValue() throws Exception;

        /**
         * 该常量需要往下偏移多少字节
         *
         * @return
         */
        public abstract int getOffset();

        /**
         * 获取tag位数值
         *
         * @return
         */
        public abstract int getTag();
    }

    /**
     * 常量池数据类型之  utf-8编码字符串
     */
    class Constant_Type_Utf8 extends Constant_Type {
        //字符串所占用的长度
        Type length;
        //字符串信息
        Type bytes;

        Constant_Type_Utf8(Type length, Type bytes) {
            this.length = length;
            this.bytes = bytes;
        }

        @Override
        public String getValue() throws Exception {
            if (bytes == null || bytes.getValue() == null) {
                return "";
            }
            //这里需要把16进制转换成字符串
            return "length:" + length.getDecimalInteger() + ",utf8-info:" + Util.toStringHex(bytes.getValue());
        }

        /**
         * 获取偏移量
         *
         * @return
         */
        @Override
        public int getOffset() {
            //tag位占用1个字节,length位占用2个字节.bytes位占用了length个字节
            int count = 1 + 2 + length.getDecimalInteger();
            return count;
        }

        @Override
        public int getTag() {
            return CONSTANT_Utf8;
        }
    }

    /**
     * 常量池数据类型之  Integer类型
     */
    class Constant_Type_Integer extends Constant_Type {

        Constant_Type_Integer(Type bytes) {
            this.bytes = bytes;
        }

        //Integer 类型的值
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "int value:" + bytes.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag位占一个字节,bytes占4个字节.
            return 1 + 4;
        }

        @Override
        public int getTag() {
            return CONSTANT_Integer;
        }
    }

    /**
     * 常量池数据类型之  Float类型
     */
    class Constant_Type_Float extends Constant_Type {

        Constant_Type_Float(Type bytes) {
            this.bytes = bytes;
        }

        //Float 类型的值
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "float value:" + bytes.getDecimalFloat();
        }

        @Override
        public int getOffset() {
            //tag位占一个字节,bytes占4个字节.
            return 1 + 4;
        }

        @Override
        public int getTag() {
            return CONSTANT_Float;
        }
    }

    /**
     * 常量池数据类型之  Long类型
     */
    class Constant_Type_Long extends Constant_Type {

        Constant_Type_Long(Type bytes) {
            this.bytes = bytes;
        }

        //Float 类型的值
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "long value:" + bytes.getDecimalLong();
        }

        @Override
        public int getOffset() {
            //tag位占一个字节,long类型占用8个字节
            return 1 + 8;
        }

        @Override
        public int getTag() {
            return CONSTANT_Long;
        }
    }

    /**
     * 常量池数据类型之  Double类型
     */
    class Constant_Type_Double extends Constant_Type {

        Constant_Type_Double(Type bytes) {
            this.bytes = bytes;
        }

        //double 类型的值
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "double value:" + bytes.getDecimalDouble();
        }

        @Override
        public int getOffset() {
            //tag位占一个字节,double类型的bytes占用8个字节.
            return 1 + 8;
        }

        @Override
        public int getTag() {
            return CONSTANT_Double;
        }
    }

    class Constant_Type_Class extends Constant_Type {

        Constant_Type_Class(Type index) {
            this.index = index;
        }

        //指向全额限定名常量的索引
        Type index;

        @Override
        public String getValue() throws Exception {
            return "name_index:#" + index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag占一位,index占两位
            return 1 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_Class;
        }
    }

    class Constant_Type_String extends Constant_Type {

        Constant_Type_String(Type index) {
            this.index = index;
        }

        //指向全额限定名常量的索引
        Type index;

        @Override
        public String getValue() throws Exception {
            return "name_index:#" + index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag占一位,index占两位
            return 1 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_String;
        }
    }

    class Constant_Type_Fieldref extends Constant_Type {

        Constant_Type_Fieldref(Type class_index, Type name_index) {
            this.class_index = class_index;
            this.name_index = name_index;
        }

        //指向字段类或者接口的CONSTANT_Class 索引值
        Type class_index;

        //指向字段描述符CONSTANT_NameAndType的索引值
        Type name_index;

        @Override
        public String getValue() throws Exception {
            return "class_index:#" + class_index.getDecimalInteger() + ",name_index:#" + name_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag占一位,index各占两位
            return 1 + 2 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_Fieldref;
        }
    }

    class Constant_Type_Methodref extends Constant_Type {

        Constant_Type_Methodref(Type class_index, Type name_index) {
            this.class_index = class_index;
            this.name_index = name_index;
        }

        //指向字段类或者接口的CONSTANT_Class 索引值
        Type class_index;

        //指向字段描述符CONSTANT_NameAndType的索引值
        Type name_index;

        @Override
        public String getValue() throws Exception {
            return "class_index:#" + class_index.getDecimalInteger() + ",name_index:#" + name_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag占一位,index各占两位
            return 1 + 2 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_Methodref;
        }
    }

    class Constant_Type_InterfaceMethodref extends Constant_Type {

        Constant_Type_InterfaceMethodref(Type class_index, Type name_index) {
            this.class_index = class_index;
            this.name_index = name_index;
        }

        //指向字段类或者接口的CONSTANT_Class 索引值
        Type class_index;

        //指向字段描述符CONSTANT_NameAndType的索引值
        Type name_index;

        @Override
        public String getValue() throws Exception {
            return "class_index:#" + class_index.getDecimalInteger() + ",name_index:#" + name_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag占一位,index各占两位
            return 1 + 2 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_InterfaceMethodref;
        }
    }

    class Constant_Type_NameAndType extends Constant_Type {

        Constant_Type_NameAndType(Type desc_index, Type name_index) {
            this.desc_index = desc_index;
            this.name_index = name_index;
        }

        //指向该字段或者方法名的长亮相的索引
        Type name_index;
        //指向字段或者方法描述常量项的索引
        Type desc_index;

        @Override
        public String getValue() throws Exception {
            return "name_index:#" + name_index.getDecimalInteger() + ",desc_index:#" + desc_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tag占一位,index各占两位
            return 1 + 2 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_NameAndType;
        }
    }

}
