/*
 * Copyright (c) 2009 Dukascopy (Suisse) SA. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Dukascopy (Suisse) SA or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. DUKASCOPY (SUISSE) SA ("DUKASCOPY")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL DUKASCOPY OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF DUKASCOPY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package ea.itrade.duk.singlejartest;

import com.dukascopy.api.*;
import ea.itrade.duk.util.DateUtil;

public class MA_Play implements IStrategy {
    private IEngine engine = null;
    private IIndicators indicators = null;
    private IHistory history;
    private int tagCounter = 0;
    private double[] ma1 = new double[Instrument.values().length];
    private IConsole console;

    public MA_Play(){}

    public void onStart(IContext context) throws JFException {
        history = context.getHistory();
        engine = context.getEngine();
        indicators = context.getIndicators();
        this.console = context.getConsole();
        console.getOut().println("Started");
    }

    public void onStop() throws JFException {
        for (IOrder order : engine.getOrders()) {
            order.close();
        }
        console.getOut().println("Stopped");
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
        console.getOut().println("tick | " + DateUtil.dateToStr(tick.getTime()) + " | "+ history.getBar(instrument, Period.ONE_MIN, OfferSide.ASK,0));
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) {
      if(period == Period.TEN_SECS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.THIRTY_SECS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.ONE_MIN){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.FIVE_MINS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.TEN_MINS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.FIFTEEN_MINS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.THIRTY_MINS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.ONE_HOUR){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.FOUR_HOURS){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.DAILY){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.WEEKLY){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }else if(period == Period.MONTHLY){
            console.getOut().println(period + " | tick.getTime() -----> "  + DateUtil.dateToStr(askBar.getTime()));
        }
    }

    //count open positions
    protected int positionsTotal(Instrument instrument) throws JFException {
        int counter = 0;
        for (IOrder order : engine.getOrders(instrument)) {
            if (order.getState() == IOrder.State.FILLED) {
                counter++;
            }
        }
        return counter;
    }

    protected String getLabel(Instrument instrument) {
        String label = instrument.name();
        label = label.substring(0, 2) + label.substring(3, 5);
        label = label + (tagCounter++);
        label = label.toLowerCase();
        return label;
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onAccount(IAccount account) throws JFException {
    }
}