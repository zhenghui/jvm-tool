package zhenghui.jvm.parse;

import static zhenghui.jvm.CommonConstant.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: ����4:45
 * һЩ������Ϣ�Ľ���
 */
public class BaseInfoParse {

    /**
     * class��ʮ�������ֽ���
     */
    private String code;

    public BaseInfoParse(String code) {
        this.code = code;
    }

    /**
     * ����������Ϣ,��������,ħ���Ͱ汾��
     *
     * @return
     */
    public String[] parseBaseInfo() {
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

    /**
     * ����access_flags
     * @param hand
     * @return
     */
    public ParseResult parseAccessFlags(int hand){
        ParseResult result = new ParseResult();
        int access_flag_end = hand + 2 * TWO;
        Type access_flag_type = new Type(code.substring(hand,access_flag_end));
        int acccess_flag = access_flag_type.getDecimalInteger();
        String[] strs = new String[1];
        strs[0] = "flags: " + getAccessFlags(acccess_flag);
        //���ʱ���ڳ����غ���,ռ�������ֽ�.
        result.setHandle(access_flag_end);
        result.setStrs(strs);
        return result;
    }

    /**
     * ���� this_class super_class,interfaces
     * @param hand
     * @return
     */
    public ParseResult parseClassInfo(int hand){
        ParseResult result = new ParseResult();
        //this_class ռ�����ֽ�
        int this_class_end = hand + 2 * TWO;
        Type this_class = new Type(code.substring(hand,this_class_end));
        //super_class ռ�������ֽ�
        int super_class_end = this_class_end + 2 * TWO;
        Type super_class = new Type(code.substring(this_class_end,super_class_end));
        //�ӿڼ�����ռ�������ֽ�
        int interface_count_end = super_class_end + 2 * TWO;
        Type interfaces_couont = new Type(code.substring(super_class_end,interface_count_end));
        //�ӿ�����
        Type[] interfaces = null;
        //��ǰ��λ��
        int current = interface_count_end;
        //ÿ���ӿ�����ռ�������ֽ�
        if(interfaces_couont.getDecimalInteger() > 0){
            interfaces = new Type[interfaces_couont.getDecimalInteger()];
            for(int i = 0; i < interfaces_couont.getDecimalInteger(); i ++){
                int end = current + 2* TWO;
                interfaces[i] = new Type(code.substring(current, end));
                current = end;
            }
        }
        //this_class �� super_classд��һ��. .interfaces����д��һ��
        String[] strs = new String[2];
        strs[0] = "this_class  index:#" + this_class.getDecimalInteger() + BLANK + "super_class index:#" + super_class.getDecimalInteger();
        String interface_msg = "interface_count:" + interfaces_couont.getDecimalInteger()+BLANK;
        if(interfaces != null){
            for(Type inter : interfaces){
                interface_msg += "#" + inter.getDecimalInteger()+",";
            }
            interface_msg = interface_msg.substring(0,interface_msg.length() -1);
        }
        strs[1] = interface_msg;
        result.setStrs(strs);
        result.setHandle(current);
        return result;
    }

    private String getAccessFlags(int access_flag){
        String flags = "";
        if(hasTag(access_flag,ACC_PUBLIC)){
            flags += " ACC_PUBLIC,";
        }
        if(hasTag(access_flag,ACC_FINAL)){
            flags += " ACC_FINAL,";
        }
        if(hasTag(access_flag,ACC_SUPER)){
            flags += " ACC_SUPER,";
        }
        if(hasTag(access_flag,ACC_INTERFACE)){
            flags += " ACC_INTERFACE,";
        }
        if(hasTag(access_flag,ACC_ABSTRACT)){
            flags += " ACC_ABSTRACT,";
        }
        if(hasTag(access_flag,ACC_SYNTHETIC)){
            flags += " ACC_SYNTHETIC,";
        }
        if(hasTag(access_flag,ACC_ANNOTATION)){
            flags += " ACC_ANNOTATION,";
        }
        if(hasTag(access_flag,ACC_ENUM)){
            flags += " ACC_ENUM,";
        }
        if(hasTag(access_flag,ACC_MODULE)){
            flags += " ACC_MODULE,";
        }
        return flags.substring(0,flags.length() -1);
    }

    /**
     * �����flag���Ƿ��ж�Ӧ��tag
     * @param flag
     * @param tag
     * @return
     */
    private boolean hasTag(int flag,int tag){
        return (flag & tag) == tag;
    }
}
