package zhenghui.jvm.parse;

import static zhenghui.jvm.CommonConstant.TWO;

/**
 * Created by IntelliJ IDEA.
 * User: zhenghui
 * Date: 13-1-14
 * Time: 下午4:45
 * 一些基本信息的解析
 */
public class BaseInfoParse {

    /**
     * class的十六进制字节码
     */
    private String code;

    public BaseInfoParse(String code) {
        this.code = code;
    }

    /**
     * 解析基础信息,包括类名,魔数和版本号
     *
     * @return
     */
    public String[] pareBaseInfo() {
        String[] strs = new String[4];
        //todo 需要加上类名
        strs[0] = "source file : xxx";
        //前4个字节是魔数
        Type magic_num = new Type(code.substring(0 * TWO, 4 * TWO));
        strs[1] = "magic num : " + magic_num.getValue();
        //minnor 版本
        Type minor_version = new Type(code.substring(4 * TWO, 6 * TWO));
        strs[2] = "minor version : " + minor_version.getValue();
        //major版本
        Type major_version = new Type(code.substring(6 * TWO, 8 * TWO));
        strs[3] = "major version : " + major_version.getValue();
        return strs;
    }
}
