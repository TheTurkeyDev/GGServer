package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.news.NewsHolder;
import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UICore extends Application
{
	private static UICore instance;
	private ConsoleTab consoleTab;
	private NewsTab newsTab;
	private ServiceTab serviceTab;

	public static void init(String[] args)
	{
		launch(args);
	}

	public UICore()
	{
		instance = this;
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Group root = new Group();

		Scene scene = new Scene(root, 800, 400);
		TabPane tabPane = new TabPane();
		BorderPane borderPane = new BorderPane();

		/*
		 * Console Tab
		 */

		tabPane.getTabs().add((consoleTab = new ConsoleTab(scene)));

		/*
		 * News Tab
		 */
		tabPane.getTabs().add((newsTab = new NewsTab(scene)));

		/*
		 * Services Tab
		 */
		tabPane.getTabs().add((serviceTab = new ServiceTab(scene)));

		// bind to take available space
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());

		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);

		scene.setFill(Color.DIMGRAY);

		// Setting the title to Stage.
		primaryStage.setTitle("Gobble Google Server");

		// Setting the scene to Stage
		primaryStage.setScene(scene);

		// Displaying the stage
		primaryStage.show();
	}

	@Override
	public void stop()
	{
		ServerCore.StopServer();
	}

	public static void consoleMessage(String message)
	{
		instance.consoleTab.console.write(message + "\n");
	}

	public static void dispatchNews(NewsHolder news)
	{
		instance.newsTab.dispatchNews(news);
	}

	public static void updateService(ActiveServiceWrapper service)
	{
		instance.serviceTab.updateService(service);
	}
}
