import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
   
   public Account(String accountID, String accountType){
      // this.transactionID = TID;
      this.transferLimit = 3000;
      this.accountType = accountType;
      this.withdrawlLimit = 3000;
      this.balance = 0;
      this.accountID = accountID;
      allAccounts.add(this);
   }
   
   public void deposit(double amount){
      balance += amount;
      System.out.println("Deposited $" + amount + " into account " + accountID);

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

   
   //display
   public void displayAccountInfo(){
      System.out.println("~~~~This is your Account Info~~~~");
      System.out.println("Account ID:" + accountID);
      System.out.println("Account Type:" + accountType);
      System.out.println("Customer:" + customer);
      System.out.println("Balance:" + balance);
      System.out.println("TransferLimit:" + transferLimit );
      System.out.println("Withdraw Limit:" + withdrawlLimit);
      System.out.println("~~~~~END~~~~~" );
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
