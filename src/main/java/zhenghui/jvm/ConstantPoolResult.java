package zhenghui.jvm;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: ����4:15
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
     * ֱ�Ӵ�ӡ����Ϣ
     */
    String[] strs;

    /**
     * ��ǰָ���λ��
     */
    int handle;
}
