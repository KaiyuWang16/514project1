import java.lang.String;
import java.util.ArrayList;
import java.net.InetAddress;

public class Peer implements java.io.Serializable {
    private String peerName;
    private InetAddress peerIP;
    private int peerPort;
    private ArrayList<String> FileAndSize = new ArrayList<String>();
    static final long serialVersionUID = 99999999999999L;
    
    public Peer(String peerName, InetAddress peerIP, int peerPort, ArrayList<String> FileAndSize){
        this.peerName = peerName;
        this.peerIP = peerIP;
        this.peerPort = peerPort;
        for(int i = 0; i < FileAndSize.size(); i++){
            this.FileAndSize.add(FileAndSize.get(i));
	}
    }

    public InetAddress getIP(){
	return peerIP;
    }
    public int getPort(){
        return peerPort;
    }
    public ArrayList<String> getFileInfo(){
        return FileAndSize;
    }
}
