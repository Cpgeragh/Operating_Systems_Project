public class Transaction {
    private String senderEmail;
    private String recipientEmail;
    private float amount;

    public Transaction(String senderEmail, String recipientEmail, float amount) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.amount = amount;
    }

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
