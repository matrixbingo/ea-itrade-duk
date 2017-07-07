package ea.itrade.duk.sdkClient.component.main;

import com.dukascopy.api.system.tester.ITesterIndicatorsParameters;

/**
 * Created by wangliang on 2017/7/7.
 */
public class IndicatorParameterBean implements ITesterIndicatorsParameters {
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