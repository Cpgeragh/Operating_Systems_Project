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

    private boolean performLogin(String email, String password) {

        Account loggedInAccount = myRegistry.findAccountByEmailAndPassword(email, password);
        return loggedInAccount != null;

    }

    public void run() {

        try {

            out = new ObjectOutputStream(myConnection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(myConnection.getInputStream());

            String message;

            do {

                sendMessage("Please enter 1 to REGISTER A NEW ACCOUNT");
                sendMessage("Please enter 2 to LOGIN");
                sendMessage("Please enter 3 to VIEW STORED ACCOUNTS");
                message = (String) in.readObject();

                if (message.equalsIgnoreCase("1")) {

                    sendMessage("Please enter the account name");
                    String name = (String) in.readObject();

                    sendMessage("Please enter the PPS number");
                    String ppsNumber = (String) in.readObject();

                    sendMessage("Please enter the email");
                    String email = (String) in.readObject();

                    sendMessage("Please enter the password");
                    String password = (String) in.readObject();

                    sendMessage("Please enter the address");
                    String address = (String) in.readObject();

                    sendMessage("Please enter the initial balance");
                    String initialBalance = (String) in.readObject();

                    myRegistry.addAccount(name, ppsNumber, email, password, address, initialBalance);

                } 
                
                else if (message.equalsIgnoreCase("2")) {

                    sendMessage("Please enter the email");
                    String email = (String) in.readObject();

                    sendMessage("Please enter the password");
                    String password = (String) in.readObject();

                    boolean loginSuccessful = performLogin(email, password);

                    if (loginSuccessful) {

                        sendMessage("Login successful");

                    }
                    
                    else {

                        sendMessage("Invalid email or password");

                    }

                } 
                
                else if (message.equalsIgnoreCase("3")) {

                    String[] myListing = myRegistry.getListing();
                    sendMessage("" + myListing.length);
                    for (int i = 0; i < myListing.length; i++) {
                        sendMessage(myListing[i]);
                    }

                }

                sendMessage("Please enter 1 to repeat");
                message = (String) in.readObject();

            } while (!message.equalsIgnoreCase("0"));

            in.close();
            out.close();

        } 
        
        catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();

        }

    }

    void sendMessage(String msg) {

        try {

            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);

        } 
        
        catch (IOException ioException) {

            ioException.printStackTrace();

        }
        
    }
}