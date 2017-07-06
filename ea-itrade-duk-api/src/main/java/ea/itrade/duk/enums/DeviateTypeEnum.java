package ea.itrade.duk.enums;

/**
 * Created by wangliang on 2017/7/6.
 */
public enum  DeviateTypeEnum {

    DeviateType_Up(0, "Deviate_Up"),
    DeviateType_dw(1, "Deviate_dw");

    DeviateTypeEnum(int type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    private int type;

    private String detail;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
