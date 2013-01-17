package zhenghui.jvm.parse;

import zhenghui.jvm.Util;

import java.util.HashMap;
import java.util.Map;

import static zhenghui.jvm.CommonConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: ����7:36
 * �����ؽ�����.
 * jvm specs
 * <url>http://docs.oracle.com/javase/specs/jvms/se5.0/html/ClassFile.doc.html</url>
 * ���������ע�Ͷ����������jvm�淶
 * <p/>
 * CONSTANT_InvokeDynamic CONSTANT_MethodHandle CONSTANT_MethodType ������������
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
     * class��ʮ�������ֽ���
     */
    private String code;

    public ConstantPoolParse(String code) {
        this.code = code;
    }

    /**
     * ������г����ص���Ϣ.
     * ��ʱֻ����utf8 ,integer float double long �Ȼ�����������
     * ��Ϊ��������Ա�����Ҫ�õ�.just for study
     */
    public static final Map<Integer, String> constantPoolMap = new HashMap<Integer, String>();


    public ParseResult pareContantPool() throws Exception {
        //����������������.ע��,��ʵ�ļ�����count-1��.��Ϊ���count�����˼���Ϊ0�ĳ���
        Type constant_pool_count = new Type(code.substring(8 * TWO, 10 * TWO));
        String[] strs = new String[constant_pool_count.getDecimalInteger()];
        strs[0] = "constant_pool_count :" + constant_pool_count.getDecimalInteger();
        int hand = 10 * TWO;//�����صĵ�һ�������Ǵӵ�ʮ���ֽڿ�ʼ��(�ų���0������)
        int tag_length = TWO;//���е�tagλ��ռ��һ���ֽ�
        for (int idx = 1; idx < constant_pool_count.getDecimalInteger(); idx++) {
            //���еĳ������Ͷ�����tagλ��ʼ��.
            Type tag = new Type(code.substring(hand, hand + tag_length));
            switch (tag.getDecimalInteger()) {
                //�����utf-8������ַ���
                case CONSTANT_Utf8:
                    //tagλ������λ��lengthλ
                    //length��ʼ���ֽ�λ��
                    int length_type_start = hand + tag_length;
                    //length �������ֽ�λ�� Ҳ��bytes��ʼ���ֽ�λ��
                    int length_type_end = length_type_start + (2 * TWO);
                    Type length = new Type(code.substring(length_type_start, length_type_end));
                    int bytes_type_end = length_type_end + length.getDecimalInteger() * TWO;
                    Type bytes_utf8 = new Type(code.substring(length_type_end, bytes_type_end));
                    Constant_Type_Utf8 constant_type_utf8 = new Constant_Type_Utf8(length, bytes_utf8);
                    strs[idx] = "#" + idx + " = Utf8" + BLANK + constant_type_utf8.getValue();
                    //����ƫ����
                    hand += constant_type_utf8.getOffset() * TWO;
                    //���뵽constantPoolMap��.
                    constantPoolMap.put(idx, Util.toStringHex(bytes_utf8.getValue()));
                    break;
                //�����Constant_Type_Integer����
                case CONSTANT_Integer:
                    //bytes�Ŀ�ʼλ��,hand ��tagλ
                    int bytes_start = hand + tag_length;
                    //byte�Ľ���λ��.bytesռ4λ
                    int bytes_end = bytes_start + 4 * TWO;
                    Type bytes_integer = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_integer = new Constant_Type_Integer(bytes_integer);
                    strs[idx] = "#" + idx + " = Integer " + BLANK + constant_type_integer.getValue();
                    hand += constant_type_integer.getOffset() * TWO;
                    //���뵽constantPoolMap��.
                    constantPoolMap.put(idx, bytes_integer.getDecimalInteger() + "");
                    break;
                case CONSTANT_Float:
                    //bytes�Ŀ�ʼλ��,hand ��tagλ
                    bytes_start = hand + tag_length;
                    //byte�Ľ���λ��.bytesռ4λ
                    bytes_end = bytes_start + 4 * TWO;
                    Type bytes_float = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_float = new Constant_Type_Float(bytes_float);
                    strs[idx] = "#" + idx + " = Float " + BLANK + constant_type_float.getValue();
                    hand += constant_type_float.getOffset() * TWO;
                    //���뵽constantPoolMap��.
                    constantPoolMap.put(idx, bytes_float.getDecimalFloat() + "f");
                    break;
                case CONSTANT_Long:
                    //bytes�Ŀ�ʼλ��,hand ��tagλ
                    bytes_start = hand + tag_length;
                    //byte�Ľ���λ��.bytesռ8λ
                    bytes_end = bytes_start + 8 * TWO;
                    Type bytes_long = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_long = new Constant_Type_Long(bytes_long);
                    strs[idx] = "#" + idx + " = Float " + BLANK + constant_type_long.getValue();
                    hand += constant_type_long.getOffset() * TWO;
                    //���뵽constantPoolMap��.
                    constantPoolMap.put(idx, bytes_long.getDecimalLong() + "l");
                    break;
                case CONSTANT_Double:
                    //bytes�Ŀ�ʼλ��,hand ��tagλ
                    bytes_start = hand + tag_length;
                    //byte�Ľ���λ��.bytesռ8λ
                    bytes_end = bytes_start + 8 * TWO;
                    Type bytes_double = new Type(code.substring(bytes_start, bytes_end));
                    Constant_Type constant_type_double = new Constant_Type_Double(bytes_double);
                    strs[idx] = "#" + idx + " = Float " + BLANK + constant_type_double.getValue();
                    hand += constant_type_double.getOffset() * TWO;
                    //���뵽constantPoolMap��.
                    constantPoolMap.put(idx, bytes_double.getDecimalDouble() + "d");
                    break;
                case CONSTANT_Class:
                    //index��ʼ��λ��
                    int index_start = hand + tag_length;
                    //indexռ�����ֽ�
                    int index_end = index_start + 2 * TWO;
                    Type index_class = new Type(code.substring(index_start, index_end));
                    Constant_Type constant_type_class = new Constant_Type_Class(index_class);
                    strs[idx] = "#" + idx + " = class " + BLANK + constant_type_class.getValue();
                    hand += constant_type_class.getOffset() * TWO;
                    break;
                case CONSTANT_String:
                    //index��ʼ��λ��
                    index_start = hand + tag_length;
                    //indexռ�����ֽ�
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
     * ���������ͻ���
     */
    abstract class Constant_Type {
        /**
         * �ó�������ʾ����
         *
         * @return
         */
        public abstract String getValue() throws Exception;

        /**
         * �ó�����Ҫ����ƫ�ƶ����ֽ�
         *
         * @return
         */
        public abstract int getOffset();

        /**
         * ��ȡtagλ��ֵ
         *
         * @return
         */
        public abstract int getTag();
    }

    /**
     * ��������������֮  utf-8�����ַ���
     */
    class Constant_Type_Utf8 extends Constant_Type {
        //�ַ�����ռ�õĳ���
        Type length;
        //�ַ�����Ϣ
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
            //������Ҫ��16����ת�����ַ���
            return "length:" + length.getDecimalInteger() + ",utf8-info:" + Util.toStringHex(bytes.getValue());
        }

        /**
         * ��ȡƫ����
         *
         * @return
         */
        @Override
        public int getOffset() {
            //tagλռ��1���ֽ�,lengthλռ��2���ֽ�.bytesλռ����length���ֽ�
            int count = 1 + 2 + length.getDecimalInteger();
            return count;
        }

        @Override
        public int getTag() {
            return CONSTANT_Utf8;
        }
    }

    /**
     * ��������������֮  Integer����
     */
    class Constant_Type_Integer extends Constant_Type {

        Constant_Type_Integer(Type bytes) {
            this.bytes = bytes;
        }

        //Integer ���͵�ֵ
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "int value:" + bytes.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagλռһ���ֽ�,bytesռ4���ֽ�.
            return 1 + 4;
        }

        @Override
        public int getTag() {
            return CONSTANT_Integer;
        }
    }

    /**
     * ��������������֮  Float����
     */
    class Constant_Type_Float extends Constant_Type {

        Constant_Type_Float(Type bytes) {
            this.bytes = bytes;
        }

        //Float ���͵�ֵ
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "float value:" + bytes.getDecimalFloat();
        }

        @Override
        public int getOffset() {
            //tagλռһ���ֽ�,bytesռ4���ֽ�.
            return 1 + 4;
        }

        @Override
        public int getTag() {
            return CONSTANT_Float;
        }
    }

    /**
     * ��������������֮  Long����
     */
    class Constant_Type_Long extends Constant_Type {

        Constant_Type_Long(Type bytes) {
            this.bytes = bytes;
        }

        //Float ���͵�ֵ
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "long value:" + bytes.getDecimalLong();
        }

        @Override
        public int getOffset() {
            //tagλռһ���ֽ�,long����ռ��8���ֽ�
            return 1 + 8;
        }

        @Override
        public int getTag() {
            return CONSTANT_Long;
        }
    }

    /**
     * ��������������֮  Double����
     */
    class Constant_Type_Double extends Constant_Type {

        Constant_Type_Double(Type bytes) {
            this.bytes = bytes;
        }

        //double ���͵�ֵ
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "double value:" + bytes.getDecimalDouble();
        }

        @Override
        public int getOffset() {
            //tagλռһ���ֽ�,double���͵�bytesռ��8���ֽ�.
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

        //ָ��ȫ���޶�������������
        Type index;

        @Override
        public String getValue() throws Exception {
            return "name_index:#" + index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagռһλ,indexռ��λ
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

        //ָ��ȫ���޶�������������
        Type index;

        @Override
        public String getValue() throws Exception {
            return "name_index:#" + index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagռһλ,indexռ��λ
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

        //ָ���ֶ�����߽ӿڵ�CONSTANT_Class ����ֵ
        Type class_index;

        //ָ���ֶ�������CONSTANT_NameAndType������ֵ
        Type name_index;

        @Override
        public String getValue() throws Exception {
            return "class_index:#" + class_index.getDecimalInteger() + ",name_index:#" + name_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagռһλ,index��ռ��λ
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

        //ָ���ֶ�����߽ӿڵ�CONSTANT_Class ����ֵ
        Type class_index;

        //ָ���ֶ�������CONSTANT_NameAndType������ֵ
        Type name_index;

        @Override
        public String getValue() throws Exception {
            return "class_index:#" + class_index.getDecimalInteger() + ",name_index:#" + name_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagռһλ,index��ռ��λ
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

        //ָ���ֶ�����߽ӿڵ�CONSTANT_Class ����ֵ
        Type class_index;

        //ָ���ֶ�������CONSTANT_NameAndType������ֵ
        Type name_index;

        @Override
        public String getValue() throws Exception {
            return "class_index:#" + class_index.getDecimalInteger() + ",name_index:#" + name_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagռһλ,index��ռ��λ
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

        //ָ����ֶλ��߷������ĳ����������
        Type name_index;
        //ָ���ֶλ��߷������������������
        Type desc_index;

        @Override
        public String getValue() throws Exception {
            return "name_index:#" + name_index.getDecimalInteger() + ",desc_index:#" + desc_index.getDecimalInteger();
        }

        @Override
        public int getOffset() {
            //tagռһλ,index��ռ��λ
            return 1 + 2 + 2;
        }

        @Override
        public int getTag() {
            return CONSTANT_NameAndType;
        }
    }

}
