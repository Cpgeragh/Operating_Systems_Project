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
            sendMessage("\nMoney lodged successfully. New balance is: " + loggedInAccount.getInitialBalance());

        } 
        
        else {

            sendMessage("\nError, Account Not Found");

        }

    }

    private void registeredUserList() {
        String[] userList = myRegistry.getListing();
    
        if (userList.length > 0) {
            // Send the number of registered users
            sendMessage("\nRegistered Users: " + userList.length);
    
            // Iterate through the user list
            for (String user : userList) {
                // Print the user information before processing
                System.out.println("DEBUG: Raw user info: " + user);
    
                // Extract information directly from the user string
                String[] userInfo = user.split("\n"); // Split by newline character
                String name = userInfo[1].substring(userInfo[1].indexOf(":") + 2);
                String ppsNumber = userInfo[2].substring(userInfo[2].indexOf(":") + 2);
                String email = userInfo[3].substring(userInfo[3].indexOf(":") + 2);
    
                // Display the user information
                sendMessage("\nName: " + name + "\nPPS Number: " + ppsNumber + "\nEmail: " + email);
            }
    
            // Send a signal to indicate the end of user information
            sendMessage("END_OF_USER_LISTING");
        } else {
            // Handle the case where no users are found
            sendMessage("\nError, No Users Found");
        }
    }//////ddddddd

    private void transferMoney() {
        try {

            float amountToTransfer = 0;

            // Prompt for recipient details
            sendMessage("\nEnter the recipient's email address:");
            String recipientEmail = (String) in.readObject();
    
            sendMessage("\nEnter the recipient's PPS number:");
            String recipientPPS = (String) in.readObject();
    
            // Validate recipient details
            Account recipientAccount = myRegistry.findAccountByEmailAndPPS(recipientEmail, recipientPPS);
    
            if (recipientAccount == null) {
    
                sendMessage("\nError: Recipient account not found.");
                return;

            }
    
            if(recipientAccount != null) {

                // Prompt for the amount to transfer
                sendMessage("\nEnter the amount to transfer: ");
                amountToTransfer = Float.parseFloat((String) in.readObject());

            }
            
    
            // Validate the amount
            if (amountToTransfer <= 0) {
                sendMessage("\nError: Invalid amount to transfer.");
                return;
            }
    
            // Check if the sender has sufficient balance
            if (loggedInAccount.getInitialBalance() < amountToTransfer) {
                sendMessage("\nError: Insufficient funds for the transfer.");
                return;
            }
    
            // Perform the money transfer
            loggedInAccount.lodgeMoney(-amountToTransfer); // Deduct from sender's balance
            recipientAccount.lodgeMoney(amountToTransfer); // Add to recipient's balance
    
            sendMessage("\nMoney transfer successful. New balance is: " + loggedInAccount.getInitialBalance());
    
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    ///// LOGIN METHOD //////
    private void successfulLogin() {

        try {

            String accountAction;

            do {

                sendMessage("\n1. Lodge money");
                sendMessage("2. Retrieve all registered users");
                sendMessage("3. Transfer money to another account");
                sendMessage("4. View all all account transactions");
                sendMessage("5. Update your password");

                accountAction = (String) in.readObject();

                if (accountAction.equalsIgnoreCase("1")) {
                    sendMessage("\nEnter the amount to lodge:");
                    float amountToLodge = Float.parseFloat((String) in.readObject());
                    lodgeMoney(amountToLodge);
                }

                else if (accountAction.equalsIgnoreCase("2")) {

                    registeredUserList();

                }

                else if (accountAction.equalsIgnoreCase("3")) {

                    transferMoney();

                }

                else if (accountAction.equalsIgnoreCase("4")) {

                }

                else if (accountAction.equalsIgnoreCase("5")) {

                }

                sendMessage("\nPlease enter 1 to repeat login menu");
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

            sendMessage("\nPlease enter the account name: ");
            String name = (String) in.readObject();

            sendMessage("Please enter the PPS number: ");
            String ppsNumber = (String) in.readObject();

            sendMessage("Please enter the email: ");
            String email = (String) in.readObject();

            sendMessage("Please enter the password: ");
            String password = (String) in.readObject();

            sendMessage("Please enter the address: ");
            String address = (String) in.readObject();

            sendMessage("Please enter the initial balance: ");
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

                sendMessage("\nPlease enter 1 to REGISTER A NEW ACCOUNT");
                sendMessage("Please enter 2 to LOGIN");
                sendMessage("Please enter 3 to VIEW STORED ACCOUNTS\n");
                message = (String) in.readObject();

                if (message.equalsIgnoreCase("1")) {

                    registration();

                }

                else if (message.equalsIgnoreCase("2")) {

                    sendMessage("\nPlease enter the email");
                    String email = (String) in.readObject();

                    sendMessage("\nPlease enter the password");
                    String password = (String) in.readObject();

                    boolean loginSuccessful = performLogin(email, password);

                    
                    if (loginSuccessful) {
                        
                        sendMessage("\nLogin successful! Please choose an option:");

                        successfulLogin();

                    }

                    else {

                        sendMessage("\nInvalid email or password");

                    }

                }

                else if (message.equalsIgnoreCase("3")) {

                    String[] myListing = myRegistry.getListing();
                    sendMessage("" + myListing.length);
                    for (int i = 0; i < myListing.length; i++) {
                        sendMessage(myListing[i]);
                    }

                }

                sendMessage("\nPlease enter 1 to repeat");
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