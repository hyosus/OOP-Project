import java.io.IOException;
//import java.time.format.DateTimeParseException;
//import java.util.InputMismatchException;
import java.util.regex.Pattern;

public class ExceptionHandling {

    // Handle empty input exception
    public static void handleEmptyInputException(String fieldName) {
        System.out.println(fieldName + " cannot be empty. Please enter again.");
    }

    // Method to handle NRIC format
    public static boolean handleNric(String nric) {
        if (nric.matches("[stfgSTFG]\\d{7}[a-zA-Z]")) {
            return true; // NRIC is valid
        } else {
            System.out.println("Invalid NRIC format. @xxxxxxx#, starting with S, T, F, G, M");
            return false; // NRIC is not valid
        }
    }

    // Method to handle Email format
    public static boolean handleEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        if (emailPattern.matcher(email).matches()) {
            return true; // Email is valid
        } else {
            System.out.println("Invalid email format. Please enter a valid email address.");
            return false; // Email is not valid
        }
    }

    // Method to handle IOException
    public static void handleIOException(IOException e) {
        System.err.println("Error reading from/ writing to CSV file: " + e.getMessage());
        // You can choose to log the exception or perform additional error handling here
    }

    // Method to handle DateTimeParseException
    public static void handleDateTimeParseException() {
        System.err.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
        // You can choose to log the exception or perform additional error handling here
    }

    // Method to handle handleNumberFormatException
    public static void handleNumberFormatException() {
        System.err.println("Invalid contact number. Please enter a valid number.");
        // You can choose to log the exception or perform additional error handling here
    }

    // Method to handle other exceptions
    public static void handleOtherException(Exception e) {
        System.err.println("An unexpected error occurred: " + e.getMessage());
        // You can choose to log the exception or perform additional error handling here
    }
}
