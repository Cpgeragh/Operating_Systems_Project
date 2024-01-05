import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Provider Class
public class Provider {

    private static final int SERVER_PORT = 3000;
    private static final int BACKLOG = 10;

    // Main Method
    public static void main(String args[]) {

        // Variables
        ServerSocket providerSocket;
        AccountRegistry sharedRegistry;

        // Try-Catch
        try {

            // Create Server Socket
            providerSocket = new ServerSocket(SERVER_PORT, BACKLOG);
            // Create Account Registry
            sharedRegistry = new AccountRegistry();

            // While Loop
            while (true) {

                // Print Confirmation to Console
                System.out.println("\nWaiting for connection");
                // Accept Connection
                Socket connection = providerSocket.accept();
                // Create Server Thread
                ServerThread T1 = new ServerThread(connection, sharedRegistry);
                // Start Server Thread
                T1.start();

            }

        }

        catch (IOException e1) {

            e1.printStackTrace();

        }

    }

}