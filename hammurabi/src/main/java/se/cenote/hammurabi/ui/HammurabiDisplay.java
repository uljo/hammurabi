package se.cenote.hammurabi.ui;

import java.net.URL;

import se.cenote.hammurabi.ui.view.GameView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class HammurabiDisplay extends Application{
	
	private int width = 1000;
	private int height = 800;
	private String title = "Hammurabi"; 
	
	public static void show(){
		HammurabiDisplay.launch((String[])null);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Parent node = new GameView();
		
		Scene scene = new Scene(node, width, height);
		
		String cssPath = "se/cenote/hammurabi/ui/application.css";
		scene.getStylesheets().add(cssPath);
		
		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
		
		
		playSound();
		
	}
	
	private void playSound2(){
		URL resource = getClass().getResource("../meso-tune.mp3");
		AudioClip clip = new AudioClip(resource.toString());
		clip.setCycleCount(AudioClip.INDEFINITE);
		clip.play();
	} 
	
	
	private void playSound(){
		URL resource = getClass().getResource("../meso-tune.mp3");
	    Media media = new Media(resource.toString());
	    MediaPlayer mediaPlayer = new MediaPlayer(media);
	    mediaPlayer.setMute(true);
	    mediaPlayer.setOnReady(new Runnable() {
			@Override
			public void run() {
				mediaPlayer.play();
			}
		});
	    
	}

}
