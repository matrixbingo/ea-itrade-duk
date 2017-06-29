package ea.itrade.duk.jForex.strategyAPI.strategy_Examples;


import com.dukascopy.api.*;
import com.dukascopy.api.IEngine.OrderCommand;

import java.util.UUID;

public class ParallelInnerStrategyCalls implements IStrategy {

    private InnerStrategy strategy1;
    private InnerStrategy strategy2;

    @Override
    public void onStart(IContext context) throws JFException {

        strategy1 = new InnerStrategy();
        strategy1.prefix = "strat5pip_";
        strategy1.amount = 0.01;
        strategy1.stopLossPips = 5;
        strategy1.takeProfitPips = 5;

        strategy2 = new InnerStrategy();
        strategy2.prefix = "strat10pip_";
        strategy2.amount = 0.02;
        strategy2.stopLossPips = 10;
        strategy2.takeProfitPips = 10;

        strategy1.onStart(context);
        strategy2.onStart(context);
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
        strategy1.onTick(instrument, tick);
        strategy2.onTick(instrument, tick);
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        strategy1.onBar(instrument, period, askBar, bidBar);
        strategy2.onBar(instrument, period, askBar, bidBar);

    }

    @Override
    public void onMessage(IMessage message) throws JFException {
        strategy1.onMessage(message);
        strategy2.onMessage(message);
    }

    @Override
    public void onAccount(IAccount account) throws JFException {
        strategy1.onAccount(account);
        strategy2.onAccount(account);
    }

    @Override
    public void onStop() throws JFException {
        strategy1.onStop();
        strategy2.onStop();
    }

}

class InnerStrategy implements IStrategy {

    // Configurable parameters
    @Configurable("prefix")
    public String prefix = "strat_";
    @Configurable("Instrument")
    public Instrument instrument = Instrument.EURUSD;
    @Configurable("Amount")
    public double amount = 0.02;
    @Configurable("Stop loss")
    public int stopLossPips = 5;
    @Configurable("Take profit")
    public int takeProfitPips = 10;

    private IEngine engine;
    private IConsole console;
    private IHistory history;

    @Override
    public void onStart(IContext context) throws JFException {
        this.engine = context.getEngine();
        this.console = context.getConsole();
        this.history = context.getHistory();

        console.getOut().println("started inner strategy: " + instrument + " " + amount + " " + stopLossPips);

        double price = history.getLastTick(instrument).getBid();
        String label = prefix + UUID.randomUUID().toString().replaceAll("-", "");

        engine.submitOrder(label, instrument, OrderCommand.BUY, amount, 0, 20,
                price - stopLossPips * instrument.getPipValue(),
                price + takeProfitPips * instrument.getPipValue());

    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {}

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar,
                      IBar bidBar) throws JFException {

    }

    @Override
    public void onMessage(IMessage message) throws JFException {
        IOrder order = message.getOrder();
        //print messages only of our order
        if(order != null && order.getLabel().startsWith(prefix)){
            console.getOut().println(message.toString());
        }
    }

    @Override
    public void onAccount(IAccount account) throws JFException {}

    @Override
    public void onStop() throws JFException {
        for(IOrder order : engine.getOrders(instrument)){
            if(order.getLabel().startsWith(prefix)){
                order.close();
            }
        }
    }

}
