package Server;
import java.io.*;
import java.net.*;

import Reactor.Reactor;





class ConnectionHandler implements Runnable {
	
	private BufferedReader in=null;
	private PrintWriter out=null;
	Socket clientSocket;
	ServerProtocol<?> protocol;
	ProtocolCallback<String> callback=null;
	
	public ConnectionHandler(Socket acceptedSocket, ServerProtocol<?> p)
	{
		clientSocket = acceptedSocket;
		protocol = p;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}
	
	public void run()
	{
		
		try {
			initialize();
		}
		catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} 
		catch (IOException e) {
			System.out.println("Error in I/O");
		} 
		
		System.out.println("Connection closed - bye bye...");
		close();

	}
	
	public void process() throws IOException
	{
		String msg;
		
		while ((msg = in.readLine()) != null)
		{
			System.out.println("Received \"" + msg + "\" from client");
			
			if (((TBGPProtocol) protocol).isEnd(msg)){
				TBGPServerDatabase.getDataInstance().removePlayer(callback);
				callback.sendMsg("SYSMSG" +msg+ "ACCEPTED: BYE BYE.");
				break;
			}
			
			((TBGPProtocol)protocol).processMessage(msg, callback);
			
		}
	}
	
	// Starts listening
	public void initialize() throws IOException
	{
		// Initialize I/O
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8"), true);
		callback= new ProtocolCallback<String>() {
			@Override
			public void sendMsg(String msg) throws IOException {
				out.println(msg);	
			}
		};
		System.out.println("I/O initialized");
		
	}
	
	// Closes the connection
	public void close()
	{
		try {
			if (in != null)
			{
				in.close();
			}
			if (out != null)
			{
				out.close();
			}
			
			clientSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception in closing I/O");
		}
	}
	
}

/**
 * The Class TBGPserver.
 */
class TBGPserver implements Runnable {
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/** The listen port. */
	private int listenPort;
	
	/** The server protocol factory. */
	private ServerProtocolFactory factory;	
	
	/**
	 * Instantiates a new TBGP server.
	 *
	 * @param port the port
	 * @param p the ServerProtocolFactory
	 */
	public TBGPserver(int port, ServerProtocolFactory p)
	{
		serverSocket = null;
		listenPort = port;
		factory = p;
	}
	
	@Override
	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenPort);
			System.out.println("Listening...");
		}
		catch (IOException e) {
			System.out.println("Cannot listen on port " + listenPort);
		}
		
		while (true)
		{
			try {
				ConnectionHandler newConnection = new ConnectionHandler(serverSocket.accept(), factory.create());
            new Thread(newConnection).start();
			}
			catch (IOException e)
			{
				System.out.println("Failed to accept on port " + listenPort);
			}
		}
	}
	

	/**
	 * Close.
	 * closing connection, and specifically the socket.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// Closes the connection
	public void close() throws IOException
	{
		serverSocket.close();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments (used for listen port)
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException
	{
		// Get port
		int port = (Integer.decode(args[0])).intValue();
		
		TBGPserver server = new TBGPserver(port, new TBGPProtocolFactory());
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			System.out.println("Server stopped");
		}
		
		

	}
}
