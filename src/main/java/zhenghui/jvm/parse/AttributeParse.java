package zhenghui.jvm.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private static final String ATTRIBUTE_CONSTANT_VALUE = "ConstantValue"; // field_info
    private static final String ATTRIBUTE_DEPRECATED = "Deprecated";//field_info class_info method_info

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
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        ParseResult[] results = null;
        int current = attribute_count_end;
        if (attribute_count.getDecimalInteger() > 0) {
            results = new ParseResult[attribute_count.getDecimalInteger()];
            for (int i = 0; i < attribute_count.getDecimalInteger(); i++) {
                //attribute_nameռ�������ֽ�,ָ�����ص�����.��Ҫ��ȡ���������ֵ.
                Type attribute_name = new Type(code.substring(current, current + 2 * TWO));
                String name = ConstantPoolParse.constantPoolMap.get(attribute_name.getDecimalInteger());
                ParseResult pr = null;
                switch (name) {
                    case ATTRIBUTE_CONSTANT_VALUE:
                        pr = readConstantValue(current);
                        break;
                    case ATTRIBUTE_DEPRECATED:
                        pr = readDeprecated(current);
                        break;
                    default:
                        //����޷�����ĳһ������
                        pr = readUnknowAttr(current);
                        break;
                }
                results[i] = pr;
                current = pr.getHandle();
            }
        }
        parseResult.setHandle(current);
        String[] strs = new String[1];
        if (results != null && results.length > 0) {
            String str = "field_info count:" + attribute_count.getDecimalInteger()+BLANK;
            for (int i = 0; i < results.length; i++) {
                ParseResult result = results[i];
                //�ֶεĽ������涼ֻ��һ��ֵ.����ֱ��д��0
                str += result.getStrs()[0] + BLANK;
            }
            strs[0] = str;
            parseResult.setStrs(strs);
        } else {
            strs[0] = "this field has no attribute";
        }
        return parseResult;
    }

    /**
     * ���������е����Ա�.��ʱû�н���ָ��.������ʱ�䲹��.
     * �����н�����������Code,Deprecated,Exceptions Synthetic
     * @param hand
     * @return
     */
    public ParseResult parseMethodAttribute(int hand){
        ParseResult result = new ParseResult();
        //attribute_countռ�������ֽ�
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        ParseResult[] results = null;
        int current = attribute_count_end;
        if (attribute_count.getDecimalInteger() > 0) {
            results = new ParseResult[attribute_count.getDecimalInteger()];
            for (int i = 0; i < attribute_count.getDecimalInteger(); i++) {
                //attribute_nameռ�������ֽ�,ָ�����ص�����.��Ҫ��ȡ���������ֵ.
                Type attribute_name = new Type(code.substring(current, current + 2 * TWO));
                String name = ConstantPoolParse.constantPoolMap.get(attribute_name.getDecimalInteger());
                ParseResult pr = null;
                switch (name) {
                    case ATTRIBUTE_CONSTANT_VALUE:
                        pr = readConstantValue(current);
                        break;
                    case ATTRIBUTE_DEPRECATED:
                        pr = readDeprecated(current);
                        break;
                    default:
                        //����޷�����ĳһ������
                        pr = readUnknowAttr(current);
                        break;
                }
                results[i] = pr;
                current = pr.getHandle();
            }
        }
        return result;
    }

    private ParseResult readCode(int hand){
        ParseResult result = new ParseResult();
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        //attribute_length ռ��4���ֽ�
        int attribute_length_end = atrribute_name_inidex_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(atrribute_name_inidex_end, attribute_length_end));



        result.setHandle(attribute_length_end + attribute_length.getDecimalInteger());
        return result;
    }

    /**
     * ���� Deprecated����
     *
     * @param hand
     * @return
     */
    private ParseResult readDeprecated(int hand) {
        ParseResult result = new ParseResult();
        //Deprecated������Ϊһ������Ե�����,ֻ�����л����޵�����.��������attribute_name �϶��� Deprecated
        //���� attribute_length�϶���0x00000000 .
        String str = "Deprecated!";
        String[] strs = new String[1];
        strs[0] = str;
        //attribute_nameռ�����ֽ�,attribute_length 4���ֽ�
        result.setHandle(hand + 6 * TWO);
        result.setStrs(strs);
        return result;
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
        String str = "ConstantValue:(attribute_name:" + ConstantPoolParse.constantPoolMap.get(attribute_name_index.getDecimalInteger()) + ",attribute_length:" + attribute_length.getDecimalInteger()
                + ",constantvalue:" + ConstantPoolParse.constantPoolMap.get(constantvalue_index.getDecimalInteger()) + ")";
        String[] strs = new String[1];
        strs[0] = str;
        result.setHandle(constantvalue_index_end);
        result.setStrs(strs);
        return result;
    }

    /**
     * ����һЩ��������
     * jvm specs �����û��Զ�������.��Ȼ,Ҳ�п�������Ϊ��û�����ȫ�����ԵĽ���,���Բ�ִ������δ���
     *
     * @param hand
     * @return
     */
    private ParseResult readUnknowAttr(int hand) {
        ParseResult result = new ParseResult();
        //atrribute_name_inidexռ�������ֽ�.���ﵱȻ���� ConstantValue
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        //attribute_length ռ��4���ֽ�
        int attribute_length_end = atrribute_name_inidex_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(atrribute_name_inidex_end, attribute_length_end));
        int attribute_index_end = attribute_length_end + attribute_length.getDecimalInteger() * TWO;
        String str = "can not parse the attribute:(attribute_name:" + ConstantPoolParse.constantPoolMap.get(attribute_name_index.getDecimalInteger()) + ",attribute_length:" + attribute_length.getDecimalInteger() + ")";
        String[] strs = new String[1];
        strs[0] = str;
        result.setHandle(attribute_index_end);
        result.setStrs(strs);
        return result;
    }

}