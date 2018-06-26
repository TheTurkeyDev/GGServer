package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;
import com.theprogrammingturkey.ggserver.services.ServiceManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

public class ServiceTab extends Tab
{
	private TableView<ActiveServiceWrapper> table;
	private final ObservableList<ActiveServiceWrapper> displayedServices = FXCollections.observableArrayList();
	private Button start;
	private Button stop;
	private Button restart;

	@SuppressWarnings("unchecked")
	public ServiceTab(Scene scene)
	{
		this.setText("Services");
		GridPane gridPane = new GridPane();
		table = new TableView<>();
		table.setRowFactory(tv -> {
			TableRow<ActiveServiceWrapper> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY)
				{
					updateUI();
				}
			});
			return row;
		});

		TableColumn<ActiveServiceWrapper, String> nameCol = new TableColumn<>("Name");
		nameCol.setMinWidth(200);
		nameCol.setCellValueFactory(new PropertyValueFactory<ActiveServiceWrapper, String>("ServiceName"));

		TableColumn<ActiveServiceWrapper, String> idCol = new TableColumn<>("ID");
		idCol.setMinWidth(200);
		idCol.setCellValueFactory(new PropertyValueFactory<ActiveServiceWrapper, String>("ServiceID"));

		TableColumn<ActiveServiceWrapper, String> statusCol = new TableColumn<>("Status");
		statusCol.setMinWidth(200);
		statusCol.setCellValueFactory(new PropertyValueFactory<ActiveServiceWrapper, String>("ServiceStatusString"));

		table.setItems(displayedServices);
		table.getColumns().addAll(nameCol, idCol, statusCol);

		TilePane buttonPane = new TilePane();
		buttonPane.setPadding(new Insets(0, 10, 0, 10));
		buttonPane.setVgap(10);
		buttonPane.setPrefColumns(1);
		buttonPane.setAlignment(Pos.CENTER);

		start = new Button("Start");
		start.setMinWidth(100);
		start.setDisable(true);
		//TODO: Take these off of the UI Thread
		start.setOnAction(e -> {
			if(table.getSelectionModel().getSelectedItem() != null)
				ServiceManager.startService(table.getSelectionModel().getSelectedItem().getServiceID());
			updateUI();
		});
		buttonPane.getChildren().add(start);
		stop = new Button("Stop");
		stop.setMinWidth(100);
		stop.setDisable(true);
		stop.setOnAction(e -> {
			if(table.getSelectionModel().getSelectedItem() != null)
				ServiceManager.stopService(table.getSelectionModel().getSelectedItem().getServiceID());
			updateUI();
		});
		buttonPane.getChildren().add(stop);
		restart = new Button("Restart");
		restart.setMinWidth(100);
		restart.setDisable(true);
		restart.setOnAction(e -> {
			if(table.getSelectionModel().getSelectedItem() != null)
				ServiceManager.restartService(table.getSelectionModel().getSelectedItem().getServiceID());
			updateUI();
		});
		buttonPane.getChildren().add(restart);

		gridPane.add(table, 0, 0);
		gridPane.add(buttonPane, 1, 0);

		this.setContent(gridPane);
	}

	public void addService(ActiveServiceWrapper service)
	{
		displayedServices.add(service);
	}

	public void updateUI()
	{
		if(table.getSelectionModel().getSelectedItem() == null)
		{
			start.setDisable(true);
			stop.setDisable(true);
			restart.setDisable(true);
			table.refresh();
			return;
		}
		else
		{

			switch(ServiceManager.getServiceStatus(table.getSelectionModel().getSelectedItem().getServiceID()))
			{
				case RUNNING:
					start.setDisable(true);
					stop.setDisable(false);
					restart.setDisable(false);
					break;
				case STOPPED:
					start.setDisable(false);
					stop.setDisable(true);
					restart.setDisable(true);
					break;
				default:
					start.setDisable(true);
					stop.setDisable(true);
					restart.setDisable(true);
					break;
			}
			table.refresh();
		}
	}

	public void updateService(ActiveServiceWrapper service)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(!displayedServices.contains(service))
					addService(service);
				updateUI();
			}
		});
	}
}
