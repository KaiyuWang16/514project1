import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.String;
import java.util.ArrayList;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
	//create the connection and the download thread
        if (args.length != 2) {
	    throw new IllegalArgumentException("Input format:<Server Address> <Server Port>");
	}
       
        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        
        //begin the connectionThread
        Thread thread = new Thread (new clientThread(serverIP, serverPort));
        thread.start();
    }
} 
