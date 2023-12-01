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

            requestSocket = new Socket("127.0.0.1", 2004);
            System.out.println("Connected to localhost in port 2004");

            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            try {

                do {

                    message = (String) in.readObject();
                    System.out.println(message);
                    response = input.next();
                    sendMessage(response);
                
                    if (response.equalsIgnoreCase("1")) {

                        for (int i = 0; i < 6; i++) {

                            message = (String) in.readObject();
                            System.out.println(message);
                            response = input.next();
                            sendMessage(response);

                        }

                    }

                     // Check for the login entry loop (option 3)
                     else if (response.equalsIgnoreCase("2")) {
                        
                        for (int i = 0; i < 2; i++) {

                            message = (String) in.readObject();
                            System.out.println(message);
                            response = input.next();
                            sendMessage(response);

                        }

                        // Process the login response from the server
                        message = (String) in.readObject();
                        System.out.println(message);

                     }

                    else if(response.equalsIgnoreCase("3")) {

						message = (String)in.readObject();
						int numMessage = Integer.parseInt(message);
						
						for(int i=0; i<numMessage; i++)
						{

							message = (String)in.readObject();
							System.out.println(message);

						}

					}
                
                    // After processing the registration or other actions
                    message = (String) in.readObject();
                    System.out.println(message);
                    response = input.next();
                    sendMessage(response);
                
                } while(response.equalsIgnoreCase("1"));
                
            } catch (ClassNotFoundException e) {

                e.printStackTrace();

            }

        } 
        
        catch (UnknownHostException unknownHost) {

            System.err.println("You are trying to connect to an unknown host!");

        } 
        
        catch (IOException ioException) {

            ioException.printStackTrace();

        } 
        
        finally {

            try {

                in.close();
                out.close();
                requestSocket.close();

            } 
            
            catch (IOException ioException) {

                ioException.printStackTrace();

            }

        }

    }

    void sendMessage(String msg) {

        try {

            out.writeObject(msg);
            out.flush();

        } 
        
        catch (IOException ioException) {

            ioException.printStackTrace();

        }

    }

    public static void main(String args[]) {

        Requester client = new Requester();
        client.run();

    }
    
}