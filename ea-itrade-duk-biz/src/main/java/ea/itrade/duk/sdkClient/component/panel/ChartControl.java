package ea.itrade.duk.sdkClient.component.panel;

import com.dukascopy.api.IChart;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.LoadingProgressListener;
import com.dukascopy.api.system.ISystemListener;
import com.dukascopy.api.system.ITesterClient;
import com.dukascopy.api.system.TesterFactory;
import com.dukascopy.api.system.tester.*;
import ea.itrade.duk.base.JForexUser;
import ea.itrade.duk.ea.MacdAndArw;
import ea.itrade.duk.sdkClient.component.base.ChartControlComm;
import ea.itrade.duk.sdkClient.component.base.ControlPanelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * This small program demonstrates how to initialize Dukascopy tester and start a strategy in GUI mode
 */
@SuppressWarnings("serial")
public class ChartControl extends JFrame implements ITesterUserInterface, ITesterExecution {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartControl.class);

    private IStrategy strategy;

    public ChartControl(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }

    public ChartControl(IStrategy strategy){
        this.strategy = strategy;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }
    
    @Override
    public void setChartPanels(Map<IChart, ITesterGui> chartPanels) {
        ChartControlComm.chartPanels = chartPanels;
        ChartControlComm.jPeriodComboBox.setChartPanels(chartPanels);
        
        if(chartPanels != null && chartPanels.size() > 0){
            
            IChart chart = chartPanels.keySet().iterator().next();
            Instrument instrument = chart.getInstrument();
            setTitle(instrument.toString() + " " + chart.getSelectedOfferSide() + " " + chart.getSelectedPeriod());
            
            JPanel chartPanel = chartPanels.get(chart).getChartPanel();
            addChartPanel(chartPanel);
        }
    }

    @Override
    public void setExecutionControl(ITesterExecutionControl executionControl) {
        ChartControlComm.executionControl = executionControl;
    }
    
    public void startStrategy() throws Exception {
        //get the instance of the IClient interface
        final ITesterClient client = TesterFactory.getDefaultInstance();
        //set the listener that will receive system events
        client.setSystemListener(new ISystemListener() {
            @Override
            public void onStart(long processId) {
                LOGGER.info("Strategy started: " + processId);
                ControlPanelUtil.updateButtons();
            }

            @Override
            public void onStop(long processId) {
                LOGGER.info("Strategy stopped: " + processId);
                ControlPanelUtil.resetButtons();
                
                File reportFile = new File("C:\\report.html");
                try {
                    client.createReport(processId, reportFile);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
                if (client.getStartedStrategies().size() == 0) {
                    //Do nothing
                }
            }

            @Override
            public void onConnect() {
                LOGGER.info("Connected");
            }

            @Override
            public void onDisconnect() {
                //tester doesn't disconnect
            }
        });

        LOGGER.info("Connecting...");
        //connect to the server using jnlp, user name and password
        //connection is needed for data downloading
        client.connect(JForexUser.JNLP_URL, JForexUser.DEMO_USERNAME, JForexUser.DEMO_PASSWORD);

        //wait for it to connect
        int i = 10; //wait max ten seconds
        while (i > 0 && !client.isConnected()) {
            Thread.sleep(1000);
            i--;
        }
        if (!client.isConnected()) {
            LOGGER.error("Failed to connect Dukascopy servers");
            System.exit(1);
        }

        //set instruments that will be used in testing
        final Set<Instrument> instruments = new HashSet<Instrument>();
        instruments.add(Instrument.EURUSD);
        
        LOGGER.info("Subscribing instruments...");
        client.setSubscribedInstruments(instruments);
        //setting initial deposit
        client.setInitialDeposit(Instrument.EURUSD.getSecondaryCurrency(), 50000);
        //load data
        LOGGER.info("Downloading data");
        Future<?> future = client.downloadData(null);
        //wait for downloading to complete
        future.get();
        //start the strategy
        LOGGER.info("Starting strategy");

        // Implementation of IndicatorParameterBean 
        final class IndicatorParameterBean implements ITesterIndicatorsParameters {
            @Override
            public boolean isEquityIndicatorEnabled() {
                return true;
            }
            @Override
            public boolean isProfitLossIndicatorEnabled() {
                return true;
            }
            @Override
            public boolean isBalanceIndicatorEnabled() {
                return true;
            }
        }
        // Implementation of TesterVisualModeParametersBean
        final class TesterVisualModeParametersBean implements ITesterVisualModeParameters {
            @Override
            public Map<Instrument, ITesterIndicatorsParameters> getTesterIndicatorsParameters() {
                Map<Instrument, ITesterIndicatorsParameters> indicatorParameters = new HashMap<Instrument, ITesterIndicatorsParameters>();
                IndicatorParameterBean indicatorParameterBean = new IndicatorParameterBean();
                indicatorParameters.put(Instrument.EURUSD, indicatorParameterBean);
                return indicatorParameters;
            }
        }
        // Create TesterVisualModeParametersBean
        TesterVisualModeParametersBean visualModeParametersBean = new TesterVisualModeParametersBean();
   
        // Start strategy
        client.startStrategy(
                strategy,
            new LoadingProgressListener() {
                @Override
                public void dataLoaded(long startTime, long endTime, long currentTime, String information) {
                    LOGGER.info(information);
                }

                @Override
                public void loadingFinished(boolean allDataLoaded, long startTime, long endTime, long currentTime) {
                }

                @Override
                public boolean stopJob() {
                    return false;
                }
            },
            visualModeParametersBean,
            this,
            this
        );
        //now it's running
    }
    
    /**
     * Center a frame on the screen 
     */
    private void centerFrame(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
    }
    
    /**
     * Add chart panel to the frame
     * @param panel
     */
    private void addChartPanel(JPanel chartPanel){
        removecurrentChartPanel();
        
        ChartControlComm.currentChartPanel = chartPanel;
        chartPanel.setPreferredSize(new Dimension(ChartControlComm.frameWidth, ChartControlComm.frameHeight - ChartControlComm.controlPanelHeight));
        chartPanel.setMinimumSize(new Dimension(ChartControlComm.frameWidth, 200));
        chartPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        getContentPane().add(chartPanel);
        this.validate();
        chartPanel.repaint();
    }
    
    private void removecurrentChartPanel(){
        if(ChartControlComm.currentChartPanel != null){
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        ChartControl.this.getContentPane().remove(ChartControlComm.currentChartPanel);
                        ChartControl.this.getContentPane().repaint();
                    }
                });             
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void showChartFrame(){
        setSize(ChartControlComm.frameWidth, ChartControlComm.frameHeight);
        centerFrame();
        ControlPanelUtil.addControlPanel();
        setVisible(true);
    }
    
    public static void main(String[] args) throws Exception {
        IStrategy strategy = new MacdAndArw();
        ChartControl testerMainGUI = new ChartControl(strategy);
        testerMainGUI.showChartFrame();
    }

}
