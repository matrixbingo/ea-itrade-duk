package ea.itrade.duk.sdkClient;

import com.dukascopy.api.*;
import com.dukascopy.api.DataType.*;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.util.TicksFeedDescriptor;
import com.dukascopy.api.system.ClientFactory;
import com.dukascopy.api.system.IClient;
import com.dukascopy.api.system.ISystemListener;
import ea.itrade.duk.base.JForexUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

import static com.dukascopy.api.DataType.*;
import static com.dukascopy.api.Instrument.EURUSD;
import static com.dukascopy.api.Period.*;
import static com.dukascopy.api.PriceRange.*;
import static com.dukascopy.api.TickBarSize.*;

/**
 * Demonstrates the usage of Standalone Charts API (open charts / panels / control presentation view)<br/>
 * UI management and controls implemeneted by Client's methods
 */
public class MainOpenChartAddChartObject extends JFrame {

	private static final long serialVersionUID = 7287096355484721028L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainOpenChartAddChartObject.class);

	private static String jnlpUrl = "https://www.dukascopy.com/client/demo/jclient/jforex.jnlp";
	// user name
	private static String userName = JForexUser.DEMO_USERNAME;
	// password
	private static String password = JForexUser.DEMO_PASSWORD;

	private final int frameWidth = 1000;
	private final int frameHeight = 600;

	private final int controlPanelHeight = 50;
	private final int controlPanelMaxHeight = 100;

	Map<IChart, ChartFrame> chartFrameMap = new HashMap<IChart, ChartFrame>();

	public static void main(String[] args) throws Exception {
		MainOpenChartAddChartObject mainGUI = new MainOpenChartAddChartObject();
		mainGUI.startStrategy();

	}

	private void startStrategy() throws Exception {
		// get the instance of the IClient interface
		final IClient client = ClientFactory.getDefaultInstance();
		// set the listener that will receive system events
		client.setSystemListener(new ISystemListener() {
			private int lightReconnects = 3;

			@Override
			public void onStart(long processId) {
				LOGGER.info("Strategy started: " + processId);
			}

			@Override
			public void onStop(long processId) {
				LOGGER.info("Strategy stopped: " + processId);
				if (client.getStartedStrategies().size() == 0) {
					System.exit(0);
				}
			}

			@Override
			public void onConnect() {
				LOGGER.info("Connected");
				lightReconnects = 3;
			}

			@Override
			public void onDisconnect() {
				LOGGER.warn("Disconnected");
				if (lightReconnects > 0) {
					client.reconnect();
					--lightReconnects;
				} else {
					try {
						// sleep for 10 seconds before attempting to reconnect
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// ignore
					}
					try {
						client.connect(jnlpUrl, userName, password);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		});

		LOGGER.info("Connecting...");
		// connect to the server using jnlp, user name and password
		client.connect(jnlpUrl, userName, password);

		// wait for it to connect
		int i = 10; // wait max ten seconds
		while (i > 0 && !client.isConnected()) {
			LOGGER.info("i=" + i);
			Thread.sleep(1000);
			i--;
		}
		if (!client.isConnected()) {
			LOGGER.error("Failed to connect Dukascopy servers");
			System.exit(1);
		}

		// subscribe to the instruments
		final Instrument[] instrArr = new Instrument[] { EURUSD };
		Set<Instrument> instruments = new HashSet<Instrument>(Arrays.asList(instrArr));

		LOGGER.info("Subscribing instruments...");
		client.setSubscribedInstruments(instruments);

		// open charts
		for (Instrument instrument : instrArr) {
			IFeedDescriptor feedDescriptor = new TicksFeedDescriptor(instrument);
			feedDescriptor.setOfferSide(OfferSide.BID);// need to set due to platform requirements
			IChart chart = client.openChart(feedDescriptor);
			final IClientGUI clientGUI = client.getClientGUI(chart);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ChartFrame frame = new ChartFrame(clientGUI, client.getSubscribedInstruments());
					chartFrameMap.put(clientGUI.getChart(), frame);
					// Handle manual close - we need to call IClient.closeChart for strategy to know that the chart is no more there
					frame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							LOGGER.info("Chart manually closed, removing the chart from the strategy context");
							client.closeChart(clientGUI.getChart());
							chartFrameMap.remove(clientGUI.getChart());
							if (chartFrameMap.isEmpty()) {
								LOGGER.info("All charts closed, stopping the program");
								System.exit(0);
							}
						}
					});
				}
			});
		}
		// a strategy that checks which charts are available from it
		client.startStrategy(new IStrategy() {

			private IContext context;

			@Override
			public void onStart(IContext context) throws JFException {
				this.context = context;
			}

			@Override
			public void onTick(Instrument instrument, ITick tick) throws JFException {
				IChart chart = context.getChart(instrument);
				if (chart != null) {
					System.out.println(chart.getFeedDescriptor() + " " + tick);
				}
			}
			public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {			}
			public void onMessage(IMessage message) throws JFException {			}
			public void onAccount(IAccount account) throws JFException {			}
			public void onStop() throws JFException {			}
		});

	}
	
	private interface IFeedChangeListener{
		void onFeedChange(IFeedDescriptor feedDescriptor);
	}
	
	@SuppressWarnings("serial")
	private class ChartObjectPanel extends JPanel {
		
		private final IChart chart;
		private ChartObjectPanel(IChart chart){
			this.chart = chart;
			addButtons();
		}
		
		private void addButtons(){			
			JButton btnVLine = new JButton("Add VLine");
			btnVLine.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					//draw the line at the time of the last drawn feed element on the chart
					ITimedData[] chartData = chart.getLastRequestedData();
					long time = chartData[chartData.length - 1].getTime();
					IChartObject obj = chart.getChartObjectFactory().createVerticalLine("vLine", time);
					obj.setColor(Color.RED);
					chart.add(obj);
				}});
			add(btnVLine);			
			JButton btnHLine = new JButton("Add HLine");
			btnHLine.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {	
					//draw the line in the chart price axis midpoint
					double price = chart.getMinPrice() + (chart.getMaxPrice() - chart.getMinPrice()) / 2;
					System.out.println(String.format("%.5f", price));					
					IChartObject obj = chart.getChartObjectFactory().createHorizontalLine("hLine", price);
					obj.setColor(Color.GREEN);
					chart.add(obj);
				}});
			add(btnHLine);
		}
		
	}

	@SuppressWarnings("serial")
	private class ChartFrame extends JFrame {

		private final IClientGUI clientGUI;
		private final Set<Instrument> availableInstruments;
		private IFeedChangeListener feedChangeListener;

		private ChartFrame(IClientGUI clientGUI, Set<Instrument> availableInstruments) {
			super(clientGUI.getChart().getFeedDescriptor().getInstrument().toString() + " chart controller");
			this.clientGUI = clientGUI;
			this.availableInstruments = availableInstruments;
			feedChangeListener = new IFeedChangeListener(){
				@Override
				public void onFeedChange(IFeedDescriptor feedDescriptor) {
					ChartFrame.this.setTitle(feedDescriptor.getInstrument() + " " + feedDescriptor.getDataType());
				}				
			};
			createFrame();
			addControlPanel();
			addChartPanel(clientGUI.getChartPanel());
			pack();
			setVisible(true);
		}

		private void createFrame() {
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
			requestFocusInWindow();
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension screenSize = tk.getScreenSize();
			int screenHeight = screenSize.height;
			int screenWidth = screenSize.width;
			// move every next chart by 20px down
			int openedChartCount = chartFrameMap.size();
			setLocation(screenWidth / 4 - openedChartCount * 20, screenHeight / 4 - openedChartCount * 20);
		}

		/**
		 * Add chart controls panel
		 */
		private void addControlPanel() {
			IFeedDescriptor feedDescriptor = clientGUI.getChart().getFeedDescriptor();
			IClientChartPresentationManager chartPresentationManager = clientGUI.getChartPresentationManager();

			setTitle(feedDescriptor.getInstrument().toString());

			JPanel controlPanel = new JPanel();
			FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
			controlPanel.setLayout(flowLayout);
			controlPanel.setPreferredSize(new Dimension(frameWidth, controlPanelHeight));
			controlPanel.setMinimumSize(new Dimension(frameWidth, controlPanelHeight));
			controlPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, controlPanelMaxHeight));

			controlPanel.add(new FeedDescriptorPanel(feedDescriptor, chartPresentationManager, availableInstruments, feedChangeListener));
			controlPanel.add(new ChartObjectPanel(clientGUI.getChart()));
			getContentPane().add(controlPanel);
		}

		/**
		 * Add chart panel to the frame
		 * 
		 * @param panel
		 */
		private void addChartPanel(JPanel chartPanel) {
			chartPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - controlPanelHeight));
			chartPanel.setMinimumSize(new Dimension(frameWidth, 200));
			chartPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
			getContentPane().add(chartPanel);
		}
	}

	@SuppressWarnings("serial")
	private class FeedDescriptorPanel extends JPanel {

		private JComboBox comboBoxDataType;
		private JComboBox comboBoxInstruments;
		private JComboBox comboBoxOfferSide;
		private JComboBox comboBoxFilter;
		private JComboBox comboBoxPeriod;
		private JComboBox comboBoxPriceRange;
		private JComboBox comboBoxReversalAmount;
		private JComboBox comboBoxTickBarSize;
		private JComboBox comboBoxDataRepresentationType;

		final IFeedDescriptor feedDescriptor;
		final IClientChartPresentationManager chartPresentationManager;
		final IFeedChangeListener feedChangeListener;

		public FeedDescriptorPanel(
				IFeedDescriptor feedDescriptor, 
				IClientChartPresentationManager chartPresentationManager, 
				Set<Instrument> availableInstruments, 
				IFeedChangeListener feedChangeListener
			) {

			this.feedDescriptor = feedDescriptor;
			this.chartPresentationManager = chartPresentationManager;
			this.feedChangeListener = feedChangeListener;
			this.setLayout(new FlowLayout(FlowLayout.LEFT));

			comboBoxDataType = setupComboBox(DataType.values(), "Data type", feedDescriptor.getDataType());
			comboBoxInstruments = setupComboBox(availableInstruments.toArray(), "Instrument", feedDescriptor.getInstrument());
			comboBoxOfferSide = setupComboBox(OfferSide.values(), "Offer Side", feedDescriptor.getOfferSide());
			comboBoxFilter = setupComboBox(Filter.values(), "Filter", feedDescriptor.getFilter());
			// Note that for most of there we put only some of the available values
			comboBoxPeriod = setupComboBox(new Period[] { TEN_SECS, ONE_MIN, TEN_MINS, ONE_HOUR, FOUR_HOURS, DAILY }, "Period",
					feedDescriptor.getPeriod());
			comboBoxPriceRange = setupComboBox(new PriceRange[] { ONE_PIP, TWO_PIPS, THREE_PIPS, FOUR_PIPS, FIVE_PIPS, SIX_PIPS },
					"Price Range", feedDescriptor.getPriceRange());
			comboBoxReversalAmount = setupComboBox(new ReversalAmount[] { ReversalAmount.ONE, ReversalAmount.TWO, ReversalAmount.THREE },
					"Reversal Amount", feedDescriptor.getReversalAmount());
			comboBoxTickBarSize = setupComboBox(new TickBarSize[] { TWO, THREE, FOUR, FIVE }, "Tick Bar Size",
					feedDescriptor.getTickBarSize());
			comboBoxDataRepresentationType = new JComboBox(feedDescriptor.getDataType().getSupportedPresentationTypes().toArray());
			comboBoxDataRepresentationType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (comboBoxDataRepresentationType.getSelectedItem() != null) {
						FeedDescriptorPanel.this.chartPresentationManager
								.setDataPresentationType((DataPresentationType) comboBoxDataRepresentationType.getSelectedItem());
					}
				}
			});

			add(comboBoxDataType);
			add(comboBoxDataRepresentationType);
			add(comboBoxInstruments);
			add(comboBoxPeriod);
			add(comboBoxOfferSide);
			add(comboBoxFilter);
			add(comboBoxPriceRange);
			add(comboBoxReversalAmount);
			add(comboBoxTickBarSize);
			
			onSelectionChange();
		}

		private void resetDataRepresentationTypeCombobox() {
			comboBoxDataRepresentationType.removeAllItems();
			for (Object o : feedDescriptor.getDataType().getSupportedPresentationTypes()) {
				comboBoxDataRepresentationType.addItem(o);
			}
		}

		private JComboBox setupComboBox(final Object items[], final String name, Object defaultValue) {
			JComboBox comboBox = new JComboBox(items);
			comboBox.setSelectedItem(defaultValue == null ? items[0] : defaultValue);
			comboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onSelectionChange();
					if (e.getSource() == comboBoxDataType) {
						resetDataRepresentationTypeCombobox();
					}
				}

			});
			comboBox.setToolTipText(name);
			return comboBox;
		}
		
		private void onSelectionChange(){
			updateFeedDesciptor();
			updateComboBoxes();
		}

		private void updateComboBoxes() {

			DataType dataType = (DataType) comboBoxDataType.getSelectedItem();

			// visibility conditions according to IFeedDescription interface documentation
			comboBoxDataType.setVisible(true);
			comboBoxInstruments.setVisible(true);
			comboBoxOfferSide.setVisible(dataType != TICKS);
			comboBoxFilter.setVisible(dataType == TIME_PERIOD_AGGREGATION);
			comboBoxPeriod.setVisible(dataType == TIME_PERIOD_AGGREGATION);
			comboBoxPriceRange.setVisible(dataType == PRICE_RANGE_AGGREGATION || dataType == POINT_AND_FIGURE || dataType == RENKO);
			comboBoxReversalAmount.setVisible(dataType == POINT_AND_FIGURE);
			comboBoxTickBarSize.setVisible(dataType == TICK_BAR);
		}

		private void updateFeedDesciptor() {

			feedDescriptor.setDataType((DataType) comboBoxDataType.getSelectedItem());
			feedDescriptor.setInstrument((Instrument) comboBoxInstruments.getSelectedItem());
			feedDescriptor.setPeriod((Period) comboBoxPeriod.getSelectedItem());
			feedDescriptor.setOfferSide((OfferSide) comboBoxOfferSide.getSelectedItem());
			feedDescriptor.setFilter((Filter) comboBoxFilter.getSelectedItem());
			feedDescriptor.setPriceRange((PriceRange) comboBoxPriceRange.getSelectedItem());
			feedDescriptor.setReversalAmount((ReversalAmount) comboBoxReversalAmount.getSelectedItem());
			feedDescriptor.setTickBarSize((TickBarSize) comboBoxTickBarSize.getSelectedItem());
			chartPresentationManager.setFeedDescriptor(feedDescriptor);
			
			feedChangeListener.onFeedChange(feedDescriptor);
		}

	}
}