import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client Class
public class Client {

    // Variables
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    String response;
    Scanner input;

    // Constructor
    Client() {

        input = new Scanner(System.in);

    }

    ///// RUN METHOD /////
    void run() {

        try {

            // Create Socket to Connect to Server
            requestSocket = new Socket("127.0.0.1", 3000);
            // Tell User Connection was Successful
            System.out.println("\nConnected to localhost on port 3000");

            // Initialize Input and Output Streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            ///// Main MENU /////
            do {

                // Display Menu Options
                for (int i = 0; i < 3; i++) {

                    // Read and Print Menu Options
                    message = (String) in.readObject();
                    System.out.println(message);

                }

                // User Input for Menu Options
                response = input.next();
                // Send User Input to Server
                sendMessage(response);
                // Print User Input
                System.out.println("\nclient > " + response);

                ///// REGISTRATION /////
                if (response.equalsIgnoreCase("1")) {

                    // Read Account Name Message
                    message = (String) in.readObject();
                    // Print Account Name Message
                    System.out.println(message);
                    // User Input for Account Name
                    response = input.next();
                    // Send User Input to Server
                    sendMessage(response);

                    // Read PPS Number Message
                    message = (String) in.readObject();
                    // Print PPS Number Message
                    System.out.println(message);
                    // User Input for PPS Number
                    response = input.next();
                    // Send User Input to Server
                    sendMessage(response);

                    // Read Email Message or Error Message
                    message = (String) in.readObject();

                    // Check if Entered PPS Number is Already Associated With an Account
                    if (message.equalsIgnoreCase(
                            "\nError: PPS number already exists. Please choose a different PPS number.")) {

                        // while Error Message Received, Keep Asking for New PPS Number
                        while (message.equalsIgnoreCase(
                                "\nError: PPS number already exists. Please choose a different PPS number.")) {

                            // Print Error Message
                            System.out.println(message + "PPS Error");
                            // User Input for New PPS Number
                            response = input.next();
                            // Send User Input to Server
                            sendMessage(response);

                            // Read Email Message or Error Message
                            message = (String) in.readObject();
                            System.out.println(message + "PPs Error Complete");

                        }

                    }

                    // Print Email Message
                    System.out.println(message + "Email");
                    // User Input for Email
                    response = input.next();
                    // Send User Input to Server
                    sendMessage(response);

                    // Read Password Message or Error Message
                    message = (String) in.readObject();

                    // Check if the entered email is already associated with an account
                    if (message.equalsIgnoreCase("\nError: Email already exists. Please choose a different email.")) {

                        // while Error Message Received, Keep Asking for New Email
                        while (message.equalsIgnoreCase("\nError: Email already exists. Please choose a different email.")) {

                            // Print Error Message
                            System.out.println(message + "Email Error");
                            // User Input for New Email
                            response = input.next();
                            // Send User Input to Server
                            sendMessage(response);
                            
                            // Read Password Message or Error Message
                            message = (String) in.readObject();
                            System.out.println(message + "Email Error Complete");

                        }

                    }

                        System.out.println(message + "Password");
                        // User Input for Password
                        response = input.next();
                        // Send User Input to Server
                        sendMessage(response);

                        // Read Address Message
                        message = (String) in.readObject();
                        // Print Address Message
                        System.out.println(message + "Address");
                        // User Input for Address
                        response = input.next();
                        // Send User Input to Server
                        sendMessage(response);

                        // Read Initial Balance Message
                        message = (String) in.readObject();
                        // Print Initial Balance Message
                        System.out.println(message + "Balance");
                        // User Input for Initial Balance
                        response = input.next();
                        // Send User Input to Server
                        sendMessage(response);                

                }

                ///// LOG IN /////
                else if (response.equalsIgnoreCase("2")) {

                    // Display Login Menu
                    for (int i = 0; i < 2; i++) {

                        // Read and Print Menu Options
                        message = (String) in.readObject();
                        System.out.println(message);
                        // User Input for Menu Options
                        response = input.next();
                        // Send User Input to Server
                        sendMessage(response);

                    }

                    // Display Login Menu
                    message = (String) in.readObject();
                    System.out.println(message);

                    // User Input for Menu Options
                    if (message.equalsIgnoreCase("\nLogin successful! Please choose an option:")) {

                        ///// LOGGED IN MENU /////
                        do {

                            // Read and Print Menu Options
                            message = (String) in.readObject();
                            System.out.println(message);

                            message = (String) in.readObject();
                            System.out.println(message);

                            message = (String) in.readObject();
                            System.out.println(message);

                            message = (String) in.readObject();
                            System.out.println(message);

                            message = (String) in.readObject();
                            System.out.println(message);

                            message = (String) in.readObject();
                            System.out.println(message);

                            // User Input for Login Menu Options
                            response = input.next();
                            // Send User Login Input to Server
                            sendMessage(response);

                            ///// LODGE MONEY /////
                            if (response.equalsIgnoreCase("1")) {

                                // Ask User to Enter Amount to Lodge
                                message = (String) in.readObject();
                                System.out.println(message);

                                // User Input for Amount to Lodge
                                float amountToLodge = input.nextFloat();
                                // Send User Lodgement Input to Server
                                sendMessage(String.valueOf(amountToLodge));

                                // Print Confirmation from Server that Lodgement Successful or Unsuccesful
                                message = (String) in.readObject();
                                System.out.println(message);

                            } // End of Lodgement Menu

                            ///// SEE REGISTERED USERS /////
                            else if (response.equalsIgnoreCase("2")) {

                                // Read Total Number of Registered Users
                                message = (String) in.readObject();
                                System.out.println(message);

                                // Read and Print User Info from Server
                                String userInfo;

                                // Read Until Server Sends "END_OF_USER_LISTING"
                                while (!(userInfo = (String) in.readObject()).equals("END_OF_USER_LISTING")) {

                                    // Print Registered User Info
                                    System.out.println(userInfo);

                                }

                            } // End of Logged In Menu

                            ///// TRANSFER MONEY /////
                            else if (response.equalsIgnoreCase("3")) {

                                // Display Message Asking User for Recipient's Email and PPS Number
                                for (int i = 0; i < 2; i++) {

                                    message = (String) in.readObject();
                                    System.out.println(message);
                                    response = input.next();
                                    sendMessage(response);

                                }

                                // Display Message Asking User for Amount to Transfer or That Recipient Account
                                // Not Found
                                message = (String) in.readObject();

                                // If Recipient Account Not Found
                                if (message.equalsIgnoreCase("\nError: Recipient account not found.")) {

                                    // Print Error Message
                                    System.out.println(message);

                                }

                                // If Recipient Account Found
                                else if (message.equalsIgnoreCase("\nEnter the amount to transfer: ")) {

                                    // Display Message Asking User for Amount to Transfer
                                    System.out.println(message);

                                    // User Input for Amount to Transfer
                                    float amountToLodge = input.nextFloat();
                                    // Send User Transfer Input to Server
                                    sendMessage(String.valueOf(amountToLodge));

                                    // Print Confirmation from Server that Transfer Successful, Unsuccesful or Not
                                    // Enough Funds
                                    message = (String) in.readObject();

                                    // If Transfer Unsuccessful
                                    if (message.equalsIgnoreCase("\nError: Invalid amount to transfer.")) {

                                        // Print Error Message
                                        System.out.println(message);

                                    }

                                    // If Not Enough Funds to Transfer
                                    else if (message
                                            .equalsIgnoreCase("\nError: Insufficient funds for the transfer.")) {

                                        // Print Error Message
                                        System.out.println(message);

                                    }

                                    // If Transfer Successful
                                    else {

                                        // Print Confirmation Message
                                        System.out.println(message);

                                    }

                                }

                            } // End of Transfer Menu

                            ///// SEE TRANSACTION HISTORY /////
                            else if (response.equalsIgnoreCase("4")) {
                                message = (String) in.readObject();

                                if (message.contains("Transaction History: ")) {

                                    // Print Total Number of Transactions
                                    System.out.println(message);

                                    // Read and Print Transaction Info from Server
                                    message = (String) in.readObject();
                                    // Print Transaction Info
                                    System.out.println(message);

                                }

                                else {

                                    // Print Error Message
                                    System.out.println(message);

                                }

                            }

                            ///// CHANGE PASSWORD /////
                            else if (response.equalsIgnoreCase("5")) {

                                // Read Message Asking User for Current Password From Server
                                message = (String) in.readObject();
                                // Print Message Asking User for New Password
                                System.out.println(message);

                                // User Input for Current Password
                                response = input.next();
                                // Send User Current Password Input to Server
                                sendMessage(response);

                                // Read Message Asking User for New Password or Error Message From Server
                                message = (String) in.readObject();

                                // If Error Message
                                if (message.equalsIgnoreCase("\nError: Incorrect password.")) {

                                    // Print Error Message
                                    System.out.println(message);
                                }

                                // If New Password
                                else if (message.equalsIgnoreCase("\nEnter your new password: ")) {

                                    // Print Message Asking User for New Password
                                    System.out.println(message);

                                    // User Input for New Password
                                    response = input.next();
                                    // Send User New Password Input to Server
                                    sendMessage(response);

                                    // Read Confirmation Message From Server
                                    message = (String) in.readObject();
                                    // Print Confirmation Message
                                    System.out.println(message);

                                }

                            }

                            ///// RETURN TO MAIN MENU /////
                            else if (response.equalsIgnoreCase("6")) {

                                continue; // Break Out of Logged In Menu

                            }

                            // User Input to Repeat or Exit
                            message = (String) in.readObject();
                            // Print Message Asking User to Repeat or Exit
                            System.out.println(message);

                            // Send User Input to Server
                            response = input.next();
                            sendMessage(response);

                        } while (response.equalsIgnoreCase("1"));

                    }

                    // If Login Unsuccessful
                    else if (message.equalsIgnoreCase("\nInvalid email or password: ")) {

                        // Print Error Message
                        message = (String) in.readObject();
                        System.out.println(message);

                    }
                    ;

                } // End of Login Menu

                ///// VIEW STORED ACCOUNTS /////
                else if (response.equalsIgnoreCase("3")) {

                    // Read Total Number of Registered Users
                    message = (String) in.readObject();
                    int numMessage = Integer.parseInt(message);

                    // Read and Print User Info from Server
                    for (int i = 0; i < numMessage; i++) {

                        message = (String) in.readObject();
                        System.out.println(message);

                    }

                }

                // Read Message Asking User to Repeat or Exit
                message = (String) in.readObject();
                System.out.println(message);

                // Send User Input to Server
                response = input.next();
                sendMessage(response);

            } while (response.equalsIgnoreCase("1"));

        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();

        }

        // Close Connection
        finally {

            try {

                in.close();
                out.close();
                requestSocket.close();

            } catch (IOException ioException) {

                ioException.printStackTrace();

            }

        }

    }

    // Send Message to Output Stream
    void sendMessage(String msg) {

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException ioException) {

            ioException.printStackTrace();

        }

    }

    // Main Method
    public static void main(String[] args) {

        // Create Client Object
        Client client = new Client();

        // Run Client
        client.run();

    }

} // End of Client Class