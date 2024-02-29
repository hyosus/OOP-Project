public class Account {   
   private double localWithdrawLimit ;
   private double overseasWithdrawLimit;
   private string accountnumber;
   private double balance;
   private double transferLimit;


    public Account(){
       this.localWithdrawLimit = 3000;
       this.overseasWithdrawLimit = 3000;
       this.balance = balance;
       this.accountnumber = accountnumber; 
    }
    public void displayAccountInfo(){

    }

    public void setTransferLimit(){
       this.transferLimit = transferLimit;

    }
    public void setOverseasWithdrawLimit(){
       this.overseasWithdrawLimit = overseasWithdrawLimit;

    }
    public void deposit(double amount){
        balance += amount;
        System.out.println("Deposited $" + amount + " into account " + accountNumber);

    }
    public void withdraw(double amount){
       if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn $" + amount + " from account " + accountNumber);
        } else {
            System.out.println("Insufficient funds in account " + accountNumber);
        }

    }

   public void getbalance(){
      return balance;
   }
           


}
