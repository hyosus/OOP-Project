
public class Account {
   private double localWithdrawLimit ;
   private double overseasWithdrawLimit;
   private String accountNumber;
   private double balance;
   private double transferLimit;
   private Customer customer;

   public Account(){
      this.localWithdrawLimit = 3000;
      this.overseasWithdrawLimit = 3000;
      this.balance = balance;
      this.accountNumber = accountNumber;
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

   public void setTransferLimit(){
      this.transferLimit = transferLimit;

   }
   public void setOverseasWithdrawLimit(){
      this.overseasWithdrawLimit = overseasWithdrawLimit;

   }
   
   

   




   


}
