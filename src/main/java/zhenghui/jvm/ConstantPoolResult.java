package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: 下午4:15
 * 
 */
public class ConstantPoolResult {

    public String[] getStrs() {
        return strs;
    }

    public void setStrs(String[] strs) {
        this.strs = strs;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    /**
     * 直接打印的信息
     */
    String[] strs;

    /**
     * 当前指针的位置
     */
    int handle;
}
