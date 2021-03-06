package ea.itrade.duk.jForex.strategyAPI.indicators.indicator_catalog;

import com.dukascopy.api.*;
import com.dukascopy.api.IChartObject.ATTR_LONG;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.drawings.ISignalDownChartObject;
import com.dukascopy.api.indicators.IIndicator;
import com.dukascopy.api.indicators.IndicatorInfo;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The strategy demonstrates how to use a set of candlestick patter indicators:
 * - On its start (i.e. in the onStart method) finds pattern occurrences 
 *   over the last 100 candlesticks and prints to console the latest occurrence by shift.
 * - On every candlestick (i.e. in the onBar method) checks if it is of any pattern of the set.
 * Also the strategy plots the indicator on chart.
 *
 */
@RequiresFullAccess
public class CandlePatternsMultiple implements IStrategy {
	
	@Configurable("")
	public Period period = Period.TEN_MINS;
	@Configurable("")
	public Instrument instrument = Instrument.EURUSD;
	@Configurable("")
	public OfferSide side = OfferSide.BID;
	@Configurable("")
	public AppliedPrice appliedPrice = AppliedPrice.CLOSE;
	@Configurable("")
	public int candleCount = 100;
	@Configurable("Penetration (applied to few patterns)")
	public double penetration = 0;
	@Configurable("Plot on chart?")
	public boolean plotOnChart = true;

	//patterns to check
	private Set<String> patternNames = new LinkedHashSet<String>(
		Arrays.asList(new String []{
			"CDLHAMMER",
			"CDLMORNINGSTAR",
			"CDLDOJI",
			"CDLSPINNINGTOP",
			"CDLSHOOTINGSTAR",
			"CDLHANGINGMAN"
	}));	
	
	private IIndicators indicators;
	private IConsole console;
	private IHistory history;
	private IContext context;
	private IChart chart;	

	@Override
	public void onStart(IContext context) throws JFException {
		indicators = context.getIndicators();
		console = context.getConsole();
		history = context.getHistory();
		chart = context.getChart(instrument);
		this.context = context;
				
		int candlesBefore = candleCount, candlesAfter = 0;
		long currBarTime = history.getBar(instrument, period, side, 0).getTime();
		
		for (String indName : patternNames){

			IIndicator indicator = context.getIndicators().getIndicator(indName);
			IndicatorInfo info = indicator.getIndicatorInfo();

			//most of the candle patterns have no optional inputs, those that have - it's penetration parameter
			Object[] optInputArray = info.getNumberOfOptionalInputs() == 0 
				? new Object[] { } 
				: new Object[] { penetration }; 
				
			Object[] patternUni = indicators.calculateIndicator(instrument, period, new OfferSide[] { side }, indName,
					new AppliedPrice[] { AppliedPrice.CLOSE }, optInputArray, Filter.NO_FILTER, candlesBefore, currBarTime, candlesAfter);
	
			//all candle patterns have just one output - we're good with 1-dimensional array
			int[] values = (int[]) patternUni[0];
			Set<Integer> occurrences = new LinkedHashSet<Integer>();

			for(int i=0; i < values.length; i++){
				int shift = values.length - 1 - i;
				if(values[i] != 0){
					occurrences.add(shift);
				}
			}
			int lastOccurrence = occurrences.isEmpty() 
				? -1 
				: Collections.min(occurrences);
			
			print(String.format("%s pattern occurances over last %s bars=%s; last occurrence shift=%s; all occurences: %s",
					indicator.getIndicatorInfo().getTitle(), candleCount,occurrences.size(), lastOccurrence, occurrences.toString()
					));			
			
			if (plotOnChart) {
				for (Integer shift : occurrences) {
					IBar bar = history.getBar(instrument, period, side, shift);
					addToChart(bar.getTime(), bar.getHigh(), values[values.length - 1 - shift], info.getTitle());
				}
			}
		}

	}
	
	private void print(Object o){
	    console.getOut().println(o);
	}

	@Override
	public void onTick(Instrument instrument, ITick tick) throws JFException {}

	@Override
	public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
		
		if(instrument != this.instrument || period != this.period){
			return;
		}
		
		for (String indName : patternNames){

			IIndicator indicator = context.getIndicators().getIndicator(indName);
			IndicatorInfo info = indicator.getIndicatorInfo();

			//most of the candle patterns have no optional inputs, those that have - it's penetration parameter
			Object[] optInputArray = info.getNumberOfOptionalInputs() == 0 
				? new Object[] { } 
				: new Object[] { penetration }; 
				
			int shift = 1;
			Object[] patternUni = indicators.calculateIndicator(instrument, period, new OfferSide[] { side }, indName,
					new AppliedPrice[] { AppliedPrice.CLOSE }, optInputArray, shift);
	
			//all candle patterns have just one output - we're good with 1-dimensional array
			int patternValue = (Integer) patternUni[0];

			if(patternValue != 0){
				print(String.format("%s pattern of value %s occurred at bar: %s",
						info.getTitle(), patternValue, bidBar.toString() ));	
				
				if (plotOnChart) {
					addToChart(bidBar.getTime(), bidBar.getHigh(), patternValue, info.getTitle());
				}
			}	
			
			
		}		
	}
	
	private void addToChart(long time, double price, int indicatorValue, String text){
		ISignalDownChartObject signal = chart.getChartObjectFactory().createSignalDown();
		
		price = adjustPrice(price, time);
		signal.setPrice(0, price);
		signal.setTime(0, time);
		signal.setText(text);
		//green for positive values, red - for negative
		signal.setColor(indicatorValue > 0 ? Color.GREEN.darker().darker() : Color.RED.darker().darker());
		chart.add(signal);
	}
	
	/**
	 * Adjust price, such that no 2 signal arrows overlap
	 */
	private double adjustPrice(double price, long time){
		double maxPrice = price;
		boolean anotherFound = false;
		for(IChartObject obj : chart.getAll()){
			//if another chart object is at about the same point (or below) on the chart
			if(obj.getAttrLong(ATTR_LONG.TIME1) == time && obj.getPrice(0) > maxPrice - instrument.getPipValue()){
				maxPrice = obj.getPrice(0);
				anotherFound = true;
			}
		}
		//raise price 10 pips higher than the current highest element
		if(anotherFound){
			price = maxPrice + instrument.getPipValue() * 10;
		}
		return price;
	}

	@Override
	public void onMessage(IMessage message) throws JFException {}

	@Override
	public void onAccount(IAccount account) throws JFException {}

	@Override
	public void onStop() throws JFException {}

}
