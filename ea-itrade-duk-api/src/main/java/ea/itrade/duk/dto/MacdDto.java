package ea.itrade.duk.dto;

/**
 * Created by wangliang on 2017/6/20.
 */
public class MacdDto {
    private int shift;
    private double[] macd;

    public MacdDto(int shift, double[] macd){
        this.shift = shift;
        this.macd = macd;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public double[] getMacd() {
        return macd;
    }

    public void setMacd(double[] macd) {
        this.macd = macd;
    }
}
