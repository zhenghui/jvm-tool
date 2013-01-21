package zhenghui.jvm.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: 下午4:15
 *
 * 最开始设计有问题,strs被设计成了数组..后面才改成list.所以才有下面这些有点奇怪的方法
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
     * 直接打印的信息
     */
    List<String> strs = new ArrayList<>();

    /**
     * 当前指针的位置
     */
    int handle;
}
