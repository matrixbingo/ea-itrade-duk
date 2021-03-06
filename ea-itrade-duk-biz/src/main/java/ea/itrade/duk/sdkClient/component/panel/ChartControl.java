package ea.itrade.duk.sdkClient.component.panel;

import com.dukascopy.api.*;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.util.TicksFeedDescriptor;
import com.dukascopy.api.system.ITesterClient;
import com.dukascopy.api.system.TesterFactory;
import com.dukascopy.api.system.tester.*;
import com.google.common.collect.Lists;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.base.JForexUser;
import ea.itrade.duk.ea.MacdDeviate;
import ea.itrade.duk.sdkClient.component.base.ChartControlComm;
import ea.itrade.duk.sdkClient.component.base.JPeriodComboBox;
import ea.itrade.duk.sdkClient.component.main.ISystemListenerImpl;
import ea.itrade.duk.sdkClient.component.main.IndicatorParameterBean;
import ea.itrade.duk.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * This small program demonstrates how to initialize Dukascopy tester and start a strategy in GUI mode
 */
@Slf4j
public class ChartControl extends MainJFrame {

    private IStrategy strategy;

    public ChartControl(IStrategy strategy){
        super(strategy);
        this.strategy = strategy;
    }

    @Override
    public void startStrategy() throws Exception {
        final ITesterClient client = TesterFactory.getDefaultInstance();
        client.setSystemListener(new ISystemListenerImpl(client));
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
        IFeedDescriptor feedDescriptor = new TicksFeedDescriptor(Constants.instrument);
        feedDescriptor.setOfferSide(Constants.offerSide);// need to set due to platform requirements
        feedDescriptor.setFilter(Constants.filer);
        feedDescriptor.setPeriod(Constants.dataIntervalPeriod);
        client.setCharts(Lists.newArrayList(feedDescriptor));
        // custom historical data
        client.setDefaultFilter(Filter.WEEKENDS);
        client.setDataInterval(ITesterClient.DataLoadingMethod.ALL_TICKS, DateUtil.str2Date(Constants.dateFrom, DateUtil.TIME_FORMAT_DETAIL).getTime(), DateUtil.str2Date(Constants.dateEnd, DateUtil.TIME_FORMAT_DETAIL).getTime());
        //client.setDataInterval(Constants.dataIntervalPeriod, OfferSide.ASK, ITesterClient.InterpolationMethod.CLOSE_TICK, DateUtil.str2Date(Constants.dateFrom, DateUtil.TIME_FORMAT_DETAIL).getTime(), DateUtil.str2Date(Constants.dateEnd, DateUtil.TIME_FORMAT_DETAIL).getTime());

        //set instruments that will be used in testing
        final Set<Instrument> instruments = new HashSet<>();
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

        // Start strategy
        client.startStrategy(
                strategy,
                new LoadingProgressListener() {
                    @Override
                    public void dataLoaded(long startTime, long endTime, long currentTime, String information) {
                        JPeriodComboBox.flag = true;
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
                new ITesterVisualModeParameters (){
                    @Override
                    public Map<Instrument, ITesterIndicatorsParameters> getTesterIndicatorsParameters() {
                        return new HashMap<Instrument, ITesterIndicatorsParameters>(){{
                            put(Constants.instrument, new IndicatorParameterBean());
                        }};
                    }
                },
                new ITesterExecution(){
                    @Override
                    public void setExecutionControl(ITesterExecutionControl executionControl) {
                        ChartControlComm.executionControl = executionControl;
                    }
                },
                new ITesterUserInterface(){
                    @Override
                    public void setChartPanels(Map<IChart, ITesterGui> chartPanels) {
                        ChartControlComm.chartPanels = chartPanels;
                        ChartControlComm.jPeriodComboBox.setChartPanels(chartPanels);
                        if(chartPanels != null && chartPanels.size() > 0){
                            IChart chart = chartPanels.keySet().iterator().next();
                            setTitle(chart.getInstrument().toString() + " " + chart.getSelectedOfferSide() + " " + chart.getSelectedPeriod());
                            addChartPanel(chartPanels.get(chart).getChartPanel());
                        }
                    }
                }
        );
    }

    public static void main(String[] args) throws Exception {
        //IStrategy strategy = new MacdAndArw();
        IStrategy strategy = new MacdDeviate();
        //IStrategy strategy = new R34ver10lot6();
        ChartControl chartControl = new ChartControl(strategy);
        chartControl.showChartFrame();
    }
}
