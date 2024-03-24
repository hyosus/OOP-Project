import java.time.LocalDate;
import java.util.Scanner;

public class setting {
    public static double MAX_BANK_TRANSFER_LIMIT = 8000.00;
    //Customer settings
    public void updateName(Customer customer, String name)
    {
        String oldName = customer.getName();
        customer.setName(name);
        System.out.println("Name has been changed from " + oldName + " to " + name);
    }
    public void updateNRIC(Customer customer, String nric)
    {
        String oldNRIC = customer.getNric();
        customer.setnric(nric);
        System.out.println("NRIC has been changed from " + oldNRIC + " to " + nric);
    }
    public void updateDateOfBirth(Customer customer, LocalDate birthDate)
    {
        LocalDate oldBirthDate = customer.getDateOfBirth();
        customer.setDateofBirth(birthDate);
        System.out.println("Date of Birth has been changed from " + oldBirthDate + " to " + birthDate);
    }
    public void updateContactNumber(Customer customer, int contactNumber)
    {
        int oldContactNumber = customer.getContactNumber();
        customer.setConteactNumber(contactNumber);
        System.out.println("Contact Number has been changed from " + oldContactNumber + " to " + contactNumber);
    }
    public void updateEmail(Customer customer, String email)
    {
        String oldEmail = customer.getEmail();
        customer.setEmail(email);
        System.out.println("Email has been changed from " + oldEmail + " to " + email);
    }
    public void updateAddress(Customer customer, String address)
    {
        String oldAddress = customer.getAddress();
        customer.setAddress(address);
        System.out.println("Address has been changed from " + oldAddress + " to " + address);
    }
    
    //Account
    public void updateTransferLimit(Account account, double amount)
    {
        double oldTransferLimit = account.getTransferLimit();
        account.setTransferLimit(amount);
        System.out.println("Transfer limit has been changed from " + oldTransferLimit + " to " + amount);
    }
    public void updateWithdrawLimit(Account account, double amount)
    {
        double oldWithdrawLimit = account.getWithdrawLimit();
        account.setWithdrawLimit(amount);
        System.out.println("Withdraw limit has been changed from " + oldWithdrawLimit + " to " + amount);
    }

    private double getValidAmount(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter the amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Amount must be greater than 0. Please try again.");
                    continue; // This will loop back, allowing the user another chance to enter a valid amount
                }
                if (amount > MAX_BANK_TRANSFER_LIMIT) {
                    System.out.println("Limit cannot exceed MAX_BANK_TRANSFER_LIMIT. Please approach a bank staff to change the limit.");
                    return -1; // Returning a sentinel value to indicate the operation should be aborted
                }
                return amount; // Valid amount entered within the accepted range
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    


    public void customerSettingMenu(Customer customer)
    {
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.println("\nCustomer system");
            System.out.println("1. Change name");
            System.out.println("2. Change NRIC");
            System.out.println("3. Change date of birth");
            System.out.println("4. Change contact number");
            System.out.println("5. Change email address");
            System.out.println("6. Change address");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Use nextLine and parse to catch non-integer inputs
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue; // Skip to the next iteration of the loop
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter a new name: ");
                    String newName = scanner.nextLine();
                    if (newName.equals(customer.getName())) {
                        throw new IllegalArgumentException("New name cannot be the same as the old name.");
                    }
                    if (newName.isEmpty()) {
                        throw new IllegalArgumentException("Name cannot be empty.");
                    }
                    updateName(customer, newName);
                    break;
                case 2:
                    System.out.print("Enter a new NRIC: ");
                    String newNRIC = scanner.nextLine();
                    if (newNRIC.equals(customer.getNric())) {
                        throw new IllegalArgumentException("New NRIC cannot be the same as the old NRIC.");
                    }
                    if (newNRIC.isEmpty()) {
                        throw new IllegalArgumentException("NRIC cannot be empty.");
                    }
                    updateNRIC(customer, newNRIC);
                    break;
                case 3:
                    System.out.print("Enter a new date of birth (YYYY-MM-DD): ");
                    String newdob = scanner.nextLine();
                    LocalDate newBirthDate = LocalDate.parse(newdob); // This can throw DateTimeParseException
                    if (newBirthDate.equals(customer.getDateOfBirth())) {
                        throw new IllegalArgumentException("New date of birth cannot be the same as the old date of birth.");
                    }
                    updateDateOfBirth(customer, newBirthDate);
                    break;
                case 4:
                    System.out.print("Enter a new contact number: ");
                    String contactNumberInput = scanner.nextLine();
                    if (contactNumberInput.isEmpty()) {
                        System.out.println("Contact number cannot be empty.");
                    } else {
                        try {
                            int newContactNumber = Integer.parseInt(contactNumberInput);
                            if (newContactNumber == customer.getContactNumber()) {
                                System.out.println("New contact number cannot be the same as the old contact number.");
                            } else {
                                updateContactNumber(customer, newContactNumber);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Contact number must be a valid integer.");
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter a new email address: ");
                    String newEmail = scanner.nextLine();
                    if (newEmail.equals(customer.getEmail())) {
                        throw new IllegalArgumentException("New email cannot be the same as the old email.");
                    }
                    if (!newEmail.contains("@") || !newEmail.contains(".")) { // Basic check for email format
                        throw new IllegalArgumentException("Invalid email format.");
                    }
                    if (newEmail.isEmpty()) {
                        throw new IllegalArgumentException("Name cannot be empty.");
                    }
                    updateEmail(customer, newEmail);
                    break;
                case 6:
                    System.out.print("Enter a new address: ");
                    String newAddress = scanner.nextLine();
                    if (newAddress.equals(customer.getAddress())) {
                        throw new IllegalArgumentException("New address cannot be the same as the old address.");
                    }
                    if (newAddress.isEmpty()) {
                        throw new IllegalArgumentException("Address cannot be empty.");
                    }
                    updateAddress(customer, newAddress);
                    break;
                case 7:
                    System.out.println("Exiting customer settings.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter 1-7.");

            
            }

        }
    }

    public void accountSetting(Account account)
    {
        while(true)
        {
            Scanner scanner  = new Scanner(System.in);

            System.out.println("\nAccount system");
            System.out.println("1. Change transfer limit");
            System.out.println("2. Change withdraw limit");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Use nextLine and parse to catch non-integer inputs
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue; // Skip to the next iteration of the loop
            }
            
            switch (choice) {
                case 1:
                    System.out.print("Enter new transfer limit: ");
                    double newTransferLimit = getValidAmount(scanner);
                    updateTransferLimit(account, newTransferLimit);
                    break;
                case 2:
                    System.out.print("Enter new withdraw limit: ");
                    double newWithdrawLimit = getValidAmount(scanner);
                    updateWithdrawLimit(account, newWithdrawLimit);
                    break;
                case 3:
                    System.out.println("Exiting account setting.");
                    break; // Exit the while loop and method
                default:
                    System.out.println("Invalid choice, please enter 1-3.");
            }

        }
    }
    public void settingMenu(Customer customer) {
        Scanner scanner = new Scanner(System.in);
    
        while (true) {
            System.out.println("\nSetting Menu");
            System.out.println("1. Customer Settings");
            System.out.println("2. Account Settings");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
    
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    customerSettingMenu(customer); // Call the method to handle customer settings
                    break;
                case "2":
                    Account account = customer.promptAccount(scanner);
                    accountSetting(account); // Call the method to handle account settings
                    break;
                case "3":
                    System.out.println("Exiting settings.");
                    return; // Exit the method
                default:
                    System.out.println("Invalid choice, please enter 1-3.");
            }
        }
    }
    

}


