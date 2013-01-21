package zhenghui.jvm.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: ����4:15
 *
 * �ʼ���������,strs����Ƴ�������..����Ÿĳ�list.���Բ���������Щ�е���ֵķ���
 */
public class ParseResult {

    public List<String> getStrs() {
        return strs;
    }
    
    public void addStr(String str){
        strs.add(str);
    }

    public void setStrs(String[] strs) {
        for(String str : strs){
            addStr(str);
        }
    }
    
    public void addStrs(List<String> strs){
        this.strs.addAll(strs);
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
    List<String> strs = new ArrayList<>();

    /**
     * ��ǰָ���λ��
     */
    int handle;
}
