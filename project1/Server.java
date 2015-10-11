import java.util.ArrayList;
import java.util.logging.Logger;
import java.lang.String;
import java.net.*;
import java.io.*;

public class Server {
    //two arraylists to store the information of files and peers
    private static ArrayList<File> fileReferences = new ArrayList<File>();
    private static ArrayList<Peer> peerReferences = new ArrayList<Peer>();
   
    public static void main(String[] args) throws IOException {
	//the port number is the input
         if (args.length != 1) {
	    throw new IllegalArgumentException("Input format: <port number>");
	 }
         int portNumber = Integer.parseInt(args[0]);
         
         //create the server socket
         ServerSocket serverSock = new ServerSocket(portNumber);
         Logger logger = Logger.getLogger("practical");
    
         while(true){
             //monitor the requests from peers
             Socket clientSock = serverSock.accept();
             System.out.println("new peer!");
             Thread thread = new Thread(new sendThread(clientSock, logger));           
             thread.start();
           
	 }
    }

    public static void addPeer(Peer peer){
        peerReferences.add(peer);
    }
    public static void addFile(Peer peer){
	ArrayList<String> FileAndSize = peer.getFileInfo();
        for (int i = 0; i < FileAndSize.size(); i = i + 2) {
	    String filename = FileAndSize.get(i);
            int filesize = Integer.parseInt(FileAndSize.get(i + 1));
            File new_file = new File(filename, filesize, 2);
            fileReferences.add(new_file);
	}
    }
   
    public static ArrayList<File> getFileInfo(){
	return fileReferences;
    }
    public static ArrayList<Peer> getPeerInfo(){
        return peerReferences;
    } 
      
}
     
