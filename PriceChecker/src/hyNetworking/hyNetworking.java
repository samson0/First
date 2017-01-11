package hyNetworking;

import java.awt.Color;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import PriceChecker.HYCommandSet;
import JarUtil.JarLoader;
import PriceCheckerInf.DatabaseInterface;
import hyUI.PriceCheckerUI;

public class hyNetworking implements Runnable{
	    private boolean DEBUG = true;
		  
	    private Socket client = null;
	    private static ConcurrentHashMap<Socket, Integer> ConnectedDevHB_Cnt = new ConcurrentHashMap<Socket, Integer>();// socket -> heartbeat count
	    private PriceCheckerUI priceCheckerUI = null;
	    
	    private OutputStream out = null;
	    private InputStream buf = null;	
	    
	    private DatabaseInterface priceCheckerDBInf = null;
	    
	    public hyNetworking(){
	    
	    }
	    
	    public hyNetworking(DatabaseInterface pcInf){
	    	this.priceCheckerDBInf = pcInf;
	    }
	    
	    public void InitNetwork(Socket client, hyUI.PriceCheckerUI priceCheckerUI){  
	        this.client = client; 
	        this.priceCheckerUI = priceCheckerUI;        
	        ConnectedDevHB_Cnt.put(client, 0);
	    }
	    
	    //Check replicate socket
	    public static boolean CheckSocket(Socket client){
	    	
	    	if(ConnectedDevHB_Cnt.size() == 0)
	    		return true;
	    		    	
	    	Iterator<Socket> it = ConnectedDevHB_Cnt.keySet().iterator();

	    	while(it.hasNext()){
	    		if(it.next().getInetAddress().getHostAddress().equals(client.getInetAddress().getHostAddress()))
	    			return false;
	    	}
	
	    	return true;
	    }
	          
	    @Override  
	    public void run() {
	        try{
	            //获取Socket的输出流，用来向客户端发送数据  
	            this.out = this.client.getOutputStream();  
	            //获取Socket的输入流，用来接收从客户端发送过来的数据  
	            //this.buf = new BufferedReader(new InputStreamReader(this.client.getInputStream()));  
	            this.buf = this.client.getInputStream();  
	            boolean flag =true;
	                       
	            byte[] receive = new byte[255];
	            int nRev = 0;
	            
	            while(flag){            	
	            	
	            	if(this.buf.available() == 0){
	            		try{
	            			Thread.sleep(100);
	            		}catch(InterruptedException e){
	            			e.printStackTrace();
	            		}
	
	            		int cnt = ConnectedDevHB_Cnt.get(this.client) + 1;
	            		//System.out.println("Run#this.heartbeat_cnt = " + cnt);

	            		if(cnt == 90){
	            			System.out.println("Send heartbeat");
	            			byte[] heartbeat = new byte[]{(byte)0xDA, (byte)0x02, HYCommandSet.HY_CMD_HEARTBEAT, 0x41};
		                    this.out.write(heartbeat);
		                    
		                    ConnectedDevHB_Cnt.put(this.client, 0);
	            		}else
	            			ConnectedDevHB_Cnt.put(this.client, cnt);
	
	            		//System.out.println("RunA#this.heartbeat_cnt = " + hashmapSocketHeartbeat.get(this.client));
	            		
	            		continue;	      
	            	}
	            	
	                //接收从客户端发送过来的数据  
	                nRev = this.buf.read(receive);
	                System.out.println("nRev =" + nRev );

	                if(DEBUG){
	                   System.out.println("Receive from " + this.client.getInetAddress().getHostAddress() + ":");
	                   for(int i = 0; i < nRev;i++){
	                       System.out.print(String.format("%02X ", receive[i]));
	                   }
	                   System.out.println("");
	                }
	                    
	                CommandProcess(Arrays.copyOf(receive, nRev));
	                //byte[] aa = new byte[]{1,2,3};
	                //CommandProcess(Arrays.copyOf(receive, nRev));  
	                //CommandProcess(aa); 
	                
	                
	                //}
	            }  
	        }catch(IOException e){
	            e.printStackTrace();  
	        }finally{
	        	try {	
	
	        		if(DEBUG)
	        			System.out.println("Disconnect..." + this.client.getInetAddress().getHostAddress());
	        			
	        		this.priceCheckerUI.Remove_Connected_Dev(this.client);
	        		ConnectedDevHB_Cnt.remove(this.client);
	        		if(ConnectedDevHB_Cnt.size() == 0){
	        			this.priceCheckerUI.Update_InfoClient_Msg("Connected client:<None>");
	        		}
	        		
	                if(this.out!=null)
	                	this.out.close();
	                if(this.buf!=null)
	                	this.buf.close();
	        		if(this.client!=null)
	                	this.client.close();
	        		
	        		if(DEBUG){
	        			System.out.println("Connected client = " + ConnectedDevHB_Cnt.size());                        
	        		}
	        		
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    private void CommandReturn(byte cmd, byte[] dat){
	    	byte[] send = new byte[4 + dat.length];
	    	byte cs = 0;
	    	
	    	send[0] = (byte)0xDA;
	    	send[1] = (byte)(send.length - 2);
	    	send[2] = cmd;
	    	cs = (byte)(send[0]^send[1]^send[2]);
	    	for(int i = 0; i < dat.length; i++){
	    		send[i + 3] = dat[i];
	    		cs ^= (byte)send[i + 3];
	    	}	    	
	    	send[send.length - 1] = cs;
	    	
	    	if(DEBUG){
	    		System.out.println("Send:");
        		for(int i = 0; i < send.length;i++){
        			System.out.print(String.format("%02X ", send[i]));
        		}
        		System.out.println("");
        	}
	    	
	    	try{
	    		this.out.write(send);
	    	}catch(IOException e) {
	    		e.printStackTrace();
	    	}
	    	
	    }
	    
	    public void SendCommand(Socket socket, byte[] dat){
	    	byte[] send = new byte[3 + dat.length];
	    	byte cs = 0;
	    	OutputStream out = null;
	    	
	    	send[0] = (byte)0xDA;
	    	send[1] = (byte)(send.length - 2);
	    	cs = (byte)(send[0]^send[1]);
	    	for(int i = 0; i < dat.length; i++){
	    		send[i + 2] = dat[i];
	    		cs ^= (byte)send[i + 2];
	    	}	    	
	    	send[send.length - 1] = cs;
	    		    	
	    	if(DEBUG){
	    		System.out.println("Send to " + socket.getInetAddress().getHostAddress() + ":");
        		for(int i = 0; i < send.length;i++){
        			System.out.print(String.format("%02X ", send[i]));
        		}
        		System.out.println("");
        	}
	    	
	    	try{
	    		out = socket.getOutputStream();
	    		
	    		out.write(send);	    		
	
	    		ConnectedDevHB_Cnt.put(socket, 0);	
	    	}catch(IOException e) {
	    		e.printStackTrace();
	    	}
	    	
	    }
	    /*
	    private byte[] stringToBig5(String str){
	    	byte[] big5 = null;
	    	
	    	try{
	            big5 = str.getBytes("BIG5");
	            //System.out.println("BIG5:");
	        	//for(int i = 0; i < big5.length;i++){
	    		//	System.out.print(String.format("%02X ", big5[i]));
	        	//}
	    		//System.out.println("");
	        }catch(UnsupportedEncodingException uee){
	           	uee.printStackTrace();
	        }
	    	
	    	return big5;
	    }*/
	    
	    public boolean CommandProcess(byte[] rev){
	    	
	    	if(rev[0] != (byte)0xDD)
	    		return false;

	    	byte cs = 0;
	    	for(int i = 0; i < rev.length - 1; i++){
	    		cs ^= (byte)rev[i];
	    	}
   	
	    	if(cs != rev[rev.length - 1])
	    		return false;
    	
	    	byte[] cmd = Arrays.copyOfRange(rev, 2, rev.length - 1);// from command to data, exclusive checksum
	    	byte[] send = null;
	    	
	    	switch(cmd[0]){
	    		case HYCommandSet.HY_CMD_HEARTBEAT:
	    			
	    			return true;
                case HYCommandSet.HY_CMD_SET_IDLE_LAYOUT:
                	//if(cmd[1] == HYCommandSet.HY_CMD_STATUS_SUCCESS)
                	//   this.priceCheckerUI.MsgBox("Set successfully");
                	return true;              	
                case HYCommandSet.HY_CMD_GET_IDLE_LAYOUT:          	
                	this.priceCheckerUI.MsgBox("The layout ID of idle status is " + cmd[2]);
                	return true;
                case HYCommandSet.HY_CMD_SET_NONIDLE_LAYOUT:
                	//if(cmd[1] == HYCommandSet.HY_CMD_STATUS_SUCCESS)
                 	//   this.priceCheckerUI.MsgBox("Set successfully");
                	return true;                	
                case HYCommandSet.HY_CMD_GET_NONIDLE_LAYOUT:
                	//this.priceCheckerUI.MsgBox("The layout ID is " + cmd[2]);
                	this.priceCheckerUI.Show_Display_Layout_ID(cmd[2]);
                	return true;
                case HYCommandSet.HY_CMD_GET_PRODUCT_INFO:	
                	if(!this.priceCheckerDBInf.connect_DB()){
                		this.priceCheckerUI.Update_DB_Status_Color(Color.RED);
                		this.priceCheckerUI.Update_DB_Status("Can't connect to database.");
                		send = new byte[1];
                		send[0] = (byte)0xF1;
                		
                		break;
                	}
                	this.priceCheckerUI.Update_DB_Status_Color(Color.BLACK);
            		this.priceCheckerUI.Update_DB_Status("OK");
                	
                	String[] price = new String[1];
                	String[] productName = new String[1];
                	String[] productImageName = new String[1];
                	if(this.priceCheckerDBInf.queryProductInfo(new String(Arrays.copyOfRange(cmd, 1, cmd.length)), price, productName, productImageName)){
                		byte[] bPrice = null;
                		byte[] bProductName = null;
                		byte[] bProductImage = null;
                		try{
                			bPrice = price[0].getBytes("UTF8");
                			bProductName = productName[0].getBytes("UTF8");
                			bProductImage = productImageName[0].getBytes("UTF8");
                	    }catch(UnsupportedEncodingException e){
                	        e.getStackTrace();
                	    }
                		int price_len = bPrice.length;
            			int productName_len = bProductName.length;
            			int productImageName_len = bProductImage.length;
                		send = new byte[1 + price_len + 1 + productName_len + 1 + productImageName_len + 1];
                    	//System.out.println("send[] size=" + send.length);                	
                    	send[0] = 0x00;                    	
                    	System.arraycopy(bPrice, 0, send, 1, price_len);
                    	send[1 + price_len] = '\0';
                    	System.arraycopy(bProductName, 0, send, 1 + price_len + 1, productName_len);
                    	send[1 + price_len + 1 + productName_len] = '\0';
                    	System.arraycopy(bProductImage, 0, send, 1 + price_len + 1 + productName_len + 1, productImageName_len);
                    	send[1 + price_len + 1 + productName_len + 1 + productImageName_len] = '\0';
                	}else{
                		send = new byte[1];
                		send[0] = 0x01;
                	}               	
                	
	    			break;
	    		case HYCommandSet.HY_CMD_GET_DATE_TIME:
                	Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    send = new byte[7];
                    send[0] = 0x00;
                    send[1] = (byte)(calendar.get(Calendar.YEAR)%100);
                    send[2] = (byte)(calendar.get(Calendar.MONTH) + 1);
                    send[3] = (byte)calendar.get(Calendar.DAY_OF_MONTH);
                    send[4] = (byte)calendar.get(Calendar.HOUR_OF_DAY);
                    send[5] = (byte)calendar.get(Calendar.MINUTE);
                    send[6] = (byte)calendar.get(Calendar.SECOND);
                	break;
	    		default:
	    			send = new byte[7];
	    			send[0] = (byte)0xF2;
	    			break;
                /*case 'a':
                //case (byte)0xA0:	
                	if(!this.priceCheckerInf.connect_DB()){
                		this.priceCheckerUI.Update_DB_Status("Can't connect to database.");
                		send = new byte[1];
                		send[0] = (byte)0xF1;
                		
                		break;
                	}
                	String[] price = new String[1];
                	if(this.priceCheckerInf.queryPrice(new String(Arrays.copyOfRange(cmd, 1, cmd.length)), price)){
                		int price_len = price[0].getBytes().length;
                		send = new byte[1 + price_len + 1];
                    	System.out.println("send[] size=" + send.length);                	
                    	send[0] = 0x00;                    	
                    	System.arraycopy(price[0].getBytes(), 0, send, 1, price_len);
                    	send[price_len + 1] = '\0';
                	}else{
                		send = new byte[1];
                		send[0] = 0x01;
                	}               	
                	
	    			break;
	    		case 'b':
                //case (byte)0xA1:	
                	if(!this.priceCheckerInf.connect_DB()){
                		this.priceCheckerUI.Update_DB_Status("Can't connect to database.");
                		send = new byte[1];
                		send[0] = (byte)0xF1;
                		
                		break;
                	}
                	String[] productName = new String[1];
                	if(this.priceCheckerInf.queryPrice(new String(Arrays.copyOfRange(cmd, 1, cmd.length)), productName)){
                		int productname_len = productName[0].getBytes().length;
                		send = new byte[1 + productname_len + 1];
                    	System.out.println("send[] size=" + send.length);                	
                    	send[0] = 0x00;                    	
                    	System.arraycopy(productName[0].getBytes(), 0, send, 1, productname_len);
                    	send[productname_len + 1] = '\0';
                	}else{
                		send = new byte[1];
                		send[0] = 0x01;
                	}               	
                	
	    			break;
	    		case 'c':
                //case (byte)0xA2:	
                	if(!this.priceCheckerInf.connect_DB()){
                		this.priceCheckerUI.Update_DB_Status("Can't connect to database.");
                		send = new byte[1];
                		send[0] = (byte)0xF1;
                		
                		break;
                	}
                	String[] productImageName = new String[1];
                	if(this.priceCheckerInf.queryPrice(new String(Arrays.copyOfRange(cmd, 1, cmd.length)), productImageName)){
                		int imageName_len = productImageName[0].getBytes().length;
                		send = new byte[1 + imageName_len + 1];
                    	System.out.println("send[] size=" + send.length);                	
                    	send[0] = 0x00;                    	
                    	System.arraycopy(productImageName[0].getBytes(), 0, send, 1, imageName_len);
                    	send[imageName_len + 1] = '\0';
                	}else{
                		send = new byte[1];
                		send[0] = 0x01;
                	}               	
                	
	    			break;	*/
	    	}
	    	
	    	CommandReturn(cmd[0], send);
	    	
	    	return true;
	    }
	    
	    public boolean send_data(Socket client_con, byte[] send_dat){	
	    	boolean bRet = true;
	    	OutputStream out = null;
	    		
	    	try{
	    		out = client_con.getOutputStream();
	    		
	    		out.write(send_dat);	    		
	
	    		ConnectedDevHB_Cnt.put(client_con, 0);	

	    	}catch(IOException e){
	    		e.printStackTrace();
	    		
	    		bRet = false;
	    	}/*finally{
	        	try {
	                if(out!=null)
	                   out.close();	        		
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }*/
	    	
	    	return bRet;
	    }
}
