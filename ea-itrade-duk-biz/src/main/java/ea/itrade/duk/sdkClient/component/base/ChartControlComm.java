package ea.itrade.duk.sdkClient.component.base;

import com.dukascopy.api.IChart;
import com.dukascopy.api.system.tester.ITesterExecutionControl;
import com.dukascopy.api.system.tester.ITesterGui;

import javax.swing.*;
import java.util.Map;

/**
 * Created by liang.wang.sh on 2017/7/2.
 */
public class ChartControlComm {
    public static final int frameWidth = 1000;
    public static final int frameHeight = 600;
    public static final int controlPanelHeight = 100;
    public static final int controlPanelMaxHeight = 150;

    public static JPanel currentChartPanel = null;
    public static ITesterExecutionControl executionControl = null;

    public static JPanel controlPanel = null;
    public static JButton startStrategyButton = null;
    public static JButton pauseButton = null;
    public static JButton continueButton = null;
    public static JButton cancelButton = null;
    public static JPeriodComboBox jPeriodComboBox = null;

    public static Map<IChart, ITesterGui> chartPanels = null;

}
