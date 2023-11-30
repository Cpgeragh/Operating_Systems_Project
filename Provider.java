import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Provider {

    public static void main(String args[]) {
        ServerSocket providerSocket;
        AccountRegistry sharedRegistry;

        try {
            providerSocket = new ServerSocket(2004, 10);
            sharedRegistry = new AccountRegistry();

            while (true) {
                System.out.println("Waiting for connection");
                Socket connection = providerSocket.accept();
                ServerThread T1 = new ServerThread(connection, sharedRegistry);
                T1.start();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}