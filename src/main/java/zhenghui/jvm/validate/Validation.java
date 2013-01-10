package zhenghui.jvm.validate;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: 下午3:45
 *
 */
public class Validation {
    
    private static final  String JVM_MAGIC_NUM = "cafebabe";
    
    public static ValidationCode validateMagicNum(String magic){
        ValidationCode code = new ValidationCode();
        if(JVM_MAGIC_NUM.equalsIgnoreCase(magic)){
            code.setSuccess(true);
        } else {
            code.setMessage("魔数不对!");
        }
        return code;
    }
}
