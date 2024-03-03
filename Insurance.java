import java.util.ArrayList;
/**
 * The Insurance class represents an insurance policy with various attributes such as insurance ID,
 * type, beneficiary names, coverage amount, and premium.
 */
public class Insurance {

    /** The unique identifier for the insurance policy. */
    private int insuranceID;

    /** The type of insurance policy. */
    private String insuranceType;

    /** The list of beneficiary names associated with the insurance policy. */
    private ArrayList<String> beneficiaryNames;

    /** The coverage amount provided by the insurance policy. */
    private double coverageAmount;

    /** The premium amount for the insurance policy. */
    private double insurancePremium;

    /**
     * Constructs a new insurance policy with the specified ID, type, list of beneficiary names,
     * coverage amount and insurance premium of the insurance.
     * @param insuranceID The unique identifier for the insurance policy.
     * @param insuranceType The type of insurance policy.
     * @param beneficiaryNames The list of beneficiary names associated with the insurance policy
     * @param coverageAmount The coverage amount of the insurance.
     * @param insurancePremium The premium (cost) of the insurance. 
     */
    public Insurance(int insuranceID, String insuranceType) {
        this.insuranceID = insuranceID;
        this.insuranceType = insuranceType;
        this.beneficiaryNames = new ArrayList<>();
        this.coverageAmount = 5000.00; 
        this.insurancePremium = 250.00;
    }

    /**
     * Retrieves the insurance ID.
     * @return The insurance ID.
     */
    public int getInsuranceID() {
        return this.insuranceID;
    }

    /**
     * Sets the insurance ID.
     * @param id The new insurance ID.
     */
    public void setInsuranceID(int id) {
        this.insuranceID = id;
    }

    /**
     * Retrieves the insurance type.
     * @return The insurance type.
     */
    public String getInsuranceType() {
        return this.insuranceType;
    }

    /**
     * Sets the insurance type.
     * @param type The new insurance type.
     */
    public void setInsuranceType(String type) {
        this.insuranceType = type;
    }

    /**
     * Retrieves the list of beneficiary names.
     * @return The list of beneficiary names.
     */
    public ArrayList<String> getBeneficiaryNames() {
        return beneficiaryNames;
    }

    /**
     * Sets the list of beneficiary names.
     * @param names The new list of beneficiary names.
     */
    public void setBeneficiaryNames(ArrayList<String> names) {
        this.beneficiaryNames = names;
    }

    /**
     * Retrieves the coverage amount.
     * @return The coverage amount.
     */
    public double getCoverageAmount() {
        return this.coverageAmount;
    }

    /**
     * Sets the coverage amount.
     * @param amt The new coverage amount.
     */
    public void setCoverageAmount(double amt) {
        this.coverageAmount = amt;
    }

    /**
     * Retrieves the insurance premium.
     * @return The insurance premium.
     */
    public double getInsurancePremium() {
        return this.insurancePremium;
    }

    /**
     * Sets the insurance premium.
     * @param premium The new insurance premium.
     */
    public void setInsurancePremium(double premium) {
        this.insurancePremium = premium;
    }

    /**
     * Displays the insurance details.
     */
    public void displayInsuranceDetails() {
        System.out.println("Insurance ID: " + insuranceID);
        System.out.println("Type: " + insuranceType);
        System.out.println("Coverage Amount: " + coverageAmount);
    }

    /**
     * Calculates the insurance premium based on the policy holder's age.
     * @param age The age of the policy holder.
     * @return The calculated insurance premium.
     */
    public double calculatePremium(int age) {
        double premium = this.insurancePremium;
        if (age > 55) {
            premium *= 1.15;
        }
        return premium;
    }

    /**
     * Updates the coverage amount of the insurance policy.
     * @param newCoverageAmount The new coverage amount.
     */
    public void updateCoverageAmount(double newCoverageAmount) {
        this.coverageAmount = newCoverageAmount;
    }

    /**
     * Checks if the coverage amount is sufficient for a given claim amount.
     * @param claimAmount The amount of the insurance claim.
     * @return True if coverage is sufficient, false otherwise.
     */
    public boolean isCoverageSufficient(double claimAmount) {
        return coverageAmount >= claimAmount;
    }

    /**
     * Processes an insurance claim.
     * @param claimAmount The amount of the insurance claim.
     */
    public void processClaim(double claimAmount) {
        if (isCoverageSufficient(claimAmount)) {
            System.out.println("Insurance claim processed successfully");
            this.coverageAmount -= claimAmount;
        } else {
            System.out.println("Insufficient coverage amount. Insurance claim denied");
        }
    }

    /**
     * Adds a beneficiary to the insurance policy.
     * @param name The name of the beneficiary to add.
     */
    public void addBeneficiary(String name) {
        beneficiaryNames.add(name);
    }

    /**
     * Removes a beneficiary from the insurance policy.
     * @param name The name of the beneficiary to remove.
     */
    public void removeBeneficiary(String name) {
        beneficiaryNames.remove(name);
    }
}
