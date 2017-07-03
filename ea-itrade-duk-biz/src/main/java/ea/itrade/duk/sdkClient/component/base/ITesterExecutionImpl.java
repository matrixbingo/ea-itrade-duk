package ea.itrade.duk.sdkClient.component.base;

import com.dukascopy.api.system.tester.ITesterExecution;
import com.dukascopy.api.system.tester.ITesterExecutionControl;

/**
 * Created by wangliang on 2017/7/3.
 */
public class ITesterExecutionImpl implements ITesterExecution {
    @Override
    public void setExecutionControl(ITesterExecutionControl executionControl) {
        ChartControlComm.executionControl = executionControl;
    }
}
