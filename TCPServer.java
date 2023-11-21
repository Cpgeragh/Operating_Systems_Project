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

                    String requestType;
                    while ((requestType = input.readLine()) != null) {
                        System.out.println("Received request from client: " + requestType);

                        String response;
                        switch (requestType) {
                            case "register":
                                response = processRegistration(input);
                                break;
                            case "login":
                                response = processLogin(input);
                                break;
                            case "lodge":
                                response = processLodge(input);
                                break;
                            case "get_users":
                                response = getUsersListing();
                                break;
                            case "transfer":
                                response = processTransfer(input);
                                break;
                            case "exit":
                                return;
                            default:
                                response = "Invalid request type";
                                break;
                        }

                        output.println(response);
                        System.out.println("Sent response to client: " + response);
                    }

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

    private static String processLodge(BufferedReader input) throws IOException {
        String ppsNumber = input.readLine();
        double amount = Double.parseDouble(input.readLine());

        User user = findUserByPPSNumber(ppsNumber);
        if (user != null) {
            user.lodge(amount);
            return "Lodged " + amount + " to the account. New balance: " + user.getBalance();
        } else {
            return "User not found.";
        }
    }

    private static String getUsersListing() {
        StringBuilder userListing = new StringBuilder("User Listing:\n");

        for (User user : users.values()) {
            userListing.append(user).append("\n");
        }

        return userListing.toString();
    }

    private static String processTransfer(BufferedReader input) throws IOException {
        String recipientEmail = input.readLine();
        String recipientPPSNumber = input.readLine();
        double amount = Double.parseDouble(input.readLine());
    
        User sender = findUserByEmail(input.readLine());
        User recipientByEmail = findUserByEmail(recipientEmail);
        User recipientByPPSNumber = findUserByPPSNumber(recipientPPSNumber);
    
        if (sender != null && recipientByEmail != null && recipientByPPSNumber != null) {
            // Verify that both email and PPS Number match the recipient
            if (recipientByEmail.equals(recipientByPPSNumber)) {
                User recipient = recipientByEmail;
    
                if (sender.getBalance() >= amount) {
                    sender.lodge(-amount); // Deduct the amount from the sender's balance
                    recipient.lodge(amount); // Add the amount to the recipient's balance
                    return "Transfer successful. New balance for sender (" + sender.getEmail() + "): " + sender.getBalance()
                            + ". New balance for recipient (" + recipient.getEmail() + "): " + recipient.getBalance();
                } else {
                    return "Insufficient funds for the transfer.";
                }
            } else {
                return "Recipient email and PPS Number do not match.";
            }
        } else {
            return "Sender or recipient not found.";
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

    private static User findUserByPPSNumber(String ppsNumber) {
        return users.get(ppsNumber);
    }

    private static class User {
        private String name;
        private String ppsNumber;
        private String email;
        private String password;
        private String address;
        private double initialBalance;
        private double balance;

        public User(String name, String ppsNumber, String email, String password, String address, double initialBalance) {
            this.name = name;
            this.ppsNumber = ppsNumber;
            this.email = email;
            this.password = password;
            this.address = address;
            this.initialBalance = initialBalance;
            this.balance = initialBalance;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public void lodge(double amount) {
            balance += amount;
        }

        public double getBalance() {
            return balance;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", ppsNumber='" + ppsNumber + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    ", initialBalance=" + initialBalance +
                    ", balance=" + balance +
                    '}';
        }
    }
}