package cpsc441.solution;

import cpsc441.doNOTmodify.ILogFactory;
import cpsc441.doNOTmodify.IUDPSocket;
import cpsc441.doNOTmodify.LogFactory;
import cpsc441.doNOTmodify.UDPSocket;

import java.net.DatagramSocket;

public class Main {

    public static void main(String [] args) {
        DatagramSocket s = null;

        configWrapper config = new configWrapper("config.txt");
        TopologyWrapper topology = new TopologyWrapper("testTopo.txt");
        System.out.println("haha");
        //You shouldn't need to modify this code.
        IUDPSocket sock = new UDPSocket(s);
        ILogFactory logFactory = new LogFactory();
        Router r = new Router(sock, logFactory);
        r.run();
    }
}
