import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.bank.components.security.*;



public class setting {
    public static double MAX_BANK_TRANSFER_LIMIT = 8000.00;
    private static final String CUSTOMERS_CSV_FILE = "customers.csv";
    
    //Empty constructor
    public setting() {
    }
    
    //Customer

    //update contact number
    public void updateContactNumber(Customer customer, int contactNumber)
    {
        int oldContactNumber = customer.getContactNumber();
        customer.setConteactNumber(contactNumber);
        System.out.println("Contact Number has been changed from " + oldContactNumber + " to " + contactNumber);
    }
    //update email
    public void updateEmail(Customer customer, String email)
    {
        String oldEmail = customer.getEmail();
        customer.setEmail(email);
        System.out.println("Email has been changed from " + oldEmail + " to " + email);
    }
    //update address
    public void updateAddress(Customer customer, String address)
    {
        String oldAddress = customer.getAddress();
        customer.setAddress(address);
        System.out.println("Address has been changed from " + oldAddress + " to " + address);
    }
    
    //Account

    //update transfer limit
    public void updateTransferLimit(Account account, double amount)
    {
        double oldTransferLimit = account.getTransferLimit();
        account.setTransferLimit(amount);
        System.out.println("Transfer limit has been changed from " + oldTransferLimit + " to " + amount);
    }
    //update withdraw limit 
    public void updateWithdrawLimit(Account account, double amount)
    {
        double oldWithdrawLimit = account.getWithdrawLimit();
        account.setWithdrawLimit(amount);
        System.out.println("Withdraw limit has been changed from " + oldWithdrawLimit + " to " + amount);
    }

    //Customer setting menu
    public void customerSettingMenu(Customer customer) {
    Scanner scanner = new Scanner(System.in);
    List<String> lines = new ArrayList<>();
    boolean changesMade = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            //look for customer information 
            if (line.startsWith(customer.getCustomerID() + ",")) { 
                System.out.println("\nCustomer system");
                System.out.println("1. Change username");
                System.out.println("2. Change password");
                System.out.println("3. Change contact number");
                System.out.println("4. Change email address");
                System.out.println("5. Change address");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // 

                String[] parts = line.split(",");
                
                switch (choice) {
                    //update username
                    case 1:
                        do{
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine(); // Assuming the name is the second part
                            if(newUsername.isEmpty())
                            {
                                ExceptionHandling.handleEmptyInputException("Username");
                            }
                            else if(newUsername.equals(parts[1]))
                            {
                                System.out.println("New username cannot be the same as the old username.");
                            }
                            else
                            {
                                String oldUsername = parts[1];
                                parts[1] = newUsername;
                                changesMade = true;
                                System.out.println("Username has been changed from " + oldUsername + " to " + newUsername);
                                break;
                            }
                        }while(true);
                        break;
                    //update password 
                    case 2:
                        do{
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            
                            if (newPassword.isEmpty())
                            {
                                ExceptionHandling.handleEmptyInputException("Password");
                            }
                            else if(newPassword.equals(parts[2]))
                            {
                                System.out.println("New password cannot be the same as the old password.");
                            }
                            else
                            {
                                String oldPassword = parts[2];
                                parts[2] = newPassword; 
                                String secretKey = parts[22];
                                String salt = parts[23];
                                String encryptedPassword = AES.encrypt(newPassword, secretKey, salt);
                                parts[21] = encryptedPassword;
                                changesMade = true;
                                System.out.println("Password has been changed from " + oldPassword + " to " + newPassword);
                                break;
                            }
                        }while(true);
                        break;
                    //update contact number
                    case 3:
                        do{
                            System.out.print("Enter new contact number: ");
                            String newContactNumber = scanner.nextLine();
                            if(newContactNumber.isEmpty())
                            {
                                ExceptionHandling.handleEmptyInputException("Contact number");
                            }
                            else if(newContactNumber.equals(parts[6]))
                            {
                                System.out.println("New contact number cannot be the same as the old contact number.");
                            }
                            else
                            {
                                parts[6] = newContactNumber;
                                updateContactNumber(customer, Integer.parseInt(parts[6]));
                                changesMade = true;
                                break;
                            }
                        }while(true);
                        break;
                    //update email
                    case 4: 
                        do
                        {
                            System.out.print("Enter new email address: ");
                            String newEmail = scanner.nextLine();
                            if(newEmail.isEmpty())
                            {
                                ExceptionHandling.handleEmptyInputException("Email");
                            }
                            else if(newEmail.equals(parts[7]))
                            {
                                System.out.println("New email address cannot be the same as the old email address.");
                            }
                            else
                            {
                                parts[7] = newEmail;
                                updateEmail(customer, parts[7]);
                                changesMade = true;
                                break;
                            }
                        }while (true);
                        break;
                    //update address
                    case 5:
                        do{
                            System.out.print("Enter new address: ");
                            String newAddress = scanner.nextLine();
                            if(newAddress.isEmpty())
                            {
                                ExceptionHandling.handleEmptyInputException("Address");
                            }
                            else if(newAddress.equals(parts[8]))
                            {
                                System.out.println("New address cannot be the same as the old address.");
                            }
                            else
                            {
                                parts[8] = newAddress;
                                updateAddress(customer, parts[8]);
                                changesMade = true;
                                break;
                            }
                        }while(true);
                        break;
                    case 6:
                        System.out.println("Exiting and saving changes if any.");
                        break;
                    default:
                        System.out.println("Invalid choice, please enter a valid number.");
                }
                line = String.join(",", parts); 
            }
            lines.add(line); 
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    //store lines in the file
    if (changesMade) {
        try (FileWriter writer = new FileWriter(CUSTOMERS_CSV_FILE, false)) {
            for (String updatedLine : lines) {
                writer.write(updatedLine + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    
    //Account setting menu
    public void accountSetting(Customer customer)
    {
        Scanner scanner = new Scanner(System.in);
        Scanner accountScanner = new Scanner(System.in);
        Account accountChoice = customer.promptAccount(accountScanner);
        boolean changesMade = false;
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_CSV_FILE))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(customer.getCustomerID().equals(parts[0]))
                {
                    //Default account
                    if(accountChoice.getAccountType().equals("Default")&&
                    parts[9].equals(accountChoice.getAccountID()))
                    {
                        System.out.println(parts[13]);
                        System.out.println("\nAccount system");
                        System.out.println("1. Change transfer limit");
                        System.out.println("2. Change withdrawal limit");
                        System.out.println("3. Exit");
                        System.out.print("Enter your choice: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        switch (choice) {
                            //update transfer limit
                            case 1:
                                do{
                                    System.out.print("Enter new transfer limit: ");
                                    double newTransferLimit = Double.parseDouble(scanner.nextLine());
                                    if(newTransferLimit <= 0)
                                    {
                                        System.out.println("Transfer limit must be greater than 0. Please try again.");
                                    }
                                    else if(newTransferLimit > MAX_BANK_TRANSFER_LIMIT)
                                    {
                                        System.out.println("Action cannot be completed(Exceed standard limit). Please approach a bank staff to change the limit.");
                                    }
                                    else if(newTransferLimit == accountChoice.getTransferLimit())
                                    {
                                        System.out.println("New transfer limit cannot be the same as the old transfer limit.");
                                    }
                                    else
                                    {
                                        parts[11] = Double.toString(newTransferLimit);
                                        updateTransferLimit(accountChoice, newTransferLimit);
                                        changesMade = true;
                                        break;
                                    }
                                }while(true);
                                break;
                            //update withdraw limit
                            case 2:
                                do{
                                    System.out.print("Enter new withdrawal limit: ");
                                    double newWithdrawLimit = Double.parseDouble(scanner.nextLine());
                                    if(newWithdrawLimit <= 0)
                                    {
                                        System.out.println("Withdrawal limit must be greater than 0. Please try again.");
                                    }
                                    else if(newWithdrawLimit > MAX_BANK_TRANSFER_LIMIT)
                                    {
                                        System.out.println("Action cannot be completed(Exceed standard limit). Please approach a bank staff to change the limit.");
                                    }
                                    else if(newWithdrawLimit == accountChoice.getWithdrawLimit())
                                    {
                                        System.out.println("New withdrawal limit cannot be the same as the old withdrawal limit.");
                                    }
                                    else
                                    {
                                        parts[12] = Double.toString(newWithdrawLimit);
                                        updateWithdrawLimit(accountChoice, newWithdrawLimit);
                                        changesMade = true;
                                        break;
                                    }
                                }while(true);
                                break;
                            case 3:
                                System.out.println("Exiting and saving changes if any.");
                                break;
                            default:
                                System.out.println("Invalid choice, please enter a valid number.");
                        }
                        line = String.join(",", parts);
                    }
                //Savings account
                if(accountChoice.getAccountType().equals("Savings")&&
                    parts[13].equals(accountChoice.getAccountID()))
                    {
                        System.out.println("\nAccount system");
                        System.out.println("1. Change transfer limit");
                        System.out.println("2. Change withdrawal limit");
                        System.out.println("3. Exit");
                        System.out.print("Enter your choice: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        switch (choice) {
                            //update transfer limit
                            case 1:
                                do{
                                    System.out.print("Enter new transfer limit: ");
                                    double newTransferLimit = Double.parseDouble(scanner.nextLine());
                                    if(newTransferLimit <= 0)
                                    {
                                        System.out.println("Transfer limit must be greater than 0. Please try again.");
                                    }
                                    else if(newTransferLimit > MAX_BANK_TRANSFER_LIMIT)
                                    {
                                        System.out.println("Action cannot be completed(Exceed standard limit). Please approach a bank staff to change the limit.");
                                    }
                                    else if(newTransferLimit == accountChoice.getTransferLimit())
                                    {
                                        System.out.println("New transfer limit cannot be the same as the old transfer limit.");
                                    }
                                    else
                                    {
                                        parts[15] = Double.toString(newTransferLimit);
                                        updateTransferLimit(accountChoice, newTransferLimit);
                                        changesMade = true;
                                        break;
                                    }
                                }while(true);
                                break;
                            //update withdraw limit
                            case 2:
                                do{
                                    System.out.print("Enter new withdrawal limit: ");
                                    double newWithdrawLimit = Double.parseDouble(scanner.nextLine());
                                    if(newWithdrawLimit <= 0)
                                    {
                                        System.out.println("Withdrawal limit must be greater than 0. Please try again.");
                                    }
                                    else if(newWithdrawLimit > MAX_BANK_TRANSFER_LIMIT)
                                    {
                                        System.out.println("Action cannot be completed(Exceed standard limit). Please approach a bank staff to change the limit.");
                                    }
                                    else if(newWithdrawLimit == accountChoice.getWithdrawLimit())
                                    {
                                        System.out.println("New withdrawal limit cannot be the same as the old withdrawal limit.");
                                    }
                                    else
                                    {
                                        parts[16] = Double.toString(newWithdrawLimit);
                                        updateWithdrawLimit(accountChoice, newWithdrawLimit);
                                        changesMade = true;
                                        break;
                                    }
                                }while(true);
                                break;
                            case 3:
                                System.out.println("Exiting and saving changes if any.");
                                break;
                            default:
                                System.out.println("Invalid choice, please enter a valid number.");
                        }line = String.join(",", parts);
                    }
                //Investment account
                if(accountChoice.getAccountType().equals("Investment")&&
                    parts[17].equals(accountChoice.getAccountID()))
                    {
                        System.out.println("\nAccount system");
                        System.out.println("1. Change transfer limit");
                        System.out.println("2. Change withdrawal limit");
                        System.out.println("3. Exit");
                        System.out.print("Enter your choice: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        switch (choice) {
                            //update transfer limit
                            case 1:
                                do{
                                    System.out.print("Enter new transfer limit: ");
                                    double newTransferLimit = Double.parseDouble(scanner.nextLine());
                                    if(newTransferLimit <= 0)
                                    {
                                        System.out.println("Transfer limit must be greater than 0. Please try again.");
                                    }
                                    else if(newTransferLimit > MAX_BANK_TRANSFER_LIMIT)
                                    {
                                        System.out.println("Action cannot be completed(Exceed standard limit). Please approach a bank staff to change the limit.");
                                    }
                                    else if(newTransferLimit == accountChoice.getTransferLimit())
                                    {
                                        System.out.println("New transfer limit cannot be the same as the old transfer limit.");
                                    }
                                    else
                                    {
                                        parts[19] = Double.toString(newTransferLimit);
                                        updateTransferLimit(accountChoice, newTransferLimit);
                                        changesMade = true;
                                        break;
                                    }
                                }while(true);
                                break;
                            //update withdraw limit
                            case 2:
                                do{
                                    System.out.print("Enter new withdrawal limit: ");
                                    double newWithdrawLimit = Double.parseDouble(scanner.nextLine());
                                    if(newWithdrawLimit <= 0)
                                    {
                                        System.out.println("Withdrawal limit must be greater than 0. Please try again.");
                                    }
                                    else if(newWithdrawLimit > MAX_BANK_TRANSFER_LIMIT)
                                    {
                                        System.out.println("Action cannot be completed(Exceed standard limit). Please approach a bank staff to change the limit.");
                                    }
                                    else if(newWithdrawLimit == accountChoice.getWithdrawLimit())
                                    {
                                        System.out.println("New withdrawal limit cannot be the same as the old withdrawal limit.");
                                    }
                                    else
                                    {
                                        parts[20] = Double.toString(newWithdrawLimit);
                                        updateWithdrawLimit(accountChoice, newWithdrawLimit);
                                        changesMade = true;
                                        break;
                                    }
                                }while(true);
                                break;
                            case 3:
                                System.out.println("Exiting and saving changes if any.");
                                break;
                            default:
                                System.out.println("Invalid choice, please enter a valid number.");
                        }line = String.join(",", parts);
                    }    
            }lines.add(line);
        }
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    
    //store lines in the file
    if(changesMade){
        try (FileWriter writer = new FileWriter(CUSTOMERS_CSV_FILE, false)) {
            for (String updatedLine : lines) {
                writer.write(updatedLine + System.lineSeparator());
            }
            }
        catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    //Setting menu
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
                    accountSetting(customer); // Call the method to handle account settings
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


