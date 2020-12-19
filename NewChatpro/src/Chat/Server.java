package Chat;

import java.net.*;

import java.io.*;
import java.io.BufferedReader;


import java.io.InputStreamReader;
import java.io.PrintWriter;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Server extends JFrame {
	private static final String BoderLayout = null;
	protected static final char[] ContentToSend = null;
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	private JLabel heading=new JLabel("Server Area");
	private JTextArea messageArea=new JTextArea();
	private JTextField messageInput=new JTextField();
	private Font font=new Font("Roboto",Font.PLAIN,20);
	
	
	
	//constructor
	public Server(){
		try {
			
		ServerSocket Server = new ServerSocket(7778);
		System.out.println("Server is ready to acccept connection");
        System.out.println("Waiting...");
        socket=Server.accept();
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());
        
        
	      creatGUI();
	      
	      handleEvents();
	      
        startReading();
        startWriting();
        
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			
			}
			@Override
			public void keyPressed(KeyEvent e) {
			
			}
			@Override
			public void keyReleased(KeyEvent e) {
			//System.out.println("key Released"+e.getKeyCode());
			if(e.getKeyCode()==10) {
				//System.out.println("You have press Enter button");
				
				String contentToSend=messageInput.getText();
				System.out.println(contentToSend);
				messageArea.append("Me:"+contentToSend+"\n");
				//out.println(ContentToSend);
				
				out.flush();
				messageInput.setText("");
				messageInput.requestFocus();
				
			}
			}
		});
		// TODO Auto-generated method stub
		
	}

	private void creatGUI() {
		this.setTitle("Server Message");
		this.setSize(500,50);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		this.setLayout(new BorderLayout());
		messageArea.setEditable(false);
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(messageArea);
		this.add(jScrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		this.setVisible(true);
		
		// TODO Auto-generated method stub
		
	}

	public void startReading(){
		//read data
		Runnable r1=()->{
			System.out.println("Reader Started..");
			 try
			 {
			while(true)
			 {
				String msg=br.readLine();
				if(msg.equals("Exit"))
				{
				System.out.println("Client reminated the chat");
				JOptionPane.showMessageDialog(this,"Client Terminated the chat");
				messageInput.setEnabled(false);
				socket.close();
				break;
				}
				messageArea.append("Client:"+msg+"\n");
				//System.out.println("Client: "+msg);
			
			}
			}
			 catch(Exception e) {
				//e.printStackTrace();
				System.out.println("Connection is closed");
			
			}
			
			};
			new Thread (r1).start();
	}
	public void startWriting() {
		//data in and out
		Runnable r2=()->{
			System.out.println("Writer Started....");
			try {
			while (!socket.isClosed()) {
				
					BufferedReader br1=new BufferedReader(new 
					InputStreamReader(System.in));
				   String content=br1.readLine();
				   
				   out.println(content);
				   out.flush();
				   if(content.equals("Exit"))
				   {
					   socket.close();
				   }
				}
			}catch(Exception e) {
					//e.printStackTrace();
				System.out.println("Connection is closed");
				}
			System.out.println("Connection is closed");
			
		};
		new Thread(r2).start(); 
	}
	
public static void main(String[]args) {
	System.out.println("This is Server...is going to start");
	new Server();
}
}

