import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    private String name;
    private int totalAccounts = 0;
    private List<Account> accounts;
    final static String CSV_FILE = "customers.csv";

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

    public static String generateRandomCustomerID() {
        Random rand = new Random();
        int fiveDigitNumber = 10000 + rand.nextInt(90000);
        return "C" + Integer.toString(fiveDigitNumber);
    }

    public static String generateRandomAccountID() {
        Random rand = new Random();
        int fiveDigitNumber = 10000 + rand.nextInt(90000);
        return "A" + Integer.toString(fiveDigitNumber);
    }

    private static boolean signup() {
        
        
        Scanner scanner = new Scanner(System.in);
        //String accountType;
        String name;
        String nric;
        LocalDate dob;
        int contactNumber;
        String email;
        String address;
        while (true) {
            System.out.println("Enter username and password to create an customer profile (type 'exit' to finish):");

            System.out.print("Username: ");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("exit") || username.equalsIgnoreCase("no")) {
                System.out.println("Sign up cancelled.");
                scanner.close();
                return false; // Return false if the user wants to exit
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            System.out.print("Enter your NRIC: ");
            nric = scanner.nextLine();
            System.out.print("Enter your date of birth (YYYY-MM-DD): ");
            dob = LocalDate.parse(scanner.nextLine());
            System.out.print("Enter your contact number: ");
            contactNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            System.out.print("Enter your address: ");
            address = scanner.nextLine();

            try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
                String customerID = generateRandomCustomerID();
                //String username = ...; // get username
                //String password = ...; // get password
                //String name = ...; // get name
                //String nric = ...; // get nric
                //String dob = ...; // get dob
                //String contactNumber = ...; // get contact number
                //String email = ...; // get email
                //String address = ...; // get address
                String defaultAccountNumber = generateRandomAccountID(); // generate random account number
                String accountBalance = "0"; // initial account balance
            
                writer.append(customerID).append(",")
                      .append(username).append(",")
                      .append(password).append(",")
                      .append(name).append(",")
                      .append(nric).append(",")
                      .append(dob.toString()).append(",")
                      .append(String.valueOf(contactNumber)).append(",")
                      .append(email).append(",")
                      .append(address).append(",")
                      .append(defaultAccountNumber).append(",")
                      .append(accountBalance)
                      .append("\n"); // go to next line for next customer
                writer.close();
            } catch (IOException e) {
                System.err.println("Error writing to CSV file: " + e.getMessage());
            }

            System.out.println("Customer profile created successfully.");
            scanner.close();
            break;
        }

        return true; // Return true if the user successfully signs up
    }

    private static void login() {
        final String CSV_FILE = "customers.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            Scanner scanner = new Scanner(System.in);
    
            System.out.println("Enter your username and password to log in:");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    System.out.println("Login successful!");
                    return;
                }
            }
    
            System.out.println("Incorrect username or password. Please try again.");
    
        } catch (IOException e) {
            System.err.println("Error reading from CSV file: " + e.getMessage());
        }
    }

    public static String showAccountTypeMenu() {
        Scanner chooseAccountScanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose account type: 1. Savings, 2. Fixed Deposit");
            System.out.print("Your choice: ");
            int accountTypeChoice = chooseAccountScanner.nextInt();

            switch (accountTypeChoice) {
                case 1:
                    chooseAccountScanner.close();
                    return "Saving";

                case 2:
                    chooseAccountScanner.close();
                    return "Fixed Deposit";

                default:
                    System.out.println("Invalid Choice. Please choose a valid option.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner mainscanner = new Scanner(System.in);
        Bank bank = new Bank(null);

        System.out.println("Choose an option: 1. Sign up, 2. Log In, 3. Exit");
        System.out.print("Your choice: ");

        int choice = mainscanner.nextInt();
        mainscanner.nextLine(); // consume newline

    switch (choice) {
        case 1: // Sign up
            signup();
            /*System.out.println("Enter Customer ID to create an account:");
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
                break;*/

        case 2:
            System.out.println("Enter Customer ID to log in: ");
            int loginCustomerID = mainscanner.nextInt();
            mainscanner.nextLine();

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
        mainscanner.close();
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