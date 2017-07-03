package ea.itrade.duk.sdkClient.component.panel;

import com.dukascopy.api.IChart;
import com.dukascopy.api.IStrategy;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.system.tester.ITesterChartController;
import com.dukascopy.api.system.tester.ITesterGui;
import ea.itrade.duk.ea.MacdAndArw;
import ea.itrade.duk.sdkClient.component.base.ChartControlComm;
import ea.itrade.duk.sdkClient.component.base.ControlPanelUtil;
import ea.itrade.duk.sdkClient.component.base.JPeriodComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This small program demonstrates how to initialize Dukascopy tester and start a strategy in GUI mode
 */
@SuppressWarnings("serial")
public class MainJFrame extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainJFrame.class);

    private IStrategy strategy;

    public MainJFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }

    public MainJFrame(IStrategy strategy){
        this.strategy = strategy;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }

    /**
     * todo 空方法 子类实现
     * @throws Exception
     */
    public void startStrategy() throws Exception {}
    
    /**
     * Center a frame on the screen 
     */
    private void centerFrame(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setLocation(screenSize.width / 4, screenSize.height / 4);
    }
    
    /**
     * Add chart panel to the frame
     * @param panel
     */
    protected void addChartPanel(JPanel chartPanel){
        removecurrentChartPanel();
        ChartControlComm.currentChartPanel = chartPanel;
        chartPanel.setPreferredSize(new Dimension(ChartControlComm.frameWidth, ChartControlComm.frameHeight - ChartControlComm.controlPanelHeight));
        chartPanel.setMinimumSize(new Dimension(ChartControlComm.frameWidth, 200));
        chartPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        getContentPane().add(chartPanel);
        this.validate();
        chartPanel.repaint();
    }
    
	private ITesterChartController getChartController() {
		if (ChartControlComm.chartPanels == null || ChartControlComm.chartPanels.size() == 0) {
			return null;
		}
		IChart chart = ChartControlComm.chartPanels.keySet().iterator().next();
		ITesterGui gui = ChartControlComm.chartPanels.get(chart);
		ITesterChartController chartController = gui.getTesterChartController();
		return chartController;
	}

    /**
     * Add buttons to start/pause/continue/cancel actions  and other buttons
     */
    private void addControlPanel(){
        
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
                            LOGGER.error(e2.getMessage(), e2);
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
                if(ChartControlComm.executionControl != null){
                    ChartControlComm.executionControl.pauseExecution();
                    ControlPanelUtil.updateButtons();
                }
            }
        });
        
        ChartControlComm.continueButton = new JButton("Continue");
        ChartControlComm.continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ChartControlComm.executionControl != null){
                    ChartControlComm.executionControl.continueExecution();
                    ControlPanelUtil.updateButtons();
                }
            }
        });
        
        ChartControlComm.cancelButton = new JButton("Cancel");
        ChartControlComm.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ChartControlComm.executionControl != null){
                    ChartControlComm.executionControl.cancelExecution();
                    ControlPanelUtil.updateButtons();
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

/*        chartControlButtons.add(new JButton("+") {{
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getChartController().addMouseMotionListener(new MouseMotionListener(){
                        @Override
                        public void mouseDragged(MouseEvent e){}
                        @Override
                        public void mouseMoved(MouseEvent e){
                            //String string = "鼠标动到位置：（" + e.getX() + "，" + e.getY() +"）";
                            //System.out.println(string);
                        }
                    });
                }
            });
        }});*/
        
        ChartControlComm.controlPanel.add(ChartControlComm.startStrategyButton);
        ChartControlComm.controlPanel.add(ChartControlComm.pauseButton);
        ChartControlComm.controlPanel.add(ChartControlComm.continueButton);
        ChartControlComm.controlPanel.add(ChartControlComm.cancelButton);
        
        ChartControlComm.controlPanel.add(ChartControlComm.jPeriodComboBox);
        
        for(JButton btn : chartControlButtons){
        	ChartControlComm.controlPanel.add(btn);
        }
        
        getContentPane().add(ChartControlComm.controlPanel);
        
        ChartControlComm.pauseButton.setEnabled(false);
        ChartControlComm.continueButton.setEnabled(false);
        ChartControlComm.cancelButton.setEnabled(false);
    }

    private void addMouseEvent(MouseListener ml){

    }
    protected void resetButtons(){
        ChartControlComm.startStrategyButton.setEnabled(true);
        ChartControlComm.pauseButton.setEnabled(false);
        ChartControlComm.continueButton.setEnabled(false);
        ChartControlComm.cancelButton.setEnabled(false);
    }
    
    private void removecurrentChartPanel(){
        if(ChartControlComm.currentChartPanel != null){
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        MainJFrame.this.getContentPane().remove(ChartControlComm.currentChartPanel);
                        MainJFrame.this.getContentPane().repaint();
                    }
                });             
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void showChartFrame(){
        setSize(ChartControlComm.frameWidth, ChartControlComm.frameHeight);
        centerFrame();
        addControlPanel();
        setVisible(true);
    }
    
    public static void main(String[] args) throws Exception {

        IStrategy strategy = new MacdAndArw();
        MainJFrame mainJFrame = new MainJFrame(strategy);


        //mainJFrame.showChartFrame();
    }
}
