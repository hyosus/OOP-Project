import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {
    private String name;
    private int totalAccounts = 0;
    private List<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    // Adds an account to the bank.
    public void createAccount(Account account) {
        accounts.add(account);
        totalAccounts++;
    }

    // Processes bank-wide transactions.
    // public void processTransactions() {
    //     for (Account account : accounts) {
    //         double interestRate = 0.02; // Example interest rate
    //         double balance = account.getBalance();
    //         double interest = balance * interestRate;
    //         account.deposit(interest); // Add interest to the account balance
    //     System.out.println("Processing transactions in " + name + " bank.");
    //     }
    // }

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
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank(null);

        System.out.println("Choose an option: 1. Sign up, 2. Log In, 3. Exit");
        System.out.print("Your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

    switch (choice) {
        case 1: // Sign up
            System.out.println("Enter Customer ID to create an account:");
            int customerID = scanner.nextInt();
            scanner.nextLine(); // consume newline

            Customer customer = new Customer(customerID, null, null, null, customerID, null, null);
            customer.setCustomerID(1);

            System.out.println("Choose account type: 1. Savings, 2. Current, 3. Fixed Deposit");
            System.out.print("Your choice: ");
            int accountTypeChoice = scanner.nextInt();
            scanner.nextLine();

            String accountType;
            switch (accountTypeChoice) {
                case 1:
                    accountType = "Savings";
                    break;

                case 2:
                    accountType = "Current";
                    break;

                case 3:
                    accountType = "Fixed Deposit";
                    break;

                default:
                    System.out.println("Invalid Choice. Please choose a valid option.");
                    return; // Add return statement to exit the method if the choice is invalid
            }

                Account newAccount = new Account(accountType, customer);
                bank.createAccount(newAccount);
                System.out.println("Account created successfully!");

                performTransactions(newAccount, scanner);
                break;

        case 2:
            System.out.println("Enter Customer ID to log in: ");
            int loginCustomerID = scanner.nextInt();
            scanner.nextLine();

            // --if want to simulate authentication (replace w authentication logic)
            // Customer loggedInCustomer = authenticateCustomer(loginCustomerID);

            // if (loggedInCustomer != null) {
            //     System.out.println("Login successful!");
            //     // Add logic for user transactions after login
            //     performTransactions(loggedInCustomer.getAccount(), scanner);
            // } else {
            //     System.out.println("Login failed. Invalid Customer ID.");
            // }

            // --if want to skip authentication and proceed directly to transactions
            // Customer loggedInCustomer = new Customer(loginCustomerID, null, null, null, loginCustomerID, null, null);

            // System.out.println("Login successful!");
            // performTransactions(loggedInCustomer.getAccount(), scanner);

            break;

        case 3:
            System.out.println("Exiting...");
            System.exit(0);

        default:
            System.out.println("Invalid choice. Please choose a valid option.");

        }
        scanner.close();
    }

    private static void performTransactions(Account account, Scanner scanner) {
        while (true) {
            System.out.println("Choose a transaction:");
            System.out.println("1. Display Account Info");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            int transactionChoice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (transactionChoice) {
                case 1:
                    account.displayAccountInfo();
                    break;

                case 2:
                    System.out.println("Enter the withdrawal amount:");
                    double withdrawalAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume the newline
                    account.withdraw(withdrawalAmount);
                    break;

                case 3:
                    System.out.println("Enter the deposit amount:");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume the newline
                    account.deposit(depositAmount);
                    break;

                case 4:
                    // transfer money logic
                    break;

                case 5:
                    System.out.println("Exiting transactions!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }
    
    // Authentication method (replace w authentication logic)
    // private static Customer authenticateCustomer(int customerID) {
    //     for (Account account : bank.getAccounts()) {
    //         Customer customer = account.getCustomer();
    //         if (customer != null && customer.getCustomerID() == customerID) {
    //             return customer;
    //         }
    //     }
    //     return null; // Customer not found
    // }
}