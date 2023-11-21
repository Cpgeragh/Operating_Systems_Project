import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private static final String serverAddress = "localhost";
    private static final int portNumber = 12345;

    private static BufferedReader input;
    private static PrintWriter output;
    private static Scanner scanner;

    private static void exitServer() {
        output.println("exit");
    }

    private static void printMenu() {
        System.out.println("Choose operation:");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Lodge Money");
        System.out.println("4. Get Users Listing");
        System.out.println("-1. Exit");
    }

    private static void registerUser() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter PPS Number: ");
        String ppsNumber = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        System.out.print("Enter Initial Balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine();

        output.println("register");
        output.println(name + "," + ppsNumber + "," + email + "," + password + "," + address + "," + initialBalance);
    }

    private static void loginUser() {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        output.println("login");
        output.println(email);
        output.println(password);
    }

    private static void lodgeMoney() {
        System.out.print("Enter PPS Number: ");
        String ppsNumber = scanner.nextLine();

        System.out.print("Enter Amount to Lodge: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        output.println("lodge");
        output.println(ppsNumber);
        output.println(amount);
    }

    private static void getUsersListing() {
        output.println("get_users");
    
        try {
            String serverResponse;
            while (!(serverResponse = input.readLine()).equals("")) {
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try (Socket socket = new Socket(serverAddress, portNumber)) {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            int choice;
            do {
                printMenu();
                System.out.print("Enter your choice (1, 2, 3, 4, or -1 to exit): ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        loginUser();
                        break;
                    case 3:
                        lodgeMoney();
                        break;
                    case 4:
                        getUsersListing();
                        break;
                    case -1:
                        exitServer();
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or -1.");
                        break;
                }

                String serverResponse = input.readLine();
                System.out.println("Server response: " + serverResponse);

            } while (choice != -1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}