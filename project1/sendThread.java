import java.io.*;
import java.net.*;
import java.util.*;
//import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.String;

public class sendThread implements Runnable {
    private Socket clientSock;
    private Logger logger;

    public sendThread(Socket clientSock, Logger logger){
        this.clientSock = clientSock;
        this.logger = logger;
    }

    public void handleRequest() throws IOException, ClassNotFoundException {
	//keep receiving the requests
        while(true) {
            ObjectInputStream serverIn = new ObjectInputStream(clientSock.getInputStream());
            String insKind_str = (String)serverIn.readObject();
            
            if(insKind_str.equals("-reg")){
                handleRegister(serverIn);
	    } else if(insKind_str.equals("-list")){
                handleFileList();
	    } else if (insKind_str.equals("-location")){
	        handleLocation(serverIn);
	    } else if(insKind_str.equals("-leave")){
                handleLeave(serverIn);
		break;
	    }
	}
        clientSock.close();
    }

    public void handleRegister(ObjectInputStream serverIn) throws IOException, ClassNotFoundException {
	//receive the peer information
        Peer peer = (Peer)serverIn.readObject();

        //store information
        Server.addPeer(peer);
        Server.addFile(peer);

        ObjectOutputStream regConfirm = new ObjectOutputStream(clientSock.getOutputStream());
        regConfirm.writeObject("-success");
        regConfirm.flush();
    }

    public void handleFileList() throws IOException, ClassNotFoundException {
	//send the file list
        ObjectOutputStream sendList = new ObjectOutputStream(clientSock.getOutputStream());
        sendList.writeObject(Server.getFileInfo());
        sendList.flush();
    }

    public void handleLocation(ObjectInputStream serverIn) throws IOException, ClassNotFoundException {
	//receive the file name
        //ObjectInputStream locationIn = new ObjectInputStream(clientSock.getInputStream());
        String fileName = (String) serverIn.readObject();
        
        //search for the file
        ArrayList<Peer> peerList = Server.getPeerInfo();
        ArrayList<Peer> locationList = new ArrayList<Peer>();
        for(int i = 0; i < peerList.size(); i++){
	    ArrayList<String> FileAndSize = peerList.get(i).getFileInfo();
            for(int j = 0; j < FileAndSize.size(); j += 2){
		if(FileAndSize.get(j).equals(fileName)){
		    locationList.add(peerList.get(i));
		}
	    }
	}
        //send the information back
        ObjectOutputStream sendLocation = new ObjectOutputStream(clientSock.getOutputStream());
        sendLocation.writeObject(locationList);
        sendLocation.flush();

    }

    public void handleLeave(ObjectInputStream serverIn) throws IOException, ClassNotFoundException {
	InetAddress IP = (InetAddress)serverIn.readObject();
        System.out.println("someone want to leave: " + IP);
        //search for the peer
        ArrayList<String> FileAndSize = new ArrayList<String>();
        for(int i = 0; i < Server.getPeerInfo().size(); i++){
	    if(Server.getPeerInfo().get(i).getIP().equals(IP)){
                FileAndSize = Server.getPeerInfo().get(i).getFileInfo();
                Server.getPeerInfo().remove(i);
	    }
	}
	//remove the files;
        HashSet<String> hs = new HashSet<String>();
        for(int i = 0; i < FileAndSize.size(); i += 2){
	    hs.add(FileAndSize.get(i));
            System.out.println(FileAndSize.get(i));
	}
        //use iterator to remove
        Iterator<File> it = Server.getFileInfo().iterator();
        while(it.hasNext()){
            String name = it.next().getName();
	    if(hs.contains(name)){
                System.out.println("delete:" + name);
		it.remove();
	    }
	}
       
    }
                       
            

    public void run() {
         try {
	    handleRequest();
	} catch (IOException e) {
            e.printStackTrace();
	} catch (ClassNotFoundException e) {
            e.printStackTrace();
	}
    }
}