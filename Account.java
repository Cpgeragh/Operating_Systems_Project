public class Account {

    private String name;
    private String ppsNumber;
    private String email;
    private String password;
    private String address;
    private float initialBalance;

    public Account(String n, String pps, String e, String pass, String addr, float balance) {
        name = n;
        ppsNumber = pps;
        email = e;
        password = pass;
        address = addr;
        initialBalance = balance;
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
        return name + "*" + ppsNumber + "*" + email + "*" + password + "*" + address + "*" + initialBalance;
    }
}