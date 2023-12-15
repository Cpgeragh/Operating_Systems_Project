import java.util.Iterator;
import java.util.LinkedList;

public class AccountRegistry {

    LinkedList<Account> accountList;

    public AccountRegistry() {
        accountList = new LinkedList<>();
    }

    public synchronized void addAccount(String name, String ppsNumber, String email, String password, String address,
            String initialBalance) {
        Account temp = new Account(name, ppsNumber, email, password, address, Float.parseFloat(initialBalance));
        accountList.add(temp);
    }

    public synchronized Account findAccountByEmailAndPassword(String email, String password) {
        Iterator<Account> iterator = accountList.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            if (account.getEmail().equalsIgnoreCase(email) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    public synchronized Account findAccountByEmailAndPPS(String email, String ppsNumber) {
        Iterator<Account> iterator = accountList.iterator();
        while (iterator.hasNext()) {
            Account account = iterator.next();
            if (account.getEmail().equalsIgnoreCase(email) && account.getPpsNumber().equals(ppsNumber)) {
                return account;
            }
        }
        return null;
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