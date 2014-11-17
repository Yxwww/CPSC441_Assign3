package cpsc441.solution;

import cpsc441.doNOTmodify.ILogFactory;
import cpsc441.doNOTmodify.IUDPSocket;

public class Router implements Runnable {

    public Router(IUDPSocket sock, ILogFactory logFactory)     {
        System.out.println(sock.toString() + " - "+logFactory.toString());
    }

    public void run() {

    }
}
