package cpsc441.solution;

import cpsc441.doNOTmodify.DVRInfo;
import cpsc441.doNOTmodify.ILogFactory;
import cpsc441.doNOTmodify.IUDPSocket;
import cpsc441.doNOTmodify.Util;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.io.*;

public class Router implements Runnable {
    public IUDPSocket socket;
    public ILogFactory log;
    public int ID;
    public configWrapper config;
    public int[] nextHop,linkCost,minCost;
    public String newLine = System.getProperty("line.separator");
    public String finalLog = "";
    public boolean gotUpdated = false;

    public Router(int id,IUDPSocket sock, ILogFactory logFactory)     {
        this.socket = sock;
        this.log    = logFactory;
        this.ID     = id;
        this.config = new configWrapper("config.txt");
        System.out.println("Generating Router ["+this.ID+"].");
        this.init();
    }

    public void init(){
        DVRInfo dvr = new DVRInfo(this.ID, 0 , 0, DVRInfo.PKT_HELLO); // hello packet!
        boolean success = false;
        while( success == false){
            try{
                this.socket.send(dvr);
                DVRInfo rcvPacket = this.socket.receive();
                this.nextHop = new int[rcvPacket.mincost.length];
                this.linkCost = rcvPacket.mincost;
                this.minCost = rcvPacket.mincost;
                initNextHop(); // initialize the nextHop
                //System.out.println(""+intArrayToString(this.linkCost) + "\n"+intArrayToString(this.minCost));
                this.finalLog+= "["+this.ID+"] send "+rcvPacket.toString()+this.newLine;

                success = true; // LET IT HANG!!!
            }catch(SocketTimeoutException e){
                //System.out.println("Router "+this.ID+ "socket timed out during init(): "+e);
                continue;
            }catch (IOException e){
                System.out.println(" IO exception: " + e);
                continue;
            }
        }// end of while
    }

    public void run() {
        // find all the adjacent IDs
        List<DVRInfo> DVRList = new ArrayList<DVRInfo>();
        for(int i=0;i<this.linkCost.length;i++){
            if(this.linkCost[i]!=config.COST_INFTY && this.linkCost[i]!=0){
                DVRInfo dvr = new DVRInfo(this.ID, i , 0, DVRInfo.PKT_ROUTE); // hello packet!
                ///System.out.println("in ["+this.ID+"] "+i+"-"+this.linkCost[i]);
                dvr.mincost = this.minCost;
                DVRList.add(dvr);
            }
        }
        //DVRInfo dvr = new DVRInfo(this.ID, 1 , 0, DVRInfo.PKT_ROUTE); // hello packet!
        boolean success = false;
        while(success == false){
            try{
                //System.out.println(dvr.toString());
                if(changed)
                for(DVRInfo anDvr : DVRList){
                    this.finalLog += ("["+this.ID+"] sent "+anDvr.toString()+this.newLine);
                    this.socket.send(anDvr);
                }

                DVRInfo rcvPacket = this.socket.receive();

                //System.out.println("In Router["+this.ID+"] received: "+rcvPacket.toString());
                receivePacketHandler(rcvPacket);
                //success = true; // LET IT HANG!!!
            }catch(SocketTimeoutException e){
                //System.out.println("Router "+this.ID+ "socket timed out during run(): "+e);
                continue;
            }catch (IOException e){
                System.out.println(" IO exception: " + e);
                continue;
            }
        }
    }

    // handles the packet that received when routing
    public void receivePacketHandler(DVRInfo rcvDVR){
        if(rcvDVR.type == DVRInfo.PKT_ROUTE && rcvDVR.sourceid!=this.config.NEM_ID) { // check if the DVR is legit from neighbours
            int[] receivedMinCost = rcvDVR.mincost;
            //System.out.println(rcvDVR.toString());
            for (int i = 0; i < receivedMinCost.length; i++) {
                if ((receivedMinCost[i] + this.minCost[rcvDVR.sourceid]) < this.minCost[i]) {
                    System.out.println("Update in Router[" + this.ID + "] distance to [" + i + "] minCost:" + this.minCost[i] + "->"
                            + (receivedMinCost[i] + this.linkCost[rcvDVR.sourceid]) + " & nextHop ID:" + this.nextHop[i] + "->"
                            + rcvDVR.sourceid);
                    this.finalLog+=("["+this.ID+"] receive "+rcvDVR.toString()+this.newLine); //updating log file
                    this.minCost[i] = (receivedMinCost[i] + this.linkCost[rcvDVR.sourceid]);
                    this.nextHop[i] = rcvDVR.sourceid;
                }
            }
        }else if(rcvDVR.type == DVRInfo.PKT_QUIT){      // when receive QUIT packet
            System.out.println("Received Quit Packet!!");
            PrintStream log = new PrintStream(System.out);
            String readableDV = Util.printdv(this.ID, this.minCost,this.nextHop);
            log.println(readableDV);

            this.finalLog+=("["+this.ID+"] quit "+rcvDVR.toString()+this.newLine);
            try{
                FileWriter logFile = new FileWriter(this.ID+".log");
                logFile.write(finalLog);
                logFile.close();
            }catch(IOException e){
                System.out.println("Unable to operate file: "+this.ID+".log     due to:" + e);
            }
            // Wanna close the socket.
            System.exit(1);
            // Not a route packet just ignored for now.
        }
    }


    // helper functions
    public String intArrayToString(int[] array){
        String returnString = "\t";
        for(int i=0;i<array.length;i++){
            returnString += array[i] + ",";
        }
        return returnString;
    }

    // Initialize nextHop array.
    public void initNextHop(){
        for(int i=0;i<this.nextHop.length;i++){
            if(this.linkCost[i]!=999&&this.linkCost[i]!=0)
            {
                this.nextHop[i] = this.ID;
            }else{
                this.nextHop[i] = -1; // no way to reach to that router yet
            }
        }
    }
}
