package ea.itrade.duk.sdkClient.component.base;

import com.dukascopy.api.IChart;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.system.tester.ITesterChartController;
import com.dukascopy.api.system.tester.ITesterGui;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.wang.sh on 2017/7/2.
 */
@Slf4j
public class ControlPanelUtil {
    /**
     * Add buttons to start/pause/continue/cancel actions  and other buttons
     */
    public static void addControlPanel() {

        ChartControlComm.controlPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        ChartControlComm.controlPanel.setLayout(flowLayout);
        ChartControlComm.controlPanel.setPreferredSize(new Dimension(ChartControlComm.frameWidth, ChartControlComm.controlPanelHeight));
        ChartControlComm.controlPanel.setMinimumSize(new Dimension(ChartControlComm.frameWidth, ChartControlComm.controlPanelHeight));
        ChartControlComm.controlPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, ChartControlComm.controlPanelMaxHeight));

        ChartControlComm.startStrategyButton = new JButton("Start strategy");
        ChartControlComm.startStrategyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChartControlComm.startStrategyButton.setEnabled(false);
                Runnable r = new Runnable() {
                    public void run() {
                        try {
                            startStrategy();
                        } catch (Exception e2) {
                            log.error(e2.getMessage(), e2);
                            e2.printStackTrace();
                            resetButtons();
                        }
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        });

        ChartControlComm.pauseButton = new JButton("Pause");
        ChartControlComm.pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ChartControlComm.executionControl != null) {
                    ChartControlComm.executionControl.pauseExecution();
                    updateButtons();
                }
            }
        });

        ChartControlComm.continueButton = new JButton("Continue");
        ChartControlComm.continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ChartControlComm.executionControl != null) {
                    ChartControlComm.executionControl.continueExecution();
                    updateButtons();
                }
            }
        });

        ChartControlComm.cancelButton = new JButton("Cancel");
        ChartControlComm.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ChartControlComm.executionControl != null) {
                    ChartControlComm.executionControl.cancelExecution();
                    updateButtons();
                }
            }
        });

        ChartControlComm.jPeriodComboBox = new JPeriodComboBox(this);

        List<JButton> chartControlButtons = new ArrayList<JButton>();

        chartControlButtons.add(new JButton("Add Indicators") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().addIndicators();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Price Marker") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activatePriceMarker();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Time Marker") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateTimeMarker();
                }
            });
        }});

        chartControlButtons.add(new JButton("Chart Auto Shift") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().setChartAutoShift();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Percent Lines") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activatePercentLines();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Channel Lines") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateChannelLines();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Poly Line") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activatePolyLine();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Short Line") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateShortLine();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Long Line") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateLongLine();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Ray Line") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateRayLine();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Horizontal Line") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateHorizontalLine();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Vertical Line") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateVerticalLine();
                }
            });
        }});

        chartControlButtons.add(new JButton("Add Text") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().activateTextMode();
                }
            });
        }});

        chartControlButtons.add(new JButton("Zoom In") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().zoomIn();
                }
            });
        }});

        chartControlButtons.add(new JButton("Zoom Out") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().zoomOut();
                }
            });
        }});

        chartControlButtons.add(new JButton("add OHLC Index") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().addOHLCInformer();
                }
            });
        }});

        chartControlButtons.add(new JButton("Bid") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().switchOfferSide(OfferSide.BID);
                }
            });
        }});

        chartControlButtons.add(new JButton("Ask") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().switchOfferSide(OfferSide.ASK);
                }
            });
        }});

        ChartControlComm.controlPanel.add(ChartControlComm.startStrategyButton);
        ChartControlComm.controlPanel.add(ChartControlComm.pauseButton);
        ChartControlComm.controlPanel.add(ChartControlComm.continueButton);
        ChartControlComm.controlPanel.add(ChartControlComm.cancelButton);

        ChartControlComm.controlPanel.add(ChartControlComm.jPeriodComboBox);

        for (JButton btn : chartControlButtons) {
            ChartControlComm.controlPanel.add(btn);
        }

        getContentPane().add(ChartControlComm.controlPanel);

        ChartControlComm.pauseButton.setEnabled(false);
        ChartControlComm.continueButton.setEnabled(false);
        ChartControlComm.cancelButton.setEnabled(false);
    }

    private static ITesterChartController getChartController() {
        if (ChartControlComm.chartPanels == null || ChartControlComm.chartPanels.size() == 0) {
            return null;
        }
        IChart chart = ChartControlComm.chartPanels.keySet().iterator().next();
        ITesterGui gui = ChartControlComm.chartPanels.get(chart);
        ITesterChartController chartController = gui.getTesterChartController();
        return chartController;
    }

    public static void updateButtons() {
        if (ChartControlComm.executionControl != null) {
            ChartControlComm.startStrategyButton.setEnabled(ChartControlComm.executionControl.isExecutionCanceled());
            ChartControlComm.pauseButton.setEnabled(!ChartControlComm.executionControl.isExecutionPaused() && !ChartControlComm.executionControl.isExecutionCanceled());
            ChartControlComm.cancelButton.setEnabled(!ChartControlComm.executionControl.isExecutionCanceled());
            ChartControlComm.continueButton.setEnabled(ChartControlComm.executionControl.isExecutionPaused());
        }
    }

    public static void resetButtons() {
        ChartControlComm.startStrategyButton.setEnabled(true);
        ChartControlComm.pauseButton.setEnabled(false);
        ChartControlComm.continueButton.setEnabled(false);
        ChartControlComm.cancelButton.setEnabled(false);
    }
}
