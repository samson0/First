package hyUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import PriceChecker.HYCommandSet;
import hyNetworking.hyNetworking;

public class PriceCheckerUI {
	private final Font InfoMsgFont = new Font("TimesRoman", Font.PLAIN, 14);
	
	private JFrame jframe = null;
	
	private JPanel leftContainer = null;
	private JPanel rightContainer = null;
	private JPanel rightTopContainer = null;
	
	private JLabel labInfoServerIP = null;//, labInfoServerPort = null;
	private JLabel labInfoMsg = null;
	//private JTextField jfServerIP = null, jfServerPort = null;
	
	private JLabel labDBStatus = null;
	

	private ButtonGroup bgConnectedDev = null;
	private HashMap<Socket, JRadioButton> rbConnectDevList = new LinkedHashMap<Socket, JRadioButton>();// socket -> radiobutton
	
	
	private ImageIcon[] imagesT2 = null;
	private String[] imageStrT2 = null;
   
	public void UI_Init(){		
        jframe = new JFrame("Price Checker");
        jframe.setSize(850, 600);
		System.out.println(jframe.getSize().getWidth());
		System.out.println(jframe.getSize().getHeight());	
		
		
		Toolkit tk= Toolkit.getDefaultToolkit();  
		Image img = tk.getImage("D:\\HY\\EclipseProject\\PriceChecker\\null.jpg");  
		jframe.setIconImage(img); 
		
        
        Container container = jframe.getContentPane();
        
        leftContainer = new JPanel();        
        rightContainer = new JPanel();
        rightTopContainer = new JPanel();
        
        leftContainer.setLayout(null);
        rightContainer.setLayout(null);
        rightTopContainer.setLayout(null);
        
        /*
        JLabel label1 = new JLabel("Label 1", JLabel.CENTER);
		label1.setBackground(Color.green);
		label1.setOpaque(true);		
		label1.addMouseListener(new MouseListener() {
			@Override  
		    public void mousePressed(MouseEvent e) {  
		        // TODO Auto-generated method stub  
		        System.out.println("鼠标按下");  
		        if(e.getButton() == MouseEvent.BUTTON1)
		           System.out.println("A");
		    }       
			
			@Override  
		    public void mouseReleased(MouseEvent e) {  
		        // TODO Auto-generated method stub  
		        System.out.println("鼠标松开");  
		    }
			
			@Override  
		    public void mouseClicked(MouseEvent e) {  
		        // TODO Auto-generated method stub  
		        System.out.print("鼠标点击----" + "\t");  
		        if (e.getClickCount()==1) {  
		            System.out.println("单击！");  
		        } else if (e.getClickCount()==2) {  
		            System.out.println("双击！");  
		        } else if (e.getClickCount()==3) {  
		            System.out.println("三连击！！");  
		        }  
		          
		    } 
			
			@Override  
		    public void mouseEntered(MouseEvent e) {  
		        // TODO Auto-generated method stub  
		        //tField.setText("鼠标已经进入窗体");  
		    }
			
			@Override  
		    public void mouseExited(MouseEvent e) {  
		        // TODO Auto-generated method stub  
		        //tField.setText("鼠标已经移出窗体");  
		    }
	    }); 
		rightTopContainer.add(label1);
		
		JButton bntTest = new JButton("Test");
		bntTest.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				Socket socket = GetSocketFromConnectedDevList();
				if(socket != null){
					hyNetworking hyNet = new hyNetworking();
					
					byte[] a = new byte[]{0x31,0x32,0x33};
					
					hyNet.send_data(socket, a);
				}
			}
		});
		rightTopContainer.add(bntTest);
		*/
        
		/*JLabel label2 = new JLabel("Label 2", JLabel.CENTER);
		label2.setBackground(Color.pink);
		label2.setOpaque(true);
		rightContainer.add(label2);*/
		
		labInfoServerIP = new JLabel("Server IP Address");
		labInfoServerIP.setBounds(0, 0, 200, 25);
		labInfoServerIP.setFont(InfoMsgFont);
		leftContainer.add(labInfoServerIP);
		
		/*jfServerIP.setFont(InfoMsgFont);
		jfServerIP.setBounds(120, 0, 120, 25);           	                    	
       	LimitedDocument limitDocumentServerIP = new LimitedDocument(15);
       	limitDocumentServerIP.setAllowChar("0123456789.");
       	jfServerIP.setDocument(limitDocumentServerIP);
       	jfServerIP.setText("192.168.1.5");
       	leftContainer.add(jfServerIP);
		
		labInfoServerPort = new JLabel("Server       Port");
		labInfoServerPort.setBounds(0, 0, 120, 25);
		labInfoServerPort.setFont(InfoMsgFont);
		leftContainer.add(labInfoServerPort);
		
		jfServerPort.setFont(InfoMsgFont);
		jfServerPort.setBounds(120, 0, 120, 25);           	                    	
       	LimitedDocument limitDocumentServerPort = new LimitedDocument(8);
       	limitDocumentServerPort.setAllowChar("0123456789");
       	jfServerPort.setDocument(limitDocumentServerPort);
       	jfServerPort.setText("57600");
       	leftContainer.add(jfServerPort);*/
		
		labInfoMsg = new JLabel("Connected client:<None>");
		labInfoMsg.setBounds(0, 25, 200, 25);
		labInfoMsg.setFont(InfoMsgFont);
		//labInfoMsg.setBackground(Color.pink);		
		//labInfoMsg.setOpaque(true);
		leftContainer.add(labInfoMsg);
		
		bgConnectedDev = new ButtonGroup();
		
		// Right top display
		JLabel labDBTitle = new JLabel("Database Connection Status:");
		labDBTitle.setBounds(0, 0, 300, 25);
		labDBTitle.setFont(InfoMsgFont);
		rightTopContainer.add(labDBTitle);
		
		labDBStatus = new JLabel("OK");
		labDBStatus.setBounds(0, 25, 300, 25);
		labDBStatus.setFont(InfoMsgFont);
		rightTopContainer.add(labDBStatus);
		
		// Right display
/* ak47		JLabel labT1 = new JLabel("Select layout(Idle):");
		labT1.setBounds(0, 10, 170, 25);
		labT1.setFont(InfoMsgFont);
		rightContainer.add(labT1);     	       	
       	
       	JButton bntT10 = new JButton("Set");
       	bntT10.setFont(InfoMsgFont);
       	bntT10.setBounds(320, 10, 60, 25);
       	bntT10.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				//if(!CheckSelectDev()){
					JOptionPane.showMessageDialog(jframe, "Please select a connected device on the left panel");
					return;					
				}// 			
				
				//
				if(tfT1.getText().equals("")){
					JOptionPane.showMessageDialog(jframe, "Please enter the Layout ID.");
					return;
				}
				
				Socket[] socket = new Socket[rbConnectDevList.size()];
				if(GetSocketFromConnectedDevList(socket) == -1)
					return;
				
				hyNetworking hyNet = new hyNetworking();
				byte[] a = new byte[]{HYCommandSet.HY_CMD_SET_IDLE_LAYOUT, Byte.valueOf(tfT1.getText())};
				for(int i = 0; i < socket.length; i++){			
					if(socket[i] != null)
					   hyNet.SendCommand(socket[i], a);
				}//
				
			}
		});
       	rightContainer.add(bntT10);
		
       	JButton bntT11 = new JButton("Get layout(Idle)");
       	bntT11.setFont(InfoMsgFont);
       	bntT11.setBounds(390, 10, 150, 25);
       	bntT11.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				if(!CheckSelectDev()){
					JOptionPane.showMessageDialog(jframe, "Please select a connected device on the left panel");
					return;					
				}
							
				Socket[] socket = new Socket[rbConnectDevList.size()];
				int r = GetSocketFromConnectedDevList(socket);
				if( r == -1)
					return;
				
				if(r == 0){
					JOptionPane.showMessageDialog(jframe, "Can only select ONE device at a time.");
					return;
				}
				
				hyNetworking hyNet = new hyNetworking();
				byte[] a = new byte[]{HYCommandSet.HY_CMD_GET_IDLE_LAYOUT};
				//for(int i = 0; i < socket.length; i++){			
					if(socket[0] != null)
					   hyNet.SendCommand(socket[0], a);
				//}
				
			}
		});
       	rightContainer.add(bntT11);
*/       	
       	
       	JLabel labT2 = new JLabel("Select display layout:");
       	labT2.setBounds(0, 50, 170, 25);
       	labT2.setFont(InfoMsgFont);
		rightContainer.add(labT2);
		
		imagesT2 = new ImageIcon[]{
				new ImageIcon("pics\\Layout#0.jpg"),
				new ImageIcon("pics\\Layout#1.jpg"),
				new ImageIcon("pics\\Layout#2.jpg")
		};
		imageStrT2 = new String[]{"#1","#2","#3"};
		imagesT2[0].setDescription(imageStrT2[0]);
		imagesT2[1].setDescription(imageStrT2[1]);
		imagesT2[2].setDescription(imageStrT2[2]);
       	Integer[] intArray = new Integer[]{new Integer(0),new Integer(1),new Integer(2)};
       	final JComboBox<Integer> setImg = new JComboBox<Integer>(intArray);
       	ComboBoxRenderer renderer= new ComboBoxRenderer(imagesT2, imageStrT2);
       	renderer.setPreferredSize(new Dimension(95, 127));
       	setImg.setRenderer(renderer);
       	setImg.setMaximumRowCount(3);
       	setImg.setBounds(170, 50, 140, 25);
       	rightContainer.add(setImg);
       	
       	JButton bntT20 = new JButton("Set");
       	bntT20.setFont(InfoMsgFont);
       	bntT20.setBounds(320, 50, 60, 25);
       	bntT20.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				if(!CheckSelectDev()){
					JOptionPane.showMessageDialog(jframe, "Please select a connected device on the left panel");
					return;					
				}
			
				Socket[] socket = new Socket[rbConnectDevList.size()];
				if(GetSocketFromConnectedDevList(socket) == -1)
					return;
				
				hyNetworking hyNet = new hyNetworking();
				byte[] a = new byte[]{HYCommandSet.HY_CMD_SET_NONIDLE_LAYOUT, Byte.valueOf((byte)setImg.getSelectedIndex())};
				for(int i = 0; i < socket.length; i++){			
					if(socket[i] != null)
					   hyNet.SendCommand(socket[i], a);
				}
				
			}
		});
       	rightContainer.add(bntT20);
		
       	JButton bntT21 = new JButton("Get display layout");
       	bntT21.setFont(InfoMsgFont);
       	bntT21.setBounds(390, 50, 150, 25);
       	bntT21.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				if(!CheckSelectDev()){
					JOptionPane.showMessageDialog(jframe, "Please select a connected device on the left panel");
					return;					
				}
				
				Socket[] socket = new Socket[rbConnectDevList.size()];
				int r = GetSocketFromConnectedDevList(socket);
				if( r == -1)
					return;
				
				if(r == 0){
					JOptionPane.showMessageDialog(jframe, "Can only select ONE device at a time.");
					return;
				}
				
				hyNetworking hyNet = new hyNetworking();
				byte[] a = new byte[]{HYCommandSet.HY_CMD_GET_NONIDLE_LAYOUT};
				//for(int i = 0; i < socket.length; i++){			
					if(socket[0] != null)
					   hyNet.SendCommand(socket[0], a);
				//}
				
			}
		});
       	rightContainer.add(bntT21);
       	
        JButton bntT30 = new JButton("Reboot device");
       	bntT30.setFont(InfoMsgFont);
       	bntT30.setBounds(320, 90, 130, 25);
       	bntT30.addActionListener(new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {			
				if(!CheckSelectDev()){
					JOptionPane.showMessageDialog(jframe, "Please select a connected device on the left panel");
					return;					
				}
				
				Object[] options = {"Yes","No"};
		        int response = JOptionPane.showOptionDialog ( null, "Are you sure you wish to reboot the device?","Reboot", JOptionPane.YES_OPTION ,JOptionPane.PLAIN_MESSAGE,
      		                   null, options, options[0] ) ;				
		        if (response != 0)
		        	return;
				
				Socket[] socket = new Socket[rbConnectDevList.size()];
				if(GetSocketFromConnectedDevList(socket) == -1)
					return;
				
				hyNetworking hyNet = new hyNetworking();
				byte[] a = new byte[]{HYCommandSet.HY_CMD_REBOOT_DEVICE};
				//byte[] a = new byte[]{(byte)0xA8 ,(byte)0x00 ,(byte)0x38 ,(byte)0x2E ,(byte)0x38 ,(byte)0x00 ,'T' 
				//		,'e' ,'x' ,'t' ,(byte)0x00 ,(byte)0x58 ,(byte)0x59 ,(byte)0x5A ,(byte)0x2E ,(byte)0x6A
				//		,(byte)0x70 ,(byte)0x67 ,(byte)0x00};
				for(int i = 0; i < socket.length; i++){			
					if(socket[i] != null)
					   hyNet.SendCommand(socket[i], a);
				}
				
			}
		});
bntT30.setEnabled(false);  	
       	rightContainer.add(bntT30);       	   	
      	
		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				false, rightTopContainer, rightContainer);
		splitPane1.setDividerLocation(0.1);
		splitPane1.setResizeWeight(0.1);				
		splitPane1.setOneTouchExpandable(false);//设置JSplitPane是否可以展开或收起(如同文件总管一般)，设为true表示打开此功能。		
		splitPane1.setDividerSize(8);
		splitPane1.setEnabled(false);
		container.add(splitPane1);
		
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				false, leftContainer, splitPane1);
		splitPane2.setResizeWeight(0.3);
		splitPane2.setDividerLocation(0.3);	
		splitPane2.setOneTouchExpandable(false);
		splitPane2.setDividerSize(8);
		splitPane2.setEnabled(false);
		container.add(splitPane2);
		
		leftContainer.setVisible(true);
		leftContainer.updateUI();		
		rightContainer.setVisible(true);
		rightContainer.updateUI();
		rightTopContainer.setVisible(true);
		rightTopContainer.updateUI();			
		
		jframe.setVisible(true);
		//jframe.setLocationRelativeTo(null);
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});		
		
	}
	
	public void Close_UI(){
        leftContainer.removeAll();;
        rightContainer.removeAll();;
        rightTopContainer.removeAll();
		
        jframe.dispose();
	}
	
	public void Update_InfoServer_Msg(String str){
		labInfoServerIP.setText(str);
	}
   
	public void Update_InfoClient_Msg(String str){
		labInfoMsg.setText(str);
	}
	
	public void Update_DB_Status_Color(Color color){
		labDBStatus.setForeground(color);
	}
	
	public void Update_DB_Status(String str){
		labDBStatus.setText(str);
	}
	
	public void MsgBox(String str){
		JOptionPane.showMessageDialog(jframe, str);
	}
	
	public void Show_Display_Layout_ID(byte index){
		//JLabel iconLabel = new JLabel(imageStrT2[index]);
		//JPanel iconPanel = new JPanel(new GridBagLayout());
	    //iconPanel.add(iconLabel);
	    
	    JPanel textPanel = new JPanel(new GridLayout(0, 1));
	    textPanel.add(new JLabel(imagesT2[index]));
	    
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.add(textPanel);
	    //mainPanel.add(iconPanel, BorderLayout.WEST);
	    JOptionPane.showMessageDialog(this.jframe, mainPanel, "Display layout" + imagesT2[index],
	                                  JOptionPane.PLAIN_MESSAGE);
		
		/*
        JOptionPane.showMessageDialog(
                null,
                imageStrT2[index],
                "Display layout", JOptionPane.INFORMATION_MESSAGE,
                imagesT2[index]);*/
	}
	
	private JRadioButton rbAll = null;
	
	public void Add_Connected_Dev(Socket socket){
		String ip = socket.getInetAddress().getHostAddress();
		
		if(rbConnectDevList.size() == 0){
			rbAll = new JRadioButton("Send to all");
			rbAll.setFont(InfoMsgFont);
			rbAll.setBounds(0, 60, 160, 25);
			
			leftContainer.add(rbAll);
			bgConnectedDev.add(rbAll);
		}
		
		JRadioButton rb = new JRadioButton(ip);
		rb.setFont(InfoMsgFont);
		rb.setBounds(0, 85 + rbConnectDevList.size()*30, 160, 25);
		leftContainer.add(rb);
			
		bgConnectedDev.add(rb);	
		
		rbConnectDevList.put(socket, rb);
		
		
		leftContainer.updateUI();
	}
	
	public void Remove_Connected_Dev(Socket socket){
    
		//String ip = socket.getInetAddress().getHostAddress();
		
		if(rbConnectDevList.containsKey(socket)){
			JRadioButton rb = rbConnectDevList.get(socket);
			//System.out.println("A = " + ConnectDevList.size());
			leftContainer.remove(rb);//remove radiobutton from UI container
			bgConnectedDev.remove(rb);//remove radiobutton from ButtonGroup
			rbConnectDevList.remove(socket);
			//System.out.println("B = " + ConnectDevList.size());
			
			Iterator<Socket> it = rbConnectDevList.keySet().iterator(); 

			int cnt = 0;
			while(it.hasNext()){
               	JRadioButton rbNext = rbConnectDevList.get(it.next()); 
               	rbNext.setBounds(0, 60 + cnt*30, 160, 25);
               	cnt++;
			}
		}
		
		if(rbConnectDevList.size() == 0){
			leftContainer.remove(rbAll);
			bgConnectedDev.remove(rbAll);
			
			rbAll = null;
		}
		
		leftContainer.updateUI();
	}
	
	public boolean CheckSelectDev(){
		Enumeration<AbstractButton> en = bgConnectedDev.getElements();
		
		while(en.hasMoreElements()){
			AbstractButton bnt = en.nextElement();
			
			if(bnt.isSelected())
				return true;
		}
		
		return false;
	}
	
	/*
	 * Return: -1: no device is selected
	 *         0:select "Send to all"
	 *         1:select a device
	 */
	public int GetSocketFromConnectedDevList(Socket[] rSocket){
		Enumeration<AbstractButton> en = bgConnectedDev.getElements();
		
		while(en.hasMoreElements()){
			AbstractButton bnt = en.nextElement();
			if(bnt.isSelected()){
			    String ip = bnt.getText();
	            
			    //Get Socket			    
			    Iterator<Socket> it = rbConnectDevList.keySet().iterator();
			    
			    if(ip.equals("Send to all")){
			    	int i = 0;
			    	while(it.hasNext()){
			    		rSocket[i++] = it.next();
			    	}
			    	return 0;
			    }			    
			    
			    while(it.hasNext()){
			    	Socket socket = it.next();
				    if(socket.getInetAddress().getHostAddress().equals(ip)){
			    		rSocket[0] = socket;
			    		return 1;
			    	}
			    }
			}				
		}		
		
		return -1;
	}	

class LimitedDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int maxLength = -1;
	private String allowCharAsString = null;

	public LimitedDocument() {
		super();
	}

	public LimitedDocument(int maxLength) {
		super();
		this.maxLength = maxLength;
	}

	public void insertString(int offset, String str, AttributeSet attrSet) throws BadLocationException {
		if (str == null) {
			return;
		}
		if (allowCharAsString != null && str.length() == 1) {
			if (allowCharAsString.indexOf(str) == -1) {
				return;
			}
		}
		char[] charVal = str.toCharArray();
		String strOldValue = getText(0, getLength());
		char[] tmp = strOldValue.toCharArray();
		if (maxLength != -1 && (tmp.length + charVal.length > maxLength)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		super.insertString(offset, str, attrSet);
	}

	public void setAllowChar(String str) {
		allowCharAsString = str;
	}

}


class ComboBoxRenderer extends JLabel implements ListCellRenderer {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font uhOhFont;
	
	private ImageIcon[] images = null;
	private String[] imageStr = null;

	public ComboBoxRenderer(ImageIcon[] image, String[] imageStr) {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		
		this.images = image;
		this.imageStr = imageStr;
	}

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
	public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {
		//Get the selected index. (The index param isn't
		//always valid, so just use the value.)
		int selectedIndex = ((Integer)value).intValue();
		
		

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		//Set the icon and text.  If icon was null, say so.
		ImageIcon icon = this.images[selectedIndex];
		String pet = this.imageStr[selectedIndex];
		setIcon(icon);
		if (icon != null) {
			setText(pet);
			setFont(list.getFont());
		} else {
			setUhOhText(pet + " (no image available)",	list.getFont());
		}

		return this;
	}

	//Set the font and text when no image was found.
	protected void setUhOhText(String uhOhText, Font normalFont) {
		if (uhOhFont == null) { //lazily create this font
			uhOhFont = normalFont.deriveFont(Font.ITALIC);
		}
		setFont(uhOhFont);
		setText(uhOhText);
	}
}

}


