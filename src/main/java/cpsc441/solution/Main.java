package cpsc441.solution;

import cpsc441.doNOTmodify.ILogFactory;
import cpsc441.doNOTmodify.IUDPSocket;
import cpsc441.doNOTmodify.LogFactory;
import cpsc441.doNOTmodify.UDPSocket;

import java.net.*;

public class Main {

    public static void main(String [] args) {
        //These two values need to be initialized from args
        // before the algorithm (which should be implemented
        // in the Router class) can work...
        if (args.length != 1) {
            System.out.println("Usage Router ID");
            System.exit(1);
        }
        int id = Integer.parseInt(args[0]);
        int nemPort = 3000;
        configWrapper config = new configWrapper("config.txt");
        try{
            InetAddress nemHost = InetAddress.getByName("localhost");
            try{
                DatagramSocket s = new DatagramSocket();//new DatagramSocket();
                //DO NOT modify this code.
                IUDPSocket sock = new UDPSocket(s, nemHost, nemPort);
                sock.setSoTimeout(config.ARQ_TIMER);
                ILogFactory logFactory = new LogFactory();
                Router r = new Router(id, sock, logFactory);
                r.run();
            }catch(SocketException e){
                System.out.println("Error creating DatagramSocket "+e);
            }



            /*Router[] listRouters = new Router[5];
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
            for(int i=0;i<listRouters.length;i++){
                listRouters[i].run();
            }*/
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
