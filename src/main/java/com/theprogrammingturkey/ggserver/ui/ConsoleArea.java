package com.theprogrammingturkey.ggserver.ui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ConsoleArea extends OutputStream
{
	private TextArea output;

	public ConsoleArea(TextArea ta)
	{
		this.output = ta;
	}

	@Override
	public void write(int i) throws IOException
	{
		write(String.valueOf((char) i));
	}

	public void write(String message)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				output.appendText(message);
				String text = output.getText();
				if(text.length() > 100000)
				{
					int end = text.length() - 100000;
					while(text.charAt(end) != '\n')
						end++;
					end++;
					output.replaceText(0, end, "");
				}
			}
		});
	}
}