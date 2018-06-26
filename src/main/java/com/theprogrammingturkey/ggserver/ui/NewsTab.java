package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.news.INewsData;
import com.theprogrammingturkey.ggserver.news.NewsHolder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class NewsTab extends Tab
{
	private ObservableList<NewsHolder> newsList;

	public NewsTab(Scene scene)
	{
		this.setText("News");

		GridPane screenSplit = new GridPane();
		screenSplit.setHgap(2);
		screenSplit.setPadding(new Insets(10, 10, 10, 10));

		newsList = FXCollections.observableArrayList(new NewsHolder(null, new TestData()), new NewsHolder(null, new TestData()));
		ListView<NewsHolder> listView = new ListView<>(newsList);
		listView.setCellFactory(new Callback<ListView<NewsHolder>, ListCell<NewsHolder>>()
		{
			@Override
			public ListCell<NewsHolder> call(ListView<NewsHolder> param)
			{
				return new NewsListItem();
			}
		});

		screenSplit.add(listView, 0, 0);

		this.setContent(screenSplit);
	}

	public void dispatchNews(NewsHolder news)
	{
		newsList.add(news);
	}

	static class TestData implements INewsData
	{

		@Override
		public String getTitle()
		{
			return "test";
		}

		@Override
		public String getDesc()
		{
			return "HELLO";
		}

		@Override
		public String getData()
		{
			return "DAta";
		}

		@Override
		public String getServiceID()
		{
			return "NULL";
		}

	}
}
