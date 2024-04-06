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
        this.transferLimit = TRANSFER_LIMIT;
        this.accountType = accountType;
        this.withdrawalLimit = WITHDRAWAL_LIMIT;
        this.balance = balance;
        this.accountID = accountID;
        this.loanList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
    }

    
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
    
    // Generate a random account ID
    public static String generateRandomDefaultAccountID() {
        String id;
        do {
            Random rand = new Random();
            int fiveDigitNumber = 10000 + rand.nextInt(90000);
            id = "A" + Integer.toString(fiveDigitNumber);
        } while (Bank.idExistsInCsv(id, CUSTOMERS_CSV_FILE));
        return id;
    }


    // Generate a random account ID depending on the account type
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
            int fiveDigitNumber = 10000 + random.nextInt(90000);
            newAccountID = prefix + fiveDigitNumber;
        } while (Bank.idExistsInCsv(newAccountID, CUSTOMERS_CSV_FILE));


        return newAccountID;
    }

    // Create a new account for the customer
    public static void createNewAccount(Scanner scanner, Customer customer) {
        System.out.println("Select the type of account to create:");
        System.out.println("1. Savings");
        System.out.println("2. Investment");


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


        String accountType = accountTypeIndex == 1 ? "Savings" : "Investment";


        if (customer.hasAccountType(accountType)) {
            System.out.println("You already have a " + accountType + " account.");
        } else {
            String accountID = generateRandomNewAccountID(accountType);
            Account newAccount = new Account(accountID, accountType, 0);
            customer.getAccounts().add(newAccount);
            System.out.println("Created a new " + accountType + " account with ID " + accountID);
            newAccount.updateCsvWithNewAccount(customer.getCustomerID(), accountType, accountID, 0);


        }
    }


    // Create a new loan for the account
    public void createLoan(String csvFile) {
        Random randomNo = new Random();
        int loanID = randomNo.nextInt(100000);
        Loan loanInstance = new Loan();
        Loan newLoan = loanInstance.newLoan(loanID);


        if (newLoan == null) {
            System.out.println("Loan application cancelled.");
            return;
        }

        this.loanList.add(newLoan);

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
        this.balance += amount;
        System.out.println("Deposited $" + amount + " into account " + this.accountID);

        updateAccountInCsv(this.accountID, this.balance);

        recordTransaction("Deposit", amount, "");
    }

    // Deposit money into the account
    public static void depositToAccount(Scanner scanner, Account depositAccount) {
        if (depositAccount == null) {
            System.out.println("No account selected.");
            return;
        }


        System.out.print("Enter deposit amount: ");
        double depositAmount = scanner.nextDouble();
        scanner.nextLine(); 


        depositAccount.deposit(depositAmount);
        depositAccount.updateAccountInCsv(depositAccount.getAccountID(), depositAccount.getBalance());
    }


    public void withdraw(double amount) {
        if (lastResetTime.toLocalDate().isBefore(LocalDate.now())) {
            lastResetTime = LocalDateTime.now();
        }

        if (amount > withdrawalLimit) {
            System.out.println("Withdrawal limit exceeded.");
            return;
        }

        if (amount <= balance) {
            balance -= amount;
            withdrawalLimit -= amount; 
            System.out.printf("Successfully withdrawn $%.2f. Remaining daily limit: $%.2f\n", amount, withdrawalLimit);

            updateAccountInCsv(accountID, balance);
            recordTransaction("Withdrawal", -amount, "");
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    // Withdraw money from the account
    public static void withdrawFromAccount(Scanner scanner, Account withdrawAccount) {
        if (withdrawAccount == null) {
            System.out.println("No account selected.");
            return;
        }


        System.out.print("Enter withdrawal amount: ");
        double withdrawalAmount = scanner.nextDouble();
        scanner.nextLine();


        withdrawAccount.withdraw(withdrawalAmount);
        withdrawAccount.updateAccountInCsv(withdrawAccount.getAccountID(), withdrawAccount.getBalance());
    }

    // Update the withdrawal limit and balance after a withdrawal
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

                    if (lastResetTime.toLocalDate().isBefore(LocalDate.now())) {
                        lastWithdrawalLimit = 3000.0; 
                    }

                    if (currentBalance >= amountToWithdraw && amountToWithdraw <= lastWithdrawalLimit) {
                        currentBalance -= amountToWithdraw;
                        lastWithdrawalLimit -= amountToWithdraw;
                        updated = true;

                        data[1] = String.valueOf(currentBalance);
                        data[2] = String.valueOf(lastWithdrawalLimit);
                        data[3] = now.toString();
                    }
                }

                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error processing account withdrawal: " + e.getMessage());
        }

        if (updated) {
            new File(CUSTOMERS_CSV_FILE).delete();
            new File(tempFile).renameTo(new File(CUSTOMERS_CSV_FILE));
        }
    }

    // Transfer money to another account
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

        this.balance -= amount;
        recipientAccount.setBalance(recipientAccount.getBalance() + amount);
        updateAccountInCsv(recipientAccountID, recipientAccount.balance);
        System.out.println(recipientAccountID + recipientAccount.balance);
        updateAccountInCsv(this.accountID, this.balance);

        this.recordTransaction("Transfer Out", -amount, recipientAccountID);
        recipientAccount.recordTransaction("Transfer In", amount, this.accountID);

        System.out.println("Transfer successful: $" + amount + " from " + this.accountID + " to " + recipientAccount.getAccountID());
    }

    // Overloading transfer method
    public void transfer(Account recipientAccount, double amount) {
        updateAccountInCsv(this.accountID, this.balance);
        updateAccountInCsv(recipientAccount.getAccountID(), recipientAccount.getBalance());
    }

    // Update balance in csv
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
        String senderAccountID = this.accountID;
        String date = LocalDate.now().toString();

        if (recipientAccountID == null || recipientAccountID.isEmpty()) {
            if ("Withdrawal".equals(transactionType)) {
                amount = -amount;
            }
            recipientAccountID = "";
        }
        logTransaction(transactionType, senderAccountID, recipientAccountID, amount, date);
    }

    // Log a transaction to the transactions CSV file
    private void logTransaction(String transactionType, String senderAccountID, String recipientAccountID, double amount, String date) {
        File transactionsFile = new File(TRANSACTIONS_CSV_FILE);
        boolean fileExistsAndNotEmpty = transactionsFile.exists() && transactionsFile.length() > 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(transactionsFile, true))) {
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


    // Generate a random transaction ID
    private String generateRandomTransactionID() {
        Random rand = new Random();
        int transactionNumber = 100000 + rand.nextInt(900000); 
        String prefix = "TRX";
        return prefix + transactionNumber;
    }

    // Update the CSV file with the new account ID and balance
    public void updateCsvWithNewAccount(String customerID, String accountType, String accountID, double balance) {
        String tempFile = "temp.csv";
        File oldFile = new File("customers.csv");
        File newFile = new File(tempFile);

        try {
            FileWriter fw = new FileWriter(tempFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner fileScanner = new Scanner(new File("customers.csv"));


            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                List<String> data = new ArrayList<>(Arrays.asList(line.split(",", -1)));


                if (data.get(0).equals(customerID)) {
                    while (data.size() <= 23) {
                        data.add("");
                    }

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

    // Load loans from the CSV file
    public void loadLoans(String filename, String accountID) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountID)) {
                    int loanID = Integer.parseInt(data[2]);
                    String loanType = data[3];
                    int loanTerm = Integer.parseInt(data[4]);
                    double principalLoanAmount = Double.parseDouble(data[5]);
                    double interestRate = Double.parseDouble(data[6]);
                    Loan loan = new Loan(loanID, loanType, loanTerm, principalLoanAmount, interestRate);

                    loan.setLoanStatus(data[1]);
                    loan.setRemainingDebt(Double.parseDouble(data[7]));
                    loan.setStartDate(LocalDate.parse(data[8], formatter));

                    loanList.add(loan);
                }
            }
        } catch (IOException e) {
        }
    }


    // Display the account(s) information a customer has
    public void displayAccountInfo() {
        System.out.println("~~~~~~~~~~~~~ This is your Account Info ~~~~~~~~~~~~~");
        System.out.printf("%-20s %s%n", "Account ID:", accountID);
        System.out.printf("%-20s %s%n", "Account Type:", accountType);
        System.out.printf("%-20s $%.2f%n", "Balance:", balance);
        System.out.printf("%-20s $%.2f%n", "Transfer Limit:", transferLimit);
        System.out.printf("%-20s $%.2f%n", "Withdrawal Limit:", withdrawalLimit);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    // Display number of loans
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
}
