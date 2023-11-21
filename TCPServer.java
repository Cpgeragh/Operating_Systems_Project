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

                    String requestType = input.readLine();
                    System.out.println("Received request from client: " + requestType);

                    String response;
                    switch (requestType) {
                        case "register":
                            response = processRegistration(input);
                            break;
                        case "login":
                            response = processLogin(input);
                            break;
                        default:
                            response = "Invalid request type";
                            break;
                    }

                    output.println(response);
                    System.out.println("Sent response to client: " + response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRegistration(BufferedReader input) throws IOException {
        String[] fields = input.readLine().split(",");

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

    private static String processLogin(BufferedReader input) throws IOException {
        String email = input.readLine();
        String password = input.readLine();

        User user = findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return "Login successful";
        } else {
            return "Login failed. Incorrect email or password.";
        }
    }

    private static User findUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
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

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}