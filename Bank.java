import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
        String id;
        do {
            Random rand = new Random();
            int fiveDigitNumber = 10000 + rand.nextInt(90000);
            id = "C" + Integer.toString(fiveDigitNumber);
        } while (idExistsInCsv(id));
        return id;
    }

    public static String generateRandomAccountID() {
        String id;
        do {
            Random rand = new Random();
            int fiveDigitNumber = 10000 + rand.nextInt(90000);
            id = "A" + Integer.toString(fiveDigitNumber);
        } while (idExistsInCsv(id));
        return id;
    }

    public static boolean idExistsInCsv(String id) {
        final String CSV_FILE = "customers.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from CSV file: " + e.getMessage());
        }
        return false;
    }

    private static boolean usernameExistsInCsv(String username) {
        final String CSV_FILE = "customers.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from CSV file: " + e.getMessage());
        }
        return false;
    }

    public static boolean signup() {
    
        Scanner scanner = new Scanner(System.in);
        //String accountType;
        String name;
        String nric;
        LocalDate dob;
        int contactNumber;
        String email;
        String address;
        while (true) {
            //check username and password
            System.out.println("Enter username and password to create an customer profile (type 'exit' to finish):");
            String username;
            do {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                if (usernameExistsInCsv(username)) {
                    System.out.print("Username already exists. Please choose another username.");
                }
                else if (username.isEmpty()) {
                    System.out.println("Username cannot be empty. Please enter a username.");
                }
                else if (username.equalsIgnoreCase("exit") || username.equalsIgnoreCase("no")) {
                    System.out.println("Sign up cancelled.");
                    return false;
                }
            } while (usernameExistsInCsv(username) || username.isEmpty());
            String password;
            do {
                System.out.print("Enter your password: ");
                password = scanner.nextLine();
                if (password.isEmpty()) {
                    System.out.println("Password cannot be empty. Please enter a password.");
                }
            } while (password.isEmpty());
            //check customer details
            System.out.print("Enter your name: ");
            do {
                name = scanner.nextLine();
                if (name.isEmpty()) {
                    System.out.print("Name cannot be empty. Please enter again:");
                }
            } while (name.isEmpty());
            
            System.out.print("Enter your NRIC: ");
            do {
                nric = scanner.nextLine();
                if (nric.isEmpty()) {
                    System.out.print("NRIC cannot be empty. Please enter again:");
                }
            } while (nric.isEmpty());
            
            while (true) {
                System.out.print("Enter your date of birth (YYYY-MM-DD): ");
                try {
                    dob = LocalDate.parse(scanner.nextLine());
                    break; // exit the loop if the input was valid
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
                }
            }
            
            while (true) {
                System.out.print("Enter your contact number: ");
                try {
                    contactNumber = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    break; // exit the loop if the input was valid
                } catch (InputMismatchException e) {
                    System.out.print("Invalid contact number. Please enter a valid number.");
                    scanner.nextLine(); // consume newline
                }
            }
            
            System.out.print("Enter your email: ");
            do {
                email = scanner.nextLine();
                if (email.isEmpty()) {
                    System.out.print("Email cannot be empty. Please enter again:");
                }
            } while (email.isEmpty());
            
            System.out.print("Enter your address: ");
            do {
                address = scanner.nextLine();
                if (address.isEmpty()) {
                    System.out.println("Address cannot be empty. Please enter again:");
                }
            } while (address.isEmpty());

            try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
                String customerID = generateRandomCustomerID(); // generate random customer ID
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
            break;
        }
        return true; // Return true if the user successfully signs up
    }

    public static boolean login() {
        final String CSV_FILE = "customers.csv";
        Scanner scanner = new Scanner(System.in);
    
        while (true) {
            System.out.println("Enter your username and password to log in, or type 'exit' to return to the main menu:");
            System.out.print("Username: ");
            String username = scanner.nextLine();
    
            if (username.equalsIgnoreCase("exit")) {
                return false;
            }
    
            System.out.print("Password: ");
            String password = scanner.nextLine();
    
            try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3 && parts[1].equals(username) && parts[2].equals(password)) {
                        System.out.println("Login successful!");
                        return true;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading from CSV file: " + e.getMessage());
            }
    
            System.out.println("Incorrect username or password. Please try again.");
        }
    }

    /*public static String showAccountTypeMenu() {
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
    }*/

    public static void showLoginMenu() {
        Scanner loginScanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. View account(s) info");
            System.out.println("2. View Branch info");
            System.out.println("3. View Insurance info");
            System.out.println("4. View Loan info");
            System.out.println("5. Deposit");
            System.out.println("6. Withdraw");
            System.out.println("7. Transfer");
            System.out.println("8. Currency Exchange");
            System.out.println("9. Credit Card");
            System.out.println("10. Logout");
            System.out.print("Your choice: ");
            int choice = loginScanner.nextInt();
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    System.out.println("Exiting...");
                    loginScanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
            }

        }

    public static void main(String[] args) {
        Scanner mainscanner = new Scanner(System.in);
        Bank bank = new Bank(null);
    
        while (true) {
            System.out.println("Choose an option:\n1. Sign up\n2. Log In\n3. Exit");
            System.out.print("Your choice: ");
    
            int choice = mainscanner.nextInt();
            mainscanner.nextLine(); // consume newline
    
            switch (choice) {
                case 1: // Sign up
                    signup();
                    break;
    
                case 2: // Log in
                    if (login()) {
                        showLoginMenu();
                    }
                    break;
                case 3: // Exit
                    System.out.println("Exiting...");
                    mainscanner.close();
                    System.exit(0);
    
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
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