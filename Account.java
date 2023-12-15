import java.util.LinkedList;

public class Account {
    private String name;
    private String ppsNumber;
    private String email;
    private String password;
    private String address;
    private float initialBalance;
    private LinkedList<Transaction> transactionHistory;

    public Account(String n, String pps, String e, String pass, String addr, float balance) {
        name = n;
        ppsNumber = pps;
        email = e;
        password = pass;
        address = addr;
        initialBalance = balance;
        transactionHistory = new LinkedList<>();
    }

    public String getAccountName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return "\nName: " + name + "\nPPS Number: " + ppsNumber + "\nEmail: " + email + "\nPassword: " + password +
                "\nAddress: " + address + "\nBalance: " + initialBalance + "\n";
    }

    public float getInitialBalance() {
        return initialBalance;
    }

    public String getPpsNumber() {
        return ppsNumber;
    }

    public void lodgeMoney(float amount) {
        initialBalance += amount;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public LinkedList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    
}