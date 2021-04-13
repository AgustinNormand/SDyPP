package src.main.java.com.TrabajoPractico_1.app;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	// parametros de consola
    	InetAddress ip = null;
		try {
			ip = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int port = Integer.valueOf(args[1]);
        Server server = new Server(ip, port);
    }
}
