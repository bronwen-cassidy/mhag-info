package org.mhag.swing;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

/**
 * @program MHAG
 * @ Combobox ToolTip Renderer Class for jewel menu tips
 * @version 1.0
 * @author Tifa@mh3
 */
@SuppressWarnings("serial")
public class ComboboxToolTipRenderer extends DefaultListCellRenderer
{
	ArrayList<String> tooltips;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus)
	{

		JComponent comp = (JComponent) super.getListCellRendererComponent(list,
			value, index, isSelected, cellHasFocus);

		if (-1 < index && null != value && null != tooltips)
		{
			list.setToolTipText(tooltips.get(index));
                }
        return comp;
	}

    public void setTooltips(ArrayList<String> tooltips)
    {
        this.tooltips = tooltips;
    }

    public void clearTooltips()
    {
        this.tooltips.clear();
    }
}
