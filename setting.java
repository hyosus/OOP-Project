import java.time.LocalDate;

public class setting {
    private Account account;
    private Customer customer;

    public void updateName(Customer customer, String name)
    {
        customer.setName(name);
    }
    public void updateNRIC(Customer customer, String nric)
    {
        customer.setnric(nric);;
    }
    public void updateDateOfBirth(Customer customer, LocalDate birthDate)
    {
        customer.setDateOfBirth(birthDate);
    }
    public void updateContactNumber(Customer customer, int contactNumber)
    {
        customer.setConteactNumber(contactNumber);
    }
    public void updateEmail(Customer customer, String email)
    {
        customer.setEmail(email);
    }
    public void updateAddress(Customer customer, String address)
    {
        customer.setAddress(address);
    }
    

    public void updateAccountInformation(Account account, double transferLimit, double localWithdrawLimit, double overseasWithdrawLimit)
    {
        account.setTransferLimit(transferLimit);
        account.setLocalWithdrawLimit(localWithdrawLimit);
        account.setOverseasWithdrawLimit(overseasWithdrawLimit);
    }

    public void updateTransferLimit(Account account, double transferlimit)
    {
        account.setTransferLimit(transferlimit);
    }
    public void updateLocalWithdrawLimit(Account account, double local)
    {
        account.setLocalWithdrawLimit(local);
    }
    public void updateOverseaWithdrawLimit(Account account, double overseas)
    {
        account.setOverseasWithdrawLimit(overseas);
    }
}


