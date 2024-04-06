import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Account {
    private String transactionID;
    private String accountType;
    private String accountID;
    private double balance;
    private double transferLimit;
    private Customer customer;
    private double withdrawalLimit;
    private List<Loan> loanList;
    private List<Transaction> transactionList;
    private static final String ACCOUNTS_CSV_FILE = "accounts.csv";

    private static final String TRANSACTIONS_CSV_FILE = "transactions.csv";
    private static final String CUSTOMERS_CSV_FILE = "customers.csv";

    private static final double TRANSFER_LIMIT = 3000.0;
    private static final double WITHDRAWAL_LIMIT = 3000.0;

    private LocalDateTime lastResetTime = LocalDateTime.now();


    public Account(String accountID, String accountType, double balance){
        // this.transactionID = TID;
        this.transferLimit = TRANSFER_LIMIT;
        this.accountType = accountType;
        this.withdrawalLimit = WITHDRAWAL_LIMIT;
        this.balance = balance;
        this.accountID = accountID;
        this.loanList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
        //allAccounts.add(this);
    }


    public static String generateRandomDefaultAccountID() {
        String id;
        do {
            Random rand = new Random();
            int fiveDigitNumber = 10000 + rand.nextInt(90000);
            id = "A" + Integer.toString(fiveDigitNumber);
        } while (Bank.idExistsInCsv(id, CUSTOMERS_CSV_FILE));
        return id;
    }


    public static String generateRandomNewAccountID(String accountType) {
        String prefix = "";
        switch (accountType) {
            case "Savings":
                prefix = "S";
                break;
            case "Investment":
                prefix = "J";
                break;
        }


        Random random = new Random();
        String newAccountID;
        do {
            int fiveDigitNumber = 10000 + random.nextInt(90000); // Generate a random 6-digit number
            newAccountID = prefix + fiveDigitNumber;
        } while (Bank.idExistsInCsv(newAccountID, CUSTOMERS_CSV_FILE)); // Keep generating a new account ID until it doesn't exist in the CSV file


        return newAccountID;
    }


    public static void createNewAccount(Scanner scanner, Customer customer) {
        System.out.println("Select the type of account to create:");
        System.out.println("1. Savings");
        System.out.println("2. Investment");


        int accountTypeIndex;
        while (true) {
            System.out.print("Enter the number of the account type: ");
            accountTypeIndex = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            if (accountTypeIndex == 1 || accountTypeIndex == 2) {
                break;
            } else {
                System.out.println("Invalid choice. Please choose a valid option.");
            }
        }


        String accountType = accountTypeIndex == 1 ? "Savings" : "Investment";


        if (customer.hasAccountType(accountType)) {
            System.out.println("You already have a " + accountType + " account.");
        } else {
            String accountID = generateRandomNewAccountID(accountType); // Generate a new account ID
            Account newAccount = new Account(accountID, accountType, 0);
            customer.getAccounts().add(newAccount);
            System.out.println("Created a new " + accountType + " account with ID " + accountID);
            newAccount.updateCsvWithNewAccount(customer.getCustomerID(), accountType, accountID, 0);


        }
    }


    public void createLoan(String csvFile) {
        Random randomNo = new Random();
        int loanID = randomNo.nextInt(100000);
        Loan loanInstance = new Loan();
        Loan newLoan = loanInstance.newLoan(loanID);


        if (newLoan == null) {
            System.out.println("Loan application cancelled.");
            return;
        }


        // Add the new loan to the loan list
        this.loanList.add(newLoan);


        // Write the new loan to the CSV file
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.append(this.accountID).append(',');
            writer.append(newLoan.getloanStatus()).append(',');
            writer.append(Integer.toString(loanID)).append(',');
            writer.append(newLoan.getLoanType()).append(',');
            writer.append(Integer.toString(newLoan.getLoanTerm())).append(',');
            writer.append(Double.toString(newLoan.getPrincipalLoanAmount())).append(',');
            writer.append(Double.toString(newLoan.getInterestRate())).append(',');
            writer.append(Double.toString(newLoan.getRemainingDebt())).append(',');
            writer.append(newLoan.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append('\n');
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the CSV file.");
            e.printStackTrace();
        }
    }


    public void deposit(double amount) {
        this.balance += amount; // Update the local balance
        System.out.println("Deposited $" + amount + " into account " + this.accountID);

        // Update the customer's balance in 'customer.csv'
        updateAccountInCsv(this.accountID, this.balance);

        // Log the deposit transaction; since it's a deposit, recipientAccountID is empty
        recordTransaction("Deposit", amount, "");
    }


    public static void depositToAccount(Scanner scanner, Account depositAccount) {
        if (depositAccount == null) {
            System.out.println("No account selected.");
            return;
        }


        System.out.print("Enter deposit amount: ");
        double depositAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character


        depositAccount.deposit(depositAmount);
        depositAccount.updateAccountInCsv(depositAccount.getAccountID(), depositAccount.getBalance());
        //writeAccountsToCSV(depositAccount.getAccountID(), depositAccount.getBalance());
    }


    public void withdraw(double amount) {
        // Reset the withdrawal limit if a new day has arrived
        if (lastResetTime.toLocalDate().isBefore(LocalDate.now())) {
            lastResetTime = LocalDateTime.now();
        }

        if (amount > withdrawalLimit) {
            System.out.println("Withdrawal limit exceeded.");
            return;
        }

        if (amount <= balance) {
            balance -= amount; // Deduct the amount from the account balance
            withdrawalLimit -= amount; // Update the remaining daily withdrawal limit
            System.out.printf("Successfully withdrawn $%.2f. Remaining daily limit: $%.2f\n", amount, withdrawalLimit);

            updateAccountInCsv(accountID, balance); // Update the new balance in 'customer.csv'
            recordTransaction("Withdrawal", -amount, ""); // Log the withdrawal
        } else {
            System.out.println("Insufficient funds.");
        }
    }


    public static void withdrawFromAccount(Scanner scanner, Account withdrawAccount) {
        if (withdrawAccount == null) {
            System.out.println("No account selected.");
            return;
        }


        System.out.print("Enter withdrawal amount: ");
        double withdrawalAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character


        withdrawAccount.withdraw(withdrawalAmount);
        withdrawAccount.updateAccountInCsv(withdrawAccount.getAccountID(), withdrawAccount.getBalance());
        //writeAccountsToCSV(withdrawAccount.getAccountID(), withdrawAccount.getBalance());
    }
    // Reads the current limit and checks if a reset is needed
// Then updates the balance and limit in the CSV after withdrawal
    public void updateWithdrawalLimitAndBalance(String accountID, double amountToWithdraw) {
        String tempFile = "temp.csv";
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountID)) {
                    double currentBalance = Double.parseDouble(data[1]);
                    double lastWithdrawalLimit = Double.parseDouble(data[2]);
                    LocalDateTime lastResetTime = LocalDateTime.parse(data[3]);
                    LocalDateTime now = LocalDateTime.now();

                    // Check if we need to reset the limit
                    if (lastResetTime.toLocalDate().isBefore(LocalDate.now())) {
                        lastWithdrawalLimit = 3000.0;  // Reset to default daily limit
                    }

                    // Update balance and withdrawal limit if there's enough balance and within the limit
                    if (currentBalance >= amountToWithdraw && amountToWithdraw <= lastWithdrawalLimit) {
                        currentBalance -= amountToWithdraw;
                        lastWithdrawalLimit -= amountToWithdraw;
                        updated = true;  // Mark as updated

                        // Update data array with new values
                        data[1] = String.valueOf(currentBalance);
                        data[2] = String.valueOf(lastWithdrawalLimit);
                        data[3] = now.toString();  // Update last reset time
                    }
                }
                // Write updated line or original line back to temp file
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error processing account withdrawal: " + e.getMessage());
        }

        // Replace original CSV with updated data if any change was made
        if (updated) {
            new File(CUSTOMERS_CSV_FILE).delete();
            new File(tempFile).renameTo(new File(CUSTOMERS_CSV_FILE));
        }
    }

    public void transfer(String recipientAccountID, double amount) {
        if (amount > this.balance) {
            System.out.println("Insufficient funds for the transfer.");
            return;
        }

        Customer recipientCustomer = Bank.findCustomerByAccountID(recipientAccountID, Bank.loadAllCustomers());
        if (recipientCustomer == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        Account recipientAccount = recipientCustomer.getAccounts().stream()
                .filter(acc -> acc.getAccountID().equals(recipientAccountID))
                .findFirst()
                .orElse(null);

        if (recipientAccount == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        // Perform the transfer
        this.balance -= amount; // Deduct from sender
        recipientAccount.setBalance(recipientAccount.getBalance() + amount); // Add to the recipient
        updateAccountInCsv(recipientAccountID, recipientAccount.balance);
        System.out.println(recipientAccountID + recipientAccount.balance);
        updateAccountInCsv(this.accountID, this.balance);

        // Log the transfer for both accounts and print success message
        this.recordTransaction("Transfer Out", -amount, recipientAccountID);
        recipientAccount.recordTransaction("Transfer In", amount, this.accountID);

        System.out.println("Transfer successful: $" + amount + " from " + this.accountID + " to " + recipientAccount.getAccountID());
    }

    //overload
    public void transfer(Account recipientAccount, double amount) {
        updateAccountInCsv(this.accountID, this.balance);
        updateAccountInCsv(recipientAccount.getAccountID(), recipientAccount.getBalance());
    }

    // update balance in csv
    public void updateAccountInCsv(String accountID, double balance) {
        final String CSV_FILE = "customers.csv";
        String tempFile = "temp.csv";
        File oldFile = new File(CSV_FILE);
        File newFile = new File(tempFile);

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner scanner = new Scanner(new File(CSV_FILE));


            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                boolean isUpdated = false;
                for (int i = 0; i < data.length; i++) {
                    if (data[i].equals(accountID) && i + 1 < data.length) {
                        data[i + 1] = String.valueOf(balance);
                        isUpdated = true;
                        break;
                    }
                }
                if (isUpdated) {
                    pw.println(String.join(",", data));
                } else {
                    pw.println(line);
                }
            }
            scanner.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(CSV_FILE);
            newFile.renameTo(dump);
        } catch (Exception e) {
            System.err.println("Error updating account in CSV file: " + e.getMessage());
        }
    }
    private void recordTransaction(String transactionType, double amount, String recipientAccountID) {
        String senderAccountID = this.accountID; // This account is the sender for outgoing transactions
        String date = LocalDate.now().toString();

        // For transactions that don't involve a recipient, such as deposits or withdrawals, recipientAccountID can be null or an empty string.
        if (recipientAccountID == null || recipientAccountID.isEmpty()) {
            // If it's a withdrawal, adjust the sign of the amount for logging
            if ("Withdrawal".equals(transactionType)) {
                amount = -amount;
            }
            // For deposits and withdrawals, only the sender account is involved
            recipientAccountID = ""; // Log with an empty string for recipient account ID
        }

        // Now use the adapted logTransaction method to log the transaction details.
        logTransaction(transactionType, senderAccountID, recipientAccountID, amount, date);
    }

    public void refreshAccountData(String accountID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountID)) {
                    // Assuming the balance is in a specific position, for example
                    this.balance = Double.parseDouble(data[2]); // Adjust index based on actual CSV structure
                    // Update any other relevant fields here
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error refreshing account data: " + e.getMessage());
        }
    }

    // logTransaction method
    private void logTransaction(String transactionType, String senderAccountID, String recipientAccountID, double amount, String date) {
        File transactionsFile = new File(TRANSACTIONS_CSV_FILE);
        boolean fileExistsAndNotEmpty = transactionsFile.exists() && transactionsFile.length() > 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(transactionsFile, true))) {
            // Check if the file is newly created or empty, and then write the headers
            if (!fileExistsAndNotEmpty) {
                String header = "TransactionID,TransactionType,SenderAccountID,RecipientAccountID,Amount,DateTime\n";
                bw.write(header);
            }

            String transactionID = generateRandomTransactionID();
            String transactionData = String.format("%s,%s,%s,%s,%.2f,%s\n", transactionID, transactionType, senderAccountID, recipientAccountID, amount, date);

            bw.write(transactionData);
        } catch (IOException e) {
            System.err.println("Could not log transaction: " + e.getMessage());
        }
    }



    private String generateRandomTransactionID() {
        Random rand = new Random();
        int transactionNumber = 100000 + rand.nextInt(900000); // Generate a 6-digit random number
        String prefix = "TRX"; // Prefix for transaction IDs
        return prefix + transactionNumber;
    }

    public void updateCsvWithNewAccount(String customerID, String accountType, String accountID, double balance) {
        String tempFile = "temp.csv";
        File oldFile = new File("customers.csv");
        File newFile = new File(tempFile);

        try {
            FileWriter fw = new FileWriter(tempFile, false); // Open in overwrite mode
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner fileScanner = new Scanner(new File("customers.csv"));


            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                List<String> data = new ArrayList<>(Arrays.asList(line.split(",", -1)));


                if (data.get(0).equals(customerID)) {
                    // Ensure the data list has enough columns
                    while (data.size() <= 23) {
                        data.add("");
                    }


                    // Update the specific columns with the new account ID and balance
                    int columnIndex = accountType.equals("Savings") ? 11 : 13;
                    data.set(columnIndex, accountID);
                    data.set(columnIndex + 1, String.valueOf(balance));
                    data.set(columnIndex + 2, String.valueOf(TRANSFER_LIMIT));
                    data.set(columnIndex + 3, String.valueOf(WITHDRAWAL_LIMIT));
                    line = String.join(",", data);
                }
                pw.println(line);
            }


            fileScanner.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File("customers.csv");
            newFile.renameTo(dump);
        } catch (Exception e) {
            System.err.println("Error updating CSV file: " + e.getMessage());
        }
    }


    public void loadLoans(String filename, String accountID) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Check if the accountID matches
                if (data[0].equals(accountID)) {
                    // Create a new Loan object and add it to the list
                    int loanID = Integer.parseInt(data[2]);
                    String loanType = data[3];
                    int loanTerm = Integer.parseInt(data[4]);
                    double principalLoanAmount = Double.parseDouble(data[5]);
                    double interestRate = Double.parseDouble(data[6]);
                    Loan loan = new Loan(loanID, loanType, loanTerm, principalLoanAmount, interestRate);


                    // Set the other attributes of the Loan object
                    loan.setLoanStatus(data[1]);
                    loan.setRemainingDebt(Double.parseDouble(data[7]));
                    loan.setStartDate(LocalDate.parse(data[8], formatter));


                    // Add the loan to the loanList
                    loanList.add(loan);
                }
            }
        } catch (IOException e) {
            // Handle exception
        }
    }


    //display
    public void displayAccountInfo() {
        System.out.println("~~~~~~~~~~~~~ This is your Account Info ~~~~~~~~~~~~~");
        System.out.printf("%-20s %s%n", "Account ID:", accountID);
        System.out.printf("%-20s %s%n", "Account Type:", accountType);
        System.out.printf("%-20s $%.2f%n", "Balance:", balance);
        System.out.printf("%-20s $%.2f%n", "Transfer Limit:", transferLimit);
        System.out.printf("%-20s $%.2f%n", "Withdrawal Limit:", withdrawalLimit);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }


    public void displayLoans() {
        if (loanList.isEmpty()) {
            System.out.println("No loans available.");
            return;
        }


        System.out.println("Number of loans: " + loanList.size());


        for (Loan loan : loanList) {
            loan.getLoanInfo();
        }
    }


    // public static Account getAccountByID(String accountID) {
    //    for (Account account : allAccounts) {
    //       if (account.getAccountID().equals(accountID)) {
    //          return account;
    //       }
    //    }
    //    return null; // Account not found
    // }


    // getter setter
    public double getBalance(){
        return balance;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    public String getAccountID(){
        return this.accountID;
    }
    public String getAccountType(){
        return this.accountType;
    }

    public void setTransferLimit(double amount){
        this.transferLimit = amount;
    }
    public double getTransferLimit()
    {
        return this.transferLimit;
    }

    public static double getDefaultTransferLimit(){
        return TRANSFER_LIMIT;
    }

    public void setWithdrawLimit(double amount){
        this.withdrawalLimit= amount;
    }
    public double getWithdrawLimit()
    {
        return this.withdrawalLimit;
    }

    public static double getDefaultWithdrawLimit(){
        return WITHDRAWAL_LIMIT;
    }

    public void setAccountType(String type){
        this.accountType = type;
    }
    public void setLocalWithdrawLimit(double localWithdrawLimit) {
    }
    public void setOverseasWithdrawLimit(double overseasWithdrawLimit) {
    }
}


class Transaction {
    private String transactionId;
    private String transactionType;
    private double amount;
    private String date;
    private String accountID;
    private String recipientAccountID;


    public Transaction(String transactionId, String transactionType, double amount, String date, String accountID) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.accountID = accountID;
    }

    public Transaction(String transactionId, String transactionType, String accountID,String recipientAccountID, double amount, String date) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.accountID = accountID;
        this.recipientAccountID = recipientAccountID;
        this.amount = amount;
        this.date = date;
    }

    public String getDetails() {
        return "Transaction ID: " + transactionId +
                ", Type: " + transactionType +
                ", Amount: " + amount +
                ", Date: " + date +
                ", Account ID: " + accountID;
    }


    public String getTransactionId() {
        return String.valueOf(this.transactionId);
    }


    public String getTransactionType() {
        return String.valueOf(this.transactionType);
    }
    public double getAmount(){
        return this.amount;
    }


    public CharSequence getDate() {
        return (CharSequence) date;
    }
    public String getAccountID(){
        return String.valueOf(this.accountID);
    }
    public String getRecipientAccountID(){
        return String.valueOf(this.recipientAccountID);
    }
}
