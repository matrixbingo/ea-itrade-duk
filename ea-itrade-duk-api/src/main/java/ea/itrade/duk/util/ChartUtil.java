package ea.itrade.duk.util;

import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.drawings.ISignalDownChartObject;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.dto.StrategyDto;
import ea.itrade.duk.dto.TimePriceDto;
import lombok.Builder;
import lombok.Setter;

import java.awt.*;

/**
 * Created by liang.wang.sh on 2017/6/24.
 */
@Builder
public class ChartUtil {

    @Setter
    private StrategyDto strategyDto;

    public void createSignalMacdUp(long time, String text) throws JFException {
        String key = Constants.getMacdUpArwKey(this.strategyDto.getChart().getSelectedPeriod(), time);
        if(Constants.arrwMap.containsKey(key)){
            return;
        }
        TimePriceDto timePrice = this.macdPrice(true);
        ISignalUpChartObject signalArr = strategyDto.getChart().getChartObjectFactory().createSignalUp(key,time + timePrice.getTime(), timePrice.price);
        signalArr.setText(text);
        signalArr.setLineWidth(2f);
        signalArr.setStickToCandleTimeEnabled(false);
        signalArr.setColor(Color.RED);
        signalArr.setOpacity(0.2f);
        Constants.arrwMap.put(key, signalArr);
        strategyDto.getChartPanel().add(signalArr);
    }

    public void createSignalMacdDw(long time, String text) throws JFException {
        String key = Constants.getMacdDwArwKey(this.strategyDto.getChart().getSelectedPeriod(), time);
        if(Constants.arrwMap.containsKey(key)){
            return;
        }
        TimePriceDto timePrice = this.macdPrice(false);
        ISignalDownChartObject signalArr = strategyDto.getChart().getChartObjectFactory().createSignalDown(key,time + timePrice.getTime(), timePrice.price);
        signalArr.setText(text);
        signalArr.setLineWidth(2f);
        signalArr.setStickToCandleTimeEnabled(false);
        signalArr.setColor(Color.GREEN);
        signalArr.setOpacity(0.2f);
        Constants.arrwMap.put(key, signalArr);
        strategyDto.getChartPanel().add(signalArr);
    }

    private TimePriceDto macdPrice(boolean isUp) throws JFException{
        long from = strategyDto.getHistory().getBar(Constants.instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 1).getTime();
        long to = strategyDto.getHistory().getBar(Constants.instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 0).getTime();
        double[][] macds = strategyDto.getIndicators().macd(Constants.instrument, this.strategyDto.getChart().getSelectedPeriod(), Constants.offerSide, Constants.appliedPrice, Constants.fastMACDPeriod, Constants.slowMACDPeriod, Constants.signalMACDPeriod, from, to);
        long half = (to - from) / 2;
        double macd = ArithUtil.round(macds[2][1], 5);

        //strategyDto.getConsole().getInfo().println(this.strategyDto.getChart().getSelectedPeriod() + " | " + DateUtil.dateToStr(to) + " | " + DateUtil.dateToStr(time) + " |----> " + ArithUtil.fromatString(macds[2][0]) + ":" + ArithUtil.fromatString(macds[2][1]));
        if(!isUp){
            if (macd > 0) {
                macd += Constants.macdArrowOffset;
            } else {
                macd = Constants.macdArrowOffset;
            }
        }else{
            if (macd > 0) {
                macd = -Constants.macdArrowOffset;
            } else {
                macd -= Constants.macdArrowOffset;
            }
        }
        return TimePriceDto.builder().price(macd).time(half).build();
    }
}
