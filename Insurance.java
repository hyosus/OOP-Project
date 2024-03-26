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
     * Currently supported types are Life insurance, Medical insurance, Travel insurance, Car Insurance and Fire Insurance.
     */
    private static final String[] INSURANCE_TYPE_OPTIONS = {"Life", "Medical", "Travel","Car","Fire"};
    /** The unique identifier for the insurance policy. */
    private String insuranceID;
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
     * @param type The type of insurance policy.
     * @param id The unique identifier for the insurance policy.
     * @param premium The premium (cost) of the insurance. 
     * @param coverage The coverage amount of the insurance.
     */
    public Insurance(String type,String id,double premium, double coverage) {
        this.insuranceType = type;
        this.insuranceID = id;
        this.coverageAmount = coverage;
        this.insurancePremium = premium;
        this.beneficiaryNames = new ArrayList<>();
    }
    //Empty constructor for Insurance
    /**
     * Constructs a empty insurance object.
     */
    public Insurance()
    {
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
    public String chooseInsuranceType()
    {
        int choice = -1;
        do{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose your insurance type.");
            for (int i = 1; i<=INSURANCE_TYPE_OPTIONS.length; i++)
            {
                System.out.println("Option" + i + ":" +INSURANCE_TYPE_OPTIONS[i-1]);
            }
            try{
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Invalid input: Please enter a number.");
            }
            switch (choice) {
                case 1:
                    this.insuranceType = INSURANCE_TYPE_OPTIONS[0];
                    break;
                case 2:
                    this.insuranceType = INSURANCE_TYPE_OPTIONS[1];
                    break;
                case 3:
                    this.insuranceType = INSURANCE_TYPE_OPTIONS[2];
                    break;
                case 4:
                    this.insuranceType = INSURANCE_TYPE_OPTIONS[3];
                    break;
                case 5:
                    this.insuranceType = INSURANCE_TYPE_OPTIONS[4];
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, 3, 4 or 5.");
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
    public String generateInsuranceID(String insuranceType) {
        String prefix;
        switch (insuranceType) {
            case "Medical":
                prefix = "MED";
                break;
            case "Life":
                prefix = "LIF";
                break;
            case "Travel":
                prefix = "TRA";
                break;
            case "Car":
                prefix = "CAR";
                break;
            case "Fire":
                prefix = "FIR";
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
    public double calculatePremium(String insuranceType) {
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
            case "Medical":
                premium = 100.00;
                if (age <= 21) {
                    premium += 50; // Adjust for ages 0-21
                } else if (age <= 50) {
                    premium += 70; // Adjust for ages 22-50
                } else {
                    premium += 60; // Adjust for ages 51+
                }
                break;
        
            case "Life":
                premium = 130.00;
                if (age <= 21) {
                    premium += 30; 
                } else if (age <= 50) {
                    premium += 100; 
                } else {
                    premium += 70; 
                }
                break;
            case "Travel":
                premium = 40.00;
                if (age <= 21) {
                    premium += 10; // Adjust for ages 0-21
                } else if (age <= 50) {
                    premium += 30; // Adjust for ages 22-50
                } else {
                    premium += 20; // Adjust for ages 51+
                }
                break;
            case "Car":
                premium = 150.00;
                if (age <= 21) {
                    premium += 100; // Adjust for ages 0-21
                } else if (age <= 50) {
                    premium += 200; // Adjust for ages 22-50
                } else {
                    premium += 150; // Adjust for ages 51+
                }
                break;
            case "Fire":
                premium = 170.00;
                if (age <= 21) {
                    premium += 50; // Adjust for ages 0-21
                } else if (age <= 50) {
                    premium += 80; // Adjust for ages 22-50
                } else {
                    premium += 60; // Adjust for ages 51+
                }
                
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
    public double calculateCoverageAmount(String insuranceType) {
        double coverage = this.coverageAmount;
        switch(insuranceType)
        {
            case "Medical":
                coverage = 5000.00;
                break;
            case "Life":
                coverage = 7000.00;
                break;
            case "Travel":
                coverage = 2000.00;
                break;
            case "Car":
                coverage = 3000.00;
                break;
            case "Fire":
                coverage = 3500.00;
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

    /**
    * Displays an interactive menu for managing an insurance policy. The menu allows the user to
    * display insurance details, process a claim, add or remove beneficiaries, or exit the program.
    *
    * @param insurance The Insurance object for which the menu operations are to be performed.
    *                  If the insurance is {@code null}, the menu will print a message and not display the options.
    */
    public void menu(Insurance insurance)
    {
        if (insurance == null)
        {
           System.out.println("Cannot display menu. Insurance does not exist.");
        }
        while (true) {
            System.out.println("\nInsurance Management System");
            System.out.println("1. Display Insurance Details");
            System.out.println("2. Process a Claim");
            System.out.println("3. Add a Beneficiary");
            System.out.println("4. Remove a Beneficiary");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner((System.in));
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

    /**
    * Guides the user through the process of creating a new insurance policy. It prompts the user
    * to select an insurance type, input their beneficiary, and confirm the creation of the policy.
    * If the user confirms, it will create and return a new Insurance object with the specified details.
    *
    * @return A new Insurance object with details specified by the user. Returns {@code null} if the user
    *         cancels the creation process.
    */
    public Insurance createInsurance() {
        String type = this.chooseInsuranceType();
        String id = this.generateInsuranceID(type);
        double premium = this.calculatePremium(type);
        double coverage = this.calculateCoverageAmount(type);

        System.out.println("Enter a beneficiary: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        String response;
        do{
            Scanner responseScanner = new Scanner(System.in);
            System.out.println("Are you sure you want to apply for this insurance? (yes/no)");
            response = responseScanner.nextLine().trim().toLowerCase();
            if ("yes".equals(response))
            {
                Insurance newinsurance = new Insurance(type, id, premium, coverage);
                newinsurance.addBeneficiary(name); // Add the beneficiary to the new insurance object
                System.out.println("Insurance successfully applied.");         
                return newinsurance;
            }
            else if("no".equals(response))
            {
                System.out.println("Action cancelled");
                return null;
            }
            else
            {
                System.out.println("Invalid input. Please type yes or no.");
            }

        }while(true);
    }
}
