import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The Insurance class represents an insurance policy with various attributes such as insurance ID,
 * type, beneficiary names, coverage amount, and premium.
 */
public class Insurance {

    /**
     * Defines the types of insurance available.
     */
    public enum InsuranceType 
    {
    Life_insurance,
    Medical_insurance,
    Travel_insurance
    }
    /** The unique identifier for the insurance policy. */
    private String insuranceID;
    /** The type of insurance policy. */
    private InsuranceType insuranceType;
    /** The list of beneficiary names associated with the insurance policy. */
    private ArrayList<String> beneficiaryNames;
    /** The coverage amount provided by the insurance policy. */
    private double coverageAmount;
    /** The premium amount for the insurance policy. */
    private double insurancePremium;

    /**
     * Constructs a new insurance policy with the specified ID, type, list of beneficiary names,
     * coverage amount and insurance premium of the insurance.
     * @param insuranceType The type of insurance policy.
     * @param insuranceID The unique identifier for the insurance policy.
     * @param coverageAmount The coverage amount of the insurance.
     * @param insurancePremium The premium (cost) of the insurance. 
     * @param beneficiaryNames The list of beneficiary names associated with the insurance policy
     */
    public Insurance() {
        this.insuranceType = chooseInsuranceType();
        this.insuranceID = generateInsuranceID(this.insuranceType);
        this.coverageAmount = calculateCoverageAmount(this.insuranceType); 
        this.insurancePremium = calculatePremium(this.insuranceType);
        this.beneficiaryNames = new ArrayList<>();
    }
    /**
     * Retrieves the insurance ID.
     * @return The insurance ID.
     */
    public String getInsuranceID() {
        return this.insuranceID;
    }
    /**
     * Sets the insurance ID.
     * @param id The new insurance ID.
     */
    public void setInsuranceID(String id) {
        this.insuranceID = id;
    }
    /**
     * Retrieves the insurance type.
     * @return The insurance type.
     */
    public InsuranceType getInsuranceType() {
        return this.insuranceType;
    }
    /**
     * Sets the insurance type.
     * @param type The new insurance type.
     */
    public void setInsuranceType(InsuranceType type) {
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
        System.out.println("Insurance Details");
        System.out.println("---------------------");
        System.out.println("Insurance ID: " + insuranceID);
        System.out.println("Type: " + insuranceType);
        System.out.println("Coverage Amount: " + coverageAmount);
        System.out.println("Premium Amount: " + insurancePremium);
        
        if(beneficiaryNames.isEmpty())
        {
            System.out.println("No beneficiary Members");
        }
        else
        {
            System.out.println("Beneficiary Members:");
            for (String name: beneficiaryNames)
            {
                System.out.println('-' + name);
            }
        }
    }

    /**
     * Prompts the user to choose an insurance type and sets the insuranceType based on
     * user input. Validates the input to ensure a valid choice is made.
     *
     * @return The chosen InsuranceType.
     */
    public InsuranceType chooseInsuranceType()
    {
        int choice = -1;
        do{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose Insurance Type: 1 for Life, 2 for Medical, 3 for Travel: ");
            try{
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Invalid input: Please enter a number.");
            }
            switch (choice) {
                case 1:
                    this.insuranceType = InsuranceType.Life_insurance;
                    break;
                case 2:
                    this.insuranceType = InsuranceType.Medical_insurance;
                    break;
                case 3:
                    this.insuranceType = InsuranceType.Travel_insurance;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2 or 3.");
                    choice = -1; 
                    
            }
        } while(choice == -1);
        return this.insuranceType;
    }

    /**
     * Generates an insurance ID based on the insurance type. The ID consists of a prefix
     * indicative of the insurance type followed by a 7-digit number.
     *
     * @param insuranceType The type of insurance for which the ID is generated.
     * @return The generated insurance ID as a String.
     */
    public String generateInsuranceID(InsuranceType insuranceType) {
        String prefix;
        switch (insuranceType) {
            case Medical_insurance:
                prefix = "MED";
                break;
            case Life_insurance:
                prefix = "LIF";
                break;
            case Travel_insurance:
                prefix = "TRA";
                break;
            default:
                throw new IllegalStateException("Unknown insurance type: " + insuranceType);
        }

        // Generate a 7-digit number
        Random random = new Random();
        int number = random.nextInt(9000000) + 1000000; // This ensures the number is always 7 digits

        // Construct the insuranceID by combining the prefix and the number
        return (prefix + number);
    }

    /**
     * Calculates the insurance premium based on the insurance type and user-provided age.
     * Different premiums are set for each insurance type, with adjustments made based on
     * age categories.
     *
     * @param insuranceType The type of insurance for which the premium is calculated.
     * @return The calculated premium amount.
     */
    public double calculatePremium(InsuranceType insuranceType) {
        double premium = this.insurancePremium;
        Scanner scanner = new Scanner(System.in);

        int age;
        while (true) {
            System.out.println("Please enter your age:");
            String input = scanner.nextLine();

            try {
                age = Integer.parseInt(input);
                if(age<0)
                {
                    System.out.println("Age cannot be negative. Please enter a valid age.");
                }
                else
                {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid age.");
            }
        }

        switch (insuranceType) {
            case Medical_insurance:
                premium = 100.00;
                if (age <= 21) {
                    premium += 50; // Adjust for ages 0-21
                } else if (age <= 50) {
                    premium += 70; // Adjust for ages 22-50
                } else {
                    premium += 60; // Adjust for ages 51+
                }
                break;
        
            case Life_insurance:
                premium = 130.00;
                if (age <= 21) {
                    premium += 30; 
                } else if (age <= 50) {
                    premium += 100; 
                } else {
                    premium += 70; 
                }
                break;
            case Travel_insurance:
                premium = 40.00;
                if (age <= 21) {
                    premium += 10; // Adjust for ages 0-21
                } else if (age <= 50) {
                    premium += 30; // Adjust for ages 22-50
                } else {
                    premium += 20; // Adjust for ages 51+
                }
                break;
        }

        return premium;        
    }

    /**
     * Calculates the coverage amount based on the insurance type. Sets a base coverage
     * amount for each type of insurance.
     *
     * @param insuranceType The type of insurance for which the coverage amount is calculated.
     * @return The calculated coverage amount.
     */
    public double calculateCoverageAmount(InsuranceType insuranceType) {
        double coverage = this.coverageAmount;
        switch(insuranceType)
        {
            case Medical_insurance:
                coverage = 5000.00;
                break;
            case Life_insurance:
                coverage = 7000.00;
                break;
            case Travel_insurance:
                coverage = 2000.00;
                break;
        }
        return coverage;
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
    public void processClaim(double claimAmount) 
    {
        System.out.println("Your current coverage amount: " +this.coverageAmount);
        if (coverageAmount == 0)
        {
            System.out.println("You no remaining coverage amount. Insurance claim denied.");
            return;
        }

        else{
            String response;
            do {
                System.out.println("Are you sure you want to process your insurance claim? (yes/no)");
                Scanner scanner = new Scanner(System.in);    
                response = scanner.nextLine().trim().toLowerCase();

                if("yes".equals(response))
                {
                    if (isCoverageSufficient(claimAmount)) 
                    {
                        System.out.println("Insurance claim processed successfully.");
                        System.out.println("You have claimed: " + claimAmount);
                        this.coverageAmount -= claimAmount;
                        System.out.println("Your remaining coverage amount: " + this.coverageAmount);
                        //amount add to customer account. 
                        break;
                    } 
                    else
                    {
                        do{
                            System.out.println("Insufficient coverage amount. Your available coverage is: " + this.coverageAmount + ".");
                            System.out.println("Do you want to claim the remaining coverage amount? (yes/no)");
                            response = scanner.nextLine().trim().toLowerCase();
                            if("yes".equals(response))
                            {
                                System.out.println("Insurance claim processed successfully.");
                                System.out.println("You have claimed: " + this.coverageAmount);
                                //amount add to customer account
                                this.coverageAmount = 0;
                                System.out.println("You have no remaining coverage amount.");
                                
                                break;
                            }
                            else if ("no".equals(response))
                            {
                                System.out.println("Action cancelled");
                                break;  
                            }
                            else
                            {
                                System.out.println("Invalid Input. Please type 'yes' or 'no'.");
                            }
                        } while(true); 
                        break;
                    } 
                    
                }
                else if("no".equals(response))
                {
                    System.out.println("Action cancelled");
                    break;
                }
                else
                {
                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                }
            }   while (true);
        }
    }

    /**
     * Adds a beneficiary to the insurance policy.
     * @param name The name of the beneficiary to add.
     */
    public void addBeneficiary(String name) {
        String response;

        do {
            System.out.println("Are you sure you want to add " + name + " as a beneficiary? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            response = scanner.nextLine().trim().toLowerCase();
            
            if ("yes".equals(response)) {
                beneficiaryNames.add(name);
                System.out.println(name + " has been added as a beneficiary.");
                break;
            } else if ("no".equals(response)) {
                System.out.println("Action cancelled.");
                break;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        } while (true);
    }

    /**
     * Removes a beneficiary from the insurance policy.
     * @param name The name of the beneficiary to remove.
     */
    public void removeBeneficiary(String name) {
        String response;
        do {
            System.out.println("Are you sure you want to remove " + name + " as a beneficiary? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            response = scanner.nextLine().trim().toLowerCase();
            
            if ("yes".equals(response)) {
                if (beneficiaryNames.remove(name)) {
                    System.out.println(name + " has been removed as a beneficiary.");
                } else {
                    System.out.println("Beneficiary not found.");
                }
                break;
            } else if ("no".equals(response)) {
                System.out.println("Action cancelled.");
                break;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        } while (true);
    }

    public static void main(String[] args) {
        Insurance insurance = new Insurance();  // Create a new Insurance object
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nInsurance Management System");
            System.out.println("1. Display Insurance Details");
            System.out.println("2. Process a Claim");
            System.out.println("3. Add a Beneficiary");
            System.out.println("4. Remove a Beneficiary");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    insurance.displayInsuranceDetails();
                    break;
                case 2:
                    System.out.print("Enter claim amount: ");
                    double claimAmount = scanner.nextDouble();
                    insurance.processClaim(claimAmount);
                    break;
                case 3:
                    System.out.print("Enter beneficiary name to add: ");
                    String addName = scanner.nextLine();
                    insurance.addBeneficiary(addName);
                    break;
                case 4:
                    System.out.print("Enter beneficiary name to remove: ");
                    String removeName = scanner.nextLine();
                    insurance.removeBeneficiary(removeName);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice, please enter 1-5.");
            }
        }
    }

}
