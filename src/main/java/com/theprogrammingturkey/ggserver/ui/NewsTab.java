package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.news.NewsHolder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class NewsTab extends Tab
{
	private ObservableList<NewsHolder> newsList;
	private VBox details;

	public NewsTab(Scene scene)
	{
		this.setText("News");

		GridPane screenSplit = new GridPane();
		screenSplit.setHgap(2);
		screenSplit.setPadding(new Insets(10, 10, 10, 10));

		newsList = FXCollections.observableArrayList();
		ListView<NewsHolder> listView = new ListView<>(newsList);
		listView.setPrefWidth(scene.getWidth() / 2);
		listView.setCellFactory(new Callback<ListView<NewsHolder>, ListCell<NewsHolder>>()
		{
			@Override
			public ListCell<NewsHolder> call(ListView<NewsHolder> param)
			{
				return new NewsListItem();
			}
		});

		screenSplit.add(listView, 0, 0);

		details = new VBox();
		details.setPrefWidth(scene.getWidth() / 2);
		details.setSpacing(5);
		details.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<NewsHolder>()
		{
			@Override
			public void changed(ObservableValue<? extends NewsHolder> observable, NewsHolder oldValue, NewsHolder newValue)
			{
				if(newValue != null)
				{
					Text title = new Text(newValue.getNews().getTitle());
					title.setFont(new Font(24));
					details.getChildren().add(title);
					details.setAlignment(Pos.TOP_CENTER);
					Text body = new Text(newValue.getNews().getData());
					body.setFont(new Font(15));
					details.getChildren().add(body);
				}
			}
		});

		screenSplit.add(details, 1, 0);

		this.setContent(screenSplit);
	}

	public void dispatchNews(NewsHolder news)
	{
		newsList.add(news);
	}
}
