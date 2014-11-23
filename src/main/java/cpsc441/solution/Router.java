package cpsc441.solution;

import cpsc441.doNOTmodify.DVRInfo;
import cpsc441.doNOTmodify.ILogFactory;
import cpsc441.doNOTmodify.IUDPSocket;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

public class Router implements Runnable {
    public IUDPSocket socket;
    public ILogFactory log;
    public int ID;
    public configWrapper config;
    public int[] nextHop,linkCost,minCost;

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
                //System.out.println(""+intArrayToString(this.linkCost) + "\n"+intArrayToString(this.minCost));
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
                System.out.println("sending dvr to "+i);
                DVRInfo dvr = new DVRInfo(this.ID, i , 0, DVRInfo.PKT_ROUTE); // hello packet!
                dvr.mincost = this.minCost;
                DVRList.add(dvr);
            }
        }
        //DVRInfo dvr = new DVRInfo(this.ID, 1 , 0, DVRInfo.PKT_ROUTE); // hello packet!
        boolean success = false;
        while(success == false){
            try{
                //System.out.println(dvr.toString());
                for(DVRInfo anDvr : DVRList){
                    this.socket.send(anDvr);
                }

                DVRInfo rcvPacket = this.socket.receive();

                System.out.println("In Router["+this.ID+"]  -- "+rcvPacket.toString());
                //System.out.println(""+intArrayToString(this.linkCost) + "\n"+intArrayToString(this.minCost));
                //success = true; // LET IT HANG!!!
            }catch(SocketTimeoutException e){
                //System.out.println("Router "+this.ID+ "socket timed out during run(): "+e);
                continue;
            }catch (IOException e){
                System.out.println(" IO exception: " + e);
                continue;
            }
        }

       /* for(int i=0;i<this.linkCost.length;i++){
            if(this.linkCost[i]!=config.COST_INFTY && this.linkCost[i]!=0){
                System.out.println("sending dvr to "+this.linkCost[i]);
                DVRInfo dvr = new DVRInfo(this.ID, this.linkCost[i] , 0, DVRInfo.PKT_ROUTE); // hello packet!
                dvr.mincost = this.minCost;

            }
        }
        DVRInfo dvr = new DVRInfo(this.ID, 0 , 0, DVRInfo.PKT_HELLO); // hello packet!
        boolean success = false;
        while(success == false){
            try{
                this.socket.send(dvr);
                System.out.println("hehe");
                DVRInfo rcvPacket = this.socket.receive();
                this.nextHop = new int[rcvPacket.mincost.length];
                this.linkCost = rcvPacket.mincost;
                this.minCost = rcvPacket.mincost;
                //System.out.println(""+intArrayToString(this.linkCost) + "\n"+intArrayToString(this.minCost));
                success = true; // LET IT HANG!!!
            }catch(SocketTimeoutException e){
                System.out.println("Router "+this.ID+ "socket timed out during run(): "+e);
                continue;
            }catch (IOException e){
                System.out.println(" IO exception: " + e);
                continue;
            }
        }// end of while
        */
    }

    // helper functions
    public String intArrayToString(int[] array){
        String returnString = "\t";
        for(int i=0;i<array.length;i++){
            returnString += array[i] + ",";
        }
        return returnString;
    }
}
