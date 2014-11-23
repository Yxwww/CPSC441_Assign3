package cpsc441.solution;

import cpsc441.doNOTmodify.ILogFactory;
import cpsc441.doNOTmodify.IUDPSocket;
import cpsc441.doNOTmodify.LogFactory;
import cpsc441.doNOTmodify.UDPSocket;

import java.net.*;

public class Main {

    public static void main(String [] args) {

        configWrapper config = new configWrapper("config.txt");

        //These two values need to be initialized from args
        // before the algorithm (which should be implemented
        // in the Router class) can work...
        int id = -1;
        int nemPort = 3000;
        try{
            InetAddress nemHost = InetAddress.getByName("localhost");;
            //DatagramSocket s = new DatagramSocket();
            //DO NOT modify this code.
            /*IUDPSocket sock = new UDPSocket(s, nemHost, nemPort);
            ILogFactory logFactory = new LogFactory();
            Router r = new Router(id, sock, logFactory);
            r.run();*/
            Router[] listRouters = new Router[5];
            for(int i=0;i<listRouters.length;i++){
                try{
                    DatagramSocket s = new DatagramSocket();
                    IUDPSocket sock = new UDPSocket(s, nemHost, nemPort);
                    sock.setSoTimeout(config.ARQ_TIMER);
                    ILogFactory logFactory = new LogFactory();
                    listRouters[i] = new Router(i,sock,logFactory);
                }catch (SocketException e){
                    System.out.println("oops on datagramSocket: " + e);
                }
            }
        }catch(UnknownHostException e){
            System.out.println("Unknown host: "+e);
        }


/*
        int Port = 3000;
        String host = "localhost";

        Router[] listRouters = new Router[5];
        for(int i=0;i<listRouters.length;i++){
            try{
                DatagramSocket s = new DatagramSocket();
                IUDPSocket sock = new UDPSocket(s);
                ILogFactory logFactory = new LogFactory();
                listRouters[i] = new Router(i,sock,logFactory);
            }catch (SocketException e){
                System.out.println("oops datagramSocket: " + e);
            }
        }*/
    }
}
