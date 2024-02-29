public class Bank {
    private String name;
    private List<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    // Adds an account to the bank.
    public void addAccount(Account account) {
        accounts.add(account);
    }

    // Processes bank-wide transactions.
    public void processTransactions() {
        System.out.println("Processing transactions in " + name + " bank.");

        // Example: Update balances for all accounts
        for (Account account : accounts) {
            double interestRate = 0.02; // Example interest rate
            double balance = account.getBalance();
            double interest = balance * interestRate;
            account.deposit(interest); // Add interest to the account balance
        }
    }

    // Displays information about all accounts in the bank.
    public void displayAccounts() {
        System.out.println("Accounts in " + name + " bank:");
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    // Gets the name of the bank.
    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        // main method logic 
    }
}
