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

        if (args.length != 3) {
            System.out.println("Usage Router <ID> <nem-host> <nem-port>");
            System.exit(1);
        }
        String host = args[1];
        int id = Integer.parseInt(args[0]);
        int nemPort = Integer.parseInt(args[2]);
        configWrapper config = new configWrapper("config.txt");
        try{
            InetAddress nemHost = InetAddress.getByName(host);
            try{
                DatagramSocket s = new DatagramSocket();//new DatagramSocket();
                //DO NOT modify this code.
                IUDPSocket sock = new UDPSocket(s, nemHost, nemPort);
                sock.setSoTimeout(config.ARQ_TIMER);
                ILogFactory logFactory = new LogFactory();
                Router r = new Router(id, sock, logFactory);
                System.out.println("Routing ...");
                r.run();
                s.close();
                System.out.println("DONE");
            }catch(SocketException e){
                System.out.println("Error creating DatagramSocket "+e);
            }
        }catch(UnknownHostException e){
            System.out.println("Unknown host: "+e);
        }
    }
}
