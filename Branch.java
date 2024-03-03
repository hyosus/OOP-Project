
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Branch class represents a bank branch with various attributes and
 * functionalities.
 */
public class Branch {
    // Attributes
    private String branchId;
    private boolean isOverseas;
    private String country;
    private String branchName;
    private String address;
    private int phoneNum;
    private int atmAvailability;
    private ArrayList<Integer> queueNum;

    /**
     * Constructor for creating a Branch object with specified attributes.
     *
     * @param bid        The branch ID.
     * @param isOverseas Indicates whether the branch is overseas.
     * @param country    The country where the branch is located.
     * @param bname      The name of the branch.
     * @param address    The address of the branch.
     * @param hp         The phone number of the branch.
     * @param atmAvail   The availability of ATMs in the branch.
     */
    public Branch(String bid, boolean isOverseas, String country, String bname, String address, int hp, int atmAvail) {
        this.branchId = bid;
        this.isOverseas = isOverseas;
        this.country = country;
        this.branchName = bname;
        this.address = address;
        this.phoneNum = hp;
        this.atmAvailability = atmAvail;
        this.queueNum = new ArrayList<>();
    }

    // Getter Setter
    /**
     * Retrieves the branch ID.
     *
     * @return The branch ID.
     */
    public String getBranchId() {
        return branchId;
    }

    /**
     * Sets the branch ID.
     *
     * @param bid The new branch ID.
     */
    public void setBranchId(String bid) {
        this.branchId = bid;
    }

    /**
     * Retrieves the branch name.
     *
     * @return The branch name.
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * Sets the branch name.
     *
     * @param bname The new branch name.
     */
    public void setBranchName(String bname) {
        this.branchName = bname;
    }

    /**
     * Retrieves the branch address.
     *
     * @return The branch address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the branch address.
     *
     * @param address The new branch address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the phone number.
     *
     * @return The phone number.
     */
    public int getPhoneNum() {
        return phoneNum;
    }

    /**
     * Sets the phone number.
     *
     * @param hp The new phone number.
     */
    public void setPhoneNum(int hp) {
        this.phoneNum = hp;
    }

    /**
     * Retrieves the ATM availability.
     *
     * @return The ATM availability.
     */
    public int getAtmAvailability() {
        return atmAvailability;
    }

    /**
     * Sets the ATM availability.
     *
     * @param atm The new ATM availability.
     */
    public void setAtmAvailability(int atm) {
        this.atmAvailability = atm;
    }

    /**
     * Retrieves the queue.
     *
     * @return The queue.
     */
    public ArrayList<Integer> getQueueNum() {
        return queueNum;
    }

    /**
     * Sets the queue number.
     *
     * @param q The new queue number.
     */
    public void setQueueNum(ArrayList<Integer> q) {
        this.queueNum = q;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

    // Methods
    /**
     * Allows the user to update certain details of the branch.
     */
    public void updateBranchDetails() {
        System.out.println("Which info to change? \n1) Branch Name \n2) Branch Address \n3) Phone number \n4) Exit");

        try (Scanner in = new Scanner(System.in)) {
            String input = in.next();

            switch (Integer.parseInt(input)) {
                case 1:
                    System.out.println("Current Branch name: " + branchName + "\nChange to: ");

                    String newBranchName = in.next();
                    setBranchName(newBranchName);

                    System.out.println("Branch name succesfully changed to: " + getBranchName());
                    break;

                case 2:
                    System.out.println("Current Address: " + address + "\nChange to: ");

                    String newAddress = in.next();
                    setAddress(newAddress);

                    System.out.println("Address succesfully changed to: " + getAddress());
                    break;

                case 3:
                    System.out.println("Current Phone number: " + phoneNum + "\nChange to: ");

                    String newPhoneNum = in.next();
                    setPhoneNum(Integer.parseInt(newPhoneNum));

                    System.out.println("Phone number succesfully changed to: " + getPhoneNum());
                    break;

                case 4:
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Generates a new queue number for the branch.
     */
    public void generateQueueNumber() {
        int nextQueueNumber = queueNum.isEmpty() ? 1 : queueNum.get(queueNum.size() - 1) + 1; // If list is empty, start
                                                                                              // with 1. Else increment
                                                                                              // from last number
        queueNum.add(nextQueueNumber); // add queue number to array list
        System.out.println("Generated queue number: " + nextQueueNumber);
    }

    /**
     * Removes a specified queue number when a customer is served.
     */
    public void removeQueue() {
        try (Scanner in = new Scanner(System.in)) {
            String input = in.next();

            int queueToBeRemoved = Integer.parseInt(input);

            if (queueNum.contains(queueToBeRemoved)) {
                queueNum.remove(queueToBeRemoved);
            } else {
                System.out.println("Invalid queue number");
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Displays the current queue and the number of people in line.
     */
    public void displayQueue() {
        System.out.println("Current queue: " + queueNum);
        System.out.println("People in line: " + queueNum.size());
    }

    /**
     * Displays information about the branch.
     */
    public void displayBranchInfo() {
        System.out.println(
                "Branch ID: " + branchId + "Country: " + country + "Branch Name: " + branchName + "Address: " + address
                        + "Phone number: " + phoneNum + "ATMs Available: " + atmAvailability);
    }
}