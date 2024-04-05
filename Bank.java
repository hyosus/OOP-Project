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
    final static String CSV_FILE = "customers.csv";
    final static String LOAN_FILE = "Loans.csv";
    final static String CREDIT_CARD_FILE = "CreditCard.csv";

    public Bank(String name) {
        this.name = name;
    }

    // Adds an account to the bank.
    // public void createAccount(Account account) {
    //     accounts.add(account);
    //     totalAccounts++;
    // }

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
    // public void displayAccounts() {
    //     System.out.println("Accounts in " + name + " bank:");
    //     for (Account account : accounts) {
    //         System.out.println(account);
    //     }
    // }

    // Gets the name of the bank.
    public String getName() {
        return name;
    }

    public static boolean idExistsInCsv(String id, String CSV_FILE) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
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
            ExceptionHandling.handleIOException(e);
        }
        return false;
    }

    // private static String promptForInput(Scanner scanner, String promptMessage) {
    //     String input;
    //     do {
    //         System.out.print(promptMessage);
    //         input = scanner.nextLine();
    //         if (isEmpty(input)) {
    //             System.out.println("Input cannot be empty. Please enter again.");
    //         }
    //     } while (isEmpty(input));
    //     return input;
    // }

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
            String username = promptForInput(scanner, "Enter your username: ", "Username");
            if (username.equalsIgnoreCase("exit") || username.equalsIgnoreCase("no")) {
                System.out.println("Sign up cancelled.");
                return false;
            } else if (usernameExistsInCsv(username)) {
                System.out.println("Username already exists. Please choose another username.");
                continue; // Reprompt for username
            } 

            String password = promptForInput(scanner, "Enter your password: ", "Password");
            String secretKey = null;
            try {
                secretKey = generateKey.generateSecretKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String salt = generateKey.generateSalt();
            String encryptedPassword = AES.encrypt(password, secretKey, salt);

            //check customer details
            name = promptForInput(scanner, "Enter your name: ", "Name");

            // NRIC Validation
            while (true) {
                nric = promptForInput(scanner, "Enter your NRIC: ", "NRIC");
                if (ExceptionHandling.handleNric(nric)) {
                    break; // Exit the loop if NRIC is valid
                }
            }

            while (true) {
                System.out.print("Enter your date of birth (YYYY-MM-DD): ");
                try {
                    dob = LocalDate.parse(scanner.nextLine());

                    // Ensure dob is not in the future
                    if (dob.isAfter(LocalDate.now())) {
                        System.out.println("Date of birth cannot be in the future. Please re-enter a valid date.");
                        continue; // Prompt to enter the dob again
                    }
                    break; // exit the loop if the input was valid
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
                        break; // exit the loop if the input was valid
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
                    break; // Exit the loop if email is valid
                }
            }


            address = promptForInput(scanner, "Enter your address: ", "Address");

            try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
                String customerID = Customer.generateRandomCustomerID(); // generate random customer ID
                String defaultAccountNumber = Account.generateRandomDefaultAccountID(); // generate random account number
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
                        .append("\n"); // go to next line for next customer
                writer.close();
            } catch (IOException e) {
                ExceptionHandling.handleIOException(e);
                System.out.println("Failed to create account.");
                break;
            }

            System.out.println("Customer profile created successfully.");
            break;
        }
        return true; // Return true if the user successfully signs up
    }

    // public static boolean login() {
    //     final String CSV_FILE = "customers.csv";
    //     Scanner scanner = new Scanner(System.in);

    //     while (true) {
    //         System.out.println("Enter your username and password to log in, or type 'exit' to return to the main menu:");
    //         System.out.print("Username: ");
    //         String username = scanner.nextLine();

    //         if (username.equalsIgnoreCase("exit")) {
    //             return false;
    //         }

    //         System.out.print("Password: ");
    //         String password = scanner.nextLine();

    //         try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 String[] parts = line.split(",");
    //                 if (parts.length >= 3 && parts[1].equals(username) && parts[2].equals(password)) {
    //                     System.out.println("Login successful!");
    //                     return true;
    //                 }
    //             }
    //         } catch (IOException e) {
    //             System.err.println("Error reading from CSV file: " + e.getMessage());
    //         }

    //         System.out.println("Incorrect username or password. Please try again.");
    //     }
    // }

    public static Customer login() {
        final String CSV_FILE = "customers.csv";
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your username and password to log in");
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            Customer customer = Customer.loadCustomerByUsernameAndPassword(username, password, CSV_FILE);

            if (customer != null) {
                customer.loadAccounts(CSV_FILE, customer.getCustomerID());
                for (Account account : customer.getAccounts()) {
                    account.loadLoans(LOAN_FILE, account.getAccountID());
                }
                customer.loadCreditCards(CREDIT_CARD_FILE, customer.getCustomerID());
                System.out.println("Login successful!");
                //scanner.close();
                return customer;
            } else {
                System.out.println("Incorrect username or password. Please try again.");
            }
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

    public static void showLoginMenu(Customer customer) {
        Scanner loginScanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. View account(s) info");
            // System.out.println("2. View Branch info");
            // System.out.println("3. View Insurance info");
            // System.out.println("4. View Loan info");
            // System.out.println("5. Take Loan");
            System.out.println("2. Deposit/Withdraw/Transfer");
            System.out.println("3. Currency Exchange");
            System.out.println("4. Show Credit Card(s)");
            System.out.println("5. Apply for Credit Card");
            System.out.println("6. Create New Account");
            System.out.println("7. Settings");
            System.out.println("8. Logout");

            System.out.print("Your choice: ");
            int choice = loginScanner.nextInt();
            loginScanner.nextLine();
            switch (choice) {
                case 1:
                    // Display Account info
                    customer.displayAllAccountInfo();
                    break;
                // case 2:
                //     // View Branch info
                //     break;
                // case 3:
                //     // View Insurance info
                //     break;
                // case 4:
                //     // View Loan info
                //     Account viewLoanChoice = customer.promptAccount(loginScanner);
                //     viewLoanChoice.displayLoans();
                //     //To Do: Add option to pay loan
                //     break;
                // case 5:
                //     // Take Loan
                //     Account loanChoice = customer.promptAccount(loginScanner);
                //     loanChoice.createLoan(LOAN_FILE);
                //     break;
                case 2:
                    // Deposit/Withdraw/Transfer
                    Account accountChoice = customer.promptAccount(loginScanner);
                    performTransactions(accountChoice, loginScanner);
                    break;
                case 3:
                    // Currency Exchange
                    break;
                case 4:
                    // Credit Card
                    CreditCardAccount creditCardChoice = customer.promptCreditCardAccount(loginScanner);
                    if (creditCardChoice == null) {
                        break;
                    }
                    creditCardChoice.creditCardAccountMenu(loginScanner);
                    break;
                case 5:
                    // Apply for Credit Card
                    CreditCardAccount.createCreditCardAccount(loginScanner, customer);
                    break;
                case 6:
                    Account.createNewAccount(loginScanner, customer);
                    break;
                case 7:
                    // Settings
                    setting setting = new setting();
                    setting.settingMenu(customer);
                    break;
                case 8:
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

    private static void performTransactions(Account account, Scanner scanner) {
        while (true) {
            System.out.println("Choose a transaction:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Display Account Info");
            System.out.println("5. Exit");

            int transactionChoice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (transactionChoice) {

                case 1:
                    System.out.println("Enter the deposit amount:");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume the newline
                    account.deposit(depositAmount);
                    break;

                case 2:
                    System.out.println("Enter the withdrawal amount:");
                    double withdrawalAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume the newline
                    account.withdraw(withdrawalAmount);
                    break;

                case 3:
                    System.out.println("Enter recipient's account ID:");
                    String recipientAccountID = scanner.nextLine();
                    System.out.println("Enter the transfer amount:");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline
                    account.transfer(account.getAccountID(), recipientAccountID, transferAmount);
                    break;
                case 4:
                    account.displayAccountInfo();
                    break;

                case 5:
                    System.out.println("Exiting transactions!");
                    return;

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
