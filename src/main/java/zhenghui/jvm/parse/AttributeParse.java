package zhenghui.jvm.parse;

import static zhenghui.jvm.CommonConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-16
 * Time: 上午11:49
 * 属性表的解析
 * 这个估计会很大,所以单独拿出来
 */
public class AttributeParse {

    private static final String ATTRIBUTE_CONSTANT_VALUE = "ConstantValue";

    public AttributeParse(String code) {
        this.code = code;
    }

    /**
     * class的十六进制字节码
     */
    private String code;

    /**
     * 解析field_info下的属性表
     *
     * @param hand 从filed_info的attribute_count(包含)开始的开始
     * @return
     */
    ParseResult parseFieldAttribute(int hand) {
        ParseResult parseResult = new ParseResult();
        //attribute_count占用两个字节
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand,attribute_count_end));



        return parseResult;
    }

    /**
     * 解析ConstantValue属性
     *
     * @param hand
     * @return
     */
    private ParseResult readConstantValue(int hand) {
        ParseResult result = new ParseResult();
        //atrribute_name_inidex占用两个字节.这里当然就是 ConstantValue
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        //attribute_length 占用4个字节 其实这里ConstantValue属性的attribute_length的值肯定是0x00000002
        int attribute_length_end = atrribute_name_inidex_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(atrribute_name_inidex_end, attribute_length_end));
        int constantvalue_index_end = attribute_length_end + 2 * TWO;
        Type constantvalue_index = new Type(code.substring(attribute_length_end, constantvalue_index_end));
        String str = "ConstantValue:(attribute_name_index:#" + attribute_name_index.getDecimalInteger() + ",attribute_length:" + attribute_length.getDecimalInteger()
                + ",constantvalue_index:#" + constantvalue_index.getDecimalInteger();
        String[] strs = new String[1];
        strs[0] = str;
        result.setHandle(constantvalue_index_end);
        result.setStrs(strs);
        return result;
    }

}