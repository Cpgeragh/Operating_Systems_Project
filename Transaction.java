// Transaction Class
public class Transaction {

    // Variables
    private String senderEmail;
    private String recipientEmail;
    private float amount;

    // Constructor
    public Transaction(String senderEmail, String recipientEmail, float amount) {

        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.amount = amount;

    }

    // Getters
    public String getSenderEmail() {

        return senderEmail;

    }

    public String getRecipientEmail() {

        return recipientEmail;

    }

    public float getAmount() {

        return amount;

    }
    
}
