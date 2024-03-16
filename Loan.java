import java.util.Scanner;
import java.time.LocalDate;

/**
 * Loan class for Banking System
 * <p>
 * Each loan has an ID, a principal amount, a balance, an interest rate, start date and loan term tied to the loan.
*/
public class Loan{

    public static final double MAX_LOAN_AMOUNT = 10000.0;
    public static final double MIN_LOAN_AMOUNT = 100.0;

    /**
     * The status of the loan.
     */
    private String loanStatus;

    /**
     * The ID of the loan.
     */
    private int loanID;

    /**
     * The type of loan.
     */
    private String loanType;

    /**
     * The principal amount of the loan.
     */
    private double principalLoanAmount;

    /**
     * Initially set to loan amount, but adjusted when user pays more than the monthly payment.
     * Used for default loan (i.e. non special loans like student loans, business loans, etc.)
     */
    private double monthlyMinPayment;

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
     * @param principalLoanAmount the principal amount of the new loan (amount will not change)
     * @param interestRate the interest rate of the new loan
     */
    public Loan(int loanID, String loanType, int loanTerm, double principalLoanAmount, double interestRate){
        this.loanStatus = "Active";
        this.loanID = loanID;
        this.loanType = loanType;
        this.principalLoanAmount = principalLoanAmount; 
        this.monthlyMinPayment = principalLoanAmount;
        this.remainingDebt = principalLoanAmount;
        this.interestRate = interestRate;
        this.startDate = LocalDate.now();
        this.loanTerm = loanTerm;
    }

    /**
     * Get the information of the loan including the loan ID, loan amount, interest rate, remaining loan, and monthly payment.
     */
    public void getLoanInfo(){
        System.out.println("Loan Information");
        System.out.println("--------------------");
        System.out.println("Loan Status: " + loanStatus);
        System.out.println("Loan ID: " + loanID);
        System.out.println("Loan Type: " + loanType);
        System.out.println("Loaned Amount: " + principalLoanAmount);
        System.out.println("Loan Term: " + loanTerm + " months");
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Remaining Loan: " + remainingDebt)  ;
        System.out.println("Minimum Monthly Payment: " + this.calculateMonthlyPayment()); //for default loan (monthly payment)
    }

    /**
     * Gets the status of this loan.
     * @return The status of this loan.
     */
    public String getloanStatus(){
        return this.loanStatus;
    }

    /**
     * Sets the status of this loan. (Active/Paid Off)
     * @param status
     */
    public void setLoanStatus(String status){
        this.loanStatus = status;
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
     * Sets the ID of this loan.
     * @param loanID
     */
    public void setLoanID(int loanID){
        this.loanID = loanID;
    }

    /**
     * Gets the type of this loan.
     * @return The type of this loan.
     */
    public String getLoanType(){
        return this.loanType;
    }

    /**
     * Sets the type of this loan.
     * @param loanType
     */

    public void setLoanType(String loanType){
        this.loanType = loanType;
    }

    /**
     * Gets the original principal amount of this loan.
     * @param loanID
     * @return The original principal amount of this loan.
     */
    public double getPrincipalLoanAmount(int loanID){
        return this.principalLoanAmount;
    }

    /**
     * Sets the principal amount of this loan.
     * @param amount
     */
    public void setPrincipalLoanAmount(double amount){
        this.principalLoanAmount = amount;
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
     * Calculates the interest of this loan. 
     * @return The interest of this loan.
     */
    public double calculateInterest() {
        return this.principalLoanAmount * this.interestRate / 100;
    }

    /**
     * Gets the remaining debt of this loan.
     * @return The remaining debt of this loan.
     */
    public double getRemainingDebt(){
        return this.remainingDebt;
    }

    //Add to principalLoanAmount for each month that passes
    public void setRemainingDebt(double debt){
        this.remainingDebt = debt;
    }

    /**
     * Gets the start date of this loan.
     * @return The start date of this loan.
     */
    public LocalDate getStartDate(){
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
     * User is able to pay a minimum of the monthly payment or more up to the remaining debt.
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
            this.monthlyMinPayment = payment - this.calculateMonthlyPayment(); // Adjusted when user pays more than the monthly payment
            this.remainingDebt -= payment;
            if (this.remainingDebt == 0) {
                System.out.println("Loan has been fully paid off");
                setLoanStatus("Paid Off");
            }
            return;
        }
        
    }

    /**
     * Calculates the minimum monthly payment for this loan.
     * <p>
     * The formula used is:
     * <pre>
     * (Remaining debt adjusted for payment over minimum * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -loanTerm))
     * </pre>
     * This formula is based on the annuity formula, which calculates the amount that needs to be paid each month to fully repay the loan over the specified term, assuming the interest rate remains constant.
     *
     * @return The monthly payment for this loan.
     */
    public double calculateMonthlyPayment(){

        double monthlyInterestRate = this.interestRate / 100 / 12;
        double monthlyPayment = (this.monthlyMinPayment * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -this.loanTerm));
        return monthlyPayment;
    }

    /**
     * Creates a new loan for the user.
     * <p>
     * The user will be prompted to enter the loan amount, loan type and choose the term, and the loan will be created with the current date as the start date.
     * The user will be shown the loan details and asked to confirm the loan application.
     * If the user confirms the loan application, the method will return the new loan.
     * If the user cancels the loan application, the method will return null.
     *
     * @return The new loan, or null if the user cancels the loan application.
     */
    public Loan newLoan(){
        // To be updated to have the new loan be added to csv tagged to user

        System.out.println("Application for new loan");

        Scanner newLoanScanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("Enter the amount to loan: ");
            principalLoanAmount = newLoanScanner.nextDouble();
            if (principalLoanAmount > MAX_LOAN_AMOUNT) {
                System.out.println("Loan amount exceeds maximum amount. Please try again.");
            } 
            else if (principalLoanAmount < MIN_LOAN_AMOUNT) {
                System.out.println("Loaned amount is below minimum amount. Please try again.");
            }
            else {
                break;
            }
        }

        while(true) {
            System.out.println("--------------------");
            System.out.println("Choose desired loan type: ");
            System.out.println("1. Personal Loan"); //default loan
            System.out.println("2. Student Loan");
            System.out.println("3. Business Loan");
            System.out.println("4. Housing Loan");

            int chosenType = newLoanScanner.nextInt();

            switch (chosenType) {
                case 1:
                    System.out.println("Personal Loan selected");
                    loanType = "Personal";
                    break;
                case 2:
                    System.out.println("Student Loan selected");
                    loanType = "Student";
                    break;
                case 3:
                    System.out.println("Business Loan selected");
                    loanType = "Business";
                    break;
                case 4:
                    System.out.println("Housing Loan selected");
                    loanType = "Housing";
                    break;
                default:
                    System.out.println("Invalid type selected. Please try again.");
                    continue;
            }
            break;
        }
        
        while (true) {
            System.out.println("--------------------");
            System.out.println("Choose desired loan term: ");
            System.out.println("1. 12 months, 2.5% interest rate");
            System.out.println("2. 24 months, 3.0% interest rate");
            System.out.println("3. 36 months, 3.5% interest rate");
            System.out.println("4. 48 months, 4.0% interest rate");
        
            int chosenTerm = newLoanScanner.nextInt();
        
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
                    continue;
            }
            break;
        }

        // Show user the loan details they have chosen
        System.out.println("--------------------");
        System.out.println("Please confirm the loan details: ");
        System.out.println("Loan Type: " + loanType);
        System.out.println("Loan Amount: " + principalLoanAmount);
        System.out.println("Loan Term: " + loanTerm + " months");
        System.out.println("Interest Rate: " + interestRate + "%");
        
        System.out.println("Confirm loan application? (Y/N)");
        String confirmLoan = newLoanScanner.next();

        while (!confirmLoan.equals("Y") && !confirmLoan.equals("N")) {
            System.out.println("Invalid input. Please try again");
            confirmLoan = newLoanScanner.next();
        }
        if (confirmLoan.equals("N")) {
            System.out.println("Loan application cancelled");
            newLoanScanner.close();
            return null;
        }

        newLoanScanner.close();
        // Need to add way to automatically generate loanID

        Loan newLoan = new Loan(loanID, loanType, loanTerm, principalLoanAmount, interestRate);

        System.out.println("Loan application successful");

        return newLoan;
    }

}
