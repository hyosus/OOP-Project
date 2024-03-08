
public class Account {
   private double localWithdrawLimit ;
   private double overseasWithdrawLimit;
   private String accountNumber;
   private double balance;
   private double transferLimit;
   private Customer customer;

   public Account(String accNumber, Customer customer){
      this.localWithdrawLimit = 3000;
      this.overseasWithdrawLimit = 3000;
      this.balance = 0;
      this.accountNumber = accNumber;
      this.customer = customer;

   }
   
   
   public void deposit(double amount){
      balance += amount;
      System.out.println("Deposited $" + amount + " into account " + accountNumber);

   }
   public void withdraw(double amount){
      if (amount > localWithdrawLimit) {
         System.err.println();
         
      }
      if (balance >= amount) {
         balance -= amount;
         System.out.println("Withdrawn $" + amount + " from account " + accountNumber);
      }
      else {
         System.out.println("Insufficient funds in account " + accountNumber);
      }

   }

   public void transfer(double amount){

   }

   //display
   public void displayAccountInfo(){
   }


   // getter setter
   public double getbalance(){
      return balance;
   }

   public void setTransferLimit(double amount){
      this.transferLimit = amount;

   }
   public double getTransferLimit()
   {
      return this.transferLimit;
   }

   public void setOverseasWithdrawLimit(double amount){
      this.overseasWithdrawLimit = amount;

   }
   public double getOverseasWithdrawLimit(){
      return this.overseasWithdrawLimit;
   }

   public void setLocalWithdrawLimit(double amount){
      this.localWithdrawLimit= amount;
   }
   public double getLocalWithdrawLimit()
   {
      return this.localWithdrawLimit;
   }

   
   

   




   


}
