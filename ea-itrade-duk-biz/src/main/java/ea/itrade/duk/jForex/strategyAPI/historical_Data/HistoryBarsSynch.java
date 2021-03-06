package ea.itrade.duk.jForex.strategyAPI.historical_Data;

import com.dukascopy.api.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HistoryBarsSynch implements IStrategy {
    
    private IHistory history;
    private IConsole console;

    @Override
    public void onStart(IContext context) throws JFException {
        history = context.getHistory();
        console = context.getConsole(); 
        context.setSubscribedInstruments(java.util.Collections.singleton(Instrument.EURUSD), true);
        
        getBarByShift();
        getBarsByTimeInterval();
        getBarsByCandleInterval();

    }
    
    private void getBarByShift() throws JFException{        
        int shift = 1;
        IBar prevBar = history.getBar(Instrument.EURUSD, Period.ONE_HOUR, OfferSide.BID, shift);
        console.getOut().println(prevBar);        
    }
      
    private void getBarsByTimeInterval() throws JFException{    
        long prevBarTime = history.getPreviousBarStart(Period.ONE_HOUR, history.getLastTick(Instrument.EURUSD).getTime());
        long startTime =  history.getTimeForNBarsBack(Period.ONE_HOUR, prevBarTime, 5);  
        List<IBar> bars = history.getBars(Instrument.EURUSD, Period.ONE_HOUR, OfferSide.BID, startTime, prevBarTime);
        int last = bars.size() - 1;
        console.getOut().format(
                "Previous bar close price=%.5f; 4th to previous bar close price=%.5f", 
                bars.get(last).getClose(), bars.get(0).getClose()).println();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date dateFrom = dateFormat.parse("05/10/2008 00:00:00");
            Date dateTo = dateFormat.parse("04/10/2010 00:00:00");
            bars = history.getBars(Instrument.EURUSD, Period.DAILY, OfferSide.ASK, dateFrom.getTime(), dateTo.getTime());
            console.getInfo().println(bars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void getBarsByCandleInterval() throws JFException{    
        long prevBarTime = history.getPreviousBarStart(Period.ONE_HOUR, history.getLastTick(Instrument.EURUSD).getTime());
        List<IBar> bars = history.getBars(Instrument.EURUSD, Period.ONE_HOUR, OfferSide.BID, Filter.NO_FILTER, 6, prevBarTime, 0);
        int last = bars.size() - 1;
        console.getOut().format(
                "Previous bar close price=%.5f; 4th to previous bar close price=%.5f", 
                bars.get(last).getClose(), bars.get(0).getClose()).println();
    }


    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {}

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {}

    @Override
    public void onMessage(IMessage message) throws JFException {}

    @Override
    public void onAccount(IAccount account) throws JFException {}

    @Override
    public void onStop() throws JFException {}

}
