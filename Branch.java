
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
    private ArrayList<String> queueNum;

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
    public ArrayList<String> getQueueNum() {
        return queueNum;
    }

    /**
     * Sets the queue number.
     *
     * @param q The new queue number.
     */
    public void setQueueNum(ArrayList<String> q) {
        this.queueNum = q;
    }

    public static void main(String[] args) {
        Branch newBranch = new Branch("123", false, "Singapore", "Clementi DBS", "430 clementi", 888811111, 3);
    }

    // Methods
    // Method that returns a Branch object
    public static Branch createBranch(String bid, boolean isOverseas, String country, String bname, String address,
            int hp, int atmAvail) {
        return new Branch(bid, isOverseas, country, bname, address, hp, atmAvail);
    }

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
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Exiting...");
                    break;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Generates a new queue number for the branch. First character of the queue 'P'
     * indicates physical queue, 'V' indicates virtual queue. Priority added at the
     * back of the queue from a range of 1 to 5, where 1 is the highest priority.
     */
    public void generateQueueNumber(boolean isPhysical, int priority) {
        int nextQueueNumber = queueNum.isEmpty() ? 1 : queueNum.size() + 1;
        String queueId = (isPhysical ? "P" : "V") + nextQueueNumber + "-" + priority;

        queueNum.add(queueId); // add queue number to array list
        System.out.println("Generated queue number: " + queueId);
    }

    /**
     * Removes a specified queue number when a customer is served.
     */
    public void removeQueue() {
        System.err.println("Queue to be removed: ");

        try (Scanner in = new Scanner(System.in)) {
            String input = in.next();

            // int queueToBeRemoved = Integer.parseInt(input);

            if (queueNum.contains(input)) {
                queueNum.remove(input);
                System.out.println("Queue removed. Current queue: " + queueNum);
            } else {
                System.out.println("Invalid queue number, try again.");
                removeQueue();
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
        ArrayList<String> virtualQueue = new ArrayList<String>();
        ArrayList<String> physicalQueue = new ArrayList<String>();

        for (String queueId : queueNum) {
            char firstChar = queueId.charAt(0);

            if (firstChar == 'V') {
                virtualQueue.add(queueId);
            } else {
                physicalQueue.add(queueId);
            }
        }
        System.out.println("Physical queues: " + physicalQueue + ", " + physicalQueue.size() + " in line");
        System.out.println("Virtual queues: " + virtualQueue + ", " + virtualQueue.size() + " in line");
    }

    /**
     * Displays information about the branch.
     */
    public void displayBranchInfo() {
        System.out.println(
                "Branch ID: " + branchId + "Overseas: " + isOverseas + "Country: " + country + "Branch Name: "
                        + branchName + "Address: " + address
                        + "Phone number: " + phoneNum + "ATMs Available: " + atmAvailability);
    }
}