import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Customer {
    private String customerID;
    private String name;
    private String nric;
    private LocalDate dateOfBirth;
    private int contactNumber;
    private String email;
    private String address;
    private List<Account> accounts;

    public Customer(String customerID, String name, String nric, LocalDate dob, int contactNumber, String email,  String address) {
        this.customerID = customerID;
        this.name = name;
        this.nric = nric;
        this.dateOfBirth = dob;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.accounts = new ArrayList<>();
        
    }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setnric(String nric) {
        this.nric = nric;
    }

    public void setConteactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void displayAllAccountInfo() {
        for (Account account : accounts) {
            account.displayAccountInfo();
        }
    }

    public void loadAccounts(String filename, String customerID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10 && parts[0].equals(customerID)) {
                    for (int i = 9; i < parts.length; i += 2) {
                        String accountId = parts[i];
                        String accountType;
                        switch (accountId.charAt(0)) {
                            case 'A':
                                accountType = "Default";
                                break;
                            case 'S':
                                accountType = "Savings";
                                break;
                            case 'J':
                                accountType = "Investment";
                                break;
                            default:
                                continue; // skip this account if the type is unknown
                        }
                        Account account = new Account(accountId, accountType);
                        if (!this.accounts.contains(account)) {
                            this.accounts.add(account);
                        }
                    }
                    break; // exit the loop once the customer is found
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static Customer loadCustomerByUsernameAndPassword(String username, String password, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].equals(username) && parts[2].equals(password)) {
                    String customerId = parts[0];
                    String name = parts[3];
                    String nric = parts[4];
                    LocalDate dob = LocalDate.parse(parts[5]);
                    int contactNumber = Integer.parseInt(parts[6]);
                    String email = parts[7];
                    String address = parts[8];

                    return new Customer(customerId, name, nric, dob, contactNumber, email,  address);

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from CSV file: " + e.getMessage());
        }

        return null; // Return null if username and password not found
  }
}
