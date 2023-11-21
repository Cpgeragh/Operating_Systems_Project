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

            String registrationRequest = prepareRegistrationRequest();
            output.println(registrationRequest);
            System.out.println("Sent registration request to server: " + registrationRequest);

            String registrationResponse = input.readLine();
            System.out.println("Received registration response from server: " + registrationResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String prepareRegistrationRequest() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter user details for registration:");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("PPS Number: ");
        String ppsNumber = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Initial Balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine();

        return name + ", " + ppsNumber + ", " + email + ", " + password + ", " + address + ", " + initialBalance;
    }
}