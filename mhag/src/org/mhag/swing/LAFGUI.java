package org.mhag.swing;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

/**
 * @program MHAG
 * @ About LAF GUI Class
 * @version 1.0
 * @author Tifa@mh3
 *
 */
public class LAFGUI {

	// add auto completion function to jComboBox ; add look and feel
	public static void setupAutoComplete(JComboBox comboBox)
	{
		comboBox.setEditable(true);
		JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		editor.setDocument(new AutoComplete(comboBox));
	}

	public static void setupLAF()
	{
		UIManager.put("Button.background", Color.WHITE);
		UIManager.put("ComboBox.background", Color.WHITE);
		UIManager.put("Panel.background", Color.WHITE);
		UIManager.put("Label.background", Color.WHITE);
		UIManager.put("RadioButton.background", Color.WHITE);
		UIManager.put("CheckBox.background", Color.WHITE);
		UIManager.put("ProgressBar.background", Color.WHITE);
		UIManager.put("TabbedPane.background", Color.WHITE);
		UIManager.put("ScrollPane.background", Color.WHITE);
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		UIManager.put("ScrollBarUI", "org.mhag.swing.MyMetalScrollBarUI");
	}

	
}
