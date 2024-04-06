import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import com.G17.Bank.Entity.CC.*;




public class CreditCardAccount extends Account {

    private static final String CREDIT_CARD_ACCOUNT_FILE = "CreditCard.csv";

    private g17_CRD creditCard;

    private static final BigDecimal CREDIT_LIMIT_STANDARD = new BigDecimal("10000.0");
    private static final BigDecimal CREDIT_LIMIT_PREMIUM = new BigDecimal("50000.0");

    CreditCardAccount(String accountID, String accountType ,double balance, g17_CRD creditCard) {
        super(accountID, accountType, balance);
        this.creditCard = creditCard;
    }

    // Get credit card object
    public g17_CRD getCreditCard() {
        return this.creditCard;
    }

    // Generate random account ID
    public static String generateRandomDefaultAccountID() {
        String id;
        do {
            Random rand = new Random();
            int fiveDigitNumber = 10000 + rand.nextInt(90000);
            id = "CC" + Integer.toString(fiveDigitNumber);
        } while (Bank.idExistsInCsv(id, CREDIT_CARD_ACCOUNT_FILE));
        return id;
    }

    // Generate random credit card number
    public static Long generateRandomCreditCardNumber() {
        long lowerBound = 1000000000000000L;
        long upperBound = 9000000000000000L;
        long randomCreditCardNumber = lowerBound + ((long) (Math.random() * (upperBound - lowerBound)));
        return randomCreditCardNumber;
    }

    // Method to create an account with credit card
    public static void createCreditCardAccount(Scanner scanner, Customer customer) {

        System.out.println("Select the type of credit card you want to apply for:");
        System.out.println("1. Standard Credit Card: Credit limit of 10,000.00 ");
        System.out.println("2. Premium Credit Card: Credit limit of 50,000.00 ");

        int accountTypeIndex;
        while (true) {
            System.out.print("Enter the number of the account type: ");
            accountTypeIndex = scanner.nextInt();
            scanner.nextLine();
            if (accountTypeIndex == 1 || accountTypeIndex == 2) {
                break;
            } else {
                System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
        String accountType = accountTypeIndex == 1 ? "Standard Credit Card" : "Premium Credit Card";



        if (customer.hasAccountType(accountType)) {
            System.out.println("You already have a " + accountType + " account.");
        } else {
            
            long creditCardNumber = generateRandomCreditCardNumber();
            String companyIssuer = "VISA";
            int cvv = 100 + (int) (Math.random() * 900);
            int cardPin = 100000 + (int) (Math.random() * 900000);
            LocalDate expiryDate = LocalDate.now().plusYears(5);
            BigDecimal creditLimit = accountTypeIndex == 1 ? CREDIT_LIMIT_STANDARD : CREDIT_LIMIT_PREMIUM;

            g17_CRD creditCard = new g17_CRD(customer.getName(), creditCardNumber, companyIssuer, cvv, cardPin, expiryDate, creditLimit);

            String accountID = generateRandomDefaultAccountID();

            CreditCardAccount creditCardAccount = new CreditCardAccount(accountID, accountType, 0.0, creditCard);

            try (FileWriter writer = new FileWriter(CREDIT_CARD_ACCOUNT_FILE, true)) {
                BufferedWriter buffer = new BufferedWriter(writer);
                PrintWriter printWriter = new PrintWriter(buffer);
                System.out.println(creditCardAccount.toCSV(customer));
                printWriter.println(creditCardAccount.toCSV(customer));
                printWriter.flush();
            } catch (IOException e) {
                ExceptionHandling.handleIOException(e);
                System.out.println("Failed to create account.");
            }
            
            customer.addAccount(creditCardAccount);

            System.out.println("Credit card application successful. Your account ID is: " + accountID);
            System.out.println("Your card pin is: " + cardPin);
            System.out.println("You can view more information in Credit Card.");
        }
    }

    // Used when creating an account to store it in the CSV file
    public String toCSV(Customer customer) {
        return this.getAccountID() + "," + customer.getCustomerID() + "," + customer.getName() + "," + creditCard.getcreditCardNumber() + "," + creditCard.getCompanyIssuer() + "," + creditCard.getCVV() + "," + creditCard.getCardPin() + "," + creditCard.getExpiryDate() + "," + creditCard.getCreditLimit() + "," + this.getAccountType() + "," + creditCard.getDailyLimit() + "," + this.getBalance();
    }

    // Method to let user pay outstanding amount
    public void payOutstandingCreditWithBalance(Scanner scanner) {
        System.out.println("Your outstanding amount is: " + this.getCreditCard().getOutstandingPayment());
        System.out.println("Balance available: " + this.getBalance());
        do {
            System.out.print("Enter the amount you want to pay: ");
            BigDecimal amount = scanner.nextBigDecimal();
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Invalid amount. Please enter a positive amount.");
            } else if (amount.compareTo(this.getCreditCard().getOutstandingPayment()) > 0 || amount.compareTo(BigDecimal.valueOf(this.getBalance())) > 0) {
                System.out.println("Unable to process payment, ensure amount does not exceed outstanding owed amount or balance.");
            } else {
                BigDecimal balance = BigDecimal.valueOf(this.getBalance());
                BigDecimal newBalance = balance.subtract(amount);
                this.setBalance(newBalance.doubleValue());
                this.getCreditCard().setOutstandingPayment(this.getCreditCard().getOutstandingPayment().subtract(amount));
                // Items to be updated in csv file
                updateAccountInCsv(this, newBalance, amount);
                System.out.println("Payment successful. Your balance is now: " + this.getBalance());
                System.out.println("Outstanding amount is now: " + this.getCreditCard().getOutstandingPayment());
                
                break;
            }
        } while (true);
    }

    // Overloaded method to update csv for account that is paying outstanding amount
    public void updateAccountInCsv(CreditCardAccount ccAcount, BigDecimal newBalance, BigDecimal amount) {
        
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader buffer = new BufferedReader(new FileReader(CREDIT_CARD_ACCOUNT_FILE))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(","));
                if (values.get(0).equals(ccAcount.getAccountID())) {
                    BigDecimal newOutstandingPayment = ccAcount.getCreditCard().getOutstandingPayment().subtract(amount);
                    values.set(11, newOutstandingPayment.toString());
                    values.set(12, Double.toString(newBalance.doubleValue()));
                    records.add(values);
                } else {
                    records.add(values);
                }
            }
        } catch (IOException e) {
            ExceptionHandling.handleIOException(e);
            System.out.println("Failed to update account.");
        }

        try (FileWriter writer = new FileWriter(CREDIT_CARD_ACCOUNT_FILE)) {
            BufferedWriter buffer = new BufferedWriter(writer);
            PrintWriter printWriter = new PrintWriter(buffer);
            for (List<String> record : records) {
                printWriter.println(String.join(",", record));
            }
            printWriter.flush();
        } catch (IOException e) {
            ExceptionHandling.handleIOException(e);
            System.out.println("Failed to update account.");
        }

    }

    // Display account information
    public void displayAccountInfo() {
        System.out.println("~~~~~~~~~~~~~ This is your Credit Card Info ~~~~~~~~~~~~~");
        System.out.println("Credit Card Number: " + this.creditCard.getcreditCardNumber());
        System.out.println("Company Issuer: " + this.creditCard.getCompanyIssuer());
        System.out.println("CVV: " + this.creditCard.getCVV());
        System.out.println("Expiry Date: " + this.creditCard.getExpiryDate());
        System.out.println("Credit Limit: " + this.creditCard.getCreditLimit());
        System.out.println("Daily Limit: " + this.creditCard.getDailyLimit());
        System.out.println("Current Balance: " + this.getBalance());
        System.out.println("Outstanding Payment: " + this.creditCard.getOutstandingPayment());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
    }

    // Sub-menu for credit card account
    public void creditCardAccountMenu(Scanner scanner){        
        do {
            System.out.println("~~~~~~~~~~~~~ Credit Card Account Menu ~~~~~~~~~~~~~");
            System.out.println("1. Show Account Info");
            System.out.println("2. Pay Outstanding Amount");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    displayAccountInfo();
                    break;
                case 2:
                    payOutstandingCreditWithBalance(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (true);
    }

    

}
