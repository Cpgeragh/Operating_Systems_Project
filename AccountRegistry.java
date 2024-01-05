import java.util.Iterator;
import java.util.LinkedList;

// Account Registry Class
public class AccountRegistry {

    // Variables
    LinkedList<Account> accountList;

    // Constructor
    public AccountRegistry() {

        accountList = new LinkedList<>();

    }

    // Add Account Method
    public synchronized void addAccount(String name, String ppsNumber, String email, String password, String address,
            String initialBalance) {

        // Create Account
        Account temp = new Account(name, ppsNumber, email, password, address, Float.parseFloat(initialBalance));
        // Add Account to List
        accountList.add(temp);

    }

    // Find Account by Email and Password Method
    public synchronized Account findAccountByEmailAndPassword(String email, String password) {

        // Create Iterator
        Iterator<Account> iterator = accountList.iterator();

        // while Loop That Iterates Through List
        while (iterator.hasNext()) {

            // Next Account in List
            Account account = iterator.next();

            // if Account Email and Password Match Return the Account
            if (account.getEmail().equalsIgnoreCase(email) && account.getPassword().equals(password)) {

                // Return Account Object to Client
                return account;

            }

        }

        // Return Null if No Account is Found
        return null;

    }

    // Find Account by Email and PPS Number Method
    public synchronized Account findAccountByEmailAndPPS(String email, String ppsNumber) {

        // Create Iterator
        Iterator<Account> iterator = accountList.iterator();

        // while Loop That Iterates Through List
        while (iterator.hasNext()) {

            // Next Account in List
            Account account = iterator.next();

            // if Account Email and PPS Number Match Return the Account
            if (account.getEmail().equalsIgnoreCase(email) && account.getPpsNumber().equals(ppsNumber)) {

                // Return Account Object to Client
                return account;

            }

        }

        // Return Null if No Account is Found
        return null;

    }

    // Converts the Account List to String Array
    public synchronized String[] getListing() {

        // Variables
        int length = accountList.size();
        int counter = 0;
        // Create Array of Strings
        String[] listing = new String[length];
        // Create Iterator
        Iterator<Account> i = accountList.iterator();

        // while Loop That Iterates Through List
        while (i.hasNext()) {

            // Next Account in List
            Account temp = i.next();
            // Add Account to Array
            listing[counter] = temp.toString();
            // Increment Counter
            counter++;

        }

        // Return String Array to Client
        return listing;

    }

    public boolean isPPSNumberUsed(String ppsNumber) {
        for (Account account : accountList) {
            if (account.getPpsNumber().equals(ppsNumber)) {
                return true;
            }
        }
        return false;
    }

    public boolean emailExists(String email) {
        // Iterate through existing accounts and check if the email already exists
        for (Account account : accountList) {
            if (account.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean ppsNumberExists(String ppsNumber) {
        // Iterate through existing accounts and check if the PPS number already exists
        for (Account account : accountList) {
            if (account.getPpsNumber().equals(ppsNumber)) {
                return true;
            }
        }
        return false;
    }

}