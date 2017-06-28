package ea.itrade.duk.jForex.strategyAPI.extending_User_Interface;

import com.dukascopy.api.*;
import ea.itrade.duk.jForex.startedAPI.iTesterClientFunctionality.TesterMainGUIMode;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Alerter is simple plug-in for Dukascopy JForex platform. It allows trader to
 * receive various type of notifications (pop-up, alarm) when some specified
 * market condition is met.
 * 
 * @author Daniel Taube
 * @version 1.2.0
 * 
 */

@RequiresFullAccess
public class Alerter implements IStrategy {

	private static final String ALERTER_TAB = "Alerter";

	private IContext context;

	private JPanel alerterPane;

	private AlerterTable alerterTable;

	private JButton addButton = new JButton("Add");

	private JButton deleteButton = new JButton("Delete");

	private boolean flag = true;

	@Configurable("Alarm file")
	public File alarmFile;

	public void onAccount(IAccount account) throws JFException {
	}

	public void onMessage(IMessage message) throws JFException {
	}

	public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
	}

	public void onStart(IContext context) throws JFException {
		this.context = context;
		this.alerterPane = context.getUserInterface().getBottomTab(ALERTER_TAB);
		this.alerterTable = new AlerterTable();
		updateButtonStates();
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				alerterTable.stopCellEditing();
				AlerterTableModel model = (AlerterTableModel) alerterTable.getModel();
				model.addAlert(createAlert());
				updateButtonStates();
			}
		});

		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				alerterTable.stopCellEditing();
				AlerterTableModel model = (AlerterTableModel) alerterTable.getModel();
				int selectedRow = alerterTable.getSelectedRow();
				if (selectedRow >= 0) {
					model.deleteAlert(alerterTable.getSelectedRow());
				}
				updateButtonStates();
			}
		});

		alerterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				updateButtonStates();
			}
		});

	}

	public void onStop() throws JFException {
		context.getUserInterface().removeBottomTab(ALERTER_TAB);
	}

	public void onTick(Instrument instrument, ITick tick) throws JFException {
		buildGui();
		AlerterTableModel alerterModel = (AlerterTableModel) alerterTable.getModel();
		List<Alert> alerts = alerterModel.getAlertList();
		for (Alert alert : alerts) {
			if (alert.getStatus() == Status.ACTIVE && alert.getInstrument() == instrument) {
				if (Condition.BID_LESS == alert.getCondition() && tick.getBid() < alert.getPrice().doubleValue()) {
					activateAlert(alert, tick.getBid());
				}
				if (Condition.BID_GREATER == alert.getCondition() && tick.getBid() > alert.getPrice().doubleValue()) {
					activateAlert(alert, tick.getBid());
				}
				if (Condition.ASK_LESS == alert.getCondition() && tick.getAsk() < alert.getPrice().doubleValue()) {
					activateAlert(alert, tick.getBid());
				}
				if (Condition.ASK_GREATER == alert.getCondition() && tick.getAsk() > alert.getPrice().doubleValue()) {
					activateAlert(alert, tick.getBid());
				}
			}
		}
	}

	private void activateAlert(Alert alert, double price) throws JFException {
		AlerterTableModel model = (AlerterTableModel) alerterTable.getModel();
		model.fireTableDataChanged();
		alert.complete();
		if (alert.getNotification() == Notification.POPUP) {
			showPopup(alert + " has been triggered by price " + price + "");
		} else if (alert.getNotification() == Notification.BEEP) {
			playSound(alarmFile);
		}
	}

	private void showPopup(String text) {
		JOptionPane optionPane = new JOptionPane(text, JOptionPane.WARNING_MESSAGE);
		JDialog dialog = optionPane.createDialog("Alert");
		dialog.setVisible(true);
	}

	private void playSound(File wavFile) throws JFException {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
			AudioFormat af = audioInputStream.getFormat();
			int nSize = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			byte[] audio = new byte[nSize];
			DataLine.Info info = new DataLine.Info(Clip.class, af, nSize);
			audioInputStream.read(audio, 0, nSize);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(af, audio, 0, nSize);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JFException(e);
		}
	}

	private void buildGui() {
		if (!flag) {
			return;
		}
		if (TesterMainGUIMode.controlPanel == null) {
			return;
		}
		alerterPane = TesterMainGUIMode.controlPanel;
		alerterPane.setLayout(new FlowLayout(FlowLayout.LEADING));
		JScrollPane scrollPane = new JScrollPane(alerterTable);
		alerterPane.add(scrollPane);
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		alerterPane.add(buttonPanel);
		flag = false;
	}

	private void updateButtonStates() {
		deleteButton.setEnabled(alerterTable.getSelectedRow() >= 0);
	}

	private Alert createAlert() {
		return new Alert(Instrument.EURUSD, context.getHistory());
	}

}

class AlerterTable extends JTable {

	private TableCellRenderer renderer;

	public AlerterTable() {
		super(new AlerterTableModel());
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setRowSelectionAllowed(true);
		getTableHeader().setReorderingAllowed(false);

		renderer = new DefaultTableCellRenderer() {

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
					int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(SwingConstants.CENTER);
				return this;
			}

		};

		TableColumn column = columnModel.getColumn(0);
		column.setCellEditor(new ComboBoxEditor(Instrument.values(), this));
		column.setMinWidth(80);
		column.setMaxWidth(80);

		column = columnModel.getColumn(1);
		column.setCellEditor(new ComboBoxEditor(Condition.values(), this));
		column.setMinWidth(70);
		column.setMaxWidth(70);

		column = columnModel.getColumn(2);
		column.setCellEditor(new SpinnerEditor());
		column.setMinWidth(80);
		column.setMaxWidth(80);

		column = columnModel.getColumn(3);
		column.setCellEditor(new ComboBoxEditor(Notification.values(), this));
		column.setMinWidth(70);
		column.setMaxWidth(70);

		column = columnModel.getColumn(4);
		column.setCellEditor(new ComboBoxEditor(new Status[] { Status.INACTIVE, Status.ACTIVE }, this));
		column.setMinWidth(150);
		column.setMaxWidth(150);
	}

	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(getPreferredSize().width, getRowHeight() * 10);

	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	public void stopCellEditing() {
		CellEditor cellEditor = getCellEditor();
		if (cellEditor != null) {
			cellEditor.stopCellEditing();
		}
	}

}

class AlerterTableModel extends AbstractTableModel {

	private List<Alert> alertList = new ArrayList<Alert>();

	private String[] columns = { "Currency", "Condition", "Price", "Event", "Status" };

	private DateFormat df = new SimpleDateFormat("hh:mm:ss");

	public AlerterTableModel() {
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public int getRowCount() {
		return alertList.size();
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Alert alert = alertList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return alert.getInstrument();
		case 1:
			return alert.getCondition();
		case 2:
			return alert.getPrice();
		case 3:
			return alert.getNotification();
		case 4:
			String notes = alert.getStatus() == Status.COMPLETED ? " at " + df.format(alert.getCompletedTime()) + " GMT" : "";
			return alert.getStatus() + notes;
		default:
			throw new IllegalArgumentException("Column " + columnIndex + " is undefined!");
		}
	}

	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}

	public List<Alert> getAlertList() {
		return alertList;
	}

	public void addAlert(Alert alert) {
		alertList.add(alert);
		fireTableRowsInserted(alertList.size() - 1, alertList.size() - 1);
	}

	public void deleteAlert(int index) {
		alertList.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Alert alert = alertList.get(rowIndex);
		if (alert != null) {
			switch (columnIndex) {
			case 0:
				alert.setInstrument((Instrument) value);
				break;
			case 1:
				alert.setCondition((Condition) value);
				break;
			case 2:
				alert.setPrice((BigDecimal) value);
				break;
			case 3:
				alert.setNotification((Notification) value);
				break;
			case 4:
				alert.setStatus((Status) value);
				break;
			default:
				throw new IllegalArgumentException("Column " + columnIndex + " is not editable!");
			}
		}
		fireTableRowsUpdated(rowIndex, rowIndex);
	}

}

class ComboBoxEditor extends AbstractCellEditor implements TableCellEditor {

	private JComboBox comboBox;

	public ComboBoxEditor(Object[] options, AlerterTable alerterTable) {
		comboBox = new JComboBox(options);
		final AlerterTable table = alerterTable;
		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				table.stopCellEditing();
			}

		});
	}

	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			return ((MouseEvent) anEvent).getClickCount() >= 2;
		}
		return true;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return comboBox;
	}

	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}

}

class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

	private JSpinner usdSpinner;

	private JSpinner jpySpinner;

	private boolean jpyMode;

	public SpinnerEditor() {
		usdSpinner = new JSpinner(new SpinnerNumberModel(0.0001d, 0.00005d, 99.99995d, 0.00005d));
		usdSpinner.setEditor(new JSpinner.NumberEditor(usdSpinner, "#0.00000"));
		jpySpinner = new JSpinner(new SpinnerNumberModel(0.01d, 0.005d, 999.995d, 0.005d));
		jpySpinner.setEditor(new JSpinner.NumberEditor(jpySpinner, "##0.000"));
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		AlerterTableModel tableModel = (AlerterTableModel) table.getModel();
		Alert alert = tableModel.getAlertList().get(row);
		if (Currency.getInstance("JPY").equals(alert.getInstrument().getSecondaryCurrency())) {
			jpyMode = true;
			jpySpinner.setValue(alert.getPrice().doubleValue());
			return jpySpinner;
		} else {
			jpyMode = false;
			usdSpinner.setValue(alert.getPrice().doubleValue());
			return usdSpinner;
		}
	}

	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			return ((MouseEvent) anEvent).getClickCount() >= 2;
		}
		return true;
	}

	public Object getCellEditorValue() {
		if (jpyMode) {
			return BigDecimal.valueOf((Double) jpySpinner.getValue()).setScale(3, RoundingMode.HALF_EVEN);
		} else {
			return BigDecimal.valueOf((Double) usdSpinner.getValue()).setScale(5, RoundingMode.HALF_EVEN);
		}
	}

}

class Alert {

	private Instrument instrument;

	private Condition condition;

	private BigDecimal price;

	private Notification notification;

	private Status status;

	private Date completedTime;

	private IHistory priceHistory;

	public Alert(Instrument instrument, IHistory priceHistory) {
		this.priceHistory = priceHistory;
		this.instrument = instrument;
		this.condition = Condition.values()[0];
		this.notification = Notification.values()[0];
		this.price = BigDecimal.valueOf(getInitialPrice());
		this.status = Status.INACTIVE;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument newInstrument) {
		if (newInstrument != instrument) {
			this.instrument = newInstrument;
			this.price = BigDecimal.valueOf(getInitialPrice());
		}
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCompletedTime() {
		return completedTime;
	}

	public void complete() {
		this.status = Status.COMPLETED;
		this.completedTime = new Date();
	}

	public String toString() {
		return "Alert on " + instrument + " " + condition + " " + price;
	}

	private double getInitialPrice() {
		ITick tick = null;
		try {
			tick = priceHistory.getLastTick(instrument);
		} catch (JFException e) {
		}
		if (tick == null) {
			return instrument.getPipValue();
		}
		if (condition.isBidCondition()) {
			return tick.getBid();
		}
		return tick.getAsk();
	}

}

enum Condition {

	BID_LESS("BID <", true), BID_GREATER("BID >", true), ASK_LESS("ASK <", false), ASK_GREATER("ASK >", false);

	private String name;

	private boolean bidCondition;

	Condition(String name, boolean bidCondition) {
		this.name = name;
		this.bidCondition = bidCondition;
	}

	public String toString() {
		return name;
	}

	public boolean isBidCondition() {
		return bidCondition;
	}

}

enum Notification {

	POPUP("Pop-up"), BEEP("Alarm");

	private String name;

	Notification(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}

enum Status {

	INACTIVE("Inactive"), ACTIVE("Active"), COMPLETED("Completed");

	private String name;

	Status(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
