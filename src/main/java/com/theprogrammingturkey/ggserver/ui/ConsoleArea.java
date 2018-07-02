package com.theprogrammingturkey.ggserver.ui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ConsoleArea
{
	private TextArea output;

	public ConsoleArea(TextArea ta)
	{
		this.output = ta;
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