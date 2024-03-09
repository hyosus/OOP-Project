import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Bank {
    private String name;
    private List<Account> accounts;
    private List<Customer> customers;

    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    // Adds an account to the bank.
    public void addAccount(Account account) {
        accounts.add(account);
    }
    // Adds a customer to the bank.
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    // Processes bank-wide transactions.
    public void processTransactions() {
        for (Account account : accounts) {
            double interestRate = 0.02; // Example interest rate
            double balance = account.getBalance();
            double interest = balance * interestRate;
            account.deposit(interest); // Add interest to the account balance
        }
        System.out.println("Processing transactions in " + name + " bank.");
    }

    // Displays information about all accounts in the bank.
    public void displayAccounts() {
        System.out.println("Accounts in " + name + " bank:");
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        Bank myBank = new Bank("MyBank");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to " + myBank.getName() + " bank!");

        // Prompt user for the type of account
        System.out.println("Are you an existing customer? 1 - Sign Up, 2 - Log In");
        int logInTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (logInTypeChoice == 1) {
            // Create a new customer profile
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your NRIC: ");
            String nric = scanner.nextLine();

            System.out.print("Enter your date of birth (YYYY-MM-DD): ");
            String dobString = scanner.nextLine();

            Date dob = null;
            try {
                dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobString);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Exiting...");
                System.exit(1);
            }

            System.out.print("Enter your contact number: ");
            int contactNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter your email address: ");
            String email = scanner.nextLine();

            System.out.print("Enter your address: ");
            String address = scanner.nextLine();

            Customer newCustomer = new Customer(name, nric, dob, contactNumber, email, address);
            myBank.addCustomer(newCustomer);

            // Display customer information
            System.out.println("\nCustomer Profile Created:");
            System.out.println(newCustomer);

            // Create a new account for the customer
            System.out.print("\nEnter the initial deposit amount: ");
            double initialDeposit = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            Account newAccount = new Account(newCustomer, initialDeposit);
            myBank.addAccount(newAccount);

            // Display account information
            System.out.println("\nAccount Created:");
            System.out.println(newAccount);
        } else if (logInTypeChoice == 2) {
            // Implement log-in logic here if needed
            System.out.println("Log-in functionality not implemented yet.");
        } else {
            System.out.println("Invalid choice. Exiting...");
            System.exit(1);
        }

        // Display initial account information
        myBank.displayAccounts();

        // Process transactions (e.g., add interest)
        myBank.processTransactions();

        // Display updated account information
        myBank.displayAccounts();

        // Close the scanner
        scanner.close();
    }
}