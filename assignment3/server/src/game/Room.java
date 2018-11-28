package game;

import java.io.IOException;
import java.util.HashMap;



public class Room {
	private TBGP tbgp;
	private String name;
	private String currGameType;
    private boolean isActive;
    private HashMap<String,Player> playersMap;
    private HashMap<String,Integer> scoreForPlayer;
    
   

    public Room(String name) {
            this.name = name;
            setActive(false);
            playersMap = new HashMap<String,Player>();
            scoreForPlayer = new HashMap<String,Integer>();
    }

	public String getName() {
		return name;
	}

	public String getCurrGameType() {
		return currGameType;
	}

	public void setCurrGameType(String currGameType) {
		this.currGameType = currGameType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public HashMap<String,Player> getPlayersMap() {
		return playersMap;
	}

	public HashMap<String,Integer> getScoreForPlayer() {
		return scoreForPlayer;
	}

	public void removePlayer(String player){
		if(playersMap.containsKey(player))
			playersMap.get(player).leaveCurrRoom();
			playersMap.remove(player);
			scoreForPlayer.remove(player);
			
			
	}
	public void addPlayer (Player player){
		if (!playersMap.containsKey(player.getName()) && !isActive){
			playersMap.put(player.getName(), player);
			scoreForPlayer.put(player.getName(), 0);
			player.setRoom(this.name);
			try {
				Game.getInstance().getCallback(player.getName()).sendMessage(new StringMessage("SYSMSG JOIN ACCEPTED"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(isActive){
			try {
				Game.getInstance().getCallback(player.getName()).sendMessage(new StringMessage("SYSMSG JOIN REJECTED GAME ALREADY STARTED"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			try {
				Game.getInstance().getCallback(player.getName()).sendMessage(new StringMessage("SYSMSG JOIN REJECTED PLAYER IS ALLREADY IN THE ROOM"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
	}
	public void start(String gameType){
		this.isActive = true;
		this.setCurrGameType(gameType);
		for (String nick : playersMap.keySet()){
			playersMap.get(nick).setIsPlaying(true);
			
		}
		if (gameType.equals("BLUFFER")){
			tbgp = new CurrGame(this);
			
	
		}
		
	}
	public void endGame(){
		this.setActive(false);
		for(String name : scoreForPlayer.keySet())
			scoreForPlayer.put(name, 0);
		for(String name : playersMap.keySet())
			playersMap.get(name).setIsPlaying(false);
		tbgp = null;
		currGameType = null;
		
		
	}
	public void sendMess(String player, StringMessage stringMessage){
		for (String name : playersMap.keySet()){
			if (!name.equals(player)){
				try {
					playersMap.get(name).getCallback().sendMessage(stringMessage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public void updateScore (int points, String player){
		if (scoreForPlayer.containsKey(player)){
			int toAdd = scoreForPlayer.get(player);
			scoreForPlayer.put(player, toAdd+points);
		}
	}
	
	public String Summary(){
		String summary = "Summary: ";
		for (String player : scoreForPlayer.keySet()){
			summary = summary+player+": "+scoreForPlayer.get(player)+" points, ";
		}
		return summary;
	}
	
	public int getNumOfPlayers(){
		return playersMap.size();
	}
	public TBGP getTbgp(){
		return tbgp;
	}





}
