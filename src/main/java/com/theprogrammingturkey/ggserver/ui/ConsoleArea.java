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
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				output.appendText(String.valueOf((char) i));
			}
		});
	}

	public void write(String message)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				output.appendText(message);
			}
		});
	}
}