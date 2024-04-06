import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Security;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import com.bank.components.security.*;
import com.G17.Bank.Entity.CC.*;



public class Customer {

    private static final String CUSTOMERS_CSV_FILE = "customers.csv";
    private static final String CREDIT_CARD_ACCOUNT_FILE = "CreditCard.csv";

    private String customerID;
    private String name;
    private String nric;
    private LocalDate dateOfBirth;
    private int contactNumber;
    private String email;
    private String address;
    private List<Account> accounts;
    private List<CreditCardAccount> creditCardAccounts;

    public Customer(String customerID, String name, String nric, LocalDate dob, int contactNumber, String email,  String address) {
        this.customerID = customerID;
        this.name = name;
        this.nric = nric;
        this.dateOfBirth = dob;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.accounts = new ArrayList<>();
        this.creditCardAccounts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setnric(String nric) {
        this.nric = nric;
    }

    public void setDateofBirth(LocalDate dob)
    {
        this.dateOfBirth = dob;
    }

    public void setConteactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void displayAllAccountInfo() {
        for (Account account : accounts) {
            account.displayAccountInfo();
        }
    }

    public boolean hasAccountType(String accountType) {
        for (Account account : accounts) {
            if (account.getAccountType().equalsIgnoreCase(accountType)) {
                return true;
            }
        }
        return false;
    }

    // Generate a random customer ID
    public static String generateRandomCustomerID() {
        String id;
        do {
            Random rand = new Random();
            int fiveDigitNumber = 10000 + rand.nextInt(90000);
            id = "C" + Integer.toString(fiveDigitNumber);
        } while (Bank.idExistsInCsv(id, CUSTOMERS_CSV_FILE));
        return id;
    }

// Load account(s) a customer has
public void loadAccounts(String filename, String customerID) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 23 && parts[0].equals(customerID)) {
                for (int i = 9; i < parts.length-3; i += 4) {
                    String accountId = parts[i];
                    if (accountId.isEmpty()) {
                        continue;
                    }

                    double balance = 0;
                    double transferLimit = 0;
                    double withdrawalLimit = 0;
                    if (!parts[i + 1].isEmpty()) {
                        balance = Double.parseDouble(parts[i + 1]); 
                    }
                    if (!parts[i + 2].isEmpty()) {
                        transferLimit = Double.parseDouble(parts[i + 2]);
                    }
                    else {
                        transferLimit = Account.getDefaultTransferLimit();
                    }
                    if (!parts[i + 3].isEmpty()) {
                        withdrawalLimit = Double.parseDouble(parts[i + 3]);
                    }
                    else {
                        withdrawalLimit = Account.getDefaultWithdrawLimit();
                    }

                    String accountType;
                    switch (accountId.charAt(0)) {
                        case 'A':
                            accountType = "Default";
                            break;
                        case 'S':
                            accountType = "Savings";
                            break;
                        case 'J':
                            accountType = "Investment";
                            break;
                        default:
                            continue;
                    }
                    // Load normal accounts
                    Account account = new Account(accountId, accountType, balance);
                    account.setTransferLimit(transferLimit);
                    account.setWithdrawLimit(withdrawalLimit);
                    if (!this.accounts.contains(account)) {
                        this.accounts.add(account);
                    }
                }
                break;
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading CSV file: " + e.getMessage());
    }
}

// Load credit card accounts from a CSV file
public void loadCreditCards(String filename, String customerID) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 12 && parts[1].equals(customerID)) {
                String accountID = parts[0];
                String customerName = parts[2];
                long creditCardNumber = Long.parseLong(parts[3]);
                String companyIssuer = parts[4];
                int cvv = Integer.parseInt(parts[5]);
                int cardPin = Integer.parseInt(parts[6]);
                LocalDate expiryDate = LocalDate.parse(parts[7]);
                BigDecimal creditLimit = new BigDecimal(parts[8]);
                String cardType = parts[9];
                BigDecimal dailySpendLimit = new BigDecimal(parts[10]);
                BigDecimal currentSpentAmount = new BigDecimal(parts[11]);
                double balance = Double.parseDouble(parts[12]);

                // Create new CreditCard object
                g17_CRD creditCard = new g17_CRD(customerName, creditCardNumber, companyIssuer, cvv, cardPin, expiryDate, creditLimit);
                creditCard.setDailyLimit(dailySpendLimit);
                creditCard.setOutstandingPayment(currentSpentAmount);

                CreditCardAccount account = new CreditCardAccount(accountID, cardType, balance, creditCard);
                if (!this.creditCardAccounts.contains(account)) {
                    this.creditCardAccounts.add(account);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading CSV file: " + e.getMessage());
    }
}

// Prompt the user to choose an existing account
    public Account promptAccount(Scanner scanner) {
        if (this.accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return null;
        }

        System.out.println("Please choose an account:");
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf("%d. Account ID: %s, Type: %s, Balance: %.2f\n", i + 1, this.accounts.get(i).getAccountID(), this.accounts.get(i).getAccountType(), this.accounts.get(i).getBalance());
        }

        int choice;
        do {
            System.out.print("Your choice: ");
            choice = scanner.nextInt();
            if (choice < 1 || choice > this.accounts.size()) {
                System.out.println("Invalid choice. Please choose a valid option.");
            }
        } while (choice < 1 || choice > this.accounts.size());

    return this.accounts.get(choice - 1);
}

// Prompt the user to choose a credit card account
public CreditCardAccount promptCreditCardAccount(Scanner scanner) {
    if (this.creditCardAccounts.isEmpty()) {
        System.out.println("No credit card to show.");
        return null;
    }

    System.out.println("Please choose a credit card account to view:");
    for (int i = 0; i < this.creditCardAccounts.size(); i++) {
        System.out.printf("%d. Account ID: %s, Credit Card Number: %d, Issuer: %s\n", i + 1, this.creditCardAccounts.get(i).getAccountID(), this.creditCardAccounts.get(i).getCreditCard().getcreditCardNumber(), this.creditCardAccounts.get(i).getCreditCard().getCompanyIssuer());
    }

    int choice;
    do {
        System.out.print("Your choice: ");
        choice = scanner.nextInt();
        if (choice < 1 || choice > this.creditCardAccounts.size()) {
            System.out.println("Invalid choice. Please choose a valid option.");
        }
    } while (choice < 1 || choice > this.creditCardAccounts.size());

    int pin;
    boolean validPin;
    do {
        System.out.print("Please enter your card PIN (or -1 to cancel): ");
        pin = scanner.nextInt();
    
        if (pin == -1) {
            return null;
        }
    
        validPin = this.creditCardAccounts.get(choice - 1).getCreditCard().isCardPinValid(pin);
        if (!validPin) {
            System.out.println("Invalid PIN. Please try again.");
        }
    } while (!validPin);

    return this.creditCardAccounts.get(choice - 1);
}

// Log in a customer using their username and password
public static Customer loadCustomerByUsernameAndPassword(String username, String password, String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 17 && parts[1].equals(username)) {
                String encryptedPassword = parts[21];
                String secretKey = parts[22];
                String salt = parts[23];

                String decryptedPassword = AES.decrypt(encryptedPassword, secretKey, salt);
                g17_SYE newOTP = new g17_SYE(username);
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter OTP:");
                int otp = scanner.nextInt();
                scanner.nextLine();

                if (password.equals(decryptedPassword)) {
                    g17_SYE.OTPStatus status = newOTP.OTPValidation(otp);
                    if (status == g17_SYE.OTPStatus.VERIFIED) {
                        String customerId = parts[0];
                        String name = parts[3];
                        String nric = parts[4];
                        LocalDate dob = LocalDate.parse(parts[5]);
                        int contactNumber = Integer.parseInt(parts[6]);
                        String email = parts[7];
                        String address = parts[8];

                        return new Customer(customerId, name, nric, dob, contactNumber, email,  address);
                    } else if (status == g17_SYE.OTPStatus.EXPIRED) {
                        System.out.println("OTP has expired. A new OTP will be sent.");
                        newOTP = new g17_SYE(username);
                    } else {
                        System.out.println("Invalid OTP. Please try again.");
                    }
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading from CSV file: " + e.getMessage());
    }

    return null;
}
}
