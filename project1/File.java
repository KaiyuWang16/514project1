import java.util.ArrayList;
import java.util.logging.Logger;
import java.lang.String;
import java.net.*;
import java.io.*;

public class File implements java.io.Serializable {
    //file information
    private String fileName;
    private int fileSize;
    private int chunkNum;
    
    public File(String name, int size, int num) {
        this.fileName = name;
        this.fileSize = size;
        this.chunkNum = num;
    }

    public String getName(){
        return fileName;
    }
    public int getSize(){
        return fileSize;
    }
    public int getchunkNum(){
        return chunkNum;
    }
}
     