package Server;

/**
 * The Interface ServerProtocol.
 *
 * @param <T> the generic type
 */
public interface ServerProtocol<T> {
	
	/**
	 * Process message.
	 * processes a given message and acts accordingly 
	 * @param msg the msg
	 * @param callback the callback
	 */
	void processMessage(T msg, ProtocolCallback<T> callback);
	
	/**
	 * Checks if the given message is one assigned to be a termination message.
	 * in our case, the string 'QUIT'.
	 * @param msg the msg
	 * @return true, if is end
	 */
	boolean isEnd(T msg);
	
}
