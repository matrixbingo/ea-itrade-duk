package ea.itrade.duk.jForex.strategyAPI.strategy_Examples;

import com.dukascopy.api.*;

@RequiresFullAccess
@Library("C:/temp/simple-strategies.jar")
public class StrategySimpleParallel implements IStrategy {

	private IStrategy strategy1;
	private IStrategy strategy2;

	@Override
	public void onStart(IContext context) throws JFException {
        strategy1 = new SimpleStrategy();
        strategy2 = new SimpleStrategy();
       /* ((SimpleStrategy)strategy1).amount = 0.001;
        ((SimpleStrategy)strategy1).stopLossPips = 40;
        ((SimpleStrategy)strategy1).takeProfitPips = 40;
        ((SimpleStrategy)strategy2).amount = 0.002;
        ((SimpleStrategy)strategy2).stopLossPips = 80;
        ((SimpleStrategy)strategy2).takeProfitPips = 80;*/
        
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
