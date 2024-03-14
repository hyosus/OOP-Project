import java.time.LocalDate;


public class Customer {
    private String customerID;
    private String name;
    private String nric;
    private LocalDate dateOfBirth;
    private int contactNumber;
    private String email;
    private String address;

    public Customer(String customerID, String name, String nric, LocalDate dob, int contactNumber, String email,  String address) {
        this.customerID = customerID;
        this.name = name;
        this.nric = nric;
        this.dateOfBirth = dob;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
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
}
