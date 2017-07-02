package ea.itrade.duk.sdkClient.component.panel;

import com.dukascopy.api.IChart;
import com.dukascopy.api.system.tester.ITesterExecution;
import com.dukascopy.api.system.tester.ITesterExecutionControl;
import com.dukascopy.api.system.tester.ITesterGui;
import com.dukascopy.api.system.tester.ITesterUserInterface;
import ea.itrade.duk.sdkClient.component.base.JPeriodComboBox;

import javax.swing.*;
import java.util.Map;

@SuppressWarnings("serial")
public class ChartControlBase implements ITesterUserInterface, ITesterExecution {

    protected final int frameWidth = 1000;
    protected final int frameHeight = 600;
    protected final int controlPanelHeight = 100;
    protected final int controlPanelMaxHeight = 150;

    protected JPanel currentChartPanel = null;
    protected ITesterExecutionControl executionControl = null;

    protected JPanel controlPanel = null;
    protected JButton startStrategyButton = null;
    protected JButton pauseButton = null;
    protected JButton continueButton = null;
    protected JButton cancelButton = null;
    protected JPeriodComboBox jPeriodComboBox = null;

    private Map<IChart, ITesterGui> chartPanels = null;

    @Override
    public void setChartPanels(Map<IChart, ITesterGui> chartPanels) {

    }

    @Override
    public void setExecutionControl(ITesterExecutionControl executionControl) {

    }
}
