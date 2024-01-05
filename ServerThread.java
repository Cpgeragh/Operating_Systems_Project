import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

// Inheritor Class Starts
public class ServerThread extends Thread {

    // New Field to Store Connection
    Socket myConnection;
    // New Fields to Store Input and Output Streams
    ObjectOutputStream out;
    ObjectInputStream in;
    AccountRegistry myRegistry;
    Account loggedInAccount;

    // Constructor
    public ServerThread(Socket s, AccountRegistry registry) {

        // Assign the Connection and Account Registry
        myConnection = s;
        myRegistry = registry;

    }

    ///// REGISTRATION METHOD /////
    private void registration() {

        try {

            // Prompt Client for Account Information
            sendMessage("\nPlease enter the account name: ");
            // Read the Account Name
            String name = (String) in.readObject();

            // Prompt Client for PPS Number
            sendMessage("\nPlease enter the PPS number (must be unique): ");
            // Read the PPS Number
            String ppsNumber = (String) in.readObject();

            // Check if Entered PPS Number is Already Associated With an Account
            if (myRegistry.ppsNumberExists(ppsNumber)) {

                // While Loop to Check if PPS Number is Unique
                while (myRegistry.ppsNumberExists(ppsNumber)) {

                    // Send the Error Message to Client
                    sendMessage("\nError: PPS number already exists. Please choose a different PPS number.");
                    // Read the New PPS Number from Client
                    ppsNumber = (String) in.readObject();

                }

            }

            sendMessage("\nPlease enter the email (must be unique): ");
            String email = (String) in.readObject();

            // Check if Entered Email is Already Associated With an Account
            if (myRegistry.emailExists(email)) {

                // While Loop to Check if Email is Unique
                while (myRegistry.emailExists(email)) {

                    // Send the Error Message to Client
                    sendMessage("\nError: Email already exists. Please choose a different email.");
                    // Read the New Email from Client
                    email = (String) in.readObject();

                }

            }

            // Prompt Client for Password
            sendMessage("\nPlease enter the password: ");
            String password = (String) in.readObject();

            // Prompt Client for Address
            sendMessage("\nPlease enter the address: ");
            String address = (String) in.readObject();

            // Prompt Client for Initial Balance
            sendMessage("\nPlease enter the initial balance: ");
            String initialBalance = (String) in.readObject();

            // Call the addAccount Method
            myRegistry.addAccount(name, ppsNumber, email, password, address, initialBalance);

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    }; // registration() Ends

    ///// PERFORM LOGIN METHOD /////
    private synchronized boolean performLogin(String email, String password) {

        // Find the Account with Given Email and Password
        loggedInAccount = myRegistry.findAccountByEmailAndPassword(email, password);
        // Return if the Account is Found
        return loggedInAccount != null;

    } // performLogin() Ends

    ///// LODGE MONEY METHOD /////
    private void lodgeMoney(float amount) {

        // Check if Logged-In Account is Not Null
        if (loggedInAccount != null) {

            // Lodge the Money
            loggedInAccount.lodgeMoney(amount);
            // Send the New Balance to the Client
            sendMessage("\nMoney lodged successfully. New balance is: " + loggedInAccount.getInitialBalance());

        }

        // If the Logged-In Account is Null
        else {

            // Send the Error Message to the Client
            sendMessage("\nError, Account Not Found");

        }

    } // lodgeMoney() Ends

    ///// REGISTERED USER LIST METHOD /////
    private void registeredUserList() {

        // Get List of Registered Users
        String[] userList = myRegistry.getListing();

        // Check if User List is Not Empty
        if (userList.length > 0) {

            // Send the Number of Registered Users to the Client
            sendMessage("\nRegistered Users: " + userList.length);

            // Iterate Through the User List
            for (String user : userList) {

                // Print the User Information
                System.out.println("DEBUG: Raw user info: " + user);

                // Split the User Information
                String[] userInfo = user.split("\n"); // Split by New Line
                String name = userInfo[1].substring(userInfo[1].indexOf(":") + 2);
                String ppsNumber = userInfo[2].substring(userInfo[2].indexOf(":") + 2);
                String email = userInfo[3].substring(userInfo[3].indexOf(":") + 2);

                // Send the User Information to the Client
                sendMessage("\nName: " + name + "\nPPS Number: " + ppsNumber + "\nEmail: " + email);

            }

            // Send Signal to Indicate End of User Information
            sendMessage("END_OF_USER_LISTING");

        }

        // If User List is Empty
        else {

            // Send the Error Message to Client
            sendMessage("\nError, No Users Found");

        }

    } // registeredUserList() Ends

    ///// TRANSFER MONEY METHOD /////
    private void transferMoney() {

        // Check if Logged-In Account is Not Null
        try {

            // Amount to Transfer
            float amountToTransfer = 0;

            // Prompt for Recipient's Email Address
            sendMessage("\nEnter the recipient's email address:");
            // Read the Recipient's Email Address
            String recipientEmail = (String) in.readObject();

            // Prompt for Recipient's PPS Number
            sendMessage("\nEnter the recipient's PPS number:");
            // Read the Recipient's PPS Number
            String recipientPPS = (String) in.readObject();

            // Find the Recipient's Account
            Account recipientAccount = myRegistry.findAccountByEmailAndPPS(recipientEmail, recipientPPS);

            // Check if Recipient Account Not Found
            if (recipientAccount == null) {

                // Send the Error Message to the Client
                sendMessage("\nError: Recipient account not found.");
                // Return from the Method to Repeat the Menu
                return;

            }

            // Check if Recipient's Email is the Same as Sender's Email
            else if (recipientAccount != null) {

                // Ask User to Enter the Amount to Transfer
                sendMessage("\nEnter the amount to transfer: ");
                // Read the Amount to Transfer
                amountToTransfer = Float.parseFloat((String) in.readObject());

                // Check if Amount to Transfer is Valid
                if (amountToTransfer <= 0) {

                    // Send the Error Message to the Client
                    sendMessage("\nError: Invalid amount to transfer.");
                    // Return from the Method to Repeat the Menu
                    return;

                }

                // Check if Sender's Balance is Less than Amount to Transfer
                else if (loggedInAccount.getInitialBalance() < amountToTransfer) {

                    // Send the Error Message to the Client
                    sendMessage("\nError: Insufficient funds for the transfer.");
                    // Return from the Method to Repeat the Menu
                    return;

                }

            }

            // Perform the Money Transfer
            loggedInAccount.lodgeMoney(-amountToTransfer); // Deduct from sender's balance
            // Add the Money to Recipient's Balance
            recipientAccount.lodgeMoney(amountToTransfer);

            // Record Transaction in the Transaction History of Sender
            loggedInAccount.addTransaction(
                    // Create a New Transaction
                    new Transaction(loggedInAccount.getEmail(), recipientAccount.getEmail(), amountToTransfer));

            // Record Transaction in the Transaction History of Recipient
            recipientAccount.addTransaction(
                    // Create a New Transaction
                    new Transaction(loggedInAccount.getEmail(), recipientAccount.getEmail(), amountToTransfer));

            // Send the New Balance to the Client
            sendMessage("\nMoney transfer successful. New balance is: " + loggedInAccount.getInitialBalance());

            // Catch the Exceptions
        } catch (IOException | ClassNotFoundException e) {

            // Print the Stack Trace
            e.printStackTrace();

        }

    } // transferMoney() ends

    ///// VIEW ACCOUNT TRANSACTIONS METHOD /////
    private void viewAccountTransactions() {

        // Check if Logged-In Account Exists
        if (loggedInAccount != null) {

            // Get Transaction History of Logged-In Account
            LinkedList<Transaction> transactions = loggedInAccount.getTransactionHistory();

            // Check if the Transaction History is Not Empty
            if (transactions.size() > 0) {

                // Send the Number of Transactions to the Client
                sendMessage("\nTransaction History: " + transactions.size() + " Transactions in Total\n");

                // Iterate Through the Transaction History
                for (Transaction transaction : transactions) {

                    // Send the Transaction Information to the Client
                    sendMessage("Sender: " + transaction.getSenderEmail() +
                            "\nRecipient: " + transaction.getRecipientEmail() +
                            "\nAmount: " + transaction.getAmount() +
                            "\n----------------------");

                }

            }

            // If the Transaction History is Empty
            else {

                sendMessage("\nNo transactions found.");

            }

        }

        // If Logged-In Account Does Not Exist
        else {

            // Send Error Message to Client
            sendMessage("\nError, Account Not Found");

        }

    } // viewAccountTransactions() ends

    ///// UPDATE PASSWORD METHOD /////
    private void updatePassword() {

        try {

            // Check if Logged-In Account Exists
            if (loggedInAccount != null) {

                // Prompt the User for Current Password
                sendMessage("\nEnter your current password:");
                // Read the Current Password
                String currentPassword = (String) in.readObject();

                // Check if Current Password is Correct
                if (!currentPassword.equals(loggedInAccount.getPassword())) {

                    // Send the Error Message to the Client
                    sendMessage("\nError: Incorrect password.");
                    // Return from the Method to Repeat the Menu
                    return;

                }

                // Prompt the User for New Password
                sendMessage("\nEnter your new password: ");
                // Read the New Password
                String newPassword = (String) in.readObject();

                // Update the Password
                loggedInAccount.setPassword(newPassword);
                // Send the Success Message to the Client
                sendMessage("\nPassword updated successfully.");

            }

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    } // updatePassword() Ends

    ///// LOGIN METHOD //////
    private void successfulLogin() {

        try {

            // Variable to Store the Account Action
            String accountAction;

            // Repeat the Menu
            do {

                // Send the Menu to the Client
                sendMessage("\n1. Lodge money");
                sendMessage("2. Retrieve all registered users");
                sendMessage("3. Transfer money to another account");
                sendMessage("4. View all all account transactions");
                sendMessage("5. Update your password");
                sendMessage("6. Return to Main Menu");

                // Read the Account Action
                accountAction = (String) in.readObject();

                // If User Chooses to Lodge Money
                if (accountAction.equalsIgnoreCase("1")) {

                    // Prompt the User for the Amount to Lodge
                    sendMessage("\nEnter the amount to lodge:");
                    // Read the Amount to Lodge
                    float amountToLodge = Float.parseFloat((String) in.readObject());
                    // Call the Lodge Money Method
                    lodgeMoney(amountToLodge);

                }

                // If User Chooses to View Registered Users
                else if (accountAction.equalsIgnoreCase("2")) {

                    // Call the Registered User List Method
                    registeredUserList();

                }

                // If User Chooses to Transfer Money
                else if (accountAction.equalsIgnoreCase("3")) {

                    // Call the Transfer Money Method
                    transferMoney();

                }

                // If User Chooses to View Account Transactions
                else if (accountAction.equalsIgnoreCase("4")) {

                    // Call the View Account Transactions Method
                    viewAccountTransactions();

                }

                // If User Chooses to Update Password
                else if (accountAction.equalsIgnoreCase("5")) {

                    // Call the Update Password Method
                    updatePassword();

                }

                // If User Chooses to Update Password
                else if (accountAction.equalsIgnoreCase("6")) {

                    // Call the Update Password Method
                    return;

                }

                // Repeat the Menu
                sendMessage("\nPlease enter 1 to repeat login menu");
                // Read the Account Action
                accountAction = (String) in.readObject();

            } while (accountAction.equals("1")); // Repeat the Menu if the User Chooses to Repeat

            // Close the Connection
            in.close();
            // Close the Output Stream
            out.close();

        }

        // Catch the Exceptions
        catch (ClassNotFoundException | IOException e) {

            // Print the Stack Trace
            e.printStackTrace();

        }

    } // successfulLogin() Ends

    ///// PROGRAM RUN METHOD /////
    public void run() {

        try {

            // Create the Input and Output Streams
            out = new ObjectOutputStream(myConnection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(myConnection.getInputStream());

            // Variable to Store the Main Menu Choice
            String message;

            // Repeat the Menu
            do {

                // Send the Menu to the Client
                sendMessage("\nPlease enter 1 to REGISTER A NEW ACCOUNT");
                sendMessage("Please enter 2 to LOGIN");
                sendMessage("Please enter 3 to VIEW STORED ACCOUNTS\n");

                // Reply From the Client For Main Menu Choice
                message = (String) in.readObject();
                System.out.println("\nclient > " + message);

                // If the User Chooses to Register New Account
                if (message.equalsIgnoreCase("1")) {

                    // Call the Registration Method
                    registration();

                }

                // If the User Chooses to Login
                else if (message.equalsIgnoreCase("2")) {

                    // Prompt Client for Email and Password
                    sendMessage("\nPlease enter the email");
                    String email = (String) in.readObject();

                    // Prompt Client for Password
                    sendMessage("\nPlease enter the password");
                    String password = (String) in.readObject();

                    // Call the performLogin Method
                    boolean loginSuccessful = performLogin(email, password);

                    // If the Login is Successful
                    if (loginSuccessful) {

                        // Send the Login Successful Message to Client
                        sendMessage("\nLogin successful! Please choose an option:");

                        // Call the Successful Login Method
                        successfulLogin();

                    }

                    // If the Login is Unsuccessful
                    else {

                        // Send the Login Unsuccessful Message to Client
                        sendMessage("\nInvalid email or password");

                    }

                }

                // If the User Chooses to View Stored Accounts
                else if (message.equalsIgnoreCase("3")) {

                    // Call the getListing Method
                    String[] myListing = myRegistry.getListing();
                    sendMessage("" + myListing.length);

                    // Iterate Through the Listing
                    for (int i = 0; i < myListing.length; i++) {

                        // Send the Listing to the Client
                        sendMessage(myListing[i]);

                    }

                }

                // Repeat the Menu
                sendMessage("\nPlease enter 1 to display the main menu");
                // Reply From the Client For Main Menu Choice
                message = (String) in.readObject();

                // Repeat the Menu if the Client Chooses to Repeat
            } while (message.equalsIgnoreCase("1"));

            // Close the Connection
            in.close();
            // Close the Output Stream
            out.close();

        }

        // Catch the Exceptions
        catch (ClassNotFoundException | IOException e) {

            // Print the Stack Trace
            e.printStackTrace();

        }

    } // run() Ends

    ///// METHOD TO SEND MESSAGES TO THE CLIENT /////
    void sendMessage(String msg) {

        // Try to Send the Message
        try {

            // Send the Message
            out.writeObject(msg);
            // Flush the Output Stream
            out.flush();
            // Print the Message
            System.out.println("\nserver > " + msg);

            // Catch the Exceptions
        } catch (IOException ioException) {

            // Print the Stack Trace
            ioException.printStackTrace();

        }

    } // sendMessage() Ends

} // Inheritor Class Ends