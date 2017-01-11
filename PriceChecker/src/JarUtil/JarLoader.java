package JarUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import PriceCheckerInf.DatabaseInterface;

/*
 * Method 1
public class JarLoader extends URLClassLoader{
	
	//static class JarLoader extends URLClassLoader {

        public JarLoader(URL[] urls) {
            super(urls);
        }

        public JarLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public void addJar(URL url) {
            this.addURL(url);
        }

    //}
}
*/

//  Method 2
public class JarLoader{
	private String strJarPath = null, strJarName = null;
	
	public JarLoader(String path, String JarName){
		this.strJarPath = path;
		this.strJarName = JarName;       
	}
	
	public boolean check_JARFile(){
		boolean bRet = true;
		
        System.out.println(strJarPath + strJarName); 
        
        File f = new File(strJarPath + strJarName);
        if( !f.exists() ){
        	bRet = false;
        }
        
        return bRet;
	}
	
	public DatabaseInterface getPriceCheckerDatabaseInstance(){
		DatabaseInterface priceCheckerInf = null;
				
		try {
			URL url = new URL("file:" + strJarPath + strJarName);
			URLClassLoader myClassLoader = new URLClassLoader(  
					                       new URL[] { url }, Thread.currentThread().getContextClassLoader());
			Class<?> myClass = myClassLoader.loadClass("demo.DemoProgram");
		
			priceCheckerInf = (DatabaseInterface)myClass.newInstance();
			myClassLoader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return priceCheckerInf;
	}
}

