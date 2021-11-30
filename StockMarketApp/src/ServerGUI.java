import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerGUI {

	private JFrame frame;
	static HashMap<String, String[]> new1 = new HashMap<String, String[]>();
	static HashMap<Integer, String[]> new2 = new HashMap<Integer, String[]>();
	
	static JLabel lblFBtxt = new JLabel("...");
	static JLabel lblVRTUtxt = new JLabel("...");
	static JLabel lblMSFTtxt = new JLabel("...");
	static JLabel lblGOOGLtxt = new JLabel("...");
	static JLabel lblYHOOtxt = new JLabel("...");
	static JLabel lblXLNXtxt = new JLabel("...");
	static JLabel lblTSLAtxt = new JLabel("...");
	static JLabel lblTXNtxt = new JLabel("...");
	JTextArea textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane();
	JScrollPane scrollPane_1 = new JScrollPane();
	JTextArea textArea2 = new JTextArea();
	private String text = "";
	private String StockText = "";
	private String text1 = "";
	private int sizeh = 0;
	private boolean btnFlag = true;
	private boolean btnFlag2 = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws InterruptedException {
		
		int port = 2000;
		
		readStock();
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//server and client connection part via sockets
		//create threads for each clients (serverWork extends Thread)
		try {
			System.out.println("About to start");
			ServerSocket serverSocket = new ServerSocket(port);
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				serverWork w1 = new serverWork(clientSocket, new1, new2);
				w1.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//read the stock details from csv file and update the hashmap
	//generate random values for price(100-150)
	public static void readStock() {
		
		String[] array;
		Random rand = new Random();
		
		try {
			File f1 = new File("stocks.csv");
			Scanner s1 = new Scanner(f1);
			while(s1.hasNextLine()) {
				String data = s1.nextLine();
				String price = String.valueOf((rand.nextInt(50)+ 100));
				array = data.split(",");
				new1.put(array[0], new String[] {price,"startup","2020-01-01 00:00:00"});
			}
			s1.close();
			//System.out.println(new1);
			//System.out.println("\n"+new1.size());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
		displayStock();
	}
	
	//display details of 8 symbols and stock log, running in the new thread
	//update gui every 500ms
	public void displayStock() {
		
		Thread t = new Thread() {
			public void run() {
				try {
					while(true) {
						
						//stock log display
						if(sizeh != new2.size()) {
							for(Map.Entry mapElement : new2.entrySet()) {
							
								String[] value = (String[])mapElement.getValue();
								text = (value[3]+" \t"+value[1]+"\t "+value[0]+"\t "+value[2]+"\t "+value[4]+"\n");
							}
							
							text1 = text1 + text;
							textArea.setText(text1);
						}
						sizeh = new2.size();
						
						//8 symbols details
						lblFBtxt.setText   ("Facebook                                 "+ (new1.get("FB"))[0] +"                    " + (new1.get("FB"))[2]);
						lblVRTUtxt.setText ("Virtusa Corporation                   "+ (new1.get("VRTU"))[0] +"                    " + (new1.get("VRTU"))[2]);
						lblMSFTtxt.setText ("Microsoft Corporation                "+ (new1.get("MSFT"))[0] +"                    " + (new1.get("MSFT"))[2]);
						lblGOOGLtxt.setText("Google                                    "+ (new1.get("GOOGL"))[0] +"                    " + (new1.get("GOOGL"))[2]);
						lblYHOOtxt.setText ("Yahoo!                                    "+ (new1.get("YHOO"))[0] +"                    " + (new1.get("YHOO"))[2]);
						lblXLNXtxt.setText ("Xilinx                                      "+ (new1.get("XLNX"))[0] +"                    " + (new1.get("XLNX"))[2]);
						lblTSLAtxt.setText ("Tesla Motors                            "+ (new1.get("TSLA"))[0] +"                    " + (new1.get("TSLA"))[2]);
						lblTXNtxt.setText  ("Texas Instruments Corporation   "+ (new1.get("TXN"))[0] +"                    " + (new1.get("TXN"))[2]);
					
						sleep(500);
					}
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		
		
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	//server gui parts, labels, buttons, textboxes, scrollers and stuff
	//also event handling methods for log button and stocks button
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.control);
		frame.setBounds(100, 100, 1400, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		scrollPane_1.setBounds(24, 73, 803, 474);
		frame.getContentPane().add(scrollPane_1);
		scrollPane_1.setVisible(false);
		textArea2.setFont(new Font("Monospaced", Font.PLAIN, 16));
		
		
		scrollPane_1.setViewportView(textArea2);
		textArea2.setVisible(false);
		
		
		JLabel lblSymbol = new JLabel("Symbol");
		lblSymbol.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblSymbol.setBounds(35, 26, 116, 37);
		frame.getContentPane().add(lblSymbol);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblName.setBounds(161, 26, 201, 37);
		frame.getContentPane().add(lblName);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblPrice.setBounds(456, 26, 138, 37);
		frame.getContentPane().add(lblPrice);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblDate.setBounds(630, 26, 246, 37);
		frame.getContentPane().add(lblDate);
		
		JLabel lblFB = new JLabel("FB");
		lblFB.setBackground(new Color(255, 255, 255));
		lblFB.setForeground(Color.BLUE);
		lblFB.setHorizontalAlignment(SwingConstants.CENTER);
		lblFB.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblFB.setBounds(35, 104, 82, 29);
		frame.getContentPane().add(lblFB);
		
		JLabel lblVRTU = new JLabel("VRTU");
		lblVRTU.setForeground(Color.BLUE);
		lblVRTU.setHorizontalAlignment(SwingConstants.CENTER);
		lblVRTU.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblVRTU.setBounds(35, 160, 82, 29);
		frame.getContentPane().add(lblVRTU);
		
		JLabel lblMSFT = new JLabel("MSFT");
		lblMSFT.setForeground(Color.BLUE);
		lblMSFT.setHorizontalAlignment(SwingConstants.CENTER);
		lblMSFT.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblMSFT.setBounds(35, 214, 82, 29);
		frame.getContentPane().add(lblMSFT);
		
		JLabel lblGOOGL = new JLabel("GOOGL");
		lblGOOGL.setForeground(Color.BLUE);
		lblGOOGL.setHorizontalAlignment(SwingConstants.CENTER);
		lblGOOGL.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblGOOGL.setBounds(35, 271, 82, 29);
		frame.getContentPane().add(lblGOOGL);
		
		JLabel lblYHOO = new JLabel("YHOO");
		lblYHOO.setForeground(Color.BLUE);
		lblYHOO.setHorizontalAlignment(SwingConstants.CENTER);
		lblYHOO.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblYHOO.setBounds(35, 327, 82, 29);
		frame.getContentPane().add(lblYHOO);
		
		JLabel lblXLNX = new JLabel("XLNX");
		lblXLNX.setForeground(Color.BLUE);
		lblXLNX.setHorizontalAlignment(SwingConstants.CENTER);
		lblXLNX.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblXLNX.setBounds(35, 382, 82, 29);
		frame.getContentPane().add(lblXLNX);
		
		JLabel lblTSLA = new JLabel("TSLA");
		lblTSLA.setForeground(Color.BLUE);
		lblTSLA.setHorizontalAlignment(SwingConstants.CENTER);
		lblTSLA.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblTSLA.setBounds(35, 438, 82, 29);
		frame.getContentPane().add(lblTSLA);
		
		JLabel lblTXN = new JLabel("TXN");
		lblTXN.setForeground(Color.BLUE);
		lblTXN.setHorizontalAlignment(SwingConstants.CENTER);
		lblTXN.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblTXN.setBounds(35, 497, 82, 29);
		frame.getContentPane().add(lblTXN);
		lblFBtxt.setBackground(new Color(176, 224, 230));
		
		lblFBtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFBtxt.setBounds(157, 104, 705, 26);
		frame.getContentPane().add(lblFBtxt);
		lblVRTUtxt.setBackground(new Color(255, 182, 193));
		
		lblVRTUtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMSFTtxt.setBackground(new Color(176, 224, 230));
		lblVRTUtxt.setBounds(157, 160, 705, 26);
		frame.getContentPane().add(lblVRTUtxt);
		
		
		lblMSFTtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMSFTtxt.setBounds(157, 211, 705, 29);
		frame.getContentPane().add(lblMSFTtxt);
		lblGOOGLtxt.setBackground(new Color(255, 182, 193));
		
		lblGOOGLtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGOOGLtxt.setBounds(157, 271, 705, 26);
		frame.getContentPane().add(lblGOOGLtxt);
		lblYHOOtxt.setBackground(new Color(176, 224, 230));
		
		lblYHOOtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblYHOOtxt.setBounds(157, 327, 705, 26);
		frame.getContentPane().add(lblYHOOtxt);
		lblXLNXtxt.setBackground(new Color(255, 182, 193));
		
		lblXLNXtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblXLNXtxt.setBounds(157, 379, 705, 29);
		frame.getContentPane().add(lblXLNXtxt);
		lblTSLAtxt.setBackground(new Color(176, 224, 230));
		
		lblTSLAtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTSLAtxt.setBounds(157, 435, 705, 29);
		frame.getContentPane().add(lblTSLAtxt);
		lblTXNtxt.setBackground(new Color(255, 182, 193));
		
		lblTXNtxt.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTXNtxt.setBounds(157, 497, 705, 26);
		frame.getContentPane().add(lblTXNtxt);
		
		
		JLabel lblstockLog = new JLabel("Stock Log");
		lblstockLog.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblstockLog.setHorizontalAlignment(SwingConstants.CENTER);
		lblstockLog.setBounds(987, 26, 290, 37);
		lblstockLog.setVisible(false);
		frame.getContentPane().add(lblstockLog);
		
		JLabel lblstock = new JLabel("Name             Symbol               bid               highest bid                 date & time");
		lblstock.setBounds(861, 73, 499, 31);
		lblstock.setVisible(false);
		frame.getContentPane().add(lblstock);
		
		JButton btnStart = new JButton("LOG");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnFlag) {
					textArea.setVisible(btnFlag);
					scrollPane.setVisible(btnFlag);
					lblstockLog.setVisible(btnFlag);
					lblstock.setVisible(btnFlag);
					btnStart.setBackground(new Color(255, 0, 0));
					btnFlag = false;
				}
				else {
					textArea.setVisible(btnFlag);
					scrollPane.setVisible(btnFlag);
					lblstockLog.setVisible(btnFlag);
					lblstock.setVisible(btnFlag);
					btnStart.setBackground(new Color(124, 252, 0));
					btnFlag = true;
				}
			}
		});
		
		btnStart.setBackground(new Color(124, 252, 0));
		btnStart.setForeground(new Color(255, 255, 255));
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnStart.setBounds(240, 570, 156, 57);
		frame.getContentPane().add(btnStart);
		
		
		scrollPane.setBounds(861, 114, 515, 513);
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(textArea);
		scrollPane.setVisible(false);
		
		
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textArea.setVisible(false);
		
		JButton btnStock = new JButton("STOCKS");
		btnStock.setForeground(new Color(255, 255, 255));
		btnStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnFlag2) {
					scrollPane_1.setVisible(btnFlag2);
					textArea2.setVisible(btnFlag2);
					btnFlag2 = false;
					btnStock.setBackground(new Color(255, 0, 0));
					for(Map.Entry mapElement : new1.entrySet()) {
						String key = (String)mapElement.getKey();
						String[] value = (String[])mapElement.getValue();
						StockText = (StockText+key+"         \t         \t \t"+value[0]+"\t       "+value[2]+"\n");
					}
					textArea2.setText(StockText);
				}
				else {
					btnStock.setBackground(new Color(124, 252, 0));
					scrollPane_1.setVisible(btnFlag2);
					textArea2.setVisible(btnFlag2);
					btnFlag2 = true;
				}
				
			}
		});
		btnStock.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnStock.setBackground(new Color(124, 252, 0));
		btnStock.setBounds(456, 570, 156, 57);
		frame.getContentPane().add(btnStock);
		
		
		
		
		
		
	}
}
