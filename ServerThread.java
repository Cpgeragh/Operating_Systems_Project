import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket myConnection;
    ObjectOutputStream out;
    ObjectInputStream in;
    AccountRegistry myRegistry;

    public ServerThread(Socket s, AccountRegistry registry) {
        myConnection = s;
        myRegistry = registry;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(myConnection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(myConnection.getInputStream());

            // Server Comms
            String message; // Declare message variable here
            do {
                // Server sends a menu to the client
                sendMessage("Please enter 1 to REGISTER AN ACCOUNT OR 2 for THE BOOK LISTING");
                message = (String)in.readObject();

                // Process based on client response
                if (message.equalsIgnoreCase("1")) {
                    // Registration logic
                    sendMessage("Please enter the account name");
                    String name = (String)in.readObject();

                    sendMessage("Please enter the PPS number");
                    String ppsNumber = (String)in.readObject();

                    sendMessage("Please enter the email");
                    String email = (String)in.readObject();

                    sendMessage("Please enter the password");
                    String password = (String)in.readObject();

                    sendMessage("Please enter the address");
                    String address = (String)in.readObject();

                    sendMessage("Please enter the initial balance");
                    String initialBalance = (String)in.readObject();

                    myRegistry.addAccount(name, ppsNumber, email, password, address, initialBalance);
                
            } 
            
            else if(message.equalsIgnoreCase("2"))
				{
					String[] myListing = myRegistry.getListing();
					
					sendMessage(""+myListing.length);
					
					for(int i=0;i<myListing.length;i++)
					{
						sendMessage(myListing[i]);
					}
				}
            
            sendMessage("Please enter 1 to repeat");
				message = (String)in.readObject();
				
            }while (message.equalsIgnoreCase("1"));

            in.close();
            out.close();

        } 
        
        catch (ClassNotFoundException classnot) {
            System.err.println("Data received in an unknown format");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}