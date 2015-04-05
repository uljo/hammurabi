package se.cenote.hammurabi;

public class AppContext {
	
	private static AppContext INSTANCE = new AppContext();
	
	private HammurabiApp app;
	
	private AppContext(){
		app = new HammurabiApp();
	}
	
	public static AppContext getInstance(){
		return INSTANCE;
	}
	
	public HammurabiApp getApp(){
		return app;
	}

}
