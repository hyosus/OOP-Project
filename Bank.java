import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.NumberFormatException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.regex.Pattern;
import java.util.Scanner;
import com.bank.components.security.*;
import com.G17.Bank.Entity.CC.*;

public class Bank {
    private String name;
    final static String CUSTOMERS_CSV_FILE = "customers.csv";
    final static String LOAN_FILE = "Loans.csv";
    final static String CREDIT_CARD_FILE = "CreditCard.csv";
    private static final List<Customer> allCustomers = loadAllCustomers();

    public Bank(String name) {
        this.name = name;
        loadAllCustomers();
    }

    public String getName() {
        return name;
    }
    
    // Method to load all customers and their accounts from the CSV file
    public static List<Customer> loadAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    String customerID = parts[0];
                    String name = parts[3];
                    String nric = parts[4];
                    LocalDate dob = LocalDate.parse(parts[5]);
                    int contactNumber = Integer.parseInt(parts[6]);
                    String email = parts[7];
                    String address = parts[8];
                    Customer customer = new Customer(customerID, name, nric, dob, contactNumber, email, address);
                    customer.loadAccounts(CUSTOMERS_CSV_FILE, customerID);
                    customer.loadCreditCards(CREDIT_CARD_FILE, customerID);
                    customers.add(customer);
                }
            }
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException | DateTimeParseException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return customers;
    }


    public static Customer findCustomerByAccountID(String accountID, List<Customer> allCustomers) {
        for (Customer customer : allCustomers) {
            for (Account account : customer.getAccounts()) {
                if (account.getAccountID().equals(accountID)) {
                    return customer; 
                }
            }
        }
        return null;
    }

    // Checks if a customer with the given ID exists in the CSV file
    public static boolean idExistsInCsv(String id, String CUSTOMERS_CSV_FILE) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(String.valueOf(id))) {
                    return true;
                }
            }
        } catch (IOException e) {
            ExceptionHandling.handleIOException(e);
        }
        return false;
    }

    // Checks if a username exists in the CSV file
    private static boolean usernameExistsInCsv(String username) {
        final String CUSTOMERS_CSV_FILE = "customers.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            ExceptionHandling.handleIOException(e);
        }
        return false;
    }

    // Prompt for input and handle empty input
    private static String promptForInput(Scanner scanner, String promptMessage, String fieldName) {
        String input;
        do {
            System.out.print(promptMessage);
            input = scanner.nextLine();
            if (isEmpty(input)) {
                ExceptionHandling.handleEmptyInputException(fieldName);
            }
        } while (isEmpty(input));
        return input;
    }

    // Method to check if a string is empty or not
    private static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Method to sign up a new customer
    public static boolean signup() {

        Scanner scanner = new Scanner(System.in);
        String name;
        String nric;
        LocalDate dob;
        int contactNumber;
        String email;
        String address;

        while (true) {
            System.out.println("Enter username and password to create an customer profile (type 'exit' to finish):");
            String username = promptForInput(scanner, "Enter your username: ", "Username");
            if (username.equalsIgnoreCase("exit") || username.equalsIgnoreCase("no")) {
                System.out.println("Sign up cancelled.");
                return false;
            } else if (usernameExistsInCsv(username)) {
                System.out.println("Username already exists. Please choose another username.");
                continue;
            }

            // Password encryption
            String password = promptForInput(scanner, "Enter your password: ", "Password");
            String secretKey = null;
            try {
                secretKey = generateKey.generateSecretKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String salt = generateKey.generateSalt();
            String encryptedPassword = AES.encrypt(password, secretKey, salt);

            name = promptForInput(scanner, "Enter your name: ", "Name");

            // NRIC Validation
            while (true) {
                nric = promptForInput(scanner, "Enter your NRIC: ", "NRIC");
                if (ExceptionHandling.handleNric(nric)) {
                    break; //
                }
            }

            // Prompt and validate date of birth
            while (true) {
                System.out.print("Enter your date of birth (YYYY-MM-DD): ");
                try {
                    dob = LocalDate.parse(scanner.nextLine());

                    if (dob.isAfter(LocalDate.now())) {
                        System.out.println("Date of birth cannot be in the future. Please re-enter a valid date.");
                        continue;
                    }
                    break;
                } catch (DateTimeParseException e) {
                    ExceptionHandling.handleDateTimeParseException();
                }
            }

            // Prompt and validate contact number
            while (true) {
                String contactNumberStr = promptForInput(scanner, "Enter your contact number: ", "Contact number");
                if (contactNumberStr.matches("[89]\\d{7}")) {
                    try {
                        contactNumber = Integer.parseInt(contactNumberStr);
                        break;
                    } catch (NumberFormatException e) {
                        ExceptionHandling.handleNumberFormatException();
                    }
                } else {
                    System.out.println("Contact number should start with either 8 or 9 and contain exactly 8 digits. Please try again.");
                }
            }

            // Prompt and validate email
            while (true) {
                email = promptForInput(scanner, "Enter your email: ", "Email");
                if (ExceptionHandling.handleEmail(email)) {
                    break;
                }
            }

            address = promptForInput(scanner, "Enter your address: ", "Address");

            try (FileWriter writer = new FileWriter(CUSTOMERS_CSV_FILE, true)) {
                String customerID = Customer.generateRandomCustomerID();
                String defaultAccountNumber = Account.generateRandomDefaultAccountID();
                String accountBalance = "0";

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
                        .append(accountBalance).append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append("").append(",")
                        .append(encryptedPassword).append(",")
                        .append(secretKey).append(",")
                        .append(salt)
                        .append("\n");
                writer.close();
            } catch (IOException e) {
                ExceptionHandling.handleIOException(e);
                System.out.println("Failed to create account.");
                break;
            }

            System.out.println("Customer profile created successfully.");
            break;
        }
        return true;
    }

    // Method to log in a customer
    public static Customer login() {
        final String CUSTOMERS_CSV_FILE = "customers.csv";
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your username and password to log in");
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            Customer customer = Customer.loadCustomerByUsernameAndPassword(username, password, CUSTOMERS_CSV_FILE);

            if (customer != null) {
                customer.loadAccounts(CUSTOMERS_CSV_FILE, customer.getCustomerID());
                for (Account account : customer.getAccounts()) {
                    account.loadLoans(LOAN_FILE, account.getAccountID());
                }
                customer.loadCreditCards(CREDIT_CARD_FILE, customer.getCustomerID());
                System.out.println("Login successful!");
                return customer;
            } else {
                System.out.println("Incorrect username or password. Please try again.");
            }
        }
    }

    // Method to display the login menu
    public static void showLoginMenu(Customer customer) {
        Scanner loginScanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. View account(s) info");
            System.out.println("2. Deposit/Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Currency Exchange");
            System.out.println("5. Show Credit Card(s)");
            System.out.println("6. Apply for Credit Card");
            System.out.println("7. Create New Account");
            System.out.println("8. Settings");
            System.out.println("9. Logout");

            System.out.print("Your choice: ");
            int choice = loginScanner.nextInt();
            loginScanner.nextLine();
            switch (choice) {
                case 1:
                    // Display Account info
                    customer.displayAllAccountInfo();
                    break;
                case 2:
                    // Deposit/Withdraw/Transfer
                    Account accountChoice = customer.promptAccount(loginScanner);
                    performTransactions(accountChoice, loginScanner);
                    break;
                case 3:
                    // Transfer
                    System.out.println("1. Internal Transfer 2. External Transfer");
                    int transferOption = loginScanner.nextInt();
                    loginScanner.nextLine();

                    if (transferOption == 1) {
                        System.out.println("Select Sender Account:");
                        Account senderAccount = customer.promptAccount(loginScanner);
                        System.out.println("Select Receiver Account:");
                        Account receiverAccount = customer.promptAccount(loginScanner);

                        if (senderAccount != null && receiverAccount != null && !senderAccount.equals(receiverAccount)) {
                            System.out.println("Enter the transfer amount:");
                            double transferAmount = loginScanner.nextDouble();
                            loginScanner.nextLine();

                            if (transferAmount > senderAccount.getBalance()) {
                                System.out.println("Insufficient funds for the transfer.");
                            } else {
                                senderAccount.setBalance(senderAccount.getBalance() - transferAmount);
                                receiverAccount.setBalance(receiverAccount.getBalance() + transferAmount);
                                System.out.println("Transfer successful: $" + transferAmount + " from " + senderAccount.getAccountID() + " to " + receiverAccount.getAccountID());

                                senderAccount.transfer(receiverAccount, transferAmount);
                            }
                        } else {
                            System.out.println("Invalid accounts selected for transfer.");
                        }

                        } else if (transferOption == 2) {
                        System.out.println("Select Sender Account:");
                        Account senderAccount = customer.promptAccount(loginScanner);
                        if (senderAccount == null) {
                            System.out.println("No sender account selected.");
                            break;
                        }

                        loginScanner.nextLine();

                        System.out.println("Enter recipient's account ID:");
                        String recipientAccountID = loginScanner.nextLine();

                        if (recipientAccountID.isEmpty()) {
                            System.out.println("Recipient account ID cannot be empty.");
                            break;
                        }

                        System.out.println("Enter the transfer amount:");
                        if (!loginScanner.hasNextDouble()) {
                            System.out.println("Invalid amount. Transfer cancelled.");
                            loginScanner.nextLine();
                            break;
                        }
                        double transferAmount = loginScanner.nextDouble();

                        senderAccount.transfer(recipientAccountID, transferAmount);
                        break;
                    }
                case 4:
                    // Currency Exchange
                    break;
                case 5:
                    // Credit Card
                    CreditCardAccount creditCardChoice = customer.promptCreditCardAccount(loginScanner);
                    if (creditCardChoice == null) {
                        break;
                    }
                    creditCardChoice.creditCardAccountMenu(loginScanner);
                    break;
                case 6:
                    // Apply for Credit Card
                    CreditCardAccount.createCreditCardAccount(loginScanner, customer);
                    break;
                case 7:
                    // Create New Account
                    Account.createNewAccount(loginScanner, customer);
                    break;
                case 8:
                    // Settings
                    setting setting = new setting();
                    setting.settingMenu(customer);
                    break;
                case 9:
                    // Logout
                    System.out.println("Exiting...");
                    loginScanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    // Method to perform Deposit/Withdraw
    private static void performTransactions(Account account, Scanner scanner) {
        while (true) {
            System.out.println("Choose a transaction:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Display Account Info");
            System.out.println("4. Exit");

            int transactionChoice = scanner.nextInt();
            scanner.nextLine();

            switch (transactionChoice) {

                case 1:
                    System.out.println("Enter the deposit amount:");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); 
                    account.deposit(depositAmount);
                    break;

                case 2:
                    System.out.println("Enter the withdrawal amount:");
                    double withdrawalAmount = scanner.nextDouble();
                    scanner.nextLine(); 
                    account.withdraw(withdrawalAmount);
                    break;

                case 3:
                    account.displayAccountInfo();
                    break;

                case 4:
                    System.out.println("Exiting transactions!");
                    return;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner mainscanner = new Scanner(System.in);
        Bank bank = new Bank("G2 Bank");

        while (true) {
            System.out.println("Choose an option:\n1. Sign up\n2. Log In\n3. Exit");
            System.out.print("Your choice: ");

            int choice = mainscanner.nextInt();
            mainscanner.nextLine();

            switch (choice) {
                case 1: // Sign up
                    signup();
                    break;

                case 2: // Log in
                {
                    showLoginMenu(login());
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

}
