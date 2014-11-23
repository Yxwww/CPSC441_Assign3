package cpsc441.doNOTmodify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSocket implements IUDPSocket {

    private final static int MAX_IP4_DATAGRAM_SIZE = 65507;
    private final DatagramSocket sock;
    private final InetAddress host;
    private final int port;


    public UDPSocket(DatagramSocket sock, InetAddress defaultHost, int defaultPort) {
        this.sock = sock;
        this.host = defaultHost;
        this.port = defaultPort;
    }

    public void setSoTimeout(int msTimeout) throws SocketException {
        sock.setSoTimeout(msTimeout);
    }

    public void send(DVRInfo info) throws IOException {
        byte [] data = info.getBytes();
        DatagramPacket pkt = new DatagramPacket(data, data.length, host, port);
        sock.send(pkt);
    }

    public DVRInfo receive() throws IOException {
        byte[] buf = new byte[MAX_IP4_DATAGRAM_SIZE];
        DatagramPacket pkt = new DatagramPacket(buf, buf.length);
        sock.receive(pkt);
        return new DVRInfo(pkt.getData());
    }
}
