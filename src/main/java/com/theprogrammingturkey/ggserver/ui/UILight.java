package com.theprogrammingturkey.ggserver.ui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.theprogrammingturkey.ggserver.commands.CommandManager;
import com.theprogrammingturkey.ggserver.news.NewsHolder;
import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;

public class UILight extends JFrame implements IUI
{
	private static final long serialVersionUID = 1L;
	private JTextArea console;
	private JTextField input;

	public UILight()
	{
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(600, 400);
		panel.setBackground(Color.GRAY);

		console = new JTextArea();
		console.setEditable(false);
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		console.setSize(560, 300);
		console.setLocation(10, 10);
		console.setBackground(Color.BLACK);
		console.setForeground(Color.GREEN);
		JScrollPane sp = new JScrollPane(console);
		sp.setSize(560, 300);
		sp.setLocation(10, 10);
		panel.add(sp);
		input = new JTextField();
		input.setEnabled(true);
		input.setEditable(true);
		input.setBackground(Color.BLACK);
		input.setForeground(Color.GREEN);
		input.setLocation(10, 325);
		input.setSize(560, 25);
		input.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					CommandManager.processCommand(input.getText());
					input.setText("");
				}
			}
		});
		panel.add(input);

		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GG Server");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void consoleMessage(String message)
	{
		if(this.console.getLineCount() > 1000)
			this.console.setText("");
		this.console.append(message + "\n");
	}

	@Override
	public void dispatchNews(NewsHolder news)
	{

	}

	@Override
	public void updateService(ActiveServiceWrapper service)
	{

	}
}
