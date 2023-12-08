import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket myConnection;
    ObjectOutputStream out;
    ObjectInputStream in;
    AccountRegistry myRegistry;
    Account loggedInAccount; // New field to store the currently logged-in account

    public ServerThread(Socket s, AccountRegistry registry) {

        myConnection = s;
        myRegistry = registry;

    }

    private boolean performLogin(String email, String password) {

        loggedInAccount = myRegistry.findAccountByEmailAndPassword(email, password);
        return loggedInAccount != null;

    }

    // Method to lodge money to the currently logged-in account
    private void lodgeMoney(float amount) {
        if (loggedInAccount != null) {
            // Call the lodgeMoney method of the Account class to update the balance
            loggedInAccount.lodgeMoney(amount);
            sendMessage("Money lodged successfully. New balance: " + loggedInAccount.getInitialBalance());
        } else {
            sendMessage("No account logged in.");
        }
    }


    ///// LOGIN METHOD //////
    private void successfulLogin() {

        try {

            String accountAction;

            do {

                sendMessage("1. Lodge money");
                sendMessage("2. Retrieve all registered users");
                sendMessage("3. Transfer money to another account");
                sendMessage("4. View all all account transactions");
                sendMessage("5. Update your password");

                accountAction = (String) in.readObject();

                if (accountAction.equalsIgnoreCase("1")) {
                    sendMessage("Enter the amount to lodge:");
                    float amountToLodge = Float.parseFloat((String) in.readObject());
                    lodgeMoney(amountToLodge);
                }

                else if (accountAction.equalsIgnoreCase("2")) {

                }

                else if (accountAction.equalsIgnoreCase("3")) {

                }

                else if (accountAction.equalsIgnoreCase("4")) {

                }

                else if (accountAction.equalsIgnoreCase("5")) {

                }

                sendMessage("Please enter 1 to repeat");
                accountAction = (String) in.readObject();

            } while (accountAction.equals("1"));

            in.close();
            out.close();

        }

        catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();

        }

    }

    ///// REGISTRATION METHOD /////
    private void registration() {

        try {

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

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    };

    ///// PROGRAM RUN METHOD /////
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

                    registration();

                }

                else if (message.equalsIgnoreCase("2")) {

                    sendMessage("Please enter the email");
                    String email = (String) in.readObject();

                    sendMessage("Please enter the password");
                    String password = (String) in.readObject();

                    boolean loginSuccessful = performLogin(email, password);

                    sendMessage("Login successful! Please choose an option:");

                    if (loginSuccessful) {

                        successfulLogin();

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

            } while (message.equalsIgnoreCase("1"));

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

} // Inheritor Class Ends