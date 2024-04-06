class Transaction {
    private String transactionId;
    private String transactionType;
    private double amount;
    private String date;
    private String accountID;
    private String recipientAccountID;

    public Transaction(String transactionId, String transactionType, double amount, String date, String accountID) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.accountID = accountID;
    }

    public Transaction(String transactionId, String transactionType, String accountID, String recipientAccountID,
            double amount, String date) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.accountID = accountID;
        this.recipientAccountID = recipientAccountID;
        this.amount = amount;
        this.date = date;
    }

    // Display the details of the transaction
    public String getDetails() {
        return "Transaction ID: " + transactionId +
                ", Type: " + transactionType +
                ", Amount: " + amount +
                ", Date: " + date +
                ", Account ID: " + accountID;
    }

    public String getTransactionId() {
        return String.valueOf(this.transactionId);
    }

    public String getTransactionType() {
        return String.valueOf(this.transactionType);
    }

    public double getAmount() {
        return this.amount;
    }

    public CharSequence getDate() {
        return (CharSequence) date;
    }

    public String getAccountID() {
        return String.valueOf(this.accountID);
    }

    public String getRecipientAccountID() {
        return String.valueOf(this.recipientAccountID);
    }
}