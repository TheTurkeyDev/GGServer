package com.theprogrammingturkey.ggserver.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.theprogrammingturkey.ggserver.commands.CommandManager;
import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;
import com.theprogrammingturkey.ggserver.services.IServiceCore;
import com.theprogrammingturkey.ggserver.services.ServiceManager;

public class UICore extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static UICore instance = null;

	private JTextArea console;
	private JTextField input;
	private JList<ActiveServiceWrapper> services;
	private JButton start;
	private JButton stop;
	private JButton restart;

	private DefaultListModel<ActiveServiceWrapper> displayedServices;

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
		displayedServices = new DefaultListModel<>();
		services = new JList<ActiveServiceWrapper>(displayedServices);
		services.setBackground(Color.BLACK);
		services.setForeground(Color.GREEN);
		services.setLocation(425, 80);
		services.setSize(150, 270);
		services.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(e.getSource() instanceof JList && !e.getValueIsAdjusting())
				{
					String id = displayedServices.get(services.getSelectedIndex()).getServiceID();

					switch(ServiceManager.getServiceStatus(id))
					{
						case RUNNING:
							start.setVisible(false);
							stop.setVisible(true);
							restart.setVisible(true);
							break;
						case STOPPED:
							start.setEnabled(true);
							start.setVisible(true);
							stop.setVisible(false);
							restart.setVisible(false);
							break;
						default:
							start.setEnabled(false);
							start.setVisible(true);
							stop.setVisible(false);
							restart.setVisible(false);
							break;

					}
				}
			}

		});
		panel.add(services);

		start = new JButton("Start");
		start.setLocation(450, 10);
		start.setSize(100, 25);
		start.setEnabled(false);
		start.addActionListener(this);
		panel.add(start);

		stop = new JButton("Stop");
		stop.setLocation(450, 10);
		stop.setSize(100, 25);
		stop.setVisible(false);
		stop.addActionListener(this);
		panel.add(stop);

		restart = new JButton("Restart");
		restart.setLocation(450, 40);
		restart.setSize(100, 25);
		restart.setVisible(false);
		restart.addActionListener(this);
		panel.add(restart);

		this.add(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GG Server");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String id = displayedServices.get(services.getSelectedIndex()).getServiceID();
		if(e.getSource().equals(start))
		{
			ServiceManager.startService(id);
		}
		else if(e.getSource().equals(stop))
		{
			ServiceManager.stopService(id);
		}
		else if(e.getSource().equals(restart))
		{
			ServiceManager.restartService(id);
		}
	}

	public void consoleMessage(String message)
	{
		this.console.append(message + "\n");
	}

	public void updateService(ActiveServiceWrapper service)
	{
		if(!displayedServices.contains(service))
			displayedServices.addElement(service);
	}
}
