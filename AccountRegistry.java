import java.util.Iterator;
import java.util.LinkedList;

public class AccountRegistry {

    LinkedList<Account> accountList;

    public AccountRegistry() {
        accountList = new LinkedList<>();
    }

    public synchronized void addAccount(String name, String ppsNumber, String email, String password, String address, String initialBalance) {
        Account temp = new Account(name, ppsNumber, email, password, address, Float.parseFloat(initialBalance));
        accountList.add(temp);
    }

    public synchronized String searchAccount(String name) {
        Iterator<Account> i = accountList.iterator();
        int found = 0;
        String response = "Not found";
        while (i.hasNext() && found == 0) {
            Account temp = i.next();

            if (temp.getAccountName().equalsIgnoreCase(name)) {
                found = 1;
                response = temp.toString();
            }
        }

        return response;
    }

    public synchronized String[] getListing() {
        int length = accountList.size();
        int counter = 0;
        String[] listing = new String[length];
        Iterator<Account> i = accountList.iterator();
        while (i.hasNext()) {
            Account temp = i.next();
            listing[counter] = temp.toString();
            counter++;
        }

        return listing;
    }
}