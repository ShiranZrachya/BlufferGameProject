package reactor.protocol;

public interface ServerProtocolFactory<T> {
   AsyncServerProtocol<T> create();
}
