package hyNetworking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Broadcast implements Runnable{
	private int UDP_PORT = 0;
	
	public Broadcast(int port){
		this.UDP_PORT = port;
	}
	
	private void BroadcastIP()throws Exception{  
        DatagramSocket dgSocket = new DatagramSocket();  
       
        byte b[] = new byte[]{'5', '7', '0', '0', '0'};  
        DatagramPacket dgPacket=new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), this.UDP_PORT);  
        dgSocket.send(dgPacket);  
        dgSocket.close();  
        //System.out.println("Send UDP broadcast.");  
    }  
	
	@Override
	public void run(){
        while(true){  
            try {  
                Thread.sleep(5000);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
            try {  
                BroadcastIP();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
       }    	
	}

}
