package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: 下午7:36
 *  解析器.
 *  jvm specs
 *  <url>http://docs.oracle.com/javase/specs/jvms/se5.0/html/ClassFile.doc.html</url>
 *  下面的所有注释都来自上面的jvm规范
 */
public class Parse {

    /**
     * 一个字节需要两位表示
     */
    private static final int TWO = 2;

    /**
     * class的十六进制字节码
     */
    private String code;

    public Parse(String code) {
        this.code = code;
    }
    

    /**
     * 解析基础信息,包括类名,魔数和版本号
     * @return
     */
    public String[] pareBaseInfo(){
        String[] strs = new String[4];
        //todo 需要加上类名
        strs[0] = "source file : xxx";  
        //前4个字节是魔数
        Type magic_num = new Type(code.substring(0*TWO,4*TWO));
        strs[1] = "magic num : " + magic_num.getValue();
        //minnor 版本
        Type minor_version = new Type(code.substring(4*TWO,6*TWO));
        strs[2] = "minor version : " + minor_version.getValue();
        //major版本
        Type major_version = new Type(code.substring(6*TWO,8*TWO));
        strs[3] = "major version : " + major_version.getValue();
        return strs;
    }
    
    public String[] pareContantPool() throws Exception{
        //常量池容量计数器.注意,真实的计数是count-1个.因为这个count包括了计数为0的常量
        Type constant_pool_count = new Type(code.substring(8*TWO,10*TWO));
        String[] strs = new String[constant_pool_count.getDecimal()];
        strs[0] = "constant_pool_count :" + constant_pool_count.getDecimal();
        int hand = 10 * TWO;//常量池的第一个常量是从第十个字节开始的(排除第0个常量)
        int tag_length = TWO;//所有的tag位都占用一个字节
        for(int idx = 1;idx < constant_pool_count.getDecimal(); idx++){
            //所有的常量类型都是以tag位开始的.
            Type tag = new Type(code.substring(hand,hand + tag_length));
            switch (tag.getDecimal()){
                //如果是utf-8编码的字符串
                case 1 :
                    //tag位后面两位是length位
                    //length开始的字节位置
                    int length_type_start = hand+tag_length;
                    //length 结束的字节位置 也是bytes开始的字节位置
                    int length_type_end = hand+tag_length+(2*TWO);
                    Type length = new Type(code.substring(length_type_start,length_type_end));
                    int bytes_type_end = length_type_end + length.getDecimal()*TWO;
                    Type bytes = new Type(code.substring(length_type_end,bytes_type_end));
                    Constant_Type_Utf8 constant_type_utf8 = new Constant_Type_Utf8(length,bytes);
                    strs[idx] = "#" + idx + " = Utf8" + "     " + constant_type_utf8.getValue();
                    break;
                default:
                    ;
            }
        }
        return strs;
    }

    /**
     * 表示无符号数.类似在jvm specs里规定的u1,u2,u4,u8
     */
    class Type {

        /**
         * 对应的16进制表示值
         */
        private String value;

        Type(String value) {
            this.value = value;
        }

        /**
         * 转换成十进制
         * @return
         */
        public int getDecimal(){
            return Integer.parseInt(value,16);
        }

        /**
         * 获取对应的16进制表示值
         * @return
         */
        public String getValue() {
            return value;
        }

    }

    /**
     * 常量池类型基类
     */
    abstract class Constant_Type{
        /**
         * 该常量的显示内容
         * @return
         */
        public abstract String getValue() throws Exception;

        /**
         * 该常量需要往下偏移多少字节
         * @return
         */
        public abstract int getOffset();
    }

    /**
     * 常量池数据类型之  utf-8编码字符串
     */
    class Constant_Type_Utf8 extends Constant_Type{
        //tag位 肯定为1
        Type tag;
        //字符串所占用的长度
        Type length;
        //字符串信息
        Type bytes;

        Constant_Type_Utf8(Type length, Type bytes) {
            this.length = length;
            this.bytes = bytes;
        }

        @Override
        public String getValue() throws Exception{
            if(bytes == null||bytes.getValue() == null){
                return "";
            }
            //这里需要把16进制转换成字符串
            return Util.toStringHex(bytes.getValue());
        }

        /**
         * 获取偏移量
         * @return
         */
        @Override
        public int getOffset(){
            //tag位占用1个字节,length位占用2个字节.bytes位占用了length个字节
            int count = 1 + 2 + length.getDecimal();
            return count;
        }
    }
    
}
