package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: ����3:45
 *
 *  //todo
 *  ���˼������,��Ȼ��ѧϰ���ʵ�,��ô����У���ԵĶ����Ͳ�д�˰�.
 */
public class Validation {
    

    public static ValidationCode validateMagicNum(String magic){
        ValidationCode code = new ValidationCode();
        if(CommonConstant.JVM_MAGIC_NUM.equalsIgnoreCase(magic)){
            code.setSuccess(true);
        } else {
            code.setMessage("ħ������!");
        }
        return code;
    }
    
    public static ValidationCode validateCompileVersion(String version){
        ValidationCode code = new ValidationCode();
        return code;
    }
}
