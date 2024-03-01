public class Insurance 
{
    private int insuranceID;
    private String insuranceType;
    private double coverageAmount;
    
    public Insurance(int insuranceID, String insuranceType)
    {
        this.insuranceID = insuranceID;
        this.insuranceType = insuranceType;
        this.coverageAmount = 5000.00; 
    }
    

    public void displayInsuranceDetails()
    {
        System.out.println("Insurance ID: " + insuranceID);
        System.out.println("Type: " + insuranceType);
        System.out.println("Coverage Amount: " +coverageAmount);
    }

    public void updateCoverageAmount(double newCoverageAmount)
    {
        this.coverageAmount = newCoverageAmount;
    }

    public boolean isCoverageSufficient(double claimAmount)
    {
        return coverageAmount >= claimAmount;
    }

    public void processClaim(double claimAmount)
    {
        if (isCoverageSufficient(claimAmount))
        {
            System.out.println("Insurance claim processed successfully");
            this.coverageAmount -=claimAmount;
        }
        else
        {
            System.out.println("Insufficient coverage amount. Insurance claim denied");
        }
    }

}