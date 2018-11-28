package ServerThreadPerClient.protocol;
import java.io.*;
import java.net.*;

public interface ServerProtocolFactory {
	   ServerProtocol create();
}