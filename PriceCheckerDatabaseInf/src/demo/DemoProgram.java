package demo;

import java.io.File;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

import PriceCheckerInf.DatabaseInterface;

public class DemoProgram implements DatabaseInterface{
     public SQLiteConnection dba = null;
	
	 public boolean connect_DB(){
		 
		 dba = new SQLiteConnection(new File(System.getProperty("user.dir") + "\\Test.db"));

		 try {					
	          dba.open(false); 	
	     }catch(SQLiteException ex){
		 	  System.out.println(ex.getMessage());
		 	  
		 	  if(dba != null){
		 		  dba.dispose();
		 	  }
		 	  return false;
		 }
		 
		 return true;
	 }
	 
	 public void disconnect_DB(){
		 dba.dispose();
	 }
	 
	 public boolean queryProductInfo(String strBarcode, String[] rPrice, String[] rProductName, String[] rProductImage){
         boolean bRet = false;
		 SQLiteStatement sta = null;
		 
		 try {
			 sta = dba.prepare(String.format("SELECT Price,ProductName,ProductImage FROM MyTable WHERE Barcode = \"" + strBarcode + "\"" ));

			 if ( sta.step() ) {
				 rPrice[0] = sta.columnString(0);
				 rProductName[0] = sta.columnString(1);
				 rProductImage[0] = sta.columnString(2);

				 bRet = true;
		     }
		 }catch(SQLiteException ex){
			 System.out.println(ex.getMessage());
		 }finally{
			 if ( sta != null ) sta.dispose();
		 }
		 
		 return bRet;		 
	 }
}
