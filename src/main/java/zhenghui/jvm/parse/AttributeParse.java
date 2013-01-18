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
 * Time: 上午11:49
 * 属性表的解析
 * 这个估计会很大,所以单独拿出来
 */
public class AttributeParse {


    private static final String ATTRIBUTE_CONSTANT_VALUE = "ConstantValue"; // field_info
    private static final String ATTRIBUTE_DEPRECATED = "Deprecated";//field_info class_info method_info

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
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        ParseResult[] results = null;
        int current = attribute_count_end;
        if (attribute_count.getDecimalInteger() > 0) {
            results = new ParseResult[attribute_count.getDecimalInteger()];
            for (int i = 0; i < attribute_count.getDecimalInteger(); i++) {
                //attribute_name占用两个字节,指向常量池的索引.需要获取这个常量的值.
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
                        //如果无法解析某一个属性
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
                //字段的解析里面都只有一个值.所以直接写死0
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
     * 解析方法中的属性表.暂时没有解析指令.后面有时间补上.
     * 方法中解析的属性有Code,Deprecated,Exceptions Synthetic
     * @param hand
     * @return
     */
    public ParseResult parseMethodAttribute(int hand){
        ParseResult result = new ParseResult();
        //attribute_count占用两个字节
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        ParseResult[] results = null;
        int current = attribute_count_end;
        if (attribute_count.getDecimalInteger() > 0) {
            results = new ParseResult[attribute_count.getDecimalInteger()];
            for (int i = 0; i < attribute_count.getDecimalInteger(); i++) {
                //attribute_name占用两个字节,指向常量池的索引.需要获取这个常量的值.
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
                        //如果无法解析某一个属性
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
        //attribute_length 占用4个字节
        int attribute_length_end = atrribute_name_inidex_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(atrribute_name_inidex_end, attribute_length_end));



        result.setHandle(attribute_length_end + attribute_length.getDecimalInteger());
        return result;
    }

    /**
     * 解析 Deprecated属性
     *
     * @param hand
     * @return
     */
    private ParseResult readDeprecated(int hand) {
        ParseResult result = new ParseResult();
        //Deprecated属性作为一个标记性的属性,只存在有或者无的区别.所以它的attribute_name 肯定是 Deprecated
        //它的 attribute_length肯定是0x00000000 .
        String str = "Deprecated!";
        String[] strs = new String[1];
        strs[0] = str;
        //attribute_name占两个字节,attribute_length 4个字节
        result.setHandle(hand + 6 * TWO);
        result.setStrs(strs);
        return result;
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
        String str = "ConstantValue:(attribute_name:" + ConstantPoolParse.constantPoolMap.get(attribute_name_index.getDecimalInteger()) + ",attribute_length:" + attribute_length.getDecimalInteger()
                + ",constantvalue:" + ConstantPoolParse.constantPoolMap.get(constantvalue_index.getDecimalInteger()) + ")";
        String[] strs = new String[1];
        strs[0] = str;
        result.setHandle(constantvalue_index_end);
        result.setStrs(strs);
        return result;
    }

    /**
     * 解析一些其他属性
     * jvm specs 允许用户自定义属性.当然,也有可能是因为我没有完成全部属性的解析,所以才执行了这段代码
     *
     * @param hand
     * @return
     */
    private ParseResult readUnknowAttr(int hand) {
        ParseResult result = new ParseResult();
        //atrribute_name_inidex占用两个字节.这里当然就是 ConstantValue
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        //attribute_length 占用4个字节
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