package se.cenote.hammurabi.ui.view;

import se.cenote.hammurabi.AppContext;
import se.cenote.hammurabi.HammurabiApp.YearStatus;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class StatusPanel extends StackPane{
	
	private GameView gameView;
	
	private Label yearLbl;
	private Text text;
	
	private Button startBtn;

	public StatusPanel(GameView gameView){
		this.gameView = gameView;
		initComponents();
		layoutComponents();
	}
	
	public void update(YearStatus yearStatus) {
		int year = yearStatus.getYear();
		String txt = yearStatus.getStatusText();
		
		yearLbl.setText("År " + Integer.toString(year));
		
		text.setText(txt);
		
		final Animation animation = new Transition() {
	        {
	            setCycleDuration(Duration.millis(2000));
	        }
	    
	        protected void interpolate(double frac) {
	            final int length = txt.length();
	            final int n = Math.round(length * (float) frac);
	            text.setText(txt.substring(0, n));
	        }
	    
	    };
	    animation.setOnFinished(e->gameView.prepareView());
	    animation.play();
	}
	
	public void showRestart(boolean value){
		startBtn.setVisible(value);
	}

	private void restart(){
		gameView.restart();
	}
	
	private void initComponents() {
		yearLbl = new Label("År 0");
		yearLbl.getStyleClass().add("header");
		
		String txt = AppContext.getInstance().getApp().getFirstYearStatus().getStatusText();
		
		text = new Text(txt);
		text.setWrappingWidth(400);
		
		startBtn = new Button("Starta om");
		startBtn.setOnAction(e->restart());
		startBtn.setVisible(false);
	}

	private void layoutComponents() {
		
		Pane yearHeader = buildHeaderPanel();
		
		FlowPane header = new FlowPane();
		header.setAlignment(Pos.CENTER);
		header.getChildren().add(yearLbl);
		
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(5));
		vBox.setPrefWidth(500);
		vBox.setAlignment(Pos.TOP_CENTER);
		vBox.setSpacing(10);
		vBox.getChildren().addAll(yearHeader, yearLbl, text, startBtn);
		
		vBox.getStyleClass().add("borderStyle");
		
		getChildren().add(vBox);
	}
	
	private Pane buildHeaderPanel(){
		TilePane yearHeader = new TilePane();
		yearHeader.setAlignment(Pos.CENTER);
		for(int i = 0; i < 10; i++){
			
			Label lbl = new Label(Integer.toString(i));
			lbl.getStyleClass().add("header");
			//lbl.setPrefWidth(Double.MAX_VALUE);
			
			FlowPane pane = new FlowPane();
			pane.setAlignment(Pos.CENTER);
			pane.getChildren().add(lbl);
			pane.setPrefWidth(40);
			
			yearHeader.getChildren().add(pane);
		}
		yearHeader.getStyleClass().add("borderStyle");
		return yearHeader;
	}

}
