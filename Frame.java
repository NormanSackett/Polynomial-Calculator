import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.*;

public class Frame extends JFrame {
	JLabel CoefLabel;
	JLabel CoefListLabel;
	JLabel Polynom1Label;
	JLabel Polynom2Label;
	JLabel XLabel;
	JLabel RootLabel;
	JLabel ValLabel;
	JLabel OperationsLabel;
	JLabel ResultLabel;
	JTextField CoefField;
	JTextField XField;
	JTextField RootField;
	JTextField ValField;
	JList<String> CoefList1;
	DefaultListModel<String> list1;
	JList<String> CoefList2;
	DefaultListModel<String> list2;
	JPanel ListPanel;
	JTextArea Polynom1;
	JTextArea Polynom2;
	JTextArea ResultArea;
	JButton Add;
	JButton Insert;
	JButton Modify;
	JButton Delete;
	JButton Clear;
	JButton Compute;
	JButton SwitchPoly; // switches between the two polynomials
	JButton Copy;
	JButton Paste;
	JButton SetToResult; // sets selected polynomial to the current result
	ButtonGroup bg;
	JRadioButton RdSum; // sum radio button
	JRadioButton RdDiff; // difference radio button
	JRadioButton RdProd; // product
	JRadioButton RdQuo; // quotient
	JFrame frm;
	
	private Polynomial polynomial1;
	private Polynomial polynomial2;
	private Polynomial result;
	private Polynomial copiedPoly;
	int selectionNum;
	
	Frame() {
		frm = new JFrame("Polynomial Calculator");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLayout(null);
		frm.setSize(1000, 750);
		
		LabelSetup(CoefLabel, "Coefficient", 10, 10, 200, 40);
		LabelSetup(CoefListLabel, "Coef. List", 310, 10, 200, 40);
		LabelSetup(XLabel, "X", 10, 100, 200, 40);
		LabelSetup(RootLabel, "Roots", 210, 610, 200, 40);
		LabelSetup(ValLabel, "Value at X", 10, 610, 200, 40);
		LabelSetup(OperationsLabel, "Operations", 180, 10, 200, 40);
		LabelSetup(ResultLabel, "Result", 770, 10, 200, 40);
		
		Polynom1Label = new JLabel("Polynomial 1");
		Polynom1Label.setBounds(540, 10, 200, 40);
		Font font = new Font("Arial", Font.BOLD, 12);
		Polynom1Label.setFont(font);
		frm.add(Polynom1Label);
		
		Polynom2Label = new JLabel("Polynomial 2");
		Polynom2Label.setBounds(540, 350, 200, 40);
		frm.add(Polynom2Label);
		
		bg = new ButtonGroup();
		
		RdSum = new JRadioButton("Sum");
		RdSum.setBounds(170, 50, 100, 50);
		bg.add(RdSum);
		frm.add(RdSum);
		
		RdDiff = new JRadioButton("Difference");
		RdDiff.setBounds(170, 100, 100, 50);
		bg.add(RdDiff);
		frm.add(RdDiff);

		RdProd = new JRadioButton("Product");
		RdProd.setBounds(170, 150, 100, 50);
		bg.add(RdProd);
		frm.add(RdProd);
		
		RdQuo = new JRadioButton("Quotient");
		RdQuo.setBounds(170, 200, 100, 50);
		bg.add(RdQuo);
		frm.add(RdQuo);
		
		CoefField = new JTextField();
		CoefField.setBounds(10, 50, 150, 40);
		CoefField.setBackground(Color.WHITE);
		frm.add(CoefField);
		
		XField = new JTextField();
		XField.setBounds(10, 140, 150, 40);
		XField.setBackground(Color.WHITE);
		frm.add(XField);
		
		RootField = new JTextField();
		RootField.setBounds(210, 650, 300, 40);
		RootField.setBackground(Color.WHITE);
		RootField.setEditable(false);
		frm.add(RootField);
		
		ValField = new JTextField();
		ValField.setBounds(10, 650, 150, 40);
		ValField.setBackground(Color.WHITE);
		ValField.setEditable(false);
		frm.add(ValField);
		
		ListPanel = new JPanel(null); // JPanel requires a layout
		ListPanel.setBounds(310, 50, 200, 400);
		ListPanel.setVisible(true);
		
		list1 = new DefaultListModel<String>();
		CoefList1 = new JList<>(list1);
		CoefList1.setBounds(0, 0, 200, 400);
		CoefList1.setBackground(Color.WHITE);
		CoefList1.setVisible(true);
		CoefList1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					ListDoubleClick(CoefList1.getSelectedValue());
				}
			}
		});
		ListPanel.add(CoefList1);
		
		list2 = new DefaultListModel<String>();
		CoefList2 = new JList<>(list2);
		CoefList2.setBounds(0, 0, 200, 400);
		CoefList2.setBackground(Color.WHITE);
		CoefList2.setVisible(false);
		CoefList2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					ListDoubleClick(CoefList2.getSelectedValue());
				}
			}
		});
		ListPanel.add(CoefList2);
		
		frm.add(ListPanel);
		
		Polynom1 = new JTextArea();
		Polynom1.setBounds(540, 50, 200, 300);
		Polynom1.setBackground(Color.WHITE);
		Polynom1.setEditable(false);
		frm.add(Polynom1);
		
		Polynom2 = new JTextArea();
		Polynom2.setBounds(540, 390, 200, 300);
		Polynom2.setBackground(Color.WHITE);
		Polynom2.setEditable(false);
		frm.add(Polynom2);
		
		ResultArea = new JTextArea();
		ResultArea.setBounds(770, 50, 200, 640);
		ResultArea.setBackground(Color.WHITE);
		ResultArea.setEditable(false);
		frm.add(ResultArea);
		
		Add = new JButton("Add");
		Add.setBounds(20, 200, 130, 50);
		Add.setHorizontalTextPosition(JButton.CENTER);
		Add.setBackground(Color.LIGHT_GRAY);
		Add.setBorder(BorderFactory.createEtchedBorder());
		Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Add);
			}
		});
		frm.add(Add);
		
		Insert = new JButton("Insert");
		Insert.setBounds(20, 260, 130, 50);
		Insert.setHorizontalTextPosition(JButton.CENTER);
		Insert.setBackground(Color.LIGHT_GRAY);
		Insert.setBorder(BorderFactory.createEtchedBorder());
		Insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Insert);
			}
		});
		frm.add(Insert);
		
		Modify = new JButton("Modify");
		Modify.setBounds(20, 320, 130, 50);
		Modify.setHorizontalTextPosition(JButton.CENTER);
		Modify.setBackground(Color.LIGHT_GRAY);
		Modify.setBorder(BorderFactory.createEtchedBorder());
		Modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Modify);
			}
		});
		frm.add(Modify);
		
		Delete = new JButton("Delete");
		Delete.setBounds(20, 380, 130, 50);
		Delete.setHorizontalTextPosition(JButton.CENTER);
		Delete.setBackground(Color.LIGHT_GRAY);
		Delete.setBorder(BorderFactory.createEtchedBorder());
		Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Delete);
			}
		});
		frm.add(Delete);
		
		Clear = new JButton("Clear All");
		Clear.setBounds(20, 440, 130, 50);
		Clear.setHorizontalTextPosition(JButton.CENTER);
		Clear.setBackground(Color.LIGHT_GRAY);
		Clear.setBorder(BorderFactory.createEtchedBorder());
		Clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Clear);
			}
		});
		frm.add(Clear);
		
		Compute = new JButton("Compute");
		Compute.setBounds(20, 550, 130, 50);
		Compute.setHorizontalTextPosition(JButton.CENTER);
		Compute.setBackground(Color.LIGHT_GRAY);
		Compute.setBorder(BorderFactory.createEtchedBorder());
		Compute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Compute);
			}
		});
		frm.add(Compute);
		
		SwitchPoly = new JButton("Switch to Poly 2");
		SwitchPoly.setBounds(340, 460, 140, 50);
		SwitchPoly.setHorizontalTextPosition(JButton.CENTER);
		SwitchPoly.setBackground(Color.LIGHT_GRAY);
		SwitchPoly.setBorder(BorderFactory.createEtchedBorder());
		SwitchPoly.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(SwitchPoly);
			}
		});
		frm.add(SwitchPoly);
		
		Copy = new JButton("Copy");
		Copy.setBounds(160, 320, 130, 50);
		Copy.setHorizontalTextPosition(JButton.CENTER);
		Copy.setBackground(Color.LIGHT_GRAY);
		Copy.setBorder(BorderFactory.createEtchedBorder());
		Copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Copy);
			}
		});
		frm.add(Copy);
		
		Paste = new JButton("Paste");
		Paste.setBounds(160, 380, 130, 50);
		Paste.setHorizontalTextPosition(JButton.CENTER);
		Paste.setBackground(Color.LIGHT_GRAY);
		Paste.setBorder(BorderFactory.createEtchedBorder());
		Paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(Paste);
			}
		});
		frm.add(Paste);
		
		SetToResult = new JButton("Set Poly 1 to Result");
		SetToResult.setBounds(160, 440, 130, 50);
		SetToResult.setHorizontalTextPosition(JButton.CENTER);
		SetToResult.setBackground(Color.LIGHT_GRAY);
		SetToResult.setBorder(BorderFactory.createEtchedBorder());
		SetToResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClick(SetToResult);
			}
		});
		frm.add(SetToResult);
		
		
		polynomial1 = new Polynomial();
		polynomial2 = new Polynomial();
		
		Polynom1.setText(polynomial1.toString());
		Polynom2.setText(polynomial2.toString());
		
		result = new Polynomial();
		selectionNum = 1;
		frm.setVisible(true);
	}
	
	public Polynomial Poly() {
		if (selectionNum == 1) {
			return polynomial1;
		}
		else {
			return polynomial2;
		}
	}
	
	public JTextArea Area() {
		if (selectionNum == 1) {
			return Polynom1;
		}
		else {
			return Polynom2;
		}
	}
	
	public JList<String> List() {
		if (selectionNum == 1) {
			return CoefList1;
		}
		else {
			return CoefList2;
		}
	}
	
	public DefaultListModel<String> Model() {
		if (selectionNum == 1) {
			return list1;
		}
		else {
			return list2;
		}
	}
	
	public void LabelSetup(JLabel lbl, String text, int x, int y, int w, int h) {
		lbl = new JLabel(text);
		lbl.setBounds(x, y, w, h);
		frm.add(lbl);
	}
	
	public void buttonClick(Object source) { // routes to button clicks
		if (source == Compute) {
			ComputeClick();
		}
		else if (source == Clear) {
			ClearClick();
			Area().setText("");
		}
		else if (source == SwitchPoly) {
			SwitchClick();
		}
		else if (source == Copy) {
			CopyClick();
		}
		else if (source == Paste) {
			PasteClick();
		}
		else if (source == SetToResult) {
			SetToResultClick();
		}
		else {
			try {
				double enteredNum = Double.valueOf(CoefField.getText());
				String coef = String.valueOf(enteredNum);
				int index = List().getSelectedIndex();
				
				if (source == Add) {
					AddClick(coef);
				}
				else if (source == Insert) {
					InsertClick(index, coef);
				}
				else if (source == Modify) {
					ModifyClick(index, coef);
				}
				else if (source == Delete) {
					DeleteClick(index);
				}
				ResetPolynomial();
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "enter a number for the coefficient", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void EvaluatePolynomials() { // routes to operation on the polynomials
		if (RdSum.isSelected()) {
			if (polynomial1.length() < polynomial2.length()) {
				result.sum(polynomial1.clone(), polynomial2.clone());
			}
			else {
				result.sum(polynomial2.clone(), polynomial1.clone());
			}
			ResultArea.setText(result.toString());
		}
		else if (RdDiff.isSelected()) {
			result.diff(polynomial1.clone(), polynomial2.clone());
			ResultArea.setText(result.toString());
		}
		else if (RdProd.isSelected()) {
			if (polynomial1.length() < polynomial2.length()) {
				result.prod(polynomial1.clone(), polynomial2.clone());
			}
			else {
				result.prod(polynomial2.clone(), polynomial1.clone());
			}
			ResultArea.setText(result.toString());
		}
		else if (RdQuo.isSelected()) {
			result.quo(polynomial1.clone(), polynomial2.clone());
			ResultArea.setText(result.toString());
		}
	}
	
	public void AddClick(String coef) {
		Model().addElement(coef);
		List().setSelectedIndex(CoefList1.getComponentCount() - 1);
	}
	
	public void InsertClick(int index, String coef) {
		Model().add(index, coef);
		if (index == -1)
			index = List().getComponentCount() - 1;
		List().setSelectedIndex(index);
	}
	
	public void ModifyClick(int index, String coef) {
		if (index == -1)
			return;
		Model().remove(index);
		Model().add(index, coef);
		List().setSelectedIndex(index);
	}
	
	public void DeleteClick(int index) {
		if (index == -1)
			return;
		
		Model().remove(index);
	}
	
	public void ClearClick() {
		Model().removeAllElements();
		ResultArea.setText("");
	}
	
	public void ComputeClick() {
		if (XField.getText().equals("") == false) {
			double value = Poly().valueAtX(Double.valueOf(XField.getText()));
			ValField.setText(String.valueOf(value));
		}
		DisplayRoots();
		EvaluatePolynomials();
	}
	
	public void ListDoubleClick(String itemClicked) {
		int index = CoefList1.getSelectedIndex();
		String coef = list1.get(index);
		CoefField.setText(coef);
	}
	
	private void ResetPolynomial() {
		int arraySize = Model().getSize();
		String[] coefArray = new String[arraySize];
		
		for (int i = 0; i < arraySize; i++)
			coefArray[i] = Model().get(i);
		
		if (selectionNum == 1) {
			polynomial1 = new Polynomial(coefArray);
		}
		else {
			polynomial2 = new Polynomial(coefArray);	
		}	
		Area().setText(Poly().toString());
	}
	
	public void SwitchClick() {
		if (selectionNum == 1) {
			selectionNum = 2;
			SwitchObjs("2");
		}
		else {
			selectionNum = 1;
			SwitchObjs("1");
		}
	}
	
	public void SwitchObjs(String selectedPolyNum) {
		Font font1 = new Font("Arial", Font.BOLD, 12);
		Font font2 = new Font("Arial", Font.PLAIN, 12);
		if (selectedPolyNum.equals("1")) {
			SwitchPoly.setText("Switch to Poly 2");
			CoefList2.setVisible(false);
			CoefList1.setVisible(true);
			Polynom1Label.setFont(font1);
			Polynom2Label.setFont(font2);
		}
		else {
			SwitchPoly.setText("Switch to Poly 1");
			CoefList1.setVisible(false);
			CoefList2.setVisible(true);
			Polynom2Label.setFont(font1);
			Polynom1Label.setFont(font2);
		}
		SetToResult.setText("Set Poly " + selectedPolyNum + " to Result");
	}
	
	public void DisplayRoots() {
		DecimalFormat df = new DecimalFormat("0.000"); //https://www.javatpoint.com
		RootField.setText("");
		
		double[] roots = Poly().getRoots();
		String rootsString = "";
		for (int i = 0; i < roots.length; i++) {
			if (Double.isNaN(roots[i]) == false) {
				rootsString += ", " + String.valueOf(df.format(roots[i]));
			}
		}
		if (rootsString.equals("") == false) {
			RootField.setText(rootsString.substring(2));
		}
	}
	
	public void CopyClick() {
		copiedPoly = Poly().clone();
	}
	
	public void PasteClick() {
		Area().setText(copiedPoly.toString());
		double[] polyCoefs = copiedPoly.getCoef().clone();
		Model().clear();
		
		for (int i = polyCoefs.length - 1; i >= 0; i--) {
			Model().addElement(String.valueOf(polyCoefs[i]));
		}
		
		if (selectionNum == 1) {
			polynomial1 = copiedPoly.clone();
		}
		else {
			polynomial2 = copiedPoly.clone();
		}
	}
	
	public void SetToResultClick() {
		Area().setText(result.toString());
		double[] polyCoefs = result.getCoef().clone();
		Model().clear();
		
		for (int i = polyCoefs.length - 1; i >= 0; i--) {
			Model().addElement(String.valueOf(polyCoefs[i]));
		}
		
		if (selectionNum == 1) {
			polynomial1 = result.clone();
		}
		else {
			polynomial2 = result.clone();
		}
	}
}
