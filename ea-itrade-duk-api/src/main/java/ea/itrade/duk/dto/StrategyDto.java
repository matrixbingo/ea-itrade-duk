package ea.itrade.duk.dto;

import com.dukascopy.api.*;
import ea.itrade.duk.base.Common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public StrategyDto(IContext context){
        this.chart = context.getChart(Common.instrument);
        this.context = context;
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();
        this.indicators = context.getIndicators();
        this.account = context.getAccount();
    }
}
