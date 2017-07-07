package ea.itrade.duk.sdkClient.component.main;

import com.dukascopy.api.system.ISystemListener;
import com.dukascopy.api.system.ITesterClient;
import ea.itrade.duk.sdkClient.component.base.ControlPanelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Created by wangliang on 2017/7/7.
 */
@Slf4j
@AllArgsConstructor
public class ISystemListenerImpl implements ISystemListener {

    private ITesterClient client;

    @Override
    public void onStart(long processId) {
        log.info("Strategy started: " + processId);
        ControlPanelUtil.updateButtons();
    }

    @Override
    public void onStop(long processId) {
        log.info("Strategy stopped: " + processId);
        ControlPanelUtil.resetButtons();

        File reportFile = new File("C:\\report.html");
        try {
            client.createReport(processId, reportFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (client.getStartedStrategies().size() == 0) {
            //Do nothing
        }
    }

    @Override
    public void onConnect() {
        log.info("Connected");
    }

    @Override
    public void onDisconnect() {
        //tester doesn't disconnect
    }
}
