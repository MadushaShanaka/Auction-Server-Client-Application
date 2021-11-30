import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class serverWork extends Thread {
	
	private Socket clientSocket;
	private HashMap<String, String[]> new1;
	private HashMap<Integer, String[]> new2;
	private static int logCount = 0;
	//server work constructor
	public serverWork(Socket clientSocket, HashMap<String, String[]> new1, HashMap<Integer, String[]> new2) {
		this.clientSocket = clientSocket;
		this.new1 = new1;
		this.new2 = new2;
	}



	public void run() {
		try {
			handleClient(clientSocket, new1, new2);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//handle client method which will take the client inputs and update hashmaps and output values to clients
	private static void handleClient(Socket clientSocket, HashMap<String,String[]> new1, HashMap<Integer, String[]> new2) throws IOException, InterruptedException {
		
		InputStream inps = clientSocket.getInputStream();
		OutputStream outs = clientSocket.getOutputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inps));
		String line;
		String[] commandArray;
		String data = "startup";
		String username = "";
		String symbol = "";
		int count = 0;
		
		outs.write(("Client Name: ").getBytes());
		
		while((line = reader.readLine()) != null ) {
			
			commandArray = line.split(" ");
			//client enter the "quit" he can exit
			if(commandArray[0].equalsIgnoreCase("quit")) {
				break;
			}
			//take username from clients
			else if(count == 0) {
				username = line;
				count++;
				outs.write(("Symbol: ").getBytes());
			}
			//take Symbol and show highest bid according to symbol
			else if(count == 1 && new1.containsKey(commandArray[0])) {
				outs.write(("Highest bid: "+(new1.get(commandArray[0]))[0]+"\n").getBytes());
				outs.write(("Your bid: ").getBytes());
				symbol = commandArray[0];
				count = 2;
			}
			//take bid value and update the highest if given bid is greater than older one
			//put the current time and update hash maps
			else if(count == 2) {
				
				if(commandArray[0].equalsIgnoreCase("price")) {
					outs.write(("Highest bid: "+(new1.get(symbol))[0]+" $\n").getBytes());
					outs.write(("Your bid: \n").getBytes());
				}
				else {
				
				String bidPrice = commandArray[0];
				
				int newBid = Integer.parseInt(bidPrice);
				int highestBid = Integer.parseInt((new1.get(symbol))[0]);
				
				Date date = Calendar.getInstance().getTime();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String strDate = df.format(date);
				
				data = (username+"  "+symbol+"  "+bidPrice+"  "+strDate);
				new2.put(logCount, new String[] {bidPrice,symbol,((new1.get(symbol))[0]),username,strDate});
				logCount++;
				
				if(newBid > highestBid) {
					new1.replace(symbol, new String[] {bidPrice,username,strDate});
				}
				
				outs.write(("Succcessfull bid! \n").getBytes());
				outs.write((symbol+": $ "+(new1.get(symbol))[0]+" "+(new1.get(symbol)[1])+" "+(new1.get(symbol)[2])+"\n").getBytes());
				outs.write(("Your bid: \n").getBytes());
				count = 2;
				}
			}
			else {
				outs.write(("Unknown Symbol or Command\n").getBytes());
			}
		}
		
		clientSocket.close();
	}
	
	
	
}
