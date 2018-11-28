package ServerThreadPerClient.protocol;
import java.io.*;
import java.net.*;


public class EchoProtocolFactory implements ServerProtocolFactory {
	public ServerProtocol create(){
		return new ThreadPerClientProtocol();
	}
}

