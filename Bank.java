import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Bank {
    private String name;
    private List<Account> accounts;
    private int numAccounts;

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
        Bank myBank = new Bank("MyBank");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to " + myBank.getName() + " bank!");

        // Prompt user for the type of account
        System.out.println("Choose account type: 1 - Regular Account, 2 - Savings Account");
        int accountTypeChoice = scanner.nextInt();

        // Create an account based on user's choice
        Account newAccount;
        if (accountTypeChoice == 1) {
            newAccount = new Account("John Doe", 1000);
        } else if (accountTypeChoice == 2) {
            newAccount = new SavingsAccount("Jane Doe", 2000);
        } else {
            System.out.println("Invalid choice. Creating a regular account by default.");
            newAccount = new Account("Default Account", 500);
        }

        // Add the new account to the bank
        myBank.addAccount(newAccount);

        // Display initial account information
        myBank.displayAccounts();

        // Process transactions (e.g., add interest)
        myBank.processTransactions();

        // Display updated account information
        myBank.displayAccounts();

        scanner.close();
    }
}
