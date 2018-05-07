package Server;
/**
 * A factory for creating ServerProtocol objects.
 */
public interface ServerProtocolFactory {
   
   /**
    * Creates the ServerProtocol.
    *
    * @return the server protocol
    */
   ServerProtocol<?> create();
}
