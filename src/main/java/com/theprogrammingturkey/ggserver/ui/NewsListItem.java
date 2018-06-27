package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.news.NewsHolder;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NewsListItem extends ListCell<NewsHolder>
{
	// private NewsHolder lastItem;

	public NewsListItem()
	{
		super();
	}

	@Override
	protected void updateItem(NewsHolder item, boolean empty)
	{
		super.updateItem(item, empty);
		setText(null); // No text in label of super class
		if(empty)
		{
			// lastItem = null;
			setGraphic(null);
		}
		else
		{
			// lastItem = item;

			GridPane newsItem = new GridPane();
			newsItem.setPadding(new Insets(5));
			newsItem.setHgap(5);
			// TODO: Switch to some sort of icon or image
			// ImageView icon = new ImageView();
			// icon.
			Circle circle = new Circle(0, 0, 10);
			newsItem.add(circle, 0, 0);

			VBox titleBody = new VBox();
			titleBody.setSpacing(2);
			Text title = new Text(item.getNews().getTitle());
			title.setFont(new Font(18));
			titleBody.getChildren().add(title);
			Text body = new Text(item.getNews().getDesc());
			titleBody.getChildren().add(body);
			newsItem.add(titleBody, 1, 0);

			setGraphic(newsItem);
		}
	}
}
