package Reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

import Server.*;
import Tokenizer.*;

/**
 * This class supplies some data to the protocol, which then processes the data,
 * possibly returning a reply. This class is implemented as an executor task.
 * 
 */
public class ProtocolTask<T> implements Runnable {

	private final ServerProtocol<T> _protocol;
	private final MessageTokenizer<T> _tokenizer;
	private final ConnectionHandler<T> _handler;
	private final ProtocolCallback<String> callback;


	public ProtocolTask(final ServerProtocol<T> protocol, final MessageTokenizer<T> tokenizer, final ConnectionHandler<T> h) {
		this._protocol = protocol;
		this._tokenizer = tokenizer;
		this._handler = h;
		callback = new ProtocolCallback<String>() { 
			@Override
			public void sendMsg(String msg) throws IOException {
				StringMessage strMsg = new StringMessage(msg);
				ByteBuffer buff = ((FixedSeparatorMessageTokenizer)_tokenizer).getBytesForMessage(strMsg);
				_handler.addOutData(buff);
				
			}
		};
	}

	// we synchronize on ourselves, in case we are executed by several threads
	// from the thread pool.
	public synchronized void run() {
      // go over all complete messages and process them.
      while (_tokenizer.hasMessage()) {
         StringMessage msg = ((FixedSeparatorMessageTokenizer)_tokenizer).nextMessage();
         ((TBGPProtocol)this._protocol).processMessage(msg.getMessage() , callback);
      }
	}

	public void addBytes(ByteBuffer b) {
		_tokenizer.addBytes(b);
	}

	public ProtocolCallback<String> getCallback() {
		return callback;
	}
}
