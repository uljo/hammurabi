package se.cenote.hammurabi.ui.view;

import se.cenote.hammurabi.HammurabiApp.Command;
import se.cenote.hammurabi.HammurabiApp.YearStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class CommandPanel extends StackPane{
	
	private GameView gameView;
	
	private TextField foodFld;
	private TextField seedFld;
	private TextField sellFld;
	private Button okBtn;

	public CommandPanel(GameView gameView){
		this.gameView = gameView;
		
		initComponents();
		layoutComponents();
		
		setOpacity(1);
		getStyleClass().add("borderStyle");
	}
	
	public void update(YearStatus status){
		
		int value = Math.min(status.getStoredFood(), status.getPopulation() * 20);
		//System.out.println("[update] food.value=" + value);
		foodFld.setText(Integer.toString(value));
		
		value = Math.min(status.getOwnedLand(), status.getPopulation() * 10);
		seedFld.setText(Integer.toString(value));
		
		foodFld.setTooltip(new Tooltip("Du har " + status.getStoredFood() + " att fördela."));
		seedFld.setTooltip(new Tooltip("Du har " + status.getOwnedLand() + " land att bruka."));
		sellFld.setTooltip(new Tooltip("Du har " + status.getOwnedLand() + " land att sälja."));
	}
	
	public Command getCommand(){
		int food = getIntValue(foodFld);
		int seed = getIntValue(seedFld);
		int sell = getIntValue(sellFld);
		
		//foodFld.setText("0");
		//seedFld.setText("0");
		//sellFld.setText("0");
		
		return new Command(food, seed, sell);
	}
	

	public void disable(boolean value) {
		okBtn.setDisable(value);
	}
	
	private static int getIntValue(TextField fld){
		int value = 0;
		String text = fld.getText();
		if(text != null && text.length() > 0){
			try{
				value = Integer.parseInt(text);
			}
			catch(Exception e){
				System.out.println("[getIntValue] Invalid input: " + text);
			}
		}
		return value;
	}

	private void initComponents() {
		foodFld = new TextField("0");
		foodFld.setPrefWidth(60);
		
		seedFld = new TextField("0");
		seedFld.setPrefWidth(60);
		
		sellFld = new TextField("0");
		sellFld.setPrefWidth(60);
		
		okBtn = new Button("Utför");
		okBtn.setOnAction(e->gameView.execute());
	}

	private void layoutComponents() {
		
		Pane yearHeader = buildHeaderPanel();
		
		Pane inputPanel = buildInputPane();
		
		Pane textHeader = buildHeaderTextPane();
		
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(5));
		vBox.setAlignment(Pos.TOP_CENTER);
		vBox.setSpacing(10);
		vBox.getChildren().addAll(yearHeader, textHeader, inputPanel);
		
		getChildren().add(vBox);
	}
	
	private Pane buildInputPane(){
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(40));
		grid.setVgap(20);
		grid.setHgap(20);
		
		grid.add(new Label("Antal skäppor till föda:"), 0, 0);
		grid.add(foodFld, 1, 0);
		
		grid.add(new Label("Antal tunnland att så:"), 0, 1);
		grid.add(seedFld, 1, 1);
		
		grid.add(new Label("Antal tunnland till försäljning:"), 0, 2);
		grid.add(sellFld, 1, 2);
		
		grid.add(okBtn, 0, 3, 2, 1);
		okBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		//okBtn.setPrefWidth(Double.MAX_VALUE);
		return grid;
	}
	
	private Pane buildHeaderTextPane(){
		FlowPane header = new FlowPane();
		header.setAlignment(Pos.CENTER);
		Label headerLbl = new Label("Befallning");
		headerLbl.getStyleClass().add("header");
		header.getChildren().add(headerLbl);
		return header;
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
			pane.setPrefWidth(30);
			
			yearHeader.getChildren().add(pane);
		}
		yearHeader.getStyleClass().add("borderStyle");
		return yearHeader;
	}

}
