package com.theprogrammingturkey.ggserver.ui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.theprogrammingturkey.ggserver.commands.CommandManager;
import com.theprogrammingturkey.ggserver.services.IServiceCore;

public class UICore extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static UICore instance = null;

	private JTextArea console;
	private JTextField input;
	private JList<String> services;

	private DefaultListModel<String> listModel;
	private Map<String, String> displayedServices = new HashMap<String, String>();

	public static void initUI()
	{
		if(instance == null)
			new UICore();
	}

	public static UICore getInstance()
	{
		return instance;
	}

	public UICore()
	{
		instance = this;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(600, 400);
		panel.setBackground(Color.GRAY);

		console = new JTextArea();
		console.setEditable(false);
		console.setWrapStyleWord(true);
		console.setSize(400, 300);
		console.setLocation(10, 10);
		console.setBackground(Color.BLACK);
		console.setForeground(Color.GREEN);
		JScrollPane sp = new JScrollPane(console);
		sp.setSize(400, 300);
		sp.setLocation(10, 10);
		panel.add(sp);
		input = new JTextField();
		input.setEnabled(true);
		input.setEditable(true);
		input.setBackground(Color.BLACK);
		input.setForeground(Color.GREEN);
		input.setLocation(10, 325);
		input.setSize(400, 25);
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
		listModel = new DefaultListModel<>();
		services = new JList<String>(listModel);
		services.setBackground(Color.BLACK);
		services.setLocation(425, 50);
		services.setSize(100, 300);
		panel.add(services);

		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GG Server");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void consoleMessage(String message)
	{
		this.console.append(message + "\n");
	}

	public void updateService(IServiceCore service)
	{
		if(!displayedServices.containsKey(service.getServiceID()))
		{
			displayedServices.put(service.getServiceID(), service.getServiceName());
			listModel.addElement(service.getServiceName());
		}
	}

}