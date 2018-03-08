import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client implements Runnable {
	private Socket X;
	private BufferedReader stdin;
	private PrintWriter out;
	public Client(Socket X) throws IOException{
		this.X = X;
		stdin = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(X.getOutputStream(),true);
		String input = null;
		Thread thread = new Thread(this);
		thread.start();
		while (true){
			input = stdin.readLine();	
			out.println(input);
		}		
	}
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(X.getInputStream()));
			while (true)
			{
				
				String message = in.readLine();
				System.out.println(message);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter hostname: ");
		String hostname = scan.nextLine();
		System.out.println("Enter portname: ");
		int port = scan.nextInt();
		
		try {
			Socket socket = new Socket(hostname, port);
			Client client = new Client(socket);
			
		}	
		catch (IOException e) {
			System.out.println("connection error");
			e.printStackTrace();
		}
		
	}

}

