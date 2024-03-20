import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Account {
   private int transactionID;
   private String accountType;
   private String accountID;
   private double balance;
   private double transferLimit;
   private Customer customer;
   private double withdrawlLimit;

   // public Account(String accountID, String accountType, Customer customer){
   //    // this.transactionID = TID;
   //    this.transferLimit = 3000;
   //    this.accountType = accountType;
   //    this.withdrawlLimit = 3000;
   //    this.balance = 0;
   //    this.accountID = accountID;
   //    this.customer = customer;

   // }

   // static list to hold all accounts
   private static List<Account> allAccounts = new ArrayList<>();
   
   public Account(String accountID, String accountType, double balance){
      // this.transactionID = TID;
      this.transferLimit = 3000;
      this.accountType = accountType;
      this.withdrawlLimit = 3000;
      this.balance = balance;
      this.accountID = accountID;
      allAccounts.add(this);
   }

   public static String generateRandomDefaultAccountID() {
      String id;
      do {
         Random rand = new Random();
         int fiveDigitNumber = 10000 + rand.nextInt(90000);
         id = "A" + Integer.toString(fiveDigitNumber);
      } while (Bank.idExistsInCsv(id));
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
      } while (Bank.idExistsInCsv(newAccountID)); // Keep generating a new account ID until it doesn't exist in the CSV file
  
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
   
   public void deposit(double amount){
      balance += amount;
      System.out.println("Deposited $" + amount + " into account " + accountID);

   }

   public static void depositToAccount(Scanner scanner, Customer customer) {
      List<Account> customerAccounts = customer.getAccounts();
      if (customerAccounts.isEmpty()) {
          System.out.println("No accounts available.");
          return;
      }
  
      System.out.println("Select an account to deposit to:");
      for (int i = 0; i < customerAccounts.size(); i++) {
          System.out.println((i + 1) + ". Account ID: " + customerAccounts.get(i).getAccountID());
      }
  
      int accountIndex;
      while (true) {
          System.out.print("Enter the number of the account: ");
          accountIndex = scanner.nextInt() - 1;
          if (accountIndex >= 0 && accountIndex < customerAccounts.size()) {
              break;
          } else {
              System.out.println("Invalid choice. Please choose a valid option.");
          }
      }
  
      System.out.print("Enter deposit amount: ");
      double depositAmount = scanner.nextDouble();
      scanner.nextLine(); // Consume the newline character
  
      Account depositAccount = customerAccounts.get(accountIndex);
      depositAccount.deposit(depositAmount);
      depositAccount.updateAccountInCsv();

      
  }


   public void withdraw(double amount){
      if (amount > withdrawlLimit) {
         System.out.println("You have hit your withdrawal limit. ");
         return;
      }
      if (balance >= amount) {
         balance -= amount;
         System.out.println("Withdrawn $" + amount + " from account " + accountID);
      }
      else {
         System.out.println("Insufficient funds in account " + accountID);
      }

   }

   public static void withdrawFromAccount(Scanner scanner, Customer customer) {
      List<Account> customerAccounts = customer.getAccounts();
      if (customerAccounts.isEmpty()) {
          System.out.println("No accounts available.");
          return;
      }
  
      System.out.println("Select an account to withdraw from:");
      for (int i = 0; i < customerAccounts.size(); i++) {
          System.out.println((i + 1) + ". Account ID: " + customerAccounts.get(i).getAccountID());
      }
  
      int accountIndex;
      while (true) {
          System.out.print("Enter the number of the account: ");
          accountIndex = scanner.nextInt() - 1;
          if (accountIndex >= 0 && accountIndex < customerAccounts.size()) {
              break;
          } else {
              System.out.println("Invalid choice. Please choose a valid option.");
          }
      }
  
      System.out.print("Enter withdrawal amount: ");
      double withdrawalAmount = scanner.nextDouble();
      scanner.nextLine(); // Consume the newline character
  
      Account withdrawAccount = customerAccounts.get(accountIndex);
      withdrawAccount.withdraw(withdrawalAmount);
      withdrawAccount.updateAccountInCsv();
  }

   public void transfer(Account recipient, double amount) {
      if (amount > transferLimit) {
         System.out.println("You have hit your Transfer limit. ");
         
      }
      if (balance >= amount) {

         balance -= amount;
         recipient.deposit(amount);
         System.out.println("Transaction successful. $" + amount + " transferred to account " + recipient.accountID + ".");
      } else {
         System.out.println("Transaction failed. Insufficient funds or invalid amount.");
      }
   }
   
   //update balance in csv
   public void updateAccountInCsv() {
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
            while (data.size() <= 14) {
                  data.add("");
            }

            // Update the specific columns with the new account ID and balance
            int columnIndex = accountType.equals("Savings") ? 11 : 13;
            data.set(columnIndex, accountID);
            data.set(columnIndex + 1, String.valueOf(balance));
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

   //display
   public void displayAccountInfo() {
      System.out.println("~~~~~~~~~~~~~ This is your Account Info ~~~~~~~~~~~~~");
      System.out.printf("%-20s %s%n", "Account ID:", accountID);
      System.out.printf("%-20s %s%n", "Account Type:", accountType);
      System.out.printf("%-20s $%.2f%n", "Balance:", balance);
      System.out.printf("%-20s $%.2f%n", "Transfer Limit:", transferLimit);
      System.out.printf("%-20s $%.2f%n", "Withdrawal Limit:", withdrawlLimit);
      System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
  }

   public void displayTransactionInfo(){

   }

   public static Account getAccountByID(String accountID) {
      for (Account account : allAccounts) {
         if (account.getAccountID().equals(accountID)) {
            return account;
         }
      }
      return null; // Account not found
   }

   // getter setter
   public double getbalance(){
      return balance;
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

   public void setWithdrawLimit(double amount){
      this.withdrawlLimit= amount;
   }
   public double getWithdrawLimit()
   {
      return this.withdrawlLimit;
   }

   public void setAccountType(String type){
      this.accountType = type;
   }
}


class Transaction {
   private String transactionId;
   private String transactionType;
   private double amount;
   private Date date;
   private String accountID;

   public Transaction(String transactionId, String transactionType, double amount, Date date, String accountID) {
      this.transactionId = transactionId;
      this.transactionType = transactionType;
      this.amount = amount;
      this.date = date;
      this.accountID = accountID;
   }

   public String getDetails() {
      return "Transaction ID: " + transactionId +
            ", Type: " + transactionType +
            ", Amount: " + amount +
            ", Date: " + date +
            ", Account ID: " + accountID;
   }


}