package ea.itrade.duk.util;

import com.dukascopy.api.JFException;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.dto.StrategyDto;
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
        String key = Constants.getMacdUpArwKey(time);
        if(Constants.arrwMap.containsKey(key)){
            return;
        }
        long from = strategyDto.getHistory().getBar(Constants.instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 1).getTime();
        long to = strategyDto.getHistory().getBar(Constants.instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 0).getTime();
        double[][] macds = strategyDto.getIndicators().macd(Constants.instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, Constants.appliedPrice, Constants.fastMACDPeriod, Constants.slowMACDPeriod, Constants.signalMACDPeriod, from, to);
        long half = (to - from) / 2;

        strategyDto.getConsole().getInfo().println(this.strategyDto.getChart().getSelectedPeriod() + " | " + DateUtil.dateToStr(to) + " | " + DateUtil.dateToStr(time) + " |----> " + ArithUtil.fromatString(macds[2][0]) + ":" + ArithUtil.fromatString(macds[2][1]));
        double macd = macds[2][1];
        if (macd > 0) {
            macd = -Constants.macdArrowOffset;
        } else {
            macd -= Constants.macdArrowOffset;
        }
        ISignalUpChartObject signalArr = strategyDto.getChart().getChartObjectFactory().createSignalUp(key,time + half, macd);
        signalArr.setText(text);
        signalArr.setLineWidth(3f);
        signalArr.setStickToCandleTimeEnabled(false);
        signalArr.setColor(Color.RED);
        Constants.arrwMap.put(key, signalArr);
        strategyDto.getChartPanel().add(signalArr);
    }
}
