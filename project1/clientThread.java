import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.String;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class clientThread implements Runnable {
    private String serverIP;
    private int serverPort;
    private Socket connectionSock;

    public clientThread(String serverIP, int serverPort) {
	this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
 
    public void handleConnection()throws IOException, ClassNotFoundException {
	//acquire the information of the peer
        System.out.println("please type in your name:");
        Scanner sc = new Scanner(System.in);
        String peerName = sc.next();

        //acquire the information of the files that the peer wants to share
        System.out.println("please input the number of files you want to share:");
        int num_of_files = Integer.parseInt(sc.next());

        //acquire the file information of this peer
        ArrayList<String> FileAndSize = new ArrayList<String>();
        for(int i = 0; i < num_of_files; i++) {
            System.out.println("please input the file name of the file you wish to share");
            String fileName = sc.next();
            FileAndSize.add(fileName);
            System.out.println("please input the file size of the file you wish to share");
            String fileSize = sc.next();
            FileAndSize.add(fileSize);
	}
        //create socket and connect to the server
        connectionSock = new Socket(serverIP, serverPort);
        
	//acquire the IP and Port number of the peer
        InetAddress userIP = connectionSock.getLocalAddress();
        int userPort = connectionSock.getLocalPort();
        System.out.println("the IP address of this PC is: " + userIP);
        System.out.println("the port of this PC is:" + userPort);
        
        boolean flagRegister = false;
        Peer peer = new Peer(peerName, userIP, userPort, FileAndSize);
        while(true){
            System.out.println("please choose the operation: 1 register 2 file list request 3 location request 4 leave the network");
            String input = sc.next();
            //handle different operations with different functions
            if(input.equals("1")){
		Register(peer, connectionSock);
	    } else if(input.equals("2")) {
		filelistRequest(connectionSock);
	    } else if(input.equals("3")){
                locationRequest(connectionSock, sc);
	    } else if(input.equals("4")){
		leaveRequest(connectionSock);
                break;
	    }

	}
        System.out.println("you leave the network");
        connectionSock.close();
    }

    public void Register(Peer peer, Socket clientSock) throws IOException, ClassNotFoundException {
	//send request sting
        //System.out.println("in register now");
        String insReg = "-reg";
        ObjectOutputStream reg_ins = new ObjectOutputStream(clientSock.getOutputStream());
        reg_ins.writeObject(insReg);
        
        //send peer information
        reg_ins.writeObject(peer);
        reg_ins.flush();

        ObjectInputStream regConfirm = new ObjectInputStream(clientSock.getInputStream());
        String receConfirm = (String)regConfirm.readObject();
        if(receConfirm.equals("-success")){
	    System.out.println("register succeed");
	}
    } 

    public void filelistRequest(Socket clientSock) throws IOException, ClassNotFoundException {
	//send request string
        String insList = "-list";
        ObjectOutputStream list_ins = new ObjectOutputStream(clientSock.getOutputStream());
        list_ins.writeObject(insList);
        list_ins.flush();

        //receive file arraylist
        ObjectInputStream listRece = new ObjectInputStream(clientSock.getInputStream());
        ArrayList<File> fileList = (ArrayList<File>)listRece.readObject();
        for(int i = 0; i < fileList.size(); i++){
            String name = fileList.get(i).getName();
            int size = fileList.get(i).getSize();
            int chunkNum = fileList.get(i).getchunkNum();
            System.out.println("File Name: " + name + " " + "Size: " + size + " " + "Number of Chunks: " + chunkNum);
	}
    }

    public void locationRequest(Socket clientSock, Scanner sc) throws IOException, ClassNotFoundException {
	//send request string
        String insLocation = "-location";
        ObjectOutputStream location_ins = new ObjectOutputStream(clientSock.getOutputStream());
        location_ins.writeObject(insLocation);
        //send file name
        System.out.println("please enter the file name");
        String fileName = sc.next();
        System.out.println("name:" + fileName);
        location_ins.writeObject(fileName);
        location_ins.flush();

        //receive information
        ObjectInputStream locationRece = new ObjectInputStream(clientSock.getInputStream());
        ArrayList<Peer> locaList = (ArrayList<Peer>)locationRece.readObject();
        for(int i = 0; i < locaList.size(); i++) {
	    System.out.println("peer IP: " + locaList.get(i).getIP() + "peer port: " + locaList.get(i).getPort());
	}
        System.out.println("That's all");
    }

    public void leaveRequest(Socket clientSock) throws IOException, ClassNotFoundException {
	//send request string
        String insLeave = "-leave";
        ObjectOutputStream leave_ins = new ObjectOutputStream(clientSock.getOutputStream());
        leave_ins.writeObject(insLeave);
        InetAddress userIP = connectionSock.getLocalAddress();
        leave_ins.writeObject(userIP);
        leave_ins.flush();

    }

    public void run() {
        try {
            handleConnection();
	} catch (IOException e) {
            e.printStackTrace();
	} catch (ClassNotFoundException e) {
            e.printStackTrace();
	}
    }
}
        

        
