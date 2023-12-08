import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester {

    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    String response;
    Scanner input;

    Requester() {
        input = new Scanner(System.in);
    }

    void run() {

        try {

            // Connect to the server
            requestSocket = new Socket("127.0.0.1", 2004);
            System.out.println("Connected to localhost on port 2004");

            // Initialize input and output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            ///// FIRST MENU /////
            do {

                message = (String) in.readObject();
                System.out.println(message);

                message = (String) in.readObject();
                System.out.println(message);

                message = (String) in.readObject();
                System.out.println(message);

                // User input
                response = input.next();
                sendMessage(response);

                if (response.equalsIgnoreCase("1")) {

                    ///// REGISTRATION /////
                    for (int i = 0; i < 6; i++) {

                        message = (String) in.readObject();
                        System.out.println(message);
                        response = input.next();
                        sendMessage(response);

                    }

                } 
                
                else if (response.equalsIgnoreCase("2")) {

                    ///// LOGGED IN DETAILS /////
                    for (int i = 0; i < 2; i++) {

                        message = (String) in.readObject();
                        System.out.println(message);
                        response = input.next();
                        sendMessage(response);

                    }

                    // Process login menu response
                    message = (String) in.readObject();
                    System.out.println(message);

                    if (message.equalsIgnoreCase("\nLogin successful! Please choose an option:")) {

                        ///// LOGGED IN MENU /////
                        do {

                            // Display menu options after successful login
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

                            // User input for menu options
                            response = input.next();
                            sendMessage(response);

                            // If the user chose to lodge money
                            if (response.equalsIgnoreCase("1")) {

                                // Display the message asking to enter the amount to lodge
                                message = (String) in.readObject();
                                System.out.println(message);

                                // User input for the amount to lodge
                                float amountToLodge = input.nextFloat();
                                sendMessage(String.valueOf(amountToLodge));

                                message = (String) in.readObject();
                                System.out.println(message);

                            }

                            // If the user chose to see registered users
                            else if (response.equalsIgnoreCase("2")) {
                                message = (String) in.readObject();
                                System.out.println(message);

                                // Read and print the user names, emails, and PPS numbers
                                String userInfo;

                                while (!(userInfo = (String) in.readObject()).equals("END_OF_USER_LISTING")) {
                                    System.out.println(userInfo);
                                    // Process userInfo as needed
                                }

                            }

                            // If the user chose to transfer money
                            else if (response.equalsIgnoreCase("3")) {///////////
                                
                                for (int i = 0; i < 2; i++) {

                                message = (String) in.readObject();
                                System.out.println(message);
                                response = input.next();
                                sendMessage(response);

                            }

                            if (message.equalsIgnoreCase("\nError: Recipient account not found.")) {

                                message = (String) in.readObject();
                                System.out.println(message);

                            };

                             // Display the message asking to enter the amount to lodge
                                message = (String) in.readObject();
                                System.out.println(message);

                                // User input for the amount to lodge
                                float amountToLodge = input.nextFloat();
                                sendMessage(String.valueOf(amountToLodge));

                                if(message.equalsIgnoreCase("\nError: Invalid amount to transfer.")){

                                    message = (String) in.readObject();
                                    System.out.println(message);

                                };

                                if(message.equalsIgnoreCase("\nError: Insufficient funds for the transfer.")){

                                    message = (String) in.readObject();
                                    System.out.println(message);

                                };

                                message = (String) in.readObject();
                                System.out.println(message);

                                           

                            }//////////

                            // After processing action
                            message = (String) in.readObject();
                            System.out.println(message);

                            // User input to repeat or exit
                            response = input.next();
                            sendMessage(response);

                        } while (response.equalsIgnoreCase("1"));

                    }

                    else if(message.equalsIgnoreCase("\nInvalid email or password: ")){
                        message = (String) in.readObject();
                        System.out.println(message);

                    };

                } 
                
                else if (response.equalsIgnoreCase("3")) {

                    // View stored accounts
                    message = (String) in.readObject();
                    int numMessage = Integer.parseInt(message);

                    for (int i = 0; i < numMessage; i++) {

                        message = (String) in.readObject();
                        System.out.println(message);

                    }

                }

                // After processing registration or other actions
                message = (String) in.readObject();
                System.out.println(message);

                // User input to repeat or exit
                response = input.next();
                sendMessage(response);

            } while (response.equalsIgnoreCase("1"));

        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();

        } 
        
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

    // Send a message to the server
    void sendMessage(String msg) {

        try {

            out.writeObject(msg);
            out.flush();

        } catch (IOException ioException) {

            ioException.printStackTrace();

        }

    }

    public static void main(String[] args) {

        Requester client = new Requester();

        client.run();

    }

}