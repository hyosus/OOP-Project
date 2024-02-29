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
