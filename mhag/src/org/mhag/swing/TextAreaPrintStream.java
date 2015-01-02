package org.mhag.swing;

import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

/**
 * @program MHAG
 * @ extend PrintStream Class for TextArea
 * @ contains method println, printf, print, and specialized reset & rewind
 * @version 1.0
 * @author Tifa@mh3
 */
public class TextAreaPrintStream extends PrintStream
{
	private JTextArea textArea;
	
	public TextAreaPrintStream(JTextArea area, OutputStream out)
	{
		super(out);
		textArea = area;
	}

	@Override
	public void println(String string)
	{
		textArea.append(string+"\n");
	}

	@Override
	public PrintStream printf(String string, Object... args)
	{
		String line = String.format(string, args);
		textArea.append(line);
		return this;
	}

	@Override
	public void print(String string)
	{
		textArea.append(string);
	}

	public void reset()
	{
		textArea.setText("");
	}

	public void rewind()
	{
		textArea.setCaretPosition(0);
	}

}
