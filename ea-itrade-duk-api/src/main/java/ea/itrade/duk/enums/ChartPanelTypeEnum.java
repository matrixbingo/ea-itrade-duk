package ea.itrade.duk.enums;

public enum ChartPanelTypeEnum {

    ChartPanelType_MACD("ChartPanelType_MACD", "MACD"),
    ChartPanelType_JDK("ChartPanelType_JDK", "JDK");

    ChartPanelTypeEnum(String type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    private String type;

    private String detail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
