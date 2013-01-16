package zhenghui.jvm.parse;

import static zhenghui.jvm.CommonConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-16
 * Time: ����11:49
 * ���Ա�Ľ���
 * ������ƻ�ܴ�,���Ե����ó���
 */
public class AttributeParse {

    private static final String ATTRIBUTE_CONSTANT_VALUE = "ConstantValue";

    public AttributeParse(String code) {
        this.code = code;
    }

    /**
     * class��ʮ�������ֽ���
     */
    private String code;

    /**
     * ����field_info�µ����Ա�
     *
     * @param hand ��filed_info��attribute_count(����)��ʼ�Ŀ�ʼ
     * @return
     */
    ParseResult parseFieldAttribute(int hand) {
        ParseResult parseResult = new ParseResult();
        //attribute_countռ�������ֽ�
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand,attribute_count_end));



        return parseResult;
    }

    /**
     * ����ConstantValue����
     *
     * @param hand
     * @return
     */
    private ParseResult readConstantValue(int hand) {
        ParseResult result = new ParseResult();
        //atrribute_name_inidexռ�������ֽ�.���ﵱȻ���� ConstantValue
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        //attribute_length ռ��4���ֽ� ��ʵ����ConstantValue���Ե�attribute_length��ֵ�϶���0x00000002
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