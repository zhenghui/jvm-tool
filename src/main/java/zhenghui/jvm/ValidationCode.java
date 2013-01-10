package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-10
 * Time: обнГ3:38
 *
 */
public final class ValidationCode {

    private boolean success = false;

    private String message;
    
    public boolean isSuccess(){
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
