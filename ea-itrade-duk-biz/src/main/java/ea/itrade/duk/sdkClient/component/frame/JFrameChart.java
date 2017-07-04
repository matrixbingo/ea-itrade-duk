package ea.itrade.duk.sdkClient.component.frame;

import com.dukascopy.api.IChart;
import com.dukascopy.api.system.tester.ITesterChartController;
import com.dukascopy.api.system.tester.ITesterGui;
import ea.itrade.duk.sdkClient.component.base.ChartControlComm;

/**
 * Created by wangliang on 2017/7/3.
 */
public class JFrameChart {

    private static ITesterChartController testerChartController;

    public static ITesterChartController getChartController() {
        if(testerChartController != null){
            return testerChartController;
        }
        if (ChartControlComm.chartPanels == null || ChartControlComm.chartPanels.size() == 0) {
            return null;
        }
        IChart chart = ChartControlComm.chartPanels.keySet().iterator().next();
        ITesterGui gui = ChartControlComm.chartPanels.get(chart);
        ITesterChartController chartController = gui.getTesterChartController();
        testerChartController = chartController;
        return chartController;
    }
}
