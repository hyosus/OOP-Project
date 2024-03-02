import java.util.ArrayList;
import java.util.Scanner;

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

    // Constructor
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
    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String bid) {
        this.branchId = bid;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String bname) {
        this.branchName = bname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int hp) {
        this.phoneNum = hp;
    }

    public int getAtmAvailability() {
        return atmAvailability;
    }

    public void setAtmAvailability(int atm) {
        this.atmAvailability = atm;
    }

    public ArrayList<Integer> getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(ArrayList<Integer> q) {
        this.queueNum = q;
    }

    public static void main(String[] args) {
    }

    // Methods
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

    public void generateQueueNumber() {
        int nextQueueNumber = queueNum.isEmpty() ? 1 : queueNum.get(queueNum.size() - 1) + 1; // If list is empty, start
                                                                                              // with 1. Else increment
                                                                                              // from last number
        queueNum.add(nextQueueNumber); // add queue number to array list
        System.out.println("Generated queue number: " + nextQueueNumber);
    }

    public void removeQueue() { // when customer is served, remove their queue number
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

    public void displayQueue() {
        System.out.println("Current queue: " + queueNum);
        System.out.println("People in line: " + queueNum.size());
    }

    public void displayBranchInfo() {
        System.out.println(
                "Branch ID: " + branchId + "Country: " + country + "Branch Name: " + branchName + "Address: " + address
                        + "Phone number: " + phoneNum + "ATMs Available: " + atmAvailability);
    }
}