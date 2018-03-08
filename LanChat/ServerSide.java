import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class ServerSide {
	protected static ArrayList<Socket> CONNECTIONS = new ArrayList<Socket>();
	public static String getTime()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
				.getInstance().getTime());
	}
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter port : ");
		int port = scan.nextInt();
		
		try {
			
			
			ServerSocket ss = new ServerSocket(port);
			
			System.out.println(getTime() + "[" + ServerSideThread.ANSI_GREEN + "SUCCESS" + ServerSideThread.ANSI_RESET + "] SERVER ESTABLISHED");
			while (true)
			{
				System.out.println(getTime() + "["+ ServerSideThread.ANSI_GREEN + "ServerThread/INFO" +  ServerSideThread.ANSI_RESET + "]: LISTENING FOR CONNECTION REQUESTS");
				Socket newSocket = ss.accept();
				
				System.out.println(getTime() + "["+ ServerSideThread.ANSI_BLUE + "ServerThread/CONNECTIONREQUEST" +  ServerSideThread.ANSI_RESET + "]: " + newSocket.getLocalAddress().getHostName());
				PrintWriter out = new PrintWriter(newSocket.getOutputStream(),true);
				
				out.println(Art.welcomeText);
				CONNECTIONS.add(newSocket);
				ServerSideThread st = new ServerSideThread(newSocket);
				
				Thread exec = new Thread(st);
				System.out.println(getTime() + "["+ ServerSideThread.ANSI_GREEN + "ServerThread/INFO" +  ServerSideThread.ANSI_RESET + "]: Initializing thread with " + newSocket.getLocalAddress().getHostName());
				exec.start();
			}
		} catch (IOException e) {
			System.out.println("Couldn't connect to port");
			e.printStackTrace();
		}
	}
}

