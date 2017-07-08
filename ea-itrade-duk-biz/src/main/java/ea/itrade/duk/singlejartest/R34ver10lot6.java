package ea.itrade.duk.singlejartest;

import com.dukascopy.api.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/*
 * Created by VisualJForex Generator, version 2.24
 * Date: 01.07.2016 06:54
 */
public class R34ver10lot6 implements IStrategy {

	private CopyOnWriteArrayList<TradeEventAction> tradeEventActions = new CopyOnWriteArrayList<TradeEventAction>();
	private static final String DATE_FORMAT_NOW = "yyyyMMdd_HHmmss";
	private IEngine engine;
	private IConsole console;
	private IHistory history;
	private IContext context;
	private IIndicators indicators;
	private IUserInterface userInterface;

	@Configurable("defaultInstrument:")
	public Instrument defaultInstrument = Instrument.EURUSD;
	@Configurable("defaultSlippage:")
	public int defaultSlippage = 5;
	@Configurable("defaultPeriod:")
	public Period defaultPeriod = Period.TEN_MINS;
	@Configurable("defaultTradeAmount:")
	public double defaultTradeAmount = 6.0;
	@Configurable("defaultTakeProfit:")
	public int defaultTakeProfit = 50;
	@Configurable("defaultStopLoss:")
	public int defaultStopLoss = 25;

	private int _MinuteCurrent;
	private boolean _NoLongtMA14H4 = false;
	private long _TimeCloseCompare;
	private double _tempVar675 = 0.0;
	private Tick LastTick =  null ;
	private long _CandleTimeNoSignalMax;
	private long _TimeFlat;
	private int _DayOfWeekCandleNoSignalMax = 0;
	private boolean _NoShortMA14H4 = false;
	private Candle candle61 =  null ;
	private Candle candle63 =  null ;
	private int _DayOfWeekCurrent;
	private Candle LastBidCandle =  null ;
	private double _widthBandCurTFConst = 0.0;
	private double _lowerBandH4;
	private List<Candle> _setCandle2to30 =  null ;
	private double _middleBandH4;
	private IOrder _CurrentPos =  null ;
	private double _MainCandleSellHigh;
	private List<Candle> candles46 =  null ;
	private double _upperBandH4;
	private double _MainCandleBuyLow;
	private double _tempVar619 = 1.0E-4;
	private Candle _candle2 =  null ;
	private Candle _candle1 =  null ;
	private Candle _Candle0H4 =  null ;
	private boolean _NoLongLowerBandH4 = false;
	private double _Fibo618Buy;
	private List<Candle> candles42 =  null ;
	private String AccountId = "";
	private double _TPforClose;
	private boolean _EndSwitch = true;
	private double UseofLeverage;
	private List<IOrder> PendingPositions =  null ;
	private boolean _NoShortMA14H4M10 = false;
	private Candle _CandleCurrent =  null ;
	private double _middleBandCurrentTimeFrame;
	private int OverWeekendEndLeverage;
	private double _BodyCandleSellX5;
	private List<IOrder> OpenPositions =  null ;
	private Period _PeriodH1 = Period.ONE_HOUR;
	private List<Candle> candles56 =  null ;
	private double _widthBandCurrentTimeFrame;
	private Period _PeriodH4 = Period.FOUR_HOURS;
	private Candle candle34 =  null ;
	private Period _PeriodM30 = Period.THIRTY_MINS;
	private Candle candle33 =  null ;
	private String AccountCurrency = "";
	private Candle candle30 =  null ;
	private Candle candle31 =  null ;
	private double _STOH533slowkH4;
	private double _Candle1H4ClosePrice;
	private double _lowerBandCurrentTimeFrame;
	private long _TimeClose;
	private double Equity;
	private double _MA20;
	private double _BodyCandleSell;
	private double _BodyCandleBuy;
	private List<IOrder> AllPositions =  null ;
	private double _CandleLowLowest;
	private double _TPforSell;
	private String _PositionClose = "ORDER_CLOSE_OK";
	private long _TimeFlatConst;
	private Candle candle52 =  null ;
	private List<Candle> candles35 =  null ;
	private Candle candle51 =  null ;
	private double _BodyCandleBuyX5;
	private int _tempVar1 = 1;
	private double Leverage;
	private boolean _StopTrend = false;
	private long _TimeFlatConst0;
	private Candle candle54 =  null ;
	private int _tempVar2 = 2;
	private boolean _NoShortUpperBandH4 = false;
	private Candle candle55 =  null ;
	private int _HourCurrent;
	private double _MA14H4Shift0;
	private long _tempVar753 = 1133776800000l;
	private long _CandleTimeNoSignal;
	private int _DayOfWeekMainCandle = 0;
	private double _MA14H4Shift1;
	private long _Candle1Time;
	private double _CandlePrevHigh;
	private boolean _isGoLong = false;
	private boolean _NoLongtMA14H4M10 = false;
	private double _STOH533slowdH4;
	private double _tempVar661 = 2.0;
	private double _CandlePrevLow;
	private Period _PeriodM15 = Period.FIFTEEN_MINS;
	private Candle candle40 =  null ;
	private Candle candle41 =  null ;
	private List<Candle> candles25 =  null ;
	private boolean _isGoShort = false;
	private Candle _Candle1H4 =  null ;
	private double _stopForSell;
	private double _upperBandCurrentTimeFrame;
	private int MarginCutLevel;
	private Candle LastAskCandle =  null ;
	private double _stopForBuy;
	private double _Fibo618Sell;
	private boolean GlobalAccount;
	private double _TPforBuy;
	private double _CandleHighHighest;
	private IMessage LastTradeEvent =  null ;
	private double _widthBandH4;


	public void onStart(IContext context) throws JFException {
		this.engine = context.getEngine();
		this.console = context.getConsole();
		this.history = context.getHistory();
		this.context = context;
		this.indicators = context.getIndicators();
		this.userInterface = context.getUserInterface();

		ITick lastITick = context.getHistory().getLastTick(defaultInstrument);
		LastTick = new Tick(lastITick, defaultInstrument);

		IBar bidBar = context.getHistory().getBar(defaultInstrument, defaultPeriod, OfferSide.BID, 1);
		IBar askBar = context.getHistory().getBar(defaultInstrument, defaultPeriod, OfferSide.ASK, 1);
		LastAskCandle = new Candle(askBar, defaultPeriod, defaultInstrument, OfferSide.ASK);
		LastBidCandle = new Candle(bidBar, defaultPeriod, defaultInstrument, OfferSide.BID);

		if (indicators.getIndicator("BBANDS") == null) {
			indicators.registerDownloadableIndicator("1304","BBANDS");
		}
		if (indicators.getIndicator("SMA") == null) {
			indicators.registerDownloadableIndicator("1314","SMA");
		}
		if (indicators.getIndicator("STOCH") == null) {
			indicators.registerDownloadableIndicator("1279","STOCH");
		}
		subscriptionInstrumentCheck(defaultInstrument);

	}

	public void onAccount(IAccount account) throws JFException {
		AccountCurrency = account.getCurrency().toString();
		Leverage = account.getLeverage();
		AccountId= account.getAccountId();
		Equity = account.getEquity();
		UseofLeverage = account.getUseOfLeverage();
		OverWeekendEndLeverage = account.getOverWeekEndLeverage();
		MarginCutLevel = account.getMarginCutLevel();
		GlobalAccount = account.isGlobal();
	}

	private void updateVariables(Instrument instrument) {
		try {
			AllPositions = engine.getOrders();
			List<IOrder> listMarket = new ArrayList<>();
			for (IOrder order: AllPositions) {
				if (order.getState().equals(IOrder.State.FILLED)){
					listMarket.add(order);
				}
			}
			List<IOrder> listPending = new ArrayList<>();
			for (IOrder order: AllPositions) {
				if (order.getState().equals(IOrder.State.OPENED)){
					listPending.add(order);
				}
			}
			OpenPositions = listMarket;
			PendingPositions = listPending;
		} catch(JFException e) {
			e.printStackTrace();
		}
	}

	public void onMessage(IMessage message) throws JFException {
		if (message.getOrder() != null) {
			updateVariables(message.getOrder().getInstrument());
			LastTradeEvent = message;
			for (TradeEventAction event :  tradeEventActions) {
				IOrder order = message.getOrder();
				if (order != null && event != null && message.getType().equals(event.getMessageType())&& order.getLabel().equals(event.getPositionLabel())) {
					Method method;
					try {
						method = this.getClass().getDeclaredMethod(event.getNextBlockId(), Integer.class);
						method.invoke(this, new Integer[] {event.getFlowId()});
					} catch (SecurityException e) {
							e.printStackTrace();
					} catch (NoSuchMethodException e) {
						  e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} 
					tradeEventActions.remove(event); 
				}
			}   
			If_block_408(2);
		}
	}

	public void onStop() throws JFException {
	}

	public void onTick(Instrument instrument, ITick tick) throws JFException {
		LastTick = new Tick(tick, instrument);
		updateVariables(instrument);


	}

	public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
		LastAskCandle = new Candle(askBar, period, instrument, OfferSide.ASK);
		LastBidCandle = new Candle(bidBar, period, instrument, OfferSide.BID);
		updateVariables(instrument);
			If_block_11(1);

	}

    public void subscriptionInstrumentCheck(Instrument instrument) {
		try {
		      if (!context.getSubscribedInstruments().contains(instrument)) {
		          Set<Instrument> instruments = new HashSet<Instrument>();
		          instruments.add(instrument);
		          context.setSubscribedInstruments(instruments, true);
		          Thread.sleep(100);
		      }
		  } catch (InterruptedException e) {
		      e.printStackTrace();
		  }
		}

    public double round(double price, Instrument instrument) {
		BigDecimal big = new BigDecimal("" + price); 
		big = big.setScale(instrument.getPipScale() + 1, BigDecimal.ROUND_HALF_UP); 
		return big.doubleValue(); 
	}

    public ITick getLastTick(Instrument instrument) {
		try { 
			return (context.getHistory().getTick(instrument, 0)); 
		} catch (JFException e) { 
			 e.printStackTrace();  
		 } 
		 return null; 
	}

	private  void If_block_10(Integer flow) {
		int argument_1 = AllPositions.size();
		int argument_2 = 1;
		if (argument_1< argument_2) {
			If_block_264(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			MultipleAction_block_190(flow);
		}
	}

	private  void If_block_11(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Instrument argument_2 = LastBidCandle.getInstrument();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_12(flow);
		}
	}

	private  void MultipleAction_block_12(Integer flow) {
		If_block_13(flow);
		If_block_14(flow);
		If_block_15(flow);
		If_block_16(flow);
		If_block_17(flow);
	}

	private  void If_block_13(Integer flow) {
		Period argument_1 = _PeriodH4;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_22(flow);
		}
	}

	private  void If_block_14(Integer flow) {
		Period argument_1 = defaultPeriod;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_21(flow);
		}
	}

	private  void If_block_15(Integer flow) {
		Period argument_1 = _PeriodH1;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_20(flow);
		}
	}

	private  void If_block_16(Integer flow) {
		Period argument_1 = _PeriodM30;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_19(flow);
		}
	}

	private  void If_block_17(Integer flow) {
		Period argument_1 = _PeriodM15;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_18(flow);
		}
	}

	private  void MultipleAction_block_18(Integer flow) {
		MultipleAction_block_58(flow);
		MultipleAction_block_24(flow);
	}

	private  void MultipleAction_block_19(Integer flow) {
		MultipleAction_block_48(flow);
		MultipleAction_block_24(flow);
	}

	private  void MultipleAction_block_20(Integer flow) {
		MultipleAction_block_44(flow);
		MultipleAction_block_24(flow);
	}

	private  void MultipleAction_block_21(Integer flow) {
		MultipleAction_block_37(flow);
		MultipleAction_block_24(flow);
	}

	private  void MultipleAction_block_22(Integer flow) {
		MultipleAction_block_27(flow);
		If_block_178(flow);
		MultipleAction_block_24(flow);
	}

	private  void MultipleAction_block_23(Integer flow) {
		MultipleAction_block_65(flow);
		If_block_246(flow);
		MultipleAction_block_76(flow);
		MultipleAction_block_284(flow);
	}

	private  void MultipleAction_block_24(Integer flow) {
		MultipleAction_block_23(flow);
		MultipleAction_block_94(flow);
		MultipleAction_block_101(flow);
		If_block_144(flow);
	}

	private  void GetHistoricalCandles_block_25(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH4;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = 3;
		int argument_5 = 30;
		subscriptionInstrumentCheck(argument_1);
 		
        try {
			long lastBarTime = history.getBar(argument_1, argument_2, argument_3, argument_4).getTime(); 
			List<IBar> tempBarList = history.getBars(argument_1, argument_2, argument_3, Filter.WEEKENDS, argument_5, lastBarTime, 0); 
			_setCandle2to30 = new ArrayList<Candle>(); 
			for (IBar bar : tempBarList) { 
				_setCandle2to30.add(new Candle(bar, argument_2, argument_1, argument_3)); 
			} 
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private void BBANDS_block_26(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH4;
		int argument_3 = 1;
		int argument_4 = 20;
		double argument_5 = 2.0;
		double argument_6 = 2.0;
		IIndicators.MaType argument_7 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[4];
		params[0] = 20;
		params[1] = 2.0;
		params[2] = 2.0;
		params[3] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"BBANDS", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._upperBandH4 = Double.NaN;
			} else { 
				this._upperBandH4 = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._middleBandH4 = Double.NaN;
			} else { 
				this._middleBandH4 = (((double [])indicatorResult[1])[0]);
			} 
			if ((new Double(((double [])indicatorResult[2])[0])) == null) {
				this._lowerBandH4 = Double.NaN;
			} else { 
				this._lowerBandH4 = (((double [])indicatorResult[2])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		Assign_block_28(flow);
	}

	private  void MultipleAction_block_27(Integer flow) {
		GetHistoricalCandle_block_30(flow);
		GetHistoricalCandle_block_31(flow);
		BBANDS_block_26(flow);
		Calculation_block_302(flow);
		Assign_block_321(flow);
	}

	private  void Assign_block_28(Integer flow) {
		double argument_1 = 0.05;
		_widthBandCurTFConst =  argument_1;
		}

	private  void GetHistoricalCandle_block_30(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH4;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar1;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle1 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		Assign_block_263(flow);
	}

	private  void GetHistoricalCandle_block_31(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH4;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar2;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle2 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		GetHistoricalCandles_block_25(flow);
	}

	private  void GetHistoricalCandle_block_33(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = defaultPeriod;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar1;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle1 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		GetHistoricalCandle_block_34(flow);
	}

	private  void GetHistoricalCandle_block_34(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = defaultPeriod;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar2;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle2 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private  void GetHistoricalCandles_block_35(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = defaultPeriod;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = 3;
		int argument_5 = 30;
		subscriptionInstrumentCheck(argument_1);
 		
        try {
			long lastBarTime = history.getBar(argument_1, argument_2, argument_3, argument_4).getTime(); 
			List<IBar> tempBarList = history.getBars(argument_1, argument_2, argument_3, Filter.WEEKENDS, argument_5, lastBarTime, 0); 
			_setCandle2to30 = new ArrayList<Candle>(); 
			for (IBar bar : tempBarList) { 
				_setCandle2to30.add(new Candle(bar, argument_2, argument_1, argument_3)); 
			} 
        } catch (JFException e) {
            e.printStackTrace();
        }
		Calculation_block_304(flow);
	}

	private void BBANDS_block_36(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = defaultPeriod;
		int argument_3 = 1;
		int argument_4 = 20;
		double argument_5 = 2.0;
		double argument_6 = 2.0;
		IIndicators.MaType argument_7 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[4];
		params[0] = 20;
		params[1] = 2.0;
		params[2] = 2.0;
		params[3] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"BBANDS", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._upperBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._upperBandCurrentTimeFrame = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._middleBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._middleBandCurrentTimeFrame = (((double [])indicatorResult[1])[0]);
			} 
			if ((new Double(((double [])indicatorResult[2])[0])) == null) {
				this._lowerBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._lowerBandCurrentTimeFrame = (((double [])indicatorResult[2])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		Assign_block_38(flow);
	}

	private  void MultipleAction_block_37(Integer flow) {
		GetHistoricalCandle_block_33(flow);
		GetHistoricalCandles_block_35(flow);
		BBANDS_block_36(flow);
	}

	private  void Assign_block_38(Integer flow) {
		double argument_1 = 0.0024;
		_widthBandCurTFConst =  argument_1;
			Assign_block_205(flow);
	}

	private  void GetHistoricalCandle_block_40(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH1;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar1;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle1 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		GetHistoricalCandle_block_41(flow);
	}

	private  void GetHistoricalCandle_block_41(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH1;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar2;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle2 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		GetHistoricalCandles_block_42(flow);
	}

	private  void GetHistoricalCandles_block_42(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH1;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = 3;
		int argument_5 = 30;
		subscriptionInstrumentCheck(argument_1);
 		
        try {
			long lastBarTime = history.getBar(argument_1, argument_2, argument_3, argument_4).getTime(); 
			List<IBar> tempBarList = history.getBars(argument_1, argument_2, argument_3, Filter.WEEKENDS, argument_5, lastBarTime, 0); 
			_setCandle2to30 = new ArrayList<Candle>(); 
			for (IBar bar : tempBarList) { 
				_setCandle2to30.add(new Candle(bar, argument_2, argument_1, argument_3)); 
			} 
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private void BBANDS_block_43(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodH1;
		int argument_3 = 1;
		int argument_4 = 20;
		double argument_5 = 2.0;
		double argument_6 = 2.0;
		IIndicators.MaType argument_7 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[4];
		params[0] = 20;
		params[1] = 2.0;
		params[2] = 2.0;
		params[3] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"BBANDS", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._upperBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._upperBandCurrentTimeFrame = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._middleBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._middleBandCurrentTimeFrame = (((double [])indicatorResult[1])[0]);
			} 
			if ((new Double(((double [])indicatorResult[2])[0])) == null) {
				this._lowerBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._lowerBandCurrentTimeFrame = (((double [])indicatorResult[2])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		Assign_block_45(flow);
	}

	private  void MultipleAction_block_44(Integer flow) {
		GetHistoricalCandle_block_40(flow);
		Calculation_block_306(flow);
		BBANDS_block_43(flow);
	}

	private  void Assign_block_45(Integer flow) {
		double argument_1 = 0.008;
		_widthBandCurTFConst =  argument_1;
			Assign_block_207(flow);
	}

	private  void GetHistoricalCandles_block_46(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodM30;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = 3;
		int argument_5 = 30;
		subscriptionInstrumentCheck(argument_1);
 		
        try {
			long lastBarTime = history.getBar(argument_1, argument_2, argument_3, argument_4).getTime(); 
			List<IBar> tempBarList = history.getBars(argument_1, argument_2, argument_3, Filter.WEEKENDS, argument_5, lastBarTime, 0); 
			_setCandle2to30 = new ArrayList<Candle>(); 
			for (IBar bar : tempBarList) { 
				_setCandle2to30.add(new Candle(bar, argument_2, argument_1, argument_3)); 
			} 
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private void BBANDS_block_47(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodM30;
		int argument_3 = 1;
		int argument_4 = 20;
		double argument_5 = 2.0;
		double argument_6 = 2.0;
		IIndicators.MaType argument_7 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[4];
		params[0] = 20;
		params[1] = 2.0;
		params[2] = 2.0;
		params[3] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"BBANDS", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._upperBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._upperBandCurrentTimeFrame = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._middleBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._middleBandCurrentTimeFrame = (((double [])indicatorResult[1])[0]);
			} 
			if ((new Double(((double [])indicatorResult[2])[0])) == null) {
				this._lowerBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._lowerBandCurrentTimeFrame = (((double [])indicatorResult[2])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		Assign_block_49(flow);
	}

	private  void MultipleAction_block_48(Integer flow) {
		GetHistoricalCandle_block_51(flow);
		Calculation_block_308(flow);
		BBANDS_block_47(flow);
	}

	private  void Assign_block_49(Integer flow) {
		double argument_1 = 0.008;
		_widthBandCurTFConst =  argument_1;
			Assign_block_208(flow);
	}

	private  void GetHistoricalCandle_block_51(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodM30;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar1;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle1 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		GetHistoricalCandle_block_52(flow);
	}

	private  void GetHistoricalCandle_block_52(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodM30;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar2;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle2 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		GetHistoricalCandles_block_46(flow);
	}

	private  void GetHistoricalCandle_block_54(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodM15;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = _tempVar1;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_candle1 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private void BBANDS_block_57(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = _PeriodM15;
		int argument_3 = 1;
		int argument_4 = 20;
		double argument_5 = 2.0;
		double argument_6 = 2.0;
		IIndicators.MaType argument_7 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[4];
		params[0] = 20;
		params[1] = 2.0;
		params[2] = 2.0;
		params[3] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"BBANDS", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._upperBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._upperBandCurrentTimeFrame = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._middleBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._middleBandCurrentTimeFrame = (((double [])indicatorResult[1])[0]);
			} 
			if ((new Double(((double [])indicatorResult[2])[0])) == null) {
				this._lowerBandCurrentTimeFrame = Double.NaN;
			} else { 
				this._lowerBandCurrentTimeFrame = (((double [])indicatorResult[2])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		Assign_block_59(flow);
	}

	private  void MultipleAction_block_58(Integer flow) {
		GetHistoricalCandle_block_54(flow);
		Calculation_block_310(flow);
		BBANDS_block_57(flow);
	}

	private  void Assign_block_59(Integer flow) {
		double argument_1 = 0.008;
		_widthBandCurTFConst =  argument_1;
			Assign_block_209(flow);
	}

	private void BBANDS_block_60(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		int argument_3 = 1;
		int argument_4 = 20;
		double argument_5 = 2.0;
		double argument_6 = 2.0;
		IIndicators.MaType argument_7 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[4];
		params[0] = 20;
		params[1] = 2.0;
		params[2] = 2.0;
		params[3] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"BBANDS", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._upperBandH4 = Double.NaN;
			} else { 
				this._upperBandH4 = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._middleBandH4 = Double.NaN;
			} else { 
				this._middleBandH4 = (((double [])indicatorResult[1])[0]);
			} 
			if ((new Double(((double [])indicatorResult[2])[0])) == null) {
				this._lowerBandH4 = Double.NaN;
			} else { 
				this._lowerBandH4 = (((double [])indicatorResult[2])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		GetHistoricalCandle_block_61(flow);
	}

	private  void GetHistoricalCandle_block_61(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = 1;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_Candle1H4 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		Calculation_block_62(flow);
	}

	private void Calculation_block_62(Integer flow) {
		double argument_1 = _upperBandH4;
		double argument_2 = _lowerBandH4;
		_widthBandH4 = argument_1 - argument_2;
		SMA_block_64(flow);
	}

	private  void GetHistoricalCandle_block_63(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		OfferSide argument_3 = OfferSide.BID;
		int argument_4 = 0;
			subscriptionInstrumentCheck(argument_1);
 		
        try {
			IBar tempBar = history.getBar(argument_1, argument_2, argument_3, argument_4); 
			_Candle0H4 = new Candle(tempBar, argument_2, argument_1, argument_3); 
        } catch (JFException e) {
            e.printStackTrace();
        }
		Assign_block_399(flow);
	}

	private void SMA_block_64(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		int argument_3 = 0;
		int argument_4 = 14;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[1];
		params[0] = 14;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"SMA", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._MA14H4Shift0 = Double.NaN;
			} else { 
				this._MA14H4Shift0 = (((double [])indicatorResult[0])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void MultipleAction_block_65(Integer flow) {
		Assign_block_71(flow);
		BBANDS_block_60(flow);
		STOCH_block_66(flow);
		Assign_block_69(flow);
	}

	private void STOCH_block_66(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		int argument_3 = 0;
		int argument_4 = 5;
		int argument_5 = 3;
		IIndicators.MaType argument_6 = IIndicators.MaType.SMA;
		int argument_7 = 3;
		IIndicators.MaType argument_8 = IIndicators.MaType.SMA;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[5];
		params[0] = 5;
		params[1] = 3;
		params[2] = 0;
		params[3] = 3;
		params[4] = 0;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"STOCH", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._STOH533slowkH4 = Double.NaN;
			} else { 
				this._STOH533slowkH4 = (((double [])indicatorResult[0])[0]);
			} 
			if ((new Double(((double [])indicatorResult[1])[0])) == null) {
				this._STOH533slowdH4 = Double.NaN;
			} else { 
				this._STOH533slowdH4 = (((double [])indicatorResult[1])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		SMA_block_67(flow);
	}

	private void SMA_block_67(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		int argument_3 = 1;
		int argument_4 = 20;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[1];
		params[0] = 20;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"SMA", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._MA20 = Double.NaN;
			} else { 
				this._MA20 = (((double [])indicatorResult[0])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
		SMA_block_68(flow);
	}

	private void SMA_block_68(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		Period argument_2 = Period.FOUR_HOURS;
		int argument_3 = 1;
		int argument_4 = 14;
		OfferSide[] offerside = new OfferSide[1];
		IIndicators.AppliedPrice[] appliedPrice = new IIndicators.AppliedPrice[1];
		offerside[0] = OfferSide.BID;
		appliedPrice[0] = IIndicators.AppliedPrice.CLOSE;
		Object[] params = new Object[1];
		params[0] = 14;
		try {
			subscriptionInstrumentCheck(argument_1);
			long time = context.getHistory().getBar(argument_1, argument_2, OfferSide.BID, argument_3).getTime();
			Object[] indicatorResult = context.getIndicators().calculateIndicator(argument_1, argument_2, offerside,
					"SMA", appliedPrice, params, Filter.WEEKENDS, 1, time, 0);
			if ((new Double(((double [])indicatorResult[0])[0])) == null) {
				this._MA14H4Shift1 = Double.NaN;
			} else { 
				this._MA14H4Shift1 = (((double [])indicatorResult[0])[0]);
			} 
		} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void Assign_block_69(Integer flow) {
		boolean argument_1 = false;
		_NoShortUpperBandH4 =  argument_1;
			Assign_block_70(flow);
	}

	private  void Assign_block_70(Integer flow) {
		boolean argument_1 = false;
		_NoLongLowerBandH4 =  argument_1;
			Assign_block_269(flow);
	}

	private  void Assign_block_71(Integer flow) {
		boolean argument_1 = false;
		_NoShortMA14H4 =  argument_1;
			Assign_block_72(flow);
	}

	private  void Assign_block_72(Integer flow) {
		boolean argument_1 = false;
		_NoLongtMA14H4 =  argument_1;
			GetHistoricalCandle_block_63(flow);
	}

	private  void If_block_73(Integer flow) {
		double argument_1 = _widthBandH4;
		double argument_2 = 0.0155;
		if (argument_1< argument_2) {
			MultipleAction_block_86(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_74(Integer flow) {
		double argument_1 = _STOH533slowkH4;
		double argument_2 = 30.0;
		if (argument_1< argument_2) {
			Assign_block_84(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			Assign_block_84(flow);
		}
	}

	private  void If_block_75(Integer flow) {
		double argument_1 = _MA14H4Shift0;
		double argument_2 = _Candle0H4.getLow();
		if (argument_1< argument_2) {
			Assign_block_93(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void MultipleAction_block_76(Integer flow) {
		If_block_73(flow);
		If_block_77(flow);
		If_block_79(flow);
		If_block_90(flow);
		If_block_92(flow);
	}

	private  void If_block_77(Integer flow) {
		double argument_1 = _Candle1H4.getClose();
		double argument_2 = _upperBandH4;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_78(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_78(Integer flow) {
		boolean argument_1 = true;
		_NoShortUpperBandH4 =  argument_1;
		}

	private  void If_block_79(Integer flow) {
		double argument_1 = _Candle1H4.getClose();
		double argument_2 = _lowerBandH4;
		if (argument_1< argument_2) {
			Assign_block_80(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_80(Integer flow) {
		boolean argument_1 = true;
		_NoLongLowerBandH4 =  argument_1;
		}

	private  void If_block_81(Integer flow) {
		double argument_1 = _STOH533slowkH4;
		double argument_2 = 70.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_83(flow);
		}
		else if (argument_1== argument_2) {
			Assign_block_83(flow);
		}
	}

	private  void If_block_82(Integer flow) {
		double argument_1 = _STOH533slowdH4;
		double argument_2 = 70.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_83(flow);
		}
		else if (argument_1== argument_2) {
			Assign_block_83(flow);
		}
	}

	private  void Assign_block_83(Integer flow) {
		boolean argument_1 = true;
		_NoLongLowerBandH4 =  argument_1;
		}

	private  void Assign_block_84(Integer flow) {
		boolean argument_1 = true;
		_NoShortUpperBandH4 =  argument_1;
		}

	private  void If_block_85(Integer flow) {
		double argument_1 = _STOH533slowdH4;
		double argument_2 = 30.0;
		if (argument_1< argument_2) {
			Assign_block_84(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			Assign_block_84(flow);
		}
	}

	private  void MultipleAction_block_86(Integer flow) {
		If_block_74(flow);
		If_block_85(flow);
		If_block_81(flow);
		If_block_82(flow);
	}

	private  void If_block_89(Integer flow) {
		double argument_1 = _MA14H4Shift0;
		double argument_2 = _Candle0H4.getHigh();
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_91(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_90(Integer flow) {
		double argument_1 = _MA14H4Shift1;
		double argument_2 = _Candle1H4.getHigh();
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_89(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_91(Integer flow) {
		boolean argument_1 = true;
		_NoShortMA14H4 =  argument_1;
		}

	private  void If_block_92(Integer flow) {
		double argument_1 = _MA14H4Shift1;
		double argument_2 = _Candle1H4.getLow();
		if (argument_1< argument_2) {
			If_block_75(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_93(Integer flow) {
		boolean argument_1 = true;
		_NoLongtMA14H4 =  argument_1;
		}

	private  void MultipleAction_block_94(Integer flow) {
		Assign_block_96(flow);
		Calculation_block_100(flow);
	}

	private  void Assign_block_95(Integer flow) {
		boolean argument_1 = false;
		_isGoShort =  argument_1;
			Assign_block_99(flow);
	}

	private  void Assign_block_96(Integer flow) {
		boolean argument_1 = false;
		_NoShortMA14H4M10 =  argument_1;
			Assign_block_97(flow);
	}

	private  void Assign_block_97(Integer flow) {
		boolean argument_1 = false;
		_NoLongtMA14H4M10 =  argument_1;
			Assign_block_95(flow);
	}

	private  void Assign_block_98(Integer flow) {
		long argument_1 = _tempVar753;
		_CandleTimeNoSignalMax =  argument_1;
		}

	private  void Assign_block_99(Integer flow) {
		boolean argument_1 = false;
		_isGoLong =  argument_1;
		}

	private void Calculation_block_100(Integer flow) {
		double argument_1 = _upperBandCurrentTimeFrame;
		double argument_2 = _lowerBandCurrentTimeFrame;
		_widthBandCurrentTimeFrame = argument_1 - argument_2;
		Assign_block_98(flow);
	}

	private  void MultipleAction_block_101(Integer flow) {
		If_block_110(flow);
		If_block_113(flow);
		If_block_393(flow);
		If_block_394(flow);
		If_block_389(flow);
	}

	private  void Assign_block_102(Integer flow) {
		boolean argument_1 = true;
		_isGoShort =  argument_1;
		}

	private  void Assign_block_103(Integer flow) {
		boolean argument_1 = true;
		_isGoLong =  argument_1;
		}

	private  void If_block_104(Integer flow) {
		boolean argument_1 = _NoShortUpperBandH4;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_106(flow);
		}
	}

	private  void If_block_105(Integer flow) {
		boolean argument_1 = _NoLongLowerBandH4;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_107(flow);
		}
	}

	private  void If_block_106(Integer flow) {
		boolean argument_1 = _NoShortMA14H4M10;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
			If_block_108(flow);
		}
		else if (argument_1 == argument_2) {
			Assign_block_102(flow);
		}
	}

	private  void If_block_107(Integer flow) {
		boolean argument_1 = _NoLongtMA14H4M10;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
			If_block_109(flow);
		}
		else if (argument_1 == argument_2) {
			Assign_block_103(flow);
		}
	}

	private  void If_block_108(Integer flow) {
		boolean argument_1 = _NoShortMA14H4;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			Assign_block_102(flow);
		}
	}

	private  void If_block_109(Integer flow) {
		boolean argument_1 = _NoLongtMA14H4;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			Assign_block_103(flow);
		}
	}

	private  void If_block_110(Integer flow) {
		double argument_1 = _MA14H4Shift0;
		double argument_2 = _candle1.getHigh();
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_111(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_111(Integer flow) {
		boolean argument_1 = true;
		_NoShortMA14H4M10 =  argument_1;
		}

	private  void Assign_block_112(Integer flow) {
		boolean argument_1 = true;
		_NoLongtMA14H4M10 =  argument_1;
		}

	private  void If_block_113(Integer flow) {
		double argument_1 = _MA14H4Shift0;
		double argument_2 = _candle1.getLow();
		if (argument_1< argument_2) {
			Assign_block_112(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void OpenatMarket_block_114(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		double argument_2 = defaultTradeAmount;
		int argument_3 = defaultSlippage;
		double argument_4 = _stopForSell;
		double argument_5 = _TPforSell;
		String argument_6 = "SELL";
		ITick tick = getLastTick(argument_1);

		IEngine.OrderCommand command = IEngine.OrderCommand.SELL;

		double stopLoss = round(argument_4, argument_1);
		double takeProfit = round(argument_5, argument_1);
		
           try {
               String label = getLabel();           
               IOrder order = context.getEngine().submitOrder(label, argument_1, command, argument_2, 0, argument_3,  stopLoss, takeProfit, 0, argument_6);
				TradeEventAction event3 = new TradeEventAction();
		event3.setMessageType(IMessage.Type.ORDER_FILL_OK);
		event3.setNextBlockId("If_block_401");
		event3.setPositionLabel(order.getLabel());
		event3.setFlowId(flow);
		tradeEventActions.add(event3);
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private  void OpenatMarket_block_115(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		double argument_2 = defaultTradeAmount;
		int argument_3 = defaultSlippage;
		double argument_4 = _stopForBuy;
		double argument_5 = _TPforBuy;
		String argument_6 = "BUY";
		ITick tick = getLastTick(argument_1);

		IEngine.OrderCommand command = IEngine.OrderCommand.BUY;

		double stopLoss = round(argument_4, argument_1);
		double takeProfit = round(argument_5, argument_1);
		
           try {
               String label = getLabel();           
               IOrder order = context.getEngine().submitOrder(label, argument_1, command, argument_2, 0, argument_3,  stopLoss, takeProfit, 0, argument_6);
				TradeEventAction event3 = new TradeEventAction();
		event3.setMessageType(IMessage.Type.ORDER_FILL_OK);
		event3.setNextBlockId("If_block_401");
		event3.setPositionLabel(order.getLabel());
		event3.setFlowId(flow);
		tradeEventActions.add(event3);
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private void Calculation_block_116(Integer flow) {
		double argument_1 = _candle1.getHigh();
		double argument_2 = 0.02;
		_stopForSell = argument_1 + argument_2;
		Calculation_block_117(flow);
	}

	private void Calculation_block_117(Integer flow) {
		double argument_1 = _candle1.getOpen();
		double argument_2 = _candle1.getClose();
		_BodyCandleSell = argument_1 - argument_2;
		Calculation_block_118(flow);
	}

	private void Calculation_block_118(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _BodyCandleSell;
		_TPforSell = argument_1 - argument_2;
	}

	private void Calculation_block_119(Integer flow) {
		double argument_1 = _candle1.getLow();
		double argument_2 = 0.02;
		_stopForBuy = argument_1 - argument_2;
		Calculation_block_120(flow);
	}

	private void Calculation_block_120(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _candle1.getOpen();
		_BodyCandleBuy = argument_1 - argument_2;
		Calculation_block_121(flow);
	}

	private void Calculation_block_121(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _BodyCandleBuy;
		_TPforBuy = argument_1 + argument_2;
	}

	private  void LoopViewer_block_122(Integer flow) {
		List<Candle> argument_1 = _setCandle2to30;
		for (Candle candle : argument_1) {
			this._CandleCurrent = candle;
			MultipleAction_block_277(flow);
		}
			MultipleAction_block_150(flow);
	}

	private  void If_block_123(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _candle1.getOpen();
		if (argument_1< argument_2) {
			If_block_145(flow);
		}
		else if (argument_1> argument_2) {
			If_block_146(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_124(Integer flow) {
		double argument_1 = _CandleCurrent.getOpen();
		double argument_2 = _candle1.getHigh();
		if (argument_1< argument_2) {
			If_block_125(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1== argument_2) {
			If_block_125(flow);
		}
	}

	private  void If_block_125(Integer flow) {
		double argument_1 = _CandleCurrent.getClose();
		double argument_2 = _candle1.getHigh();
		if (argument_1< argument_2) {
			If_block_126(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1== argument_2) {
			If_block_126(flow);
		}
	}

	private  void If_block_126(Integer flow) {
		double argument_1 = _CandleCurrent.getOpen();
		double argument_2 = _candle1.getClose();
		if (argument_1< argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1> argument_2) {
			If_block_127(flow);
		}
		else if (argument_1== argument_2) {
			If_block_127(flow);
		}
	}

	private  void If_block_127(Integer flow) {
		double argument_1 = _CandleCurrent.getClose();
		double argument_2 = _candle1.getClose();
		if (argument_1< argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1> argument_2) {
			If_block_383(flow);
		}
		else if (argument_1== argument_2) {
			If_block_383(flow);
		}
	}

	private  void Assign_block_128(Integer flow) {
		long argument_1 = _CandleCurrent.getTime();
		_CandleTimeNoSignal =  argument_1;
			If_block_129(flow);
	}

	private  void If_block_129(Integer flow) {
		long argument_1 = _CandleTimeNoSignal;
		long argument_2 = _CandleTimeNoSignalMax;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_130(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_130(Integer flow) {
		long argument_1 = _CandleTimeNoSignal;
		_CandleTimeNoSignalMax =  argument_1;
		}

	private void Calculation_block_131(Integer flow) {
		long argument_1 = _Candle1Time;
		long argument_2 = _CandleTimeNoSignalMax;
		_TimeFlat = (long)(argument_1 - argument_2);
		GetTimeUnit_block_316(flow);
	}

	private  void If_block_132(Integer flow) {
		long argument_1 = _TimeFlat;
		long argument_2 = _TimeFlatConst;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_164(flow);
		}
		else if (argument_1== argument_2) {
			If_block_164(flow);
		}
	}

	private  void If_block_133(Integer flow) {
		double argument_1 = _candle2.getOpen();
		double argument_2 = _candle1.getHigh();
		if (argument_1< argument_2) {
			If_block_142(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			If_block_142(flow);
		}
	}

	private  void If_block_134(Integer flow) {
		double argument_1 = _candle2.getOpen();
		double argument_2 = _candle1.getLow();
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_143(flow);
		}
		else if (argument_1== argument_2) {
			If_block_143(flow);
		}
	}

	private  void LoopViewer_block_135(Integer flow) {
		List<Candle> argument_1 = _setCandle2to30;
		for (Candle candle : argument_1) {
			this._CandleCurrent = candle;
			MultipleAction_block_276(flow);
		}
			MultipleAction_block_149(flow);
	}

	private  void If_block_136(Integer flow) {
		double argument_1 = _CandleCurrent.getOpen();
		double argument_2 = _candle1.getLow();
		if (argument_1< argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1> argument_2) {
			If_block_137(flow);
		}
		else if (argument_1== argument_2) {
			If_block_137(flow);
		}
	}

	private  void If_block_137(Integer flow) {
		double argument_1 = _CandleCurrent.getClose();
		double argument_2 = _candle1.getLow();
		if (argument_1< argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1> argument_2) {
			If_block_138(flow);
		}
		else if (argument_1== argument_2) {
			If_block_138(flow);
		}
	}

	private  void If_block_138(Integer flow) {
		double argument_1 = _CandleCurrent.getOpen();
		double argument_2 = _candle1.getClose();
		if (argument_1< argument_2) {
			If_block_139(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1== argument_2) {
			If_block_139(flow);
		}
	}

	private  void If_block_139(Integer flow) {
		double argument_1 = _CandleCurrent.getClose();
		double argument_2 = _candle1.getClose();
		if (argument_1< argument_2) {
			If_block_384(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_128(flow);
		}
		else if (argument_1== argument_2) {
			If_block_384(flow);
		}
	}

	private void Calculation_block_140(Integer flow) {
		long argument_1 = _Candle1Time;
		long argument_2 = _CandleTimeNoSignalMax;
		_TimeFlat = (long)(argument_1 - argument_2);
		GetTimeUnit_block_298(flow);
	}

	private  void If_block_141(Integer flow) {
		long argument_1 = _TimeFlat;
		long argument_2 = _TimeFlatConst;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_10(flow);
		}
		else if (argument_1== argument_2) {
			If_block_10(flow);
		}
	}

	private  void If_block_142(Integer flow) {
		double argument_1 = _candle2.getOpen();
		double argument_2 = _candle1.getClose();
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			LoopViewer_block_135(flow);
		}
		else if (argument_1== argument_2) {
			LoopViewer_block_135(flow);
		}
	}

	private  void If_block_143(Integer flow) {
		double argument_1 = _candle2.getOpen();
		double argument_2 = _candle1.getClose();
		if (argument_1< argument_2) {
			LoopViewer_block_122(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			LoopViewer_block_122(flow);
		}
	}

	private  void If_block_144(Integer flow) {
		double argument_1 = _widthBandCurrentTimeFrame;
		double argument_2 = _widthBandCurTFConst;
		if (argument_1< argument_2) {
			If_block_123(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_145(Integer flow) {
		boolean argument_1 = _isGoShort;
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_133(flow);
		}
	}

	private  void If_block_146(Integer flow) {
		boolean argument_1 = _isGoLong;
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_134(flow);
		}
	}

	private  void MultipleAction_block_149(Integer flow) {
		Calculation_block_116(flow);
		Calculation_block_140(flow);
	}

	private  void MultipleAction_block_150(Integer flow) {
		Calculation_block_119(flow);
		Calculation_block_131(flow);
	}

	private  void If_block_164(Integer flow) {
		int argument_1 = AllPositions.size();
		int argument_2 = 1;
		if (argument_1< argument_2) {
			If_block_266(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			MultipleAction_block_186(flow);
		}
	}

	private  void SetTakeProfit_block_165(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = _TPforSell;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("If_block_401");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void PositionsViewer_block_166(Integer flow) {
		List<IOrder> argument_1 = OpenPositions;
		for (IOrder order : argument_1){
			if (order.getState() == IOrder.State.OPENED||order.getState() == IOrder.State.FILLED){
				_CurrentPos = order;
				If_block_243(flow);
			}
		}
	}

	private  void If_block_167(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_168(flow);
		}
	}

	private  void If_block_168(Integer flow) {
		double argument_1 = _TPforSell;
		double argument_2 = _CurrentPos.getTakeProfitPrice();
		if (argument_1< argument_2) {
			SetTakeProfit_block_165(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void PositionsViewer_block_169(Integer flow) {
		List<IOrder> argument_1 = OpenPositions;
		for (IOrder order : argument_1){
			if (order.getState() == IOrder.State.OPENED||order.getState() == IOrder.State.FILLED){
				_CurrentPos = order;
				If_block_244(flow);
			}
		}
	}

	private  void If_block_170(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_171(flow);
		}
	}

	private  void If_block_171(Integer flow) {
		double argument_1 = _TPforBuy;
		double argument_2 = _CurrentPos.getTakeProfitPrice();
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			SetTakeProfit_block_172(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void SetTakeProfit_block_172(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = _TPforBuy;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("If_block_401");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private void Calculation_block_174(Integer flow) {
		long argument_1 = _Candle1Time;
		long argument_2 = _TimeFlat;
		_TimeClose = (long)(argument_1 + argument_2);
		Assign_block_230(flow);
	}

	private void Calculation_block_175(Integer flow) {
		long argument_1 = _Candle1Time;
		long argument_2 = _TimeFlat;
		_TimeClose = (long)(argument_1 + argument_2);
		Assign_block_233(flow);
	}

	private  void If_block_178(Integer flow) {
		int argument_1 = AllPositions.size();
		int argument_2 = 1;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			PositionsViewer_block_198(flow);
		}
	}

	private  void CloseandCancelPosition_block_185(Integer flow) {
		try {
			if (_CurrentPos != null && (_CurrentPos.getState() == IOrder.State.OPENED||_CurrentPos.getState() == IOrder.State.FILLED)){
				_CurrentPos.close();
				TradeEventAction event = new TradeEventAction();
				event.setMessageType(IMessage.Type.ORDER_CLOSE_OK);
				event.setNextBlockId("If_block_401");
				event.setPositionLabel(_CurrentPos.getLabel());
				event.setFlowId(flow);
				tradeEventActions.add(event);
			}
		} catch (JFException e)  {
			e.printStackTrace();
		}
	}

	private  void MultipleAction_block_186(Integer flow) {
		PositionsViewer_block_169(flow);
		If_block_351(flow);
	}

	private void Calculation_block_187(Integer flow) {
		long argument_1 = _Candle1Time;
		long argument_2 = _TimeFlat;
		_TimeCloseCompare = (long)(argument_1 + argument_2);
		PositionsViewer_block_340(flow);
	}

	private  void MultipleAction_block_190(Integer flow) {
		PositionsViewer_block_166(flow);
		If_block_349(flow);
	}

	private void Calculation_block_191(Integer flow) {
		long argument_1 = _Candle1Time;
		long argument_2 = _TimeFlat;
		_TimeCloseCompare = (long)(argument_1 + argument_2);
		PositionsViewer_block_337(flow);
	}

	private  void If_block_192(Integer flow) {
		long argument_1 = _TimeCloseCompare;
		long argument_2 = _TimeClose;
		if (argument_1< argument_2) {
			If_block_338(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_193(flow);
		}
		else if (argument_1== argument_2) {
			If_block_338(flow);
		}
	}

	private  void Assign_block_193(Integer flow) {
		long argument_1 = _TimeCloseCompare;
		_TimeClose =  argument_1;
			If_block_335(flow);
	}

	private  void PositionsViewer_block_198(Integer flow) {
		List<IOrder> argument_1 = OpenPositions;
		for (IOrder order : argument_1){
			if (order.getState() == IOrder.State.OPENED||order.getState() == IOrder.State.FILLED){
				_CurrentPos = order;
				If_block_201(flow);
			}
		}
	}

	private  void If_block_201(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			If_block_202(flow);
		}
		else if (argument_1 == argument_2) {
			If_block_204(flow);
		}
	}

	private  void If_block_202(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _MainCandleSellHigh;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_231(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void CloseandCancelPosition_block_203(Integer flow) {
		try {
			if (_CurrentPos != null && (_CurrentPos.getState() == IOrder.State.OPENED||_CurrentPos.getState() == IOrder.State.FILLED)){
				_CurrentPos.close();
				TradeEventAction event = new TradeEventAction();
				event.setMessageType(IMessage.Type.ORDER_CLOSE_OK);
				event.setNextBlockId("If_block_421");
				event.setPositionLabel(_CurrentPos.getLabel());
				event.setFlowId(flow);
				tradeEventActions.add(event);
			}
		} catch (JFException e)  {
			e.printStackTrace();
		}
	}

	private  void If_block_204(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _MainCandleBuyLow;
		if (argument_1< argument_2) {
			If_block_234(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_205(Integer flow) {
		long argument_1 = _candle1.getTime();
		_Candle1Time =  argument_1;
		}

	private  void Assign_block_207(Integer flow) {
		long argument_1 = _candle1.getTime();
		_Candle1Time =  argument_1;
		}

	private  void Assign_block_208(Integer flow) {
		long argument_1 = _candle1.getTime();
		_Candle1Time =  argument_1;
		}

	private  void Assign_block_209(Integer flow) {
		long argument_1 = _candle1.getTime();
		_Candle1Time =  argument_1;
		}

	private  void If_block_213(Integer flow) {
		int argument_1 = AllPositions.size();
		int argument_2 = 1;
		if (argument_1< argument_2) {
			MultipleAction_block_417(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void OpenatMarket_block_214(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		double argument_2 = defaultTradeAmount;
		int argument_3 = defaultSlippage;
		double argument_4 = 0.0;
		double argument_5 = 0.0;
		String argument_6 = "Trend";
		ITick tick = getLastTick(argument_1);

		IEngine.OrderCommand command = IEngine.OrderCommand.BUY;

		double stopLoss = round(argument_4, argument_1);
		double takeProfit = round(argument_5, argument_1);
		
           try {
               String label = getLabel();           
               IOrder order = context.getEngine().submitOrder(label, argument_1, command, argument_2, 0, argument_3,  stopLoss, takeProfit, 0, argument_6);
				TradeEventAction event3 = new TradeEventAction();
		event3.setMessageType(IMessage.Type.ORDER_FILL_OK);
		event3.setNextBlockId("If_block_401");
		event3.setPositionLabel(order.getLabel());
		event3.setFlowId(flow);
		tradeEventActions.add(event3);
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private  void If_block_215(Integer flow) {
		boolean argument_1 = LastTradeEvent.getOrder().isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			OpenatMarket_block_214(flow);
		}
		else if (argument_1 == argument_2) {
			OpenatMarket_block_216(flow);
		}
	}

	private  void OpenatMarket_block_216(Integer flow) {
		Instrument argument_1 = defaultInstrument;
		double argument_2 = defaultTradeAmount;
		int argument_3 = defaultSlippage;
		double argument_4 = 0.0;
		double argument_5 = 0.0;
		String argument_6 = "Trend";
		ITick tick = getLastTick(argument_1);

		IEngine.OrderCommand command = IEngine.OrderCommand.SELL;

		double stopLoss = round(argument_4, argument_1);
		double takeProfit = round(argument_5, argument_1);
		
           try {
               String label = getLabel();           
               IOrder order = context.getEngine().submitOrder(label, argument_1, command, argument_2, 0, argument_3,  stopLoss, takeProfit, 0, argument_6);
				TradeEventAction event3 = new TradeEventAction();
		event3.setMessageType(IMessage.Type.ORDER_FILL_OK);
		event3.setNextBlockId("If_block_401");
		event3.setPositionLabel(order.getLabel());
		event3.setFlowId(flow);
		tradeEventActions.add(event3);
        } catch (JFException e) {
            e.printStackTrace();
        }
	}

	private  void If_block_219(Integer flow) {
		double argument_1 = _Candle1H4ClosePrice;
		double argument_2 = _upperBandH4;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			SetTakeProfit_block_268(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_220(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			If_block_221(flow);
		}
		else if (argument_1 == argument_2) {
			If_block_219(flow);
		}
	}

	private  void If_block_221(Integer flow) {
		double argument_1 = _Candle1H4ClosePrice;
		double argument_2 = _lowerBandH4;
		if (argument_1< argument_2) {
			SetTakeProfit_block_257(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_224(Integer flow) {
		long argument_1 = 1135893600000l;
		_TimeCloseCompare =  argument_1;
		}

	private  void If_block_227(Integer flow) {
		double argument_1 = LastTradeEvent.getOrder().getProfitLossInPips();
		double argument_2 = 2.0;
		if (argument_1< argument_2) {
			If_block_238(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_228(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_MainCandleSellHigh =  argument_1;
			Assign_block_237(flow);
	}

	private  void Assign_block_229(Integer flow) {
		double argument_1 = _candle1.getLow();
		_MainCandleBuyLow =  argument_1;
			Assign_block_236(flow);
	}

	private  void Assign_block_230(Integer flow) {
		double argument_1 = _candle1.getLow();
		_CandlePrevLow =  argument_1;
			OpenatMarket_block_114(flow);
	}

	private  void If_block_231(Integer flow) {
		double argument_1 = _candle1.getLow();
		double argument_2 = _CandlePrevLow;
		if (argument_1< argument_2) {
			Assign_block_229(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_232(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_232(Integer flow) {
		double argument_1 = _CandlePrevLow;
		_MainCandleBuyLow =  argument_1;
			Assign_block_236(flow);
	}

	private  void Assign_block_233(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_CandlePrevHigh =  argument_1;
			OpenatMarket_block_115(flow);
	}

	private  void If_block_234(Integer flow) {
		double argument_1 = _candle1.getHigh();
		double argument_2 = _CandlePrevHigh;
		if (argument_1< argument_2) {
			Assign_block_235(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_228(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_235(Integer flow) {
		double argument_1 = _CandlePrevHigh;
		_MainCandleSellHigh =  argument_1;
			Assign_block_237(flow);
	}

	private  void Assign_block_236(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_CandlePrevHigh =  argument_1;
			If_block_261(flow);
	}

	private  void Assign_block_237(Integer flow) {
		double argument_1 = _candle1.getLow();
		_CandlePrevLow =  argument_1;
			If_block_259(flow);
	}

	private  void If_block_238(Integer flow) {
		double argument_1 = LastTradeEvent.getOrder().getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			If_block_213(flow);
		}
	}

	private  void SetTakeProfit_block_239(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = 0.0;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("CloseandCancelPosition_block_203");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void If_block_242(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_271(flow);
		}
		else if (argument_1== argument_2) {
			If_block_220(flow);
		}
	}

	private  void If_block_243(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_167(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_244(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_170(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_245(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			SetTakeProfit_block_239(flow);
		}
		else if (argument_1== argument_2) {
			CloseandCancelPosition_block_203(flow);
		}
	}

	private  void If_block_246(Integer flow) {
		int argument_1 = AllPositions.size();
		int argument_2 = 1;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			PositionsViewer_block_248(flow);
		}
	}

	private  void PositionsViewer_block_248(Integer flow) {
		List<IOrder> argument_1 = OpenPositions;
		for (IOrder order : argument_1){
			if (order.getState() == IOrder.State.OPENED||order.getState() == IOrder.State.FILLED){
				_CurrentPos = order;
				If_block_242(flow);
			}
		}
	}

	private  void If_block_251(Integer flow) {
		double argument_1 = _CurrentPos.getProfitLossInPips();
		double argument_2 = 1.0;
		if (argument_1< argument_2) {
			If_block_252(flow);
		}
		else if (argument_1> argument_2) {
			CloseandCancelPosition_block_185(flow);
		}
		else if (argument_1== argument_2) {
			CloseandCancelPosition_block_185(flow);
		}
	}

	private  void If_block_252(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			Calculation_block_253(flow);
		}
		else if (argument_1 == argument_2) {
			Calculation_block_254(flow);
		}
	}

	private void Calculation_block_253(Integer flow) {
		double argument_1 = _CurrentPos.getOpenPrice();
		double argument_2 = _tempVar619;
		_TPforClose = argument_1 - argument_2;
		SetTakeProfit_block_256(flow);
	}

	private void Calculation_block_254(Integer flow) {
		double argument_1 = _CurrentPos.getOpenPrice();
		double argument_2 = _tempVar619;
		_TPforClose = argument_1 + argument_2;
		SetTakeProfit_block_256(flow);
	}

	private  void SetTakeProfit_block_256(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = _TPforClose;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("If_block_401");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void SetTakeProfit_block_257(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = 1.01;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("If_block_272");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void If_block_259(Integer flow) {
		double argument_1 = _Candle1H4ClosePrice;
		double argument_2 = _lowerBandH4;
		if (argument_1< argument_2) {
			If_block_262(flow);
		}
		else if (argument_1> argument_2) {
			If_block_245(flow);
		}
		else if (argument_1== argument_2) {
			If_block_245(flow);
		}
	}

	private  void SetTakeProfit_block_260(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		int argument_2 = 1000;
		boolean isLong = argument_1.isLong();
		double takeProfit;
		if (isLong) {
			takeProfit = getLastTick(argument_1.getInstrument()).getBid() + argument_1.getInstrument().getPipValue() * argument_2;
		} else {
			takeProfit = getLastTick(argument_1.getInstrument()).getAsk() - argument_1.getInstrument().getPipValue() * argument_2;
		}
		takeProfit = round(takeProfit, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("CloseandCancelPosition_block_203");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void If_block_261(Integer flow) {
		double argument_1 = _Candle1H4ClosePrice;
		double argument_2 = _upperBandH4;
		if (argument_1< argument_2) {
			If_block_245(flow);
		}
		else if (argument_1> argument_2) {
			If_block_262(flow);
		}
		else if (argument_1== argument_2) {
			If_block_245(flow);
		}
	}

	private  void If_block_262(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			CloseandCancelPosition_block_203(flow);
		}
		else if (argument_1== argument_2) {
			SetTakeProfit_block_260(flow);
		}
	}

	private  void Assign_block_263(Integer flow) {
		double argument_1 = _candle1.getClose();
		_Candle1H4ClosePrice =  argument_1;
		}

	private  void If_block_264(Integer flow) {
		Period argument_1 = _PeriodH4;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
			Assign_block_287(flow);
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			Assign_block_288(flow);
		}
	}

	private void Calculation_block_265(Integer flow) {
		long argument_1 = _TimeFlat;
		double argument_2 = 2.0;
		_TimeFlat = (long)(argument_1 * argument_2);
		Calculation_block_323(flow);
	}

	private  void If_block_266(Integer flow) {
		Period argument_1 = _PeriodH4;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
			Assign_block_289(flow);
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			Assign_block_290(flow);
		}
	}

	private void Calculation_block_267(Integer flow) {
		long argument_1 = _TimeFlat;
		double argument_2 = 2.0;
		_TimeFlat = (long)(argument_1 * argument_2);
		Calculation_block_325(flow);
	}

	private  void SetTakeProfit_block_268(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = 1.71;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("If_block_272");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void Assign_block_269(Integer flow) {
		double argument_1 = _Candle1H4.getClose();
		_Candle1H4ClosePrice =  argument_1;
			Assign_block_224(flow);
	}

	private  void If_block_271(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		long argument_2 = _TimeClose;
		if (argument_1< argument_2) {
			GetTimeUnit_block_312(flow);
		}
		else if (argument_1> argument_2) {
			If_block_251(flow);
		}
		else if (argument_1== argument_2) {
			If_block_251(flow);
		}
	}

	private  void If_block_272(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		long argument_2 = _TimeClose;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			CloseandCancelPosition_block_185(flow);
		}
		else if (argument_1== argument_2) {
			CloseandCancelPosition_block_185(flow);
		}
	}

	private  void If_block_274(Integer flow) {
		double argument_1 = _CandleCurrent.getLow();
		double argument_2 = _CandleLowLowest;
		if (argument_1< argument_2) {
			Assign_block_275(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_275(Integer flow) {
		double argument_1 = _CandleCurrent.getLow();
		_CandleLowLowest =  argument_1;
		}

	private  void MultipleAction_block_276(Integer flow) {
		If_block_124(flow);
	}

	private  void MultipleAction_block_277(Integer flow) {
		If_block_136(flow);
	}

	private  void If_block_278(Integer flow) {
		double argument_1 = _CandleCurrent.getHigh();
		double argument_2 = _CandleHighHighest;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_279(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_279(Integer flow) {
		double argument_1 = _CandleCurrent.getHigh();
		_CandleHighHighest =  argument_1;
		}

	private  void Assign_block_280(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_CandleHighHighest =  argument_1;
		}

	private  void Assign_block_281(Integer flow) {
		double argument_1 = _candle1.getLow();
		_CandleLowLowest =  argument_1;
		}

	private  void If_block_282(Integer flow) {
		double argument_1 = _candle1.getHigh();
		double argument_2 = _candle2.getHigh();
		if (argument_1< argument_2) {
			Assign_block_286(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_280(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_283(Integer flow) {
		double argument_1 = _candle1.getLow();
		double argument_2 = _candle2.getLow();
		if (argument_1< argument_2) {
			Assign_block_281(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_285(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void MultipleAction_block_284(Integer flow) {
		If_block_282(flow);
		If_block_283(flow);
	}

	private  void Assign_block_285(Integer flow) {
		double argument_1 = _candle2.getLow();
		_CandleLowLowest =  argument_1;
		}

	private  void Assign_block_286(Integer flow) {
		double argument_1 = _candle2.getHigh();
		_CandleHighHighest =  argument_1;
		}

	private  void Assign_block_287(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_MainCandleSellHigh =  argument_1;
			Calculation_block_174(flow);
	}

	private  void Assign_block_288(Integer flow) {
		double argument_1 = _CandleHighHighest;
		_MainCandleSellHigh =  argument_1;
			Calculation_block_265(flow);
	}

	private  void Assign_block_289(Integer flow) {
		double argument_1 = _candle1.getLow();
		_MainCandleBuyLow =  argument_1;
			Calculation_block_175(flow);
	}

	private  void Assign_block_290(Integer flow) {
		double argument_1 = _CandleLowLowest;
		_MainCandleBuyLow =  argument_1;
			Calculation_block_267(flow);
	}

	private  void GetTimeUnit_block_298(Integer flow) {
		long argument_1 = _Candle1Time;
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_DayOfWeekMainCandle = calendar.get(Calendar.DAY_OF_WEEK);
		GetTimeUnit_block_299(flow);
	}

	private  void GetTimeUnit_block_299(Integer flow) {
		long argument_1 = _CandleTimeNoSignalMax;
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_DayOfWeekCandleNoSignalMax = calendar.get(Calendar.DAY_OF_WEEK);
		If_block_300(flow);
	}

	private  void If_block_300(Integer flow) {
		int argument_1 = _DayOfWeekCandleNoSignalMax;
		int argument_2 = _DayOfWeekMainCandle;
		if (argument_1< argument_2) {
			If_block_141(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_385(flow);
		}
		else if (argument_1== argument_2) {
			If_block_141(flow);
		}
	}

	private  void Assign_block_301(Integer flow) {
		long argument_1 = _TimeFlatConst;
		_TimeFlat =  argument_1;
			If_block_10(flow);
	}

	private void Calculation_block_302(Integer flow) {
		long argument_1 = _candle1.getTime();
		long argument_2 = _candle2.getTime();
		_TimeFlatConst0 = (long)(argument_1 - argument_2);
		Calculation_block_303(flow);
	}

	private void Calculation_block_303(Integer flow) {
		long argument_1 = _TimeFlatConst0;
		double argument_2 = 3.0;
		_TimeFlatConst = (long)(argument_1 * argument_2);
	}

	private void Calculation_block_304(Integer flow) {
		long argument_1 = _candle1.getTime();
		long argument_2 = _candle2.getTime();
		_TimeFlatConst0 = (long)(argument_1 - argument_2);
		Calculation_block_305(flow);
	}

	private void Calculation_block_305(Integer flow) {
		long argument_1 = _TimeFlatConst0;
		double argument_2 = 4.0;
		_TimeFlatConst = (long)(argument_1 * argument_2);
	}

	private void Calculation_block_306(Integer flow) {
		long argument_1 = _candle1.getTime();
		long argument_2 = _candle2.getTime();
		_TimeFlatConst = (long)(argument_1 - argument_2);
		Calculation_block_307(flow);
	}

	private void Calculation_block_307(Integer flow) {
		long argument_1 = _TimeFlatConst;
		double argument_2 = 4.0;
		_TimeFlatConst = (long)(argument_1 * argument_2);
	}

	private void Calculation_block_308(Integer flow) {
		long argument_1 = _candle1.getTime();
		long argument_2 = _candle2.getTime();
		_TimeFlatConst = (long)(argument_1 - argument_2);
		Calculation_block_309(flow);
	}

	private void Calculation_block_309(Integer flow) {
		long argument_1 = _TimeFlatConst;
		double argument_2 = 4.0;
		_TimeFlatConst = (long)(argument_1 * argument_2);
	}

	private void Calculation_block_310(Integer flow) {
		long argument_1 = _candle1.getTime();
		long argument_2 = _candle2.getTime();
		_TimeFlatConst = (long)(argument_1 - argument_2);
		Calculation_block_311(flow);
	}

	private void Calculation_block_311(Integer flow) {
		long argument_1 = _TimeFlatConst;
		double argument_2 = 4.0;
		_TimeFlatConst = (long)(argument_1 * argument_2);
	}

	private  void GetTimeUnit_block_312(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_DayOfWeekCurrent = calendar.get(Calendar.DAY_OF_WEEK);
		If_block_314(flow);
	}

	private  void GetTimeUnit_block_313(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_HourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
		If_block_315(flow);
	}

	private  void If_block_314(Integer flow) {
		int argument_1 = _DayOfWeekCurrent;
		int argument_2 = 6;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			GetTimeUnit_block_313(flow);
		}
	}

	private  void If_block_315(Integer flow) {
		int argument_1 = _HourCurrent;
		int argument_2 = 19;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			GetTimeUnit_block_409(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void GetTimeUnit_block_316(Integer flow) {
		long argument_1 = _Candle1Time;
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_DayOfWeekMainCandle = calendar.get(Calendar.DAY_OF_WEEK);
		GetTimeUnit_block_317(flow);
	}

	private  void GetTimeUnit_block_317(Integer flow) {
		long argument_1 = _CandleTimeNoSignalMax;
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_DayOfWeekCandleNoSignalMax = calendar.get(Calendar.DAY_OF_WEEK);
		If_block_318(flow);
	}

	private  void If_block_318(Integer flow) {
		int argument_1 = _DayOfWeekCandleNoSignalMax;
		int argument_2 = _DayOfWeekMainCandle;
		if (argument_1< argument_2) {
			If_block_132(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_386(flow);
		}
		else if (argument_1== argument_2) {
			If_block_132(flow);
		}
	}

	private  void Assign_block_319(Integer flow) {
		long argument_1 = _TimeFlatConst;
		_TimeFlat =  argument_1;
			If_block_164(flow);
	}

	private  void Assign_block_320(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		_TimeClose =  argument_1;
			If_block_251(flow);
	}

	private  void Assign_block_321(Integer flow) {
		double argument_1 = _upperBandH4;
		_upperBandCurrentTimeFrame =  argument_1;
			Assign_block_322(flow);
	}

	private  void Assign_block_322(Integer flow) {
		double argument_1 = _lowerBandH4;
		_lowerBandCurrentTimeFrame =  argument_1;
			Assign_block_387(flow);
	}

	private void Calculation_block_323(Integer flow) {
		double argument_1 = _BodyCandleSell;
		double argument_2 = 5.0;
		_BodyCandleSellX5 = argument_1 * argument_2;
		If_block_324(flow);
	}

	private  void If_block_324(Integer flow) {
		double argument_1 = _BodyCandleSellX5;
		double argument_2 = _widthBandH4;
		if (argument_1< argument_2) {
			If_block_328(flow);
		}
		else if (argument_1> argument_2) {
			Calculation_block_174(flow);
		}
		else if (argument_1== argument_2) {
			If_block_328(flow);
		}
	}

	private void Calculation_block_325(Integer flow) {
		double argument_1 = _BodyCandleBuy;
		double argument_2 = 5.0;
		_BodyCandleBuyX5 = argument_1 * argument_2;
		If_block_326(flow);
	}

	private  void If_block_326(Integer flow) {
		double argument_1 = _BodyCandleBuyX5;
		double argument_2 = _widthBandH4;
		if (argument_1< argument_2) {
			If_block_330(flow);
		}
		else if (argument_1> argument_2) {
			Calculation_block_175(flow);
		}
		else if (argument_1== argument_2) {
			If_block_330(flow);
		}
	}

	private  void If_block_328(Integer flow) {
		int argument_1 = _DayOfWeekMainCandle;
		int argument_2 = 2;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_362(flow);
		}
		else if (argument_1== argument_2) {
			Calculation_block_174(flow);
		}
	}

	private  void If_block_330(Integer flow) {
		int argument_1 = _DayOfWeekMainCandle;
		int argument_2 = 2;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_363(flow);
		}
		else if (argument_1== argument_2) {
			Calculation_block_175(flow);
		}
	}

	private  void If_block_331(Integer flow) {
		long argument_1 = _TimeFlat;
		int argument_2 = 187200000;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_301(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_333(Integer flow) {
		long argument_1 = _TimeFlat;
		int argument_2 = 187200000;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_319(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_335(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_348(flow);
		}
	}

	private  void CloseandCancelPosition_block_336(Integer flow) {
		try {
			if (_CurrentPos != null && (_CurrentPos.getState() == IOrder.State.OPENED||_CurrentPos.getState() == IOrder.State.FILLED)){
				_CurrentPos.close();
				TradeEventAction event = new TradeEventAction();
				event.setMessageType(IMessage.Type.ORDER_CLOSE_OK);
				event.setNextBlockId("If_block_401");
				event.setPositionLabel(_CurrentPos.getLabel());
				event.setFlowId(flow);
				tradeEventActions.add(event);
			}
		} catch (JFException e)  {
			e.printStackTrace();
		}
	}

	private  void PositionsViewer_block_337(Integer flow) {
		List<IOrder> argument_1 = OpenPositions;
		for (IOrder order : argument_1){
			if (order.getState() == IOrder.State.OPENED||order.getState() == IOrder.State.FILLED){
				_CurrentPos = order;
				If_block_192(flow);
			}
		}
	}

	private  void If_block_338(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_358(flow);
		}
	}

	private void Calculation_block_339(Integer flow) {
		long argument_1 = _TimeClose;
		long argument_2 = _TimeFlat;
		_TimeClose = (long)(argument_1 - argument_2);
	}

	private  void PositionsViewer_block_340(Integer flow) {
		List<IOrder> argument_1 = OpenPositions;
		for (IOrder order : argument_1){
			if (order.getState() == IOrder.State.OPENED||order.getState() == IOrder.State.FILLED){
				_CurrentPos = order;
				If_block_341(flow);
			}
		}
	}

	private  void If_block_341(Integer flow) {
		long argument_1 = _TimeCloseCompare;
		long argument_2 = _TimeClose;
		if (argument_1< argument_2) {
			If_block_343(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_344(flow);
		}
		else if (argument_1== argument_2) {
			If_block_343(flow);
		}
	}

	private  void If_block_342(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			If_block_347(flow);
		}
		else if (argument_1 == argument_2) {
		}
	}

	private  void If_block_343(Integer flow) {
		boolean argument_1 = _CurrentPos.isLong();
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			If_block_357(flow);
		}
		else if (argument_1 == argument_2) {
		}
	}

	private  void Assign_block_344(Integer flow) {
		long argument_1 = _TimeCloseCompare;
		_TimeClose =  argument_1;
			If_block_342(flow);
	}

	private void Calculation_block_346(Integer flow) {
		long argument_1 = _TimeClose;
		long argument_2 = _TimeFlat;
		_TimeClose = (long)(argument_1 - argument_2);
	}

	private  void If_block_347(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_361(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_348(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_359(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_349(Integer flow) {
		Period argument_1 = _PeriodH4;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
			Calculation_block_191(flow);
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			Calculation_block_353(flow);
		}
	}

	private  void If_block_351(Integer flow) {
		Period argument_1 = _PeriodH4;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
			Calculation_block_187(flow);
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			Calculation_block_354(flow);
		}
	}

	private void Calculation_block_353(Integer flow) {
		long argument_1 = _TimeFlat;
		double argument_2 = 2.0;
		_TimeFlat = (long)(argument_1 * argument_2);
		Calculation_block_191(flow);
	}

	private void Calculation_block_354(Integer flow) {
		long argument_1 = _TimeFlat;
		double argument_2 = 2.0;
		_TimeFlat = (long)(argument_1 * argument_2);
		Calculation_block_187(flow);
	}

	private  void If_block_357(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 1.01;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Calculation_block_346(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_358(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 0.0;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_360(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_359(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 1.71;
		if (argument_1< argument_2) {
			CalculationExpression_block_376(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_360(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 1.71;
		if (argument_1< argument_2) {
			Calculation_block_339(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_361(Integer flow) {
		double argument_1 = _CurrentPos.getTakeProfitPrice();
		double argument_2 = 1.01;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			CalculationExpression_block_377(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_362(Integer flow) {
		int argument_1 = _DayOfWeekCandleNoSignalMax;
		int argument_2 = 2;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			Calculation_block_174(flow);
		}
	}

	private  void If_block_363(Integer flow) {
		int argument_1 = _DayOfWeekCandleNoSignalMax;
		int argument_2 = 2;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			Calculation_block_175(flow);
		}
	}

	private  void Assign_block_366(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_MainCandleSellHigh =  argument_1;
			Assign_block_367(flow);
	}

	private  void Assign_block_367(Integer flow) {
		double argument_1 = _candle1.getLow();
		_CandlePrevLow =  argument_1;
			SetTakeProfit_block_372(flow);
	}

	private  void Assign_block_368(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_CandlePrevHigh =  argument_1;
			SetTakeProfit_block_381(flow);
	}

	private  void Assign_block_370(Integer flow) {
		double argument_1 = _candle1.getLow();
		_MainCandleBuyLow =  argument_1;
			Assign_block_368(flow);
	}

	private  void If_block_371(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _Fibo618Sell;
		if (argument_1< argument_2) {
			Assign_block_366(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void SetTakeProfit_block_372(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = 0.0;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("CloseandCancelPosition_block_336");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private void CalculationExpression_block_376(Integer flow) {
		_Fibo618Sell = _CurrentPos.getTakeProfitPrice()-(_CurrentPos.getTakeProfitPrice()-_MainCandleBuyLow)*0.618;
		If_block_371(flow);
	}

	private void CalculationExpression_block_377(Integer flow) {
		_Fibo618Buy = _CurrentPos.getTakeProfitPrice()+(_MainCandleSellHigh-_CurrentPos.getTakeProfitPrice())*0.618;
		If_block_378(flow);
	}

	private  void If_block_378(Integer flow) {
		double argument_1 = _candle1.getClose();
		double argument_2 = _Fibo618Buy;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_370(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void SetTakeProfit_block_381(Integer flow) {
		IOrder argument_1 = _CurrentPos;
		double argument_2 = 0.0;
		boolean isLong = argument_1.isLong();
		double takeProfit = round(argument_2, argument_1.getInstrument());
		try {
			argument_1.setTakeProfitPrice(takeProfit);
				TradeEventAction event = new TradeEventAction();
			event.setMessageType(IMessage.Type.ORDER_CHANGED_OK);
			event.setNextBlockId("CloseandCancelPosition_block_382");
			event.setPositionLabel(argument_1.getLabel());
			event.setFlowId(flow);
			tradeEventActions.add(event);
	} catch (JFException e) {
			e.printStackTrace();
		}
	}

	private  void CloseandCancelPosition_block_382(Integer flow) {
		try {
			if (_CurrentPos != null && (_CurrentPos.getState() == IOrder.State.OPENED||_CurrentPos.getState() == IOrder.State.FILLED)){
				_CurrentPos.close();
				TradeEventAction event = new TradeEventAction();
				event.setMessageType(IMessage.Type.ORDER_CLOSE_OK);
				event.setNextBlockId("If_block_401");
				event.setPositionLabel(_CurrentPos.getLabel());
				event.setFlowId(flow);
				tradeEventActions.add(event);
			}
		} catch (JFException e)  {
			e.printStackTrace();
		}
	}

	private  void If_block_383(Integer flow) {
		long argument_1 = _CandleCurrent.getTime();
		long argument_2 = _CandleTimeNoSignalMax;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_278(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_384(Integer flow) {
		long argument_1 = _CandleCurrent.getTime();
		long argument_2 = _CandleTimeNoSignalMax;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_274(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_385(Integer flow) {
		double argument_1 = _candle1.getHigh();
		_CandleHighHighest =  argument_1;
			If_block_331(flow);
	}

	private  void Assign_block_386(Integer flow) {
		double argument_1 = _candle1.getLow();
		_CandleLowLowest =  argument_1;
			If_block_333(flow);
	}

	private  void Assign_block_387(Integer flow) {
		long argument_1 = _candle1.getTime();
		_Candle1Time =  argument_1;
		}

	private  void If_block_389(Integer flow) {
		Period argument_1 = _PeriodH4;
		Period argument_2 = LastBidCandle.getPeriod();
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			MultipleAction_block_395(flow);
		}
	}

	private  void Assign_block_390(Integer flow) {
		boolean argument_1 = true;
		_isGoLong =  argument_1;
		}

	private  void Assign_block_392(Integer flow) {
		boolean argument_1 = true;
		_isGoShort =  argument_1;
			Assign_block_390(flow);
	}

	private  void If_block_393(Integer flow) {
		double argument_1 = _MA14H4Shift1;
		double argument_2 = _MA20;
		if (argument_1< argument_2) {
			If_block_104(flow);
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void If_block_394(Integer flow) {
		double argument_1 = _MA14H4Shift1;
		double argument_2 = _MA20;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			If_block_105(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void MultipleAction_block_395(Integer flow) {
		Assign_block_392(flow);
		If_block_396(flow);
	}

	private  void If_block_396(Integer flow) {
		double argument_1 = _STOH533slowkH4;
		double argument_2 = _STOH533slowdH4;
		if (argument_1< argument_2) {
			Assign_block_398(flow);
		}
		else if (argument_1> argument_2) {
			Assign_block_397(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void Assign_block_397(Integer flow) {
		boolean argument_1 = false;
		_isGoShort =  argument_1;
		}

	private  void Assign_block_398(Integer flow) {
		boolean argument_1 = false;
		_isGoLong =  argument_1;
		}

	private  void Assign_block_399(Integer flow) {
		boolean argument_1 = false;
		_isGoShort =  argument_1;
			Assign_block_400(flow);
	}

	private  void Assign_block_400(Integer flow) {
		boolean argument_1 = false;
		_isGoLong =  argument_1;
		}

	private  void If_block_401(Integer flow) {
		boolean argument_1 = _EndSwitch;
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			Assign_block_402(flow);
		}
		else if (argument_1 == argument_2) {
			Assign_block_403(flow);
		}
	}

	private  void Assign_block_402(Integer flow) {
		boolean argument_1 = true;
		_EndSwitch =  argument_1;
		}

	private  void Assign_block_403(Integer flow) {
		boolean argument_1 = false;
		_EndSwitch =  argument_1;
		}

	private  void If_block_408(Integer flow) {
		String argument_1 = LastTradeEvent.getType().name();
		String argument_2 = _PositionClose;
		if (argument_1!= null && !argument_1.equals(argument_2)) {
		}
		else if (argument_1!= null && argument_1.equals(argument_2)) {
			If_block_227(flow);
		}
	}

	private  void GetTimeUnit_block_409(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_MinuteCurrent = calendar.get(Calendar.MINUTE);
		If_block_411(flow);
	}

	private  void If_block_411(Integer flow) {
		int argument_1 = _MinuteCurrent;
		int argument_2 = 30;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_320(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void GetTimeUnit_block_413(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_DayOfWeekCurrent = calendar.get(Calendar.DAY_OF_WEEK);
		If_block_414(flow);
	}

	private  void If_block_414(Integer flow) {
		int argument_1 = _DayOfWeekCurrent;
		int argument_2 = 6;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
		}
		else if (argument_1== argument_2) {
			GetTimeUnit_block_415(flow);
		}
	}

	private  void GetTimeUnit_block_415(Integer flow) {
		long argument_1 = LastAskCandle.getTime();
		Date date = new Date(argument_1);
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		_HourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
		If_block_416(flow);
	}

	private  void If_block_416(Integer flow) {
		int argument_1 = _HourCurrent;
		int argument_2 = 19;
		if (argument_1< argument_2) {
		}
		else if (argument_1> argument_2) {
			Assign_block_418(flow);
		}
		else if (argument_1== argument_2) {
		}
	}

	private  void MultipleAction_block_417(Integer flow) {
		Assign_block_419(flow);
		GetTimeUnit_block_413(flow);
		If_block_420(flow);
	}

	private  void Assign_block_418(Integer flow) {
		boolean argument_1 = true;
		_StopTrend =  argument_1;
		}

	private  void Assign_block_419(Integer flow) {
		boolean argument_1 = false;
		_StopTrend =  argument_1;
		}

	private  void If_block_420(Integer flow) {
		boolean argument_1 = _StopTrend;
		boolean argument_2 = false;
		if (argument_1!= argument_2) {
		}
		else if (argument_1 == argument_2) {
			If_block_215(flow);
		}
	}

	private  void If_block_421(Integer flow) {
		boolean argument_1 = _EndSwitch;
		boolean argument_2 = true;
		if (argument_1!= argument_2) {
			Assign_block_422(flow);
		}
		else if (argument_1 == argument_2) {
			Assign_block_423(flow);
		}
	}

	private  void Assign_block_422(Integer flow) {
		boolean argument_1 = true;
		_EndSwitch =  argument_1;
		}

	private  void Assign_block_423(Integer flow) {
		boolean argument_1 = false;
		_EndSwitch =  argument_1;
		}

class Candle  {

    IBar bar;
    Period period;
    Instrument instrument;
    OfferSide offerSide;

    public Candle(IBar bar, Period period, Instrument instrument, OfferSide offerSide) {
        this.bar = bar;
        this.period = period;
        this.instrument = instrument;
        this.offerSide = offerSide;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public OfferSide getOfferSide() {
        return offerSide;
    }

    public void setOfferSide(OfferSide offerSide) {
        this.offerSide = offerSide;
    }

    public IBar getBar() {
        return bar;
    }

    public void setBar(IBar bar) {
        this.bar = bar;
    }

    public long getTime() {
        return bar.getTime();
    }

    public double getOpen() {
        return bar.getOpen();
    }

    public double getClose() {
        return bar.getClose();
    }

    public double getLow() {
        return bar.getLow();
    }

    public double getHigh() {
        return bar.getHigh();
    }

    public double getVolume() {
        return bar.getVolume();
    }
}
class Tick {

    private ITick tick;
    private Instrument instrument;

    public Tick(ITick tick, Instrument instrument){
        this.instrument = instrument;
        this.tick = tick;
    }

    public Instrument getInstrument(){
       return  instrument;
    }

    public double getAsk(){
       return  tick.getAsk();
    }

    public double getBid(){
       return  tick.getBid();
    }

    public double getAskVolume(){
       return  tick.getAskVolume();
    }

    public double getBidVolume(){
        return tick.getBidVolume();
    }

   public long getTime(){
       return  tick.getTime();
    }

   public ITick getTick(){
       return  tick;
    }
}

    protected String getLabel() {
        String label;
        label = "IVF" + getCurrentTime(LastTick.getTime()) + generateRandom(10000) + generateRandom(10000);
        return label;
    }

    private String getCurrentTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(time);
    }

    private static String generateRandom(int n) {
        int randomNumber = (int) (Math.random() * n);
        String answer = "" + randomNumber;
        if (answer.length() > 3) {
            answer = answer.substring(0, 4);
        }
        return answer;
    }

    class TradeEventAction {
		private IMessage.Type messageType;
		private String nextBlockId = "";
		private String positionLabel = "";
		private int flowId = 0;

        public IMessage.Type getMessageType() {
            return messageType;
        }

        public void setMessageType(IMessage.Type messageType) {
            this.messageType = messageType;
        }

        public String getNextBlockId() {
            return nextBlockId;
        }

        public void setNextBlockId(String nextBlockId) {
            this.nextBlockId = nextBlockId;
        }
        public String getPositionLabel() {
            return positionLabel;
       }

        public void setPositionLabel(String positionLabel) {
            this.positionLabel = positionLabel;
        }
        public int getFlowId() {
            return flowId;
        }
        public void setFlowId(int flowId) {
            this.flowId = flowId;
        }
    }
}