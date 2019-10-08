package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.news.NewsHolder;
import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UIJavaFx extends Application implements IUI
{
	private ConsoleTab consoleTab;
	private NewsTab newsTab;
	private ServiceTab serviceTab;

	public static void init(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		UICore.setUIIstance(this);
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
		System.exit(0);
	}

	@Override
	public void consoleMessage(String message)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(consoleTab != null)
					consoleTab.console.write(message + "\n");
			}
		});
	}

	@Override
	public void dispatchNews(NewsHolder news)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				newsTab.dispatchNews(news);
			}
		});
	}

	@Override
	public void updateService(ActiveServiceWrapper service)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				serviceTab.updateService(service);
			}
		});
	}
}