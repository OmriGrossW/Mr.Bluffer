package Server;
import java.io.IOException;

/**
 * The Interface ProtocolCallback.
 *
 * @param <T> the generic type
 */
public interface ProtocolCallback<T> {
	
	/**
	 * Send msg.
	 * the method used to communicate between server and client and vice-versa
	 * @param msg the msg 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void sendMsg(T msg) throws IOException;
}
