import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPServer {
    private static final Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        final int portNumber = 12345;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("TCPServer is listening on port " + portNumber);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String registrationRequest = input.readLine();
                    System.out.println("Received registration request from client: " + registrationRequest);

                    String registrationResponse = processRegistration(registrationRequest);
                    output.println(registrationResponse);
                    System.out.println("Sent registration response to client: " + registrationResponse);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRegistration(String registrationRequest) {
        String[] fields = registrationRequest.split(",");

        if (fields.length != 6) {
            return "Invalid registration request. Please provide all required fields.";
        }

        String name = fields[0];
        String ppsNumber = fields[1];
        String email = fields[2];

        if (users.containsKey(ppsNumber) || users.containsValue(email)) {
            return "Registration failed. PPS Number or Email already registered.";
        }

        String password = fields[3];
        String address = fields[4];
        double initialBalance = Double.parseDouble(fields[5]);

        User newUser = new User(name, ppsNumber, email, password, address, initialBalance);

        users.put(ppsNumber, newUser);

        return "Registration successful";
    }

    private static class User {
        private String name;
        private String ppsNumber;
        private String email;
        private String password;
        private String address;
        private double initialBalance;

        public User(String name, String ppsNumber, String email, String password, String address, double initialBalance) {
            this.name = name;
            this.ppsNumber = ppsNumber;
            this.email = email;
            this.password = password;
            this.address = address;
            this.initialBalance = initialBalance;
        }
    }
}