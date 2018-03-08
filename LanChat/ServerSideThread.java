import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;


public class ServerSideThread implements Runnable {
	private Socket currSocket;
	private String address;
	private String name;
	private BufferedReader in;
	private PrintWriter out;
	private String timeStamp;
	protected static final String ANSI_RESET = "\u001B[0m";
	protected static final String ANSI_BLACK = "\u001B[30m";
	protected static final String ANSI_RED = "\u001B[31m";
	protected static final String ANSI_GREEN = "\u001B[32m";
	protected static final String ANSI_YELLOW = "\u001B[33m";
	protected static final String ANSI_BLUE = "\u001B[34m";
	protected static final String ANSI_PURPLE = "\u001B[35m";
	protected static final String ANSI_CYAN = "\u001B[36m";
	protected static final String ANSI_WHITE = "\u001B[37m";

	public ServerSideThread(Socket currSocket) {
		this.currSocket = currSocket;
		address = currSocket.getLocalAddress().getHostName();

	}

	public void run() {
		try {
			out = new PrintWriter(currSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					currSocket.getInputStream()));
			out.println("Server > Enter your name: ");
			name = in.readLine().toUpperCase();

			out.println("Server > Welcome " + name);
			broadcast(name + " has joined the room", true);

			while (true) {
				out = new PrintWriter(currSocket.getOutputStream(), true);

				String stdin = in.readLine();
				
				if (stdin == null) {
					disconnect();
					return;
				}
				broadcast(stdin, false);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() throws IOException
	{
		for(int i = 0; i < ServerSide.CONNECTIONS.size(); i++) 
			{
				if (ServerSide.CONNECTIONS.get(i) == currSocket)
				{
					ServerSide.CONNECTIONS.remove(i);
				}
			}
			
			broadcast(name + " has disconnected",true);
			timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
					.getInstance().getTime());
			System.out.println(timeStamp + "[" + ANSI_RED + "ServerThread/DISCONNECT" + ANSI_RESET + "]:" + address + "(" + name +")");
	}

	private void broadcast(String x, boolean server) throws IOException {
		String userOut = "";
		String serverOut = "";

		timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
				.getInstance().getTime());
		
		if (server) {
			serverOut += timeStamp + "["+ ANSI_PURPLE + "ServerThread/BORADCAST" + ANSI_RESET + "]: ";
			userOut += "Server: ";
		} else {
			serverOut += timeStamp + "["+ ANSI_CYAN + "ServerThread/TRAFFIC" + ANSI_RESET + "]: ";
			serverOut +=  address + "(" + name +") > ";
			userOut += name + ": ";

		}
		System.out.println(serverOut + x);
		for (Socket s : ServerSide.CONNECTIONS) {
			out = new PrintWriter(s.getOutputStream(), true);
			out.println(userOut + x);
			

		}
	}

}
