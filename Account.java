import java.util.LinkedList;

// Account Class
public class Account {

    // Variables
    private String name;
    private String ppsNumber;
    private String email;
    private String password;
    private String address;
    private float initialBalance;
    private LinkedList<Transaction> transactionHistory;

    // Constructor
    public Account(String n, String pps, String e, String pass, String addr, float balance) {

        // Set Variables
        name = n;
        ppsNumber = pps;
        email = e;
        password = pass;
        address = addr;
        initialBalance = balance;
        transactionHistory = new LinkedList<>();

    }

    // Getters
    public String getAccountName() {

        return name;

    }

    public String getEmail() {

        return email;

    }

    public String getPassword() {

        return password;

    }

    public float getInitialBalance() {
        
        return initialBalance;
    }

    public String getPpsNumber() {

        return ppsNumber;

    }

    // Get Transaction History Method
    public LinkedList<Transaction> getTransactionHistory() {

        return transactionHistory;

    }

    // toString Method
    public String toString() {

        return "\nName: " + name + "\nPPS Number: " + ppsNumber + "\nEmail: " + email + "\nPassword: " + password +
                "\nAddress: " + address + "\nBalance: " + initialBalance + "\n";

    }

    // Setters
    public void setPassword(String newPassword) {

        this.password = newPassword;

    }

    // Lodge Money Method
    public void lodgeMoney(float amount) {

        initialBalance += amount;

    }

    // Add Transaction Method
    public void addTransaction(Transaction transaction) {

        transactionHistory.add(transaction);

    }

}