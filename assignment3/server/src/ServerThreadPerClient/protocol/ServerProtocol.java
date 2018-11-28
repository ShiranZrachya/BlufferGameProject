package ServerThreadPerClient.protocol;
import java.io.*;
import java.net.*;
import game.*;

public interface ServerProtocol<T> {
	
	void processMessage(T msg,ProtocolCallback<T> callback);
	
	boolean isEnd(T msg);
	
}
