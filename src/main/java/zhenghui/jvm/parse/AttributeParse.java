package zhenghui.jvm.parse;

import zhenghui.jvm.Util;

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
    private static final String ATTRIBUTE_LINE_NUMBER_TABLE = "LineNumberTable";// code_attribute
    private static final String ATTRIBUTE_LOCAL_VARIALBLE_TABLE = "LocalVariableTable";//code_attribute
    private static final String ATTRIBUTE_CODE = "Code";//method_info
    private static final String ATTRIBUTE_SOURCE_FILE = "SourceFile"; //class info

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
            String str = "field_info count:" + attribute_count.getDecimalInteger() + BLANK;
            for (int i = 0; i < results.length; i++) {
                ParseResult result = results[i];
                //字段的解析里面都只有一个值.所以直接写死0
                str += result.getStrs().get(0) + BLANK;
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
     * 方法中解析的属性有Code,Deprecated,Exceptions
     *
     * @param hand
     * @return
     */
    public ParseResult parseMethodAttribute(int hand) {
        ParseResult result = new ParseResult();
        //attribute_count占用两个字节
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        int current = attribute_count_end;
        if (attribute_count.getDecimalInteger() > 0) {
            for (int i = 0; i < attribute_count.getDecimalInteger(); i++) {
                //attribute_name占用两个字节,指向常量池的索引.需要获取这个常量的值.
                Type attribute_name = new Type(code.substring(current, current + 2 * TWO));
                String name = ConstantPoolParse.constantPoolMap.get(attribute_name.getDecimalInteger());
                ParseResult pr;
                switch (name) {
                    case ATTRIBUTE_CODE:
                        pr = readCode(current);
                        break;
                    case ATTRIBUTE_DEPRECATED:
                        pr = readDeprecated(current);
                        break;
                    default:
                        //如果无法解析某一个属性
                        pr = readUnknowAttr(current);
                        break;
                }
                result.addStrs(pr.getStrs());
                current = pr.getHandle();
            }
        }
        result.setHandle(current);
        return result;
    }

    /**
     * 解析class级别的属性
     *
     * @param hand
     * @return
     */
    public ParseResult parseAttribute(int hand) {
        ParseResult result = new ParseResult();
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        int current = attribute_count_end;
        if (attribute_count.getDecimalInteger() > 0) {
            for (int i = 0; i < attribute_count.getDecimalInteger(); i++) {
                int attribute_name_index_end = current + 2 * TWO;
                Type attribute_name = new Type(code.substring(current, attribute_name_index_end));
                String name = ConstantPoolParse.constantPoolMap.get(attribute_name.getDecimalInteger());
                ParseResult pr;
                switch (name) {
                    case ATTRIBUTE_SOURCE_FILE:
                        pr = parseSourceFile(current);
                        break;
                    default:
                        pr = readUnknowAttr(current);
                        break;
                }
                current = pr.getHandle();
                result.addStrs(pr.getStrs());
            }
        } else {
            result.addStr("this class has no attribute");
        }
        result.setHandle(current);

        return result;
    }

    private ParseResult parseSourceFile(int hand) {
        ParseResult result = new ParseResult();
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        //attribute_length 占用4个字节
        int attribute_length_end = atrribute_name_inidex_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(atrribute_name_inidex_end, attribute_length_end));
        int sourcefile_index_end = attribute_length_end + 2 * TWO;
        Type sourcefile_index = new Type(code.substring(attribute_length_end, sourcefile_index_end));
        result.addStr(ConstantPoolParse.constantPoolMap.get(attribute_name_index.getDecimalInteger()) + ":" + ConstantPoolParse.constantPoolMap.get(sourcefile_index.getDecimalInteger()));
        result.setHandle(attribute_length_end + attribute_length.getDecimalInteger() * TWO);
        return result;
    }

    private ParseResult readCode(int hand) {
        ParseResult result = new ParseResult();
        int atrribute_name_inidex_end = hand + 2 * TWO;
        Type attribute_name_index = new Type(code.substring(hand, atrribute_name_inidex_end));
        result.addStr(ConstantPoolParse.constantPoolMap.get(attribute_name_index.getDecimalInteger()) + ":");
        //attribute_length 占用4个字节
        int attribute_length_end = atrribute_name_inidex_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(atrribute_name_inidex_end, attribute_length_end));
        /**下面是attribute的内容.包括max_stack,max_locals.code_length等等**/
        int max_stack_end = attribute_length_end + 2 * TWO;
        Type max_stack = new Type(code.substring(attribute_length_end, max_stack_end));
        int max_locals_end = max_stack_end + 2 * TWO;
        Type max_locals = new Type(code.substring(max_stack_end, max_locals_end));
        result.addStr("stack=" + max_stack.getDecimalInteger() + BLANK + "locals=" + max_locals.getDecimalInteger());
        int code_length_end = max_locals_end + 4 * TWO;
        Type code_length = new Type(code.substring(max_locals_end, code_length_end));
        //todo 解析指令码 .这部分有点复杂,后面再做.
        int code_end = code_length_end + code_length.getDecimalInteger() * TWO;
        Type code_type = new Type(code.substring(code_length_end, code_end));
        result.addStr("code:" + code_type.getValue());
        /** end**/
        int exception_table_length_end = code_end + 2 * TWO;
        Type exception_table_length = new Type(code.substring(code_end, exception_table_length_end));
        //todo 异常表的解析,到时候与指令码解析一起做.这里就先跳过了
        //可以查看下 exception_table的结构,是由8个字节组成一个结构.
        int exception_table_end = exception_table_length_end + exception_table_length.getDecimalInteger() * 8 * TWO;
//        Type exception_table = new Type(code.substring(exception_table_length_end,exception_table_end));
        ParseResult pr = parseCodeAttribute(exception_table_end);
        result.addStrs(pr.getStrs());
        result.setHandle(attribute_length_end + attribute_length.getDecimalInteger() * TWO);
        return result;
    }

    /**
     * 解析Code中的属性.
     * 现在只解析两个属性 LineNumberTable 和 LocalVariableTable
     *
     * @param hand
     * @return
     */
    private ParseResult parseCodeAttribute(int hand) {
        ParseResult result = new ParseResult();
        int attribute_count_end = hand + 2 * TWO;
        Type attribute_count = new Type(code.substring(hand, attribute_count_end));
        int count = attribute_count.getDecimalInteger();
        result.addStr("attribute count : " + count);
        int current = attribute_count_end;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                int attribute_name_index_end = current + 2 * TWO;
                Type attribute_name_index = new Type(code.substring(current, attribute_name_index_end));
                String name = ConstantPoolParse.constantPoolMap.get(attribute_name_index.getDecimalInteger());
                ParseResult pr;
                switch (name) {
                    case ATTRIBUTE_LINE_NUMBER_TABLE:
                        pr = readLineNumberTable(current);
                        break;
                    case ATTRIBUTE_LOCAL_VARIALBLE_TABLE:
                        pr = readLocalVariableTable(current);
                        break;
                    default:
                        pr = readUnknowAttr(current);
                        break;
                }
                current = pr.getHandle();
                result.addStrs(pr.getStrs());
            }
        }
        result.setHandle(current);
        return result;
    }

    /**
     * 解析LineNumberTable
     *
     * @param hand
     * @return
     */
    private ParseResult readLineNumberTable(int hand) {
        ParseResult result = new ParseResult();
        int attibute_name_end = hand + 2 * TWO;
        Type attribute_name = new Type(code.substring(hand, attibute_name_end));
        result.addStr("attribute_name:" + ConstantPoolParse.constantPoolMap.get(attribute_name.getDecimalInteger()));
        int attribute_length_end = attibute_name_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(attibute_name_end, attribute_length_end));
        int line_number_table_length_end = attribute_length_end + 2 * TWO;
        Type line_number_table_length = new Type(code.substring(attribute_length_end, line_number_table_length_end));
        int current = line_number_table_length_end;
        for (int i = 0; i < line_number_table_length.getDecimalInteger(); i++) {
            int start_pc_end = current + 2 * TWO;
            Type start_pc = new Type(code.substring(current, start_pc_end));
            int line_number_end = start_pc_end + 2 * TWO;
            Type line_number = new Type(code.substring(start_pc_end, line_number_end));
            result.addStr("line " + line_number.getDecimalInteger() + ": " + start_pc.getDecimalInteger());
            current = line_number_end;
        }
        result.setHandle(attribute_length_end + attribute_length.getDecimalInteger() * TWO);
        return result;
    }

    private ParseResult readLocalVariableTable(int hand) {
        ParseResult result = new ParseResult();
        int attibute_name_end = hand + 2 * TWO;
        Type attribute_name = new Type(code.substring(hand, attibute_name_end));
        result.addStr("attribute_name:" + ConstantPoolParse.constantPoolMap.get(attribute_name.getDecimalInteger()));
        int attribute_length_end = attibute_name_end + 4 * TWO;
        Type attribute_length = new Type(code.substring(attibute_name_end, attribute_length_end));
        int local_variable_table_length_end = attribute_length_end + 2 * TWO;
        Type local_variable_table_length = new Type(code.substring(attribute_length_end, local_variable_table_length_end));
        result.addStr("start" + BLANK + "length" + BLANK + "name" + BLANK + "signature" + BLANK + "slot");
        int current = local_variable_table_length_end;
        for (int i = 0; i < local_variable_table_length.getDecimalInteger(); i++) {
            int start_pc_end = current + 2 * TWO;
            Type start_pc = new Type(code.substring(current, start_pc_end));
            int length_end = start_pc_end + 2 * TWO;
            Type length = new Type(code.substring(start_pc_end, length_end));
            int name_index_end = length_end + 2 * TWO;
            Type name_index = new Type(code.substring(length_end, name_index_end));
            int descriptor_index_end = name_index_end + 2 * TWO;
            Type descriptor_index = new Type(code.substring(name_index_end, descriptor_index_end));
            int index_end = descriptor_index_end + 2 * TWO;
            Type index = new Type(code.substring(descriptor_index_end, index_end));
            result.addStr(start_pc.getDecimalInteger() + BLANK + length.getDecimalInteger() + BLANK + ConstantPoolParse.constantPoolMap.get(name_index.getDecimalInteger())
                    + BLANK + ConstantPoolParse.constantPoolMap.get(descriptor_index.getDecimalInteger()) + BLANK + index.getDecimalInteger());
            current = index_end;
        }
        result.setHandle(attribute_length_end + attribute_length.getDecimalInteger() * TWO);
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