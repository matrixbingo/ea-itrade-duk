package ea.itrade.duk.sdkClient.component.panel;

import com.dukascopy.api.*;
import com.dukascopy.api.system.ISystemListener;
import com.dukascopy.api.system.ITesterClient;
import com.dukascopy.api.system.TesterFactory;
import com.dukascopy.api.system.tester.*;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.base.JForexUser;
import ea.itrade.duk.ea.MacdBeili;
import ea.itrade.duk.sdkClient.component.base.ChartControlComm;
import ea.itrade.duk.sdkClient.component.base.ControlPanelUtil;
import ea.itrade.duk.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * This small program demonstrates how to initialize Dukascopy tester and start a strategy in GUI mode
 */
@Slf4j
public class ChartControl extends MainJFrame implements ITesterUserInterface, ITesterExecution {

    private IStrategy strategy;

    public ChartControl(IStrategy strategy){
        super(strategy);
        this.strategy = strategy;
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

    @Override
    public void startStrategy() throws Exception {
        //get the instance of the IClient interface
        final ITesterClient client = TesterFactory.getDefaultInstance();
        //set the listener that will receive system events
        client.setSystemListener(new ISystemListener() {
            @Override
            public void onStart(long processId) {
                log.info("Strategy started: " + processId);
                ControlPanelUtil.updateButtons();
            }

            @Override
            public void onStop(long processId) {
                log.info("Strategy stopped: " + processId);
                resetButtons();

                File reportFile = new File("C:\\report.html");
                try {
                    client.createReport(processId, reportFile);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                if (client.getStartedStrategies().size() == 0) {
                    //Do nothing
                }
            }

            @Override
            public void onConnect() {
                log.info("Connected");
            }

            @Override
            public void onDisconnect() {
                //tester doesn't disconnect
            }
        });

        log.info("Connecting...");
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
            log.error("Failed to connect Dukascopy servers");
            System.exit(1);
        }
        // custom historical data
        client.setDefaultFilter(Filter.WEEKENDS);
        client.setDataInterval(ITesterClient.DataLoadingMethod.ALL_TICKS, DateUtil.str2Date(Constants.dateFrom, DateUtil.TIME_FORMAT_DETAIL).getTime(), DateUtil.str2Date(Constants.dateEnd, DateUtil.TIME_FORMAT_DETAIL).getTime());
        //client.setDataInterval(Constants.dataIntervalPeriod, OfferSide.ASK, ITesterClient.InterpolationMethod.CLOSE_TICK, DateUtil.str2Date(Constants.dateFrom, DateUtil.TIME_FORMAT_DETAIL).getTime(), DateUtil.str2Date(Constants.dateEnd, DateUtil.TIME_FORMAT_DETAIL).getTime());

        //set instruments that will be used in testing
        final Set<Instrument> instruments = new HashSet<Instrument>();
        instruments.add(Instrument.EURUSD);

        log.info("Subscribing instruments...");
        client.setSubscribedInstruments(instruments);
        //setting initial deposit
        client.setInitialDeposit(Instrument.EURUSD.getSecondaryJFCurrency(), 50000);
        //load data
        log.info("Downloading data");
        Future<?> future = client.downloadData(null);
        //wait for downloading to complete
        future.get();
        //start the strategy
        log.info("Starting strategy");

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
                Map<Instrument, ITesterIndicatorsParameters> indicatorParameters = new HashMap<>();
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
                        log.info(information);
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

    public static void main(String[] args) throws Exception {
        //IStrategy strategy = new MacdAndArw();
        IStrategy strategy = new MacdBeili();
        ChartControl chartControl = new ChartControl(strategy);
        chartControl.showChartFrame();

    }

}
