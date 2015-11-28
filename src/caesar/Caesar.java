package caesar;

import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

/**
 * Diese Klasse geniert sowohl das graphische Fenster der Caesar-Verschluesselung, als auch die interne Ver- und Entschluesselung.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Caesar {
	
	private JFrame frame1 = new JFrame("Caesar-Verschlüsselung");
	private NumberFormat format = NumberFormat.getInstance(); 
	private NumberFormatter formatter = new NumberFormatter(format);
	private String umbruch = System.getProperty("line.separator");
	private Button buttonverschluesseln = new Button("-->");
	private Button buttonentschluesseln = new Button("<--");
	private JFormattedTextField schluesselfeld = new JFormattedTextField(formatter);
	private JTextArea klartext = new JTextArea();
	private JScrollPane klartextScrollPane = new JScrollPane(klartext);
	private JTextArea geheimtext = new JTextArea();
	private JScrollPane geheimtextScrollPane = new JScrollPane(geheimtext);

	private String klartextstring = new String();
	private String geheimtextstring = new String();
	private int schluessel;
	
	public Caesar() {
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(800,600));
		frame1.setMinimumSize(new Dimension(600,400));
		frame1.setResizable(true);
		
		buttonverschluesseln.setVisible(true);
		buttonverschluesseln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonverschluesseln_ActionPerformed();
			}
		});
		buttonentschluesseln.setVisible(true);
		buttonentschluesseln.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonentschluesseln_ActionPerformed();
			}
		});
		schluesselfeld.setToolTipText("Schlüssel");
		schluesselfeld.setHorizontalAlignment(JTextField.CENTER);
		schluesselfeld.setVisible(true);
		klartext.setText("Heizölrückstoßabdämpfung");
		klartext.setLineWrap(true);
		klartext.setWrapStyleWord(true);
		klartext.setToolTipText("Klartext");
		geheimtext.setLineWrap(true);
	    geheimtext.setWrapStyleWord(true);
	    geheimtext.setToolTipText("Geheimtext");
		
		JPanel mittelflaeche = new JPanel();
		mittelflaeche.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = 0;
        mittelflaeche.add(buttonverschluesseln, c);
        c.gridy = 1;
        mittelflaeche.add(schluesselfeld, c);
        c.gridy = 2;
        mittelflaeche.add(buttonentschluesseln, c);
		
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridBagLayout());
		cp.add(klartextScrollPane,new GridBagFelder(1,1,1,1,0.49,1));
		cp.add(mittelflaeche,new GridBagFelder(2,1,1,1,0.02,1));
		cp.add(geheimtextScrollPane,new GridBagFelder(3,1,1,1,0.49,1));
		
		format.setGroupingUsed(false); 
		formatter.setAllowsInvalid(false);
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}

	/**
	 * Diese Methode wird ausgefuehrt, wenn der Button zum Verschluesseln angeklickt wird.<br>
	 * Sie liest den darin enthaltenen String aus, speichert ihn in einem charArray und stoesst, wenn der String nicht leer ist die Verschluesselung an.
	 */
	private void buttonverschluesseln_ActionPerformed() {
		klartextstring = klartext.getText();
		char[] verschluesselarr = klartextstring.toCharArray();
		if(schluesseleinlesen()) {
			char[] verschluesselarrfertig = verschluesseln(verschluesselarr);
			geheimtextstring = String.valueOf(verschluesselarrfertig);
			geheimtext.setText(geheimtextstring);
		}
	}
	
	/**
	 * Diese Methode fuehrt die Verschluesselung durch. Sie verschiebt alle chars des Ursprungstextes um den eingelesenen Schluessel.
	 * @param charArray Nimmt den Ursprungstext entgegen.
	 * @return Gibt den verschluesselten Text aus.
	 */
	private char[] verschluesseln(char[] charArray) {
		char[] cryptArray = new char[charArray.length];
 
        for(int i=0;i<charArray.length;i++) {
        	int verschiebung = (charArray[i] + schluessel)%256;
        	cryptArray[i] = (char)(verschiebung);
        }
        return cryptArray;
	}
	
	/**
	 * Diese Methode wird ausgefuehrt, wenn der Button zum Entschluesseln angeklickt wird.<br>
	 * Sie liest den darin enthaltenen String aus, speichert ihn in einem charArray und stoesst, wenn der String nicht leer ist die Entschluesselung an.
	 */
	private void buttonentschluesseln_ActionPerformed() {
		geheimtextstring = geheimtext.getText();
		char[] entschluesselarr = geheimtextstring.toCharArray();
		if(schluesseleinlesen()) {
			char[] entschluesselarrfertig = entschluesseln(entschluesselarr);
			klartextstring = String.valueOf(entschluesselarrfertig);
			klartext.setText(klartextstring);
		}
	}
	
	/**
	 * Diese Methode fuehrt die Entschluesselung durch. Sie verschiebt alle chars des verschluesselten Textes um den eingelesenen Schluessel.
	 * @param charArray Nimmt den verschluesselten Text entgegen.
	 * @return Gibt den Ursprungstext aus.
	 */
	private char[] entschluesseln(char[] charArray) {
		char[] cryptArray = new char[charArray.length];
        int verschiebung; 
        
        for(int i=0;i<charArray.length;i++) {
        	if(charArray[i] - schluessel < 0) {
        		verschiebung = charArray[i] - schluessel + 256;
        	} else {
        		verschiebung = (charArray[i] - schluessel)%256;
        	}
        	cryptArray[i] = (char)(verschiebung);
        }
        return cryptArray;
    }
	
	/**
	 * Diese Methode liest den Schluessel ein. Wenn dieser leer ist, gibt sie false zurueck und stoppt die nicht moegliche Verschluesselung.
	 * @return Gibt den Erfolg des Schluesseleinlesens zurueck.
	 */
	private boolean schluesseleinlesen() {
		String schluesselstr = schluesselfeld.getText();
		try {
			schluessel = Integer.parseInt(schluesselstr);
			if(schluessel < 0) {
				schluessel = schluessel%-256;
				schluessel = 256 + schluessel;
			}
			return true;
		} catch(NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Dein Schlüssel ist ungültig."+umbruch+"Bitte wähle eine Zahl im Bereich von 1 bis 256.", "Falscher Schlüssel", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
	}

	public static void main(String[] args) {
		new Caesar();
	}
}