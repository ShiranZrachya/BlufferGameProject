package game;


import java.util.ArrayList;
import java.util.HashMap;



public class Game {

private ArrayList<String> gamesList;
private HashMap<String, Player> playersMap;
private HashMap<String,Room> roomMap;
private HashMap<ProtocolCallback<StringMessage>,String> callbackMap;


private static class GameHolder {
    private static Game instance = new Game();
}

private Game() {
	gamesList = new ArrayList<String>();
	gamesList.add("BLUFFER");
	playersMap = new HashMap<String, Player>();
	roomMap = new HashMap<String,Room>();
	callbackMap = new HashMap<ProtocolCallback<StringMessage>,String>();
	
}
public static Game getInstance() {
    return GameHolder.instance;
}


public synchronized void addPlayer(String name,ProtocolCallback<StringMessage> callback){
    if(!playersMap.containsKey(name)){
            Player p = new Player(name,callback);
            playersMap.put(name,p);
            callbackMap.put(callback,name);
    }
 
}

public void removePlayer(String name){
	  if(playersMap.containsKey(name)){
		  callbackMap.remove(playersMap.get(name).getCallback());
          if (playersMap.get(name).getRoom()!=null)
        	  roomMap.get(playersMap.get(name).getRoom()).removePlayer(name);
          playersMap.remove(name);
          
	  }
}
public String getName(ProtocolCallback<StringMessage> callback){
	String ans; 
    if(callbackMap.containsKey(callback)){
    	ans = callbackMap.get(callback);
    }
    else
    	ans = null;
    return ans;
}
public synchronized void  addRoom(String room){
	if(!roomMap.containsKey(room)){
		Room r = new Room(room);
		roomMap.put(room, r);
	}
	
}
public Room getRoom(String player){
	return roomMap.get(playersMap.get(player).getRoom());
	
}
public Player getPlayer (String player){
	return playersMap.get(player);
}
public void addToRoom (String player, String room){
	if (roomMap.containsKey(room) && playersMap.containsKey(player)){
		roomMap.get(room).addPlayer(playersMap.get(player));
	}
}

public boolean isExsist(ProtocolCallback<StringMessage> callback){
	if(callbackMap.containsKey(callback)){
		if(callbackMap.get(callback)!=null)
			return true;
		else return false;
	}
	return false;
}
public void start (String player, String gameType){
	roomMap.get(playersMap.get(player).getRoom()).start(gameType);
}
public String GamesList(){
	String list = "";
	for (String game : gamesList)
		list = list+game+", ";
	return list;
	
}
public HashMap<String,Room> getRoomMap() {
	return roomMap;
}
public HashMap<ProtocolCallback<StringMessage>,String> getCallbackMap(){
	return callbackMap;
}
public ProtocolCallback<StringMessage> getCallback(String name) {
    return playersMap.get(name).getCallback();
}

public boolean IsNameExsist(String name){
	if(playersMap.containsKey(name))
		return true;
	return false;
}
public boolean containsRoom(String room) {
	return roomMap.containsKey(room);
}

public boolean containsGame (String gameType){
	return gamesList.contains(gameType);
}
public void txtResp (String ans, String name) {
    if(this.getRoom(name).getCurrGameType().equals("BLUFFER")){
            ((CurrGame)this.getRoom(name).getTbgp()).getCurrentGame().GetFakeAnswers(ans, this.playersMap.get(name).getCallback());
    }
}
public void selectResp (int ans, String name) {
    if(this.getRoom(name).getCurrGameType().equals("BLUFFER")){
            ((CurrGame)this.getRoom(name).getTbgp()).getCurrentGame().sendChoise(ans, name);


    }
}



}
