package ServerThreadPerClient.protocol;


import java.io.IOException;

import game.Game;
import game.ProtocolCallback;
import game.StringMessage;

public class ThreadPerClientProtocol implements ServerProtocol<StringMessage> { 


	@Override
	public void processMessage(StringMessage msg, ProtocolCallback<StringMessage> callback) {
		// TODO Auto-generated method stub
		
		if (msg.getMessage().startsWith("NICK")){           
			String name=null;
			if(msg.getMessage().length()>"Nick ".length())
				name=msg.getMessage().substring(5);
			if(!Game.getInstance().isExsist(callback)&&name!=null)
				if(!Game.getInstance().IsNameExsist(name)){
					Game.getInstance().addPlayer(name, callback);
					try {
						callback.sendMessage(new StringMessage("SYSMSG NICK "+name+" ACCEPTED"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else{
					try {
						callback.sendMessage(new StringMessage("SYSMSG NICK "+name+ "ALREADY EXSIST"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			else{
				try {
					callback.sendMessage(new StringMessage("SYSMSG YOU ARE ALREADY GOT A NICK"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(msg.getMessage().startsWith("JOIN")){
			String room=null;
			String name= Game.getInstance().getName(callback);
				if(msg.getMessage().length()>"JOIN ".length()){
					room= msg.getMessage().substring(5);
				} else
					try {
						callback.sendMessage(new StringMessage("SYSMSG ROOM NAME WAS NOT ENTERED"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
            if(Game.getInstance().isExsist(callback)){
            	if(Game.getInstance().getRoom(name)!=null && !Game.getInstance().getPlayer(name).isPlaying())
                    Game.getInstance().getRoom(name).removePlayer(name);
                    if(Game.getInstance().containsRoom(room)){
                    	Game.getInstance().addToRoom(name, room);
                     }
                     else{
                    	 Game.getInstance().addRoom(room);
                    	 Game.getInstance().addToRoom(name, room);
                         }
            } else
				try {
					callback.sendMessage(new StringMessage("SYSMSG ENTER NICK BEFORE JOIN"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else if(msg.getMessage().startsWith("MSG")){
                if(Game.getInstance().isExsist(callback) && Game.getInstance().getPlayer(Game.getInstance().getName(callback)).isPlaying() && msg.getMessage().length()>"MSG ".length()){
                	 String name = Game.getInstance().getName(callback);
                	 Game.getInstance().getRoom(name).sendMess(name,new StringMessage("USERMSG "+name+": "+msg.getMessage().substring(4)));
                } else
					try {
						callback.sendMessage(new StringMessage("SYSMSG REQUEST REJECTED"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        }
		else if (msg.getMessage().startsWith("STARTGAME")){
            if(msg.getMessage().length()>"STARTGAME ".length() && Game.getInstance().isExsist(callback)){
            	
            	if(!Game.getInstance().getPlayer(Game.getInstance().getName(callback)).isPlaying() && Game.getInstance().containsGame(msg.getMessage().substring(10))){
            	
            		if(!Game.getInstance().getRoom(Game.getInstance().getName(callback)).isActive()){
                    	try {
                    		
							callback.sendMessage(new StringMessage("SYSMSG START GAME ACCEPTED"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	String name = Game.getInstance().getName(callback);
                    	Game.getInstance().start(name, msg.getMessage().substring(10));
                    
            		}
            	}
            } else
				try {
					callback.sendMessage(new StringMessage("SYSMSG STARTGAME REJECTED"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
            else if(msg.getMessage().startsWith("LISTGAMES")){
                if(Game.getInstance().isExsist(callback)&& !(Game.getInstance().getPlayer(Game.getInstance().getName(callback)).isPlaying()) && Game.getInstance().getRoom(Game.getInstance().getName(callback)).isActive())
					try {
						callback.sendMessage(new StringMessage("SYSMSG THE GAMES ARE: "+Game.getInstance().GamesList()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				else
					try {
						callback.sendMessage(new StringMessage ("SYSMSG LISTGAMES REJECTED"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
            else if(msg.getMessage().startsWith("TXTRESP")){
                if(msg.getMessage().substring(6).length()>2 && Game.getInstance().isExsist(callback)&& Game.getInstance().getPlayer(Game.getInstance().getName(callback)).isPlaying()&& Game.getInstance().getRoom(Game.getInstance().getName(callback)).isActive()){
                    Game.getInstance().txtResp(msg.getMessage().substring(8).toLowerCase(),Game.getInstance().getName(callback));
                }
                else
                	Game.getInstance().getRoom(Game.getInstance().getName(callback)).sendMess(Game.getInstance().getName(callback), new StringMessage("SYSMSG TXTRESP REJECTED"));    
        }
            else if(msg.getMessage().startsWith("SELECTRESP")){	
                if(msg.getMessage().substring(11).length()>0 && Game.getInstance().isExsist(callback)&&  Game.getInstance().getPlayer(Game.getInstance().getName(callback)).isPlaying()&& Game.getInstance().getRoom(Game.getInstance().getName(callback)).isActive()){
                	int choice = -1;
                    try{
                    	choice= new Integer(msg.getMessage().substring(11));
                    }
                    catch(NumberFormatException e){
                    	
                           
                    }
                    if(choice>-1 && choice<=Game.getInstance().getRoom(Game.getInstance().getName(callback)).getNumOfPlayers() )
                    	Game.getInstance().selectResp(choice, Game.getInstance().getName(callback));
                    else{
                    	Game.getInstance().getRoom(Game.getInstance().getName(callback)).sendMess(Game.getInstance().getName(callback),new StringMessage("SYSMSG SELECTRESP REJECTED "));
                    }
                    
                }
                else
                	Game.getInstance().getRoom(Game.getInstance().getName(callback)).sendMess(Game.getInstance().getName(callback), new StringMessage("SYSMSG SELECTRESP REJECTED")); 
                    
            }	
        
        else if(msg.getMessage().startsWith("QUIT")){
        	if(Game.getInstance().isExsist(callback) && Game.getInstance().getRoom(Game.getInstance().getName(callback)).isActive() && Game.getInstance().getPlayer(Game.getInstance().getName(callback)).isPlaying()){
        		try {
					callback.sendMessage(new StringMessage("SYSMSG QUIT REJECTED"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
            else{
            	Game.getInstance().removePlayer(Game.getInstance().getName(callback));
            	try {
					callback.sendMessage(new StringMessage("SYSMSG QUIT ACCEPTED"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                }
        } else{
			try {
				callback.sendMessage(new StringMessage("NOT SUPPORTED"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

	}

	@Override
	public boolean isEnd(StringMessage msg) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
