import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        final String serverAddress = "localhost";
        final int portNumber = 12345;

        try (Socket socket = new Socket(serverAddress, portNumber);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Choose operation:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Enter your choice (1 or 2): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser(scanner, output);
                    break;
                case 2:
                    loginUser(scanner, output);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    break;
            }

            String serverResponse = input.readLine();
            System.out.println("Server response: " + serverResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void registerUser(Scanner scanner, PrintWriter output) {
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

    private static void loginUser(Scanner scanner, PrintWriter output) {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        output.println("login");
        output.println(email);
        output.println(password);
    }
}