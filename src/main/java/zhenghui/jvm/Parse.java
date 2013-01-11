package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: ����7:36
 * ������.
 * jvm specs
 * <url>http://docs.oracle.com/javase/specs/jvms/se5.0/html/ClassFile.doc.html</url>
 * ���������ע�Ͷ����������jvm�淶
 */
public class Parse {

    /**
     * һ���ֽ���Ҫ��λ��ʾ
     */
    private static final int TWO = 2;
    
    private static final String BLANK = "     ";

    /**
     * class��ʮ�������ֽ���
     */
    private String code;

    public Parse(String code) {
        this.code = code;
    }


    /**
     * ����������Ϣ,��������,ħ���Ͱ汾��
     *
     * @return
     */
    public String[] pareBaseInfo() {
        String[] strs = new String[4];
        //todo ��Ҫ��������
        strs[0] = "source file : xxx";
        //ǰ4���ֽ���ħ��
        Type magic_num = new Type(code.substring(0 * TWO, 4 * TWO));
        strs[1] = "magic num : " + magic_num.getValue();
        //minnor �汾
        Type minor_version = new Type(code.substring(4 * TWO, 6 * TWO));
        strs[2] = "minor version : " + minor_version.getValue();
        //major�汾
        Type major_version = new Type(code.substring(6 * TWO, 8 * TWO));
        strs[3] = "major version : " + major_version.getValue();
        return strs;
    }

    public String[] pareContantPool() throws Exception {
        //����������������.ע��,��ʵ�ļ�����count-1��.��Ϊ���count�����˼���Ϊ0�ĳ���
        Type constant_pool_count = new Type(code.substring(8 * TWO, 10 * TWO));
        String[] strs = new String[constant_pool_count.getDecimal()];
        strs[0] = "constant_pool_count :" + constant_pool_count.getDecimal();
        int hand = 10 * TWO;//�����صĵ�һ�������Ǵӵ�ʮ���ֽڿ�ʼ��(�ų���0������)
        int tag_length = TWO;//���е�tagλ��ռ��һ���ֽ�
        for (int idx = 1; idx < constant_pool_count.getDecimal(); idx++) {
            //���еĳ������Ͷ�����tagλ��ʼ��.
            Type tag = new Type(code.substring(hand, hand + tag_length));
            switch (tag.getDecimal()) {
                //�����utf-8������ַ���
                case 1:
                    //tagλ������λ��lengthλ
                    //length��ʼ���ֽ�λ��
                    int length_type_start = hand + tag_length;
                    //length �������ֽ�λ�� Ҳ��bytes��ʼ���ֽ�λ��
                    int length_type_end = length_type_start + (2 * TWO);
                    Type length = new Type(code.substring(length_type_start, length_type_end));
                    int bytes_type_end = length_type_end + length.getDecimal() * TWO;
                    Type bytes_utf8 = new Type(code.substring(length_type_end, bytes_type_end));
                    Constant_Type_Utf8 constant_type_utf8 = new Constant_Type_Utf8(length, bytes_utf8);
                    strs[idx] = "#" + idx + " = Utf8" + BLANK + constant_type_utf8.getValue();
                    //����ƫ����
                    hand += constant_type_utf8.getOffset() * TWO;
                    break;
                //�����Constant_Type_Integer����
                case 3:
                    //bytes�Ŀ�ʼλ��,hand ��tagλ
                    int bytes_start = hand + tag_length;
                    //byte�Ľ���λ��.bytesռ4λ
                    int bytes_end = bytes_start + 4 * TWO;
                    Type bytes_integer = new Type(code.substring(bytes_start,bytes_end));
                    Constant_Type_Integer constant_type_integer = new Constant_Type_Integer(bytes_integer);
                    strs[idx] = "#" + idx + " = Integer " + BLANK + constant_type_integer.getValue();
                    hand += constant_type_integer.getOffset() * TWO;
                    break;
                default:
                    ;
            }
        }
        return strs;
    }

    /**
     * ��ʾ�޷�����.������jvm specs��涨��u1,u2,u4,u8
     */
    class Type {

        /**
         * ��Ӧ��16���Ʊ�ʾֵ
         */
        private String value;

        Type(String value) {
            this.value = value;
        }

        /**
         * ת����ʮ����
         *
         * @return
         */
        public int getDecimal() {
            return Integer.parseInt(value, 16);
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
            return "length:" + length.getDecimal() + ",utf8-info:" + Util.toStringHex(bytes.getValue());
        }

        /**
         * ��ȡƫ����
         *
         * @return
         */
        @Override
        public int getOffset() {
            //tagλռ��1���ֽ�,lengthλռ��2���ֽ�.bytesλռ����length���ֽ�
            int count = 1 + 2 + length.getDecimal();
            return count;
        }
    }

    /**
     * ��������������֮  Integer����
     * ֻ����final������²Ż����
     * Ҳ���� ֻ�� final int a = 20; �����������,20�Żᱻ���뵽������
     */
    class Constant_Type_Integer extends Constant_Type {

        Constant_Type_Integer(Type bytes) {
            this.bytes = bytes;
        }

        //Integer ���͵�ֵ
        Type bytes;

        @Override
        public String getValue() throws Exception {
            return "int-value:" + bytes.getDecimal();
        }

        @Override
        public int getOffset() {
            //tagλռһ���ֽ�,bytesռ4���ֽ�.
            return 1 + 4;
        }
    }

}