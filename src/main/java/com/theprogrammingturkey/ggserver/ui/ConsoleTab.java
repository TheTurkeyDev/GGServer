package com.theprogrammingturkey.ggserver.ui;

import java.io.PrintStream;

import com.theprogrammingturkey.ggserver.commands.CommandManager;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

public class ConsoleTab extends Tab
{
	public ConsoleArea console;

	public ConsoleTab(Scene scene)
	{
		// TODO: Beautify!
		this.setText("Console");
		GridPane consolePane = new GridPane();
		consolePane.setVgap(10);
		consolePane.setPadding(new Insets(10, 10, 10, 10));

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefHeight(scene.getHeight() - 30);
		scrollPane.setPrefWidth(scene.getWidth());
		scrollPane.setPadding(new Insets(10, 10, 10, 10));
		TextArea consoleText = new TextArea();
		consoleText.setPrefHeight(scene.getHeight() - 30);
		consoleText.setPrefWidth(scene.getWidth());
		scrollPane.setContent(consoleText);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);

		TextField consoleInput = new TextField();
		consoleInput.setPrefWidth(scene.getWidth());
		consoleInput.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.ENTER)
			{
				CommandManager.processCommand(consoleInput.getText());
				consoleInput.setText("");
			}
		});

		console = new ConsoleArea(consoleText);
		PrintStream ps = new PrintStream(console, true);
		System.setOut(ps);
		System.setErr(ps);

		consolePane.add(consoleText, 0, 0);
		consolePane.add(consoleInput, 0, 1);
		this.setContent(consolePane);
	}
}
