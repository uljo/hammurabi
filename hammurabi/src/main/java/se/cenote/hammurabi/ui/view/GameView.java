package se.cenote.hammurabi.ui.view;

import se.cenote.hammurabi.AppContext;
import se.cenote.hammurabi.HammurabiApp.Command;
import se.cenote.hammurabi.HammurabiApp.YearStatus;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameView extends BorderPane{
	
	private static final String TITLE = "HAMMURABI";
	private static final String[] IMAGES = new String[]{"uruk-city-4.jpg", "uruk-city-3.jpg", "uruk-city-2.jpg", "uriiizikkurat.jpg"};

	private ImagePanel imagePanel;
	
	private StatusPanel statusPanel;
	private CommandPanel commandPanel;
	
	private YearStatus currStatus;
	
	public GameView(){
		initComponents();
		layoutComponents();
		
		imagePanel.start();
	}
	
	/**
	 * 1. Change header image
	 * 2. Hide befallning
	 * 3. Utför befallning
	 * 4. Uppdatera ny status
	 * 
	 */
	void execute(){
		
		FadeTransition ft = new FadeTransition(new Duration(500), commandPanel);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setAutoReverse(true);
		ft.play();
		ft.setOnFinished(e->updateStatus());
		
		imagePanel.next();
	}
	
	void prepareView(){
		if(currStatus.getPopulation() <= 0 || currStatus.getYear() == 10){
			commandPanel.disable(true);
			statusPanel.showRestart(true);
			System.out.println("GAME OVER!");
		}
		else{
			showCommand();
		}
	}
	
	void showCommand(){
		
		commandPanel.update(currStatus);
		commandPanel.disable(false);
		
		FadeTransition ft = new FadeTransition(new Duration(500), commandPanel);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.setAutoReverse(true);
		ft.play();
	}
	
	private void updateStatus(){
		Command command = commandPanel.getCommand();
		
		currStatus = AppContext.getInstance().getApp().execute(command);
		
		statusPanel.update(currStatus);
	}
	
	void restart(){
		currStatus = AppContext.getInstance().getApp().start();
		statusPanel.showRestart(false);
		statusPanel.update(currStatus);
		commandPanel.disable(false);
	}
	
	private void initComponents() {
		
		/*
		int i = 0;
		for(String family : Font.getFamilies()){
			System.out.println((++i) + ". " + family);
		}
		*/
		
		imagePanel = new ImagePanel(TITLE, IMAGES);

		statusPanel = new StatusPanel(this);
		commandPanel = new CommandPanel(this);
	}

	private void layoutComponents() {
		
		setPadding(new Insets(10));

		setTop(imagePanel);
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.TOP_CENTER);
		hBox.setSpacing(5);
		hBox.getChildren().addAll(statusPanel, commandPanel);
		
		setCenter(hBox);
	}
	
	private static class ImagePanel extends StackPane{
		
		private Text title;
		private String fontFamily = "Papyrus";
		
		private String[] imageNames;
		private int imgCount;
		
		private StackPane stack;
		private ImageView imgView1;
		private ImageView imgView2;

		
		public ImagePanel(String text, String[] imageNames){
			
			this.imageNames = imageNames;
			
			title = createText(text, fontFamily);
			
			Rectangle2D viewport = new Rectangle2D(0, 0, 976, 405);
			
			imgView1 = createImageView("../../" + imageNames[0], viewport);
			imgView2 = createImageView("../../" + imageNames[1], viewport);
			
			stack = new StackPane();
			stack.getChildren().add(imgView1);
			
			getChildren().addAll(stack, title);
			
			setOpacity(1);
		}
		
		public void start(){
			fadeIn(title, 6000);
		}
		
		public void next(){
			ImageView prevView = getCurrView();
			
			ImageView nextView = getNextView();
			
			stack.getChildren().add(0, nextView);
			
			FadeTransition fadeOut = buildFadeOut(prevView, 2000);
			fadeOut.setOnFinished(e->stack.getChildren().remove(1));
			fadeOut.play();
		}
		
		private ImageView getCurrView(){
			return  imgCount%2 == 0 ? imgView1 : imgView2;
		}
		
		private ImageView getNextView(){
			ImageView nextView = imgCount%2 == 0 ? imgView2 : imgView1;
			nextView.setImage(getImage());
			nextView.setOpacity(1.0);
			return nextView;
		}
		
		private Image getImage(){
			
			if(imgCount > imageNames.length-2){
				imgCount = 0;
			}
			else{
				imgCount++;
			}
			
			String path = "../../" + imageNames[imgCount];
			Image img = new Image(getClass().getResourceAsStream(path));
			return img;
		}
		
		private static void fadeIn(Node node, long msek){
			FadeTransition ft = buildFade(node, msek, true);
			ft.play();
		}
		
		private static FadeTransition buildFadeOut(Node node, long msek){
			return buildFade(node, msek, false);
		}
		
		private static FadeTransition buildFade(Node node, long msek, boolean fadeIn){
			FadeTransition ft = new FadeTransition(Duration.millis(msek), node);
		    ft.setFromValue(fadeIn ? 0.0 : 1.0);
		    ft.setToValue(fadeIn ? 1.0 : 0.0);
		    ft.setAutoReverse(true);
		    return ft;
		}
		
		private Text createText(String text, String fontFamily){
			DropShadow ds = new DropShadow();
			ds.setOffsetY(3.0f);
			ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
			
			Text title = new Text(text);
			title.setFont(Font.font(fontFamily, FontWeight.BOLD, 115));
			title.setFill(Color.WHITE);
			title.setEffect(ds);
			title.setCache(true);
			
			return title;
		}
		
		private ImageView createImageView(String path, Rectangle2D viewport){
			
			Image img = new Image(getClass().getResourceAsStream(path));
			
			ImageView imageView = new ImageView(img);
			imageView.setSmooth(true);
			imageView.setViewport(viewport);
			return imageView;
		}
		
	}


}
