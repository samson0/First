package PriceChecker;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import JarUtil.JarLoader;
import hyNetworking.Broadcast;
//import JarUtil.JarLoader;
import hyNetworking.hyNetworking;
import hyUI.PriceCheckerUI;
import PriceCheckerInf.DatabaseInterface;


public class PriceChecker {
	
	final static String strJarPath = System.getProperty("user.dir") + "\\";
	final static String strJarName	= "PriceCheckerDatabase.jar";

	final static int TCP_Port = 57000;
	
	public static void main(String[] args) {
	
	   //Check if the program is already running.
	   {
			ServerSocket serverTest = null;
		    try{
			    serverTest = new ServerSocket(); 
			    serverTest.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), TCP_Port));			    
		    }catch(BindException io){
			    io.printStackTrace(); 
			    JFrame frame = new JFrame();
		    	frame.setVisible(true);
		    	frame.setLocationRelativeTo(null);
		    	JOptionPane.showMessageDialog(frame, new String("The program is already running."));
		    	frame.dispose();

		    	System.exit(0);
		    }catch(IOException e){
		        e.printStackTrace();
		    }finally{
		    	try{
		    		if(serverTest!=null)
		    			serverTest.close();
		    		 System.out.println("Closed");
		    	}catch(IOException e){
			        e.printStackTrace();
		    	}
		    }
		}

		JarLoader jarLoader = new JarLoader(strJarPath, strJarName);
        if( !jarLoader.check_JARFile() ){
        	System.out.println("JAR file \""+ strJarPath + strJarName + "\"not exist!");
        	
        	JFrame frame = new JFrame();
        	frame.setVisible(true);
        	frame.setLocationRelativeTo(null);
        	JOptionPane.showMessageDialog(frame, new String("JAR file \""+ strJarPath + strJarName + "\"not exist!"));
        	frame.dispose();

        	System.exit(0);
        }   
    
        /*
        //Big 5
        try{
        byte[] aa = new String("С").getBytes("BIG5");
        for(int i = 0; i < aa.length;i++){
			System.out.print(String.format("%02X ", aa[i]));
		}
		System.out.println("");
        }catch(UnsupportedEncodingException e){
        	
        }
        //GB3231
        {
            byte[] aa = new String("С").getBytes();
            for(int i = 0; i < aa.length;i++){
    			System.out.print(String.format("%02X ", aa[i]));
    		}
    		System.out.println("");
        }*/
        
        /*
        //UTF-8
        try{
        byte[] aa = new String("1һ�h").getBytes("UTF8");
        for(int i = 0; i < aa.length;i++){
			System.out.print(String.format("%02X ", aa[i]));
		}
		System.out.println("");
        }catch(UnsupportedEncodingException e){
        	
        }*/

        
        //Test
        /*File file = new File("C:\\Users\\Samson\\Desktop\\003A.jpg");
        try{
        FileInputStream fis = new FileInputStream(file);
        byte[] sendBytes = new byte[1024];
        int length = 0;
        while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
        	System.out.println("length=" + length);
        	for(int i = 0; i < length;i++){
    			System.out.print(String.format("%02X ", sendBytes[i]));
    		}
    		System.out.println("");
        }}catch(FileNotFoundException ex){
        	ex.printStackTrace();
        }catch(IOException e){
        	e.printStackTrace();
        }*/        
        
        
        
        final DatabaseInterface priceCheckerDBInf = jarLoader.getPriceCheckerDatabaseInstance();
    	
        final PriceCheckerUI priceCheckerUI = new PriceCheckerUI();
        priceCheckerUI.UI_Init();
        
        new Thread(){
            @Override
        	public void run() {            	
            	try{
            		//List<Socket> listConnectedDev = new ArrayList<Socket>();
            		
            		System.out.println("Server IP:" + InetAddress.getLocalHost());
            		@SuppressWarnings("resource")
					ServerSocket server = new ServerSocket(); 
            		server.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), TCP_Port ));
            		
            		priceCheckerUI.Update_InfoServer_Msg("Server IP:" + server.getInetAddress().getHostAddress());
            		
            		Socket client = null;  

            		while(true){
            			
            			client = server.accept();          			
System.out.println("New connection:" + client.getInetAddress().getHostAddress());

            			if(hyNetworking.CheckSocket(client)){            				
            				
            				hyNetworking hyNet = new hyNetworking(priceCheckerDBInf);
            				hyNet.InitNetwork(client, priceCheckerUI);
            				new Thread(hyNet).start();
                			
                			System.out.println(client.getInetAddress().getHostAddress() + " connected..."); 
                			priceCheckerUI.Update_InfoClient_Msg("Connected client:");
                			priceCheckerUI.Add_Connected_Dev(client);
                			
                			//break;
            			}else{
            				//client.close();
            			}
            		}
            		//server.close();
            	}catch(IOException e){
            		e.printStackTrace();
            	}
                 
            }
        }.start();   
         
        new Thread(new Broadcast(27000)).start();
    }
    
}
