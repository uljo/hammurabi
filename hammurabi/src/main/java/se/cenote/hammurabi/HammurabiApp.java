package se.cenote.hammurabi;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import se.cenote.hammurabi.ui.HammurabiDisplay;


public class HammurabiApp {
	
	private GameEngine engine;
	
	public static void main(String[] args){
		AppContext.getInstance().getApp();
		HammurabiDisplay.show();
	}

	public HammurabiApp(){
		engine = new GameEngine();
	}
	

	public YearStatus start() {
		engine.restart();
		
		return getFirstYearStatus();
	}
	
	public int getCurrYear(){
		return engine.getCurrYear();
	}
	
	public YearStatus getFirstYearStatus(){
		return engine.getFirstYearStatus();
	}

	public YearStatus execute(Command command) {
		
		return engine.getNextStatus(command);
	}
	
	public static class Command{
		
		private int food;
		private int cult;
		private int sell;
		
		public Command(int food, int cult, int sell) {
			this.food = food;
			this.cult = cult;
			this.sell = sell;
		}
		public int getFood() {
			return food;
		}
		public int getCultivate() {
			return cult;
		}
		public int getSell() {
			return sell;
		}
	}
	
	public static class YearStatus{
		
		private int year;
		
		// People
		private int population;
		
		private int immigrants;
		private int starved;
		private int plagueDeaths;
		
		// Food
		private int storedFood;
		
		private int harvested;
		private int harvestRatio;
		private int eatenByRats;
		
		// Land
		private int ownedLand;
		private int landPrice;
		
		
		public YearStatus(int year, int population,
				int immigrants, int starved, int plagueDeaths, int storedFood, int harvested,
				int harvestRatio, int eatenByRats, int ownedLand, int landPrice) {
			this.year = year;
			this.population = population;
			this.immigrants = immigrants;
			this.starved = starved;
			this.plagueDeaths = plagueDeaths;
			this.storedFood = storedFood;
			this.harvested = harvested;
			this.harvestRatio = harvestRatio;
			this.eatenByRats = eatenByRats;
			this.ownedLand = ownedLand;
			this.landPrice = landPrice;
		}

		public int getYear() {
			return year;
		}

		public int getPopulation() {
			return population;
		}

		public int getImmigrants() {
			return immigrants;
		}

		public int getStarved() {
			return starved;
		}

		public int getPlagueDeaths() {
			return plagueDeaths;
		}

		public int getStoredFood() {
			return storedFood;
		}
		
		public int getHarvested(){
			return harvested;
		}

		public int getHarvestRatio() {
			return harvestRatio;
		}

		public int getEatenByRats() {
			return eatenByRats;
		}

		public int getOwnedLand() {
			return ownedLand;
		}

		public int getLandPrice() {
			return landPrice;
		}
		
		
		public String getStatusText(){
			String text = "O, great Hammurabi!\n\n"
				+ "You are in year " + year + " of your ten year rule.\n"
				+ "In the previous year " + starved + " people starved to death.\n"
				+ "There were " + plagueDeaths + " deaths from the plague.\n"
				+ "In the previous year " + immigrants + " people entered the kingdom.\n"
				+ "The population is now " + population + ".\n\n"
				+ "We harvested " + harvested + " bushels at " + harvestRatio + " bushels per acre.\n"
				+ "Rats destroyed " + eatenByRats + " bushels, leaving " + storedFood + " bushels in storage.\n"
				+ "The city owns " + ownedLand + " acres of land.\n"
				+ "Land is currently worth " + landPrice + " bushels per acre.";
			
			String textSE = "O, Store Hammurabi!\n\n"
					+ "Efter " + year + " år av din tio-åriga regim.\n"
					+ "Förra året svalt " + starved + " undersåtar till döds.\n"
					+ plagueDeaths + " undersåtar föll offer för pesten.\n"
					+ "Förra året invandrade " + immigrants + " nya undersåtar till ditt kungadöma.\n"
					+ "Din befolkning är nu " + population + ".\n\n"
					+ "Vi skördade " + harvested + " skäppor, " + harvestRatio + " skäppor per tunnland.\n"
					+ "Råttor förstörde " + eatenByRats + " skäppor, återstående " + storedFood + " skäppor lagras i ert förråd.\n"
					+ "Staden äger " + ownedLand + " tunnland mark.\n"
					+ "Mark kostar för närvarande " + landPrice + " skäppor per tunnland.";
				
			
			if(population <= 0)
				textSE = "Alla dina undersåtar svalt ihjäl under året.\nDu är en undermålig härskare.";
			
			return textSE;
		}
	}

	private static class GameEngine{
		

		private Random random;
		
		private int currYear;
		
		private List<YearStatus> yearStatus;
		
		// People
		private int population;
		private int starved;
		private int plagueDeaths;
		private int immigrants;
		
		// Food
		private int harvested;
		private int harvestRatio;
		private int eatenByRats;
		private int storedFood;
		
		// Land
		private int ownedLand;
		private int landPrice;
		
		public GameEngine(){
			restart();
		}
		
		public void restart(){
			
			random = new Random();
			
			yearStatus = new ArrayList<YearStatus>();
			
			currYear = 0;
			population = 100;
			immigrants = 5;
			plagueDeaths = 0;
			
			harvested = 3000;
			harvestRatio = 3;
			eatenByRats = 200;
			storedFood = 2800;
			
			ownedLand = 1000;
			landPrice = 19;
			
		}
		
		public int getCurrYear() {
			return currYear;
		}

		public YearStatus getFirstYearStatus(){

			YearStatus status = new YearStatus(currYear, population,immigrants, starved, plagueDeaths, 
					storedFood, harvested, harvestRatio, eatenByRats, ownedLand, landPrice);
			
			yearStatus.add(status);
			
			return status;
		}
		
		public YearStatus getNextStatus(Command command){
			
			currYear++;
			
			YearStatus prevStatus = yearStatus.get(yearStatus.size()-1);
			
			// People
			int foodCost = command.getFood();
			starved = prevStatus.getPopulation() - (foodCost/20);
			plagueDeaths = isPlauge() ? prevStatus.getPopulation()/2 : 0;
			
			immigrants = getImmigrants(ownedLand, storedFood, prevStatus.getPopulation());
			
			population = prevStatus.getPopulation() - starved - plagueDeaths + immigrants;
			
			
			// Land
			landPrice = getLandPrice();
			ownedLand = prevStatus.getOwnedLand() + command.getSell();
			
			int landExpense = command.getSell() * landPrice;
			if(command.getSell() > 0){
				// Vi har köpt land
				landExpense *= -1;
			}
			
			
			// Food
			int consumed = command.getFood();
			int cult = command.getCultivate();
			int cultCost = cult * 2;
			
			storedFood -= consumed + cultCost + landExpense;
			
			
			harvestRatio = getHarvestRatio();
			harvested = population <= 0 ? 0 : cult * harvestRatio;
			storedFood +=  harvested;
			
			eatenByRats = getRatEaten(storedFood);
			storedFood -= eatenByRats;
			
			// check we don´t get negative value
			storedFood = Math.max(0, storedFood);
			
			
			YearStatus newStatus = new YearStatus(currYear, population,immigrants, starved, plagueDeaths, 
						storedFood, harvested, harvestRatio, eatenByRats, ownedLand, landPrice);

			yearStatus.add(newStatus);
			
			//print status
			System.out.println("[getNextStatus] year: " + currYear + ", pop: " + population + ", food: " + storedFood + ", land: " + ownedLand);
			
			return newStatus;
		}
		
		private boolean isPlauge(){
			int value = random.nextInt(100);
			return value <= 15;
		}
		
		private int getRatEaten(int food){
			float eaten = 0;
			int value = random.nextInt(100);
			boolean isRatYear = value <= 40;
			if(isRatYear){
				value = random.nextInt(3) + 1;
				eaten = food * value/10.0f;
			}
			
			return (int)eaten;
		}
		
		private int getHarvestRatio(){
			return  random.nextInt(8) + 1;
		}
		
		private int getLandPrice(){
			return random.nextInt(7) + 17;
		}
		
		private int getImmigrants(int land, int food, int population){
			if(population > 0)
				return (20 * land + food) / (100 * population) + 1;
			else
				return 0;
		}
		
	}

	
}
