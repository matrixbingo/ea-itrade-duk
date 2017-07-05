package ea.itrade.duk.dto;

import com.dukascopy.api.*;
import ea.itrade.duk.base.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by liang.wang.sh on 2017/7/1.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDto {
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IAccount account;
    private IChart chart;
    private IChartPanel chartPanel;

    public StrategyDto(IContext context){
        this.chart = context.getChart(Constants.instrument);
        this.context = context;
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.indicators = context.getIndicators();
        this.account = context.getAccount();
    }

    public IBar getBar(Period period, int shift) throws JFException{
        return this.history.getBar(Constants.instrument, period, Constants.offerSide, shift);
    }

    public List<IBar> getBars(Period period, long from, long to) throws JFException{
        return this.history.getBars(Constants.instrument, period, Constants.offerSide, Constants.filer, from, to);
    }

    public double[][] macd( Period period, long from, long to) throws JFException{
        return this.indicators.macd(Constants.instrument, period, Constants.offerSide, Constants.appliedPrice, Constants.fastMACDPeriod, Constants.slowMACDPeriod, Constants.signalMACDPeriod, Constants.filer, from, to);
    }
}
