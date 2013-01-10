package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: 下午3:45
 *
 *  //todo
 *  最后思考了下,既然是学习性质的,那么这种校验性的东西就不写了吧.
 */
public class Validation {
    

    public static ValidationCode validateMagicNum(String magic){
        ValidationCode code = new ValidationCode();
        if(CommonConstant.JVM_MAGIC_NUM.equalsIgnoreCase(magic)){
            code.setSuccess(true);
        } else {
            code.setMessage("魔数不对!");
        }
        return code;
    }
    
    public static ValidationCode validateCompileVersion(String version){
        ValidationCode code = new ValidationCode();
        return code;
    }
}
