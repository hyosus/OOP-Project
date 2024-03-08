import java.util.Date;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Loan class for Banking System
 * <p>
 * Each loan has an ID, a principal amount, a balance, an interest rate, a term, start date and a account number tied to the loan.
 */
public class Loan{

    public static final double MAX_LOAN_AMOUNT = 10000.0;

    /**
     * The ID of the loan.
     */
    private int loanID;

    /**
     * The principal amount of the loan.
     */
    private double loanAmount;

    /**
     * Initially set to loan amount, but adjusted when user pays more than the monthly payment.
     */
    private double loanPaymentAdjusted;

    /**
     * The remaining balance of the loan. Initially set to the loan amount.
     */
    private double remainingDebt;

    /**
     * The annual interest rate of the loan, represented as a decimal (e.g., 0.05 for 5%).
     */
    private double interestRate;

    /**
     * The term of the loan in months.
     */
    private int loanTerm;

    /**
     * The date the loan was started.
     */
    private LocalDate startDate;

    /**
     * Constructs a new Loan with the specified account number, ID, amount, term and interest rate.
     * <p>
     * The start date is set to the current date and time.
     * The remaining debt and adjusted loan amount are both set to the initial loan amount.
     *
     * @param account the account tied to the new loan
     * @param loanID the ID of the new loan
     * @param loanTerm the term of the new loan in months (e.g., 12 for 1 year)
     * @param loanAmount the principal amount of the new loan
     * @param interestRate the interest rate of the new loan
     */
    public Loan(int loanID, int loanTerm, double loanAmount, double interestRate){
        this.loanID = loanID;
        this.loanAmount = loanAmount; 
        this.loanPaymentAdjusted = loanAmount;
        this.remainingDebt = loanAmount;
        this.interestRate = interestRate;
        this.startDate = LocalDate.now();
        this.loanTerm = loanTerm;
    }

    /**
     * Get the information of the loan including the loan ID, loan amount, interest rate, remaining loan, and monthly payment.
     */
    public void getLoanInfo(){
        System.out.println("Loan Information");
        System.out.println("Loan ID: " + loanID);
        System.out.println("Loaned Amount: " + loanAmount);
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Remaining Loan: " + remainingDebt)  ;
        System.out.println("Minimum Monthly Payment: " + this.calculateMonthlyPayment());
    }

    /**
    * Gets the ID of this loan.
    *
    * @return The ID of this loan.
    */
    public int getLoanID(){
        return this.loanID;
    }

    /**
     * Gets the original principal amount of this loan.
     * @param loanID
     * @return The original principal amount of this loan.
     */
    public double getLoanAmount(int loanID){
        return this.loanAmount;
    }

    /**
     * Sets the principal amount of this loan.
     * @param amount
     */
    public void setLoanAmount(double amount){
        this.loanAmount = amount;
    }

    /**
     * Gets the interest rate of this loan.
     * @return The interest rate of this loan.
     */
    public double getInterestRate(){
        return this.interestRate;
    }

    /**
     * Sets the interest rate of this loan. (e.g. 5 = 5% interest rate)
     * @param rate
     */
    public void setInterestRate(double rate){
        this.interestRate = rate;
    }

    /**
     * Gets the remaining debt of this loan.
     * @return The remaining debt of this loan.
     */
    public double getRemainingDebt(){
        return this.remainingDebt;
    }

    /**
     * Gets the start date of this loan.
     * @return The start date of this loan.
     */
    public Date getStartDate(){
        return this.startDate;
    }

    public void setStartDate(LocalDate date){
        this.startDate = date;
    }

    /**
     * Gets the term of this loan. (in months)
     * @return
     */
    public int getLoanTerm(){
        return this.loanTerm;
    }

    /**
     * Sets the term of this loan. (in months)
     * @param term
     */
    public void setLoanTerm(int term){
        this.loanTerm = term;
    }

    /**
     * Makes a payment on this loan.
     * <p>
     * If the payment is less than the monthly payment, the method will print an error message and return.
     * If the payment is greater than the remaining debt, the method will print an error message and return.
     * Otherwise, the method will subtract the payment from the remaining debt and return.
     *
     * @param payment The amount to be paid on this loan.
     */
    public void loanPayment(double payment){
        if (payment < this.calculateMonthlyPayment()){
            System.out.println("Payment is less than the monthly payment required. Please try again.");
            System.out.println("Minimum Payment: " + this.calculateMonthlyPayment());
            return;
        }
        else if (payment > this.remainingDebt){
            System.out.println("Payment exceeds remaining loan amount, Please try again.");
            System.out.println("Remaining Debt: " + this.remainingDebt);
            return;
        }
        else{
            System.out.println("Payment Successful");
            this.loanPaymentAdjusted = payment - this.calculateMonthlyPayment(); // Adjusted when user pays more than the monthly payment
            this.remainingDebt -= payment;
            return;
        }
        
    }

    /**
     * Calculates the monthly payment for this loan.
     * <p>
     * The formula used is:
     * <pre>
     * (adjusted loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -loanTerm))
     * </pre>
     * This formula is based on the annuity formula, which calculates the amount that needs to be paid each month to fully repay the loan over the specified term, assuming the interest rate remains constant.
     *
     * @return The monthly payment for this loan.
     */
    public double calculateMonthlyPayment(){

        double monthlyInterestRate = this.interestRate / 12 / 100;
        double monthlyPayment = (this.loanPaymentAdjusted * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -this.loanTerm));
        return monthlyPayment;
    }

    public Loan newLoan(){
        // To be updated to have the new loan be added to csv tagged to user

        System.out.println("Application for new loan");

        Scanner newLoanScanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("Enter the amount to loan: ");
            loanAmount = newLoanScanner.nextDouble();
            if (loanAmount > MAX_LOAN_AMOUNT) {
                System.out.println("Loan amount exceeds maximum amount. Please try again.");
            } 
            else if (loanAmount < 0) {
                System.out.println("Invalid loan amount. Please try again.");
            }
            else {
                break;
            }
        }
        
        int chosenTerm = 0;
        do {
            System.out.println("Choose desired loan term: ");
            System.out.println("1. 12 months, 2.5% interest rate");
            System.out.println("2. 24 months, 3.0% interest rate");
            System.out.println("3. 36 months, 3.5% interest rate");
            System.out.println("4. 48 months, 4.0% interest rate");

            chosenTerm = newLoanScanner.nextInt();

            switch (chosenTerm) {
                case 1:
                    System.out.println("1 year term selected");
                    loanTerm = 12;
                    interestRate = 2.5;
                    break;
                case 2:
                    System.out.println("2 year term selected");
                    loanTerm = 24;
                    interestRate = 3.0;
                    break;
                case 3:
                    System.out.println("3 year term selected");
                    loanTerm = 36;
                    interestRate = 3.5;
                    break;
                case 4:
                    System.out.println("4 year term selected");
                    loanTerm = 48;
                    interestRate = 4.0;
                    break;
                default:
                    System.out.println("Invalid term selected. Please try again.");
            }
        } while (chosenTerm != 1 && chosenTerm != 2 && chosenTerm != 3 && chosenTerm != 4);
        
        

        // Show user the loan details they have chosen
        System.out.println("Please confirm the loan details: ");
        System.out.println("Loan Amount: " + loanAmount);
        System.out.println("Loan Term: " + loanTerm + " months");
        System.out.println("Interest Rate: " + interestRate + "%");
        
        System.out.println("Confirm loan application? (Y/N)");
        String confirmLoan = newLoanScanner.next();
        if (confirmLoan.equals("N")) {
            System.out.println("Loan application cancelled");
            newLoanScanner.close();
            return null;
        }

        newLoanScanner.close();
        // Need to add way to automatically generate loanID

        Loan newLoan = new Loan(loanID, loanTerm, loanAmount, interestRate);

        System.out.println("Loan application successful");

        return newLoan;
    }

}