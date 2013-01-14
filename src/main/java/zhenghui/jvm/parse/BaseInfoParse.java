package zhenghui.jvm.parse;

import static zhenghui.jvm.CommonConstant.TWO;

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
    public String[] pareBaseInfo() {
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
}
