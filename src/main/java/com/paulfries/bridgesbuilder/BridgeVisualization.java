package com.paulfries.bridgesbuilder;

import java.util.List;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BridgeVisualization extends Application {

	private final int WIDTH = 1024;
	private final int HEIGHT = 1024;

	private final int MARGIN_LEFT = 50;
	private final int MARGIN_TOP = 50;

	private final int CELL_SIZE = 30;

	private static char[][] layout = (new BridgesBuilder()).handOverLayout();
	@Override
	public void start(Stage stage) {

		// Create JavaFx
		GridPane root = new GridPane();
		Scene scene = new Scene(root, 2*WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

		Pane paneLeft = new Pane();
		paneLeft.setMaxSize(WIDTH, HEIGHT);

		Pane paneRight = new Pane();
		paneRight.setMaxSize(WIDTH, HEIGHT);

		root.add(paneLeft, 0, 0);
		root.add(paneRight, 1, 0);
		
		renderLayout(layout, paneLeft, true);
		renderLayout(layout, paneRight, false);
	}

	public static void main(String[] args) {
		launch();
	}

	public void renderLayout(char[][] layout, Pane pane, boolean renderBridges) {

		List<Character> bridges = List.of(BridgesBuilder.VERTICAL1, BridgesBuilder.VERTICAL2, BridgesBuilder.HORIZONTAL1, BridgesBuilder.HORIZONTAL2);

		for(int x=0; x<layout.length; x++) {
			for(int y=0; y<layout[x].length; y++) {
				
				char value = layout[x][y];
				
				// Skip bridge, if not renderBridges
				if(!renderBridges && bridges.contains(value)) continue;
				
				switch(value) {
				case BridgesBuilder.ISLAND:
				{
					Circle circle1 = new Circle(MARGIN_LEFT+x*CELL_SIZE, MARGIN_TOP+y*CELL_SIZE, CELL_SIZE/2);
					pane.getChildren().add(circle1);
					Circle circle2 = new Circle(MARGIN_LEFT+x*2*CELL_SIZE, MARGIN_TOP+y*CELL_SIZE, CELL_SIZE/2);
					pane.getChildren().add(circle2);
					break;
				}
				case BridgesBuilder.VERTICAL1:
				{
					// x, y, width, height
					Rectangle rectangle = new Rectangle(MARGIN_LEFT+x*CELL_SIZE-1, MARGIN_TOP+y*CELL_SIZE-CELL_SIZE/2,3, CELL_SIZE);
					pane.getChildren().add(rectangle);
					break;
				}
				case BridgesBuilder.HORIZONTAL1:
				{
					Rectangle rectangle = new Rectangle(MARGIN_LEFT+x*CELL_SIZE-CELL_SIZE/2, MARGIN_TOP+CELL_SIZE*y-1, CELL_SIZE,3);
					pane.getChildren().add(rectangle);
					break;
				}
				case BridgesBuilder.VERTICAL2:
				{
					// x, y, width, height
					Rectangle rectangle = new Rectangle(MARGIN_LEFT+x*CELL_SIZE-3, MARGIN_TOP+y*CELL_SIZE-CELL_SIZE/2, 7, CELL_SIZE);
					pane.getChildren().add(rectangle);
					break;
				}
				case BridgesBuilder.HORIZONTAL2:
				{
					Rectangle rectangle = new Rectangle(MARGIN_LEFT+x*CELL_SIZE-CELL_SIZE/2, MARGIN_TOP+CELL_SIZE*y-3, CELL_SIZE, 7);
					pane.getChildren().add(rectangle);
					break;
				}
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				{
					Circle circle = new Circle(MARGIN_LEFT+x*CELL_SIZE, MARGIN_TOP+y*CELL_SIZE, CELL_SIZE/2);
					pane.getChildren().add(circle);
					Text text = new Text(MARGIN_LEFT+x*CELL_SIZE-CELL_SIZE/4,MARGIN_TOP+y*CELL_SIZE, Character.toString(value));
					text.setFill(Color.WHITE);
					text.setTextOrigin(VPos.CENTER);
					pane.getChildren().add(text);
					break;
				}
				default:
				}
			}
		}
	}
}