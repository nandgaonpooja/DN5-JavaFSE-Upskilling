import java.util.Scanner;
class InvalidAgeException extends Exception {
    public InvalidAgeException(String msg) {
        super(msg);
    }
}
public class AgeValidator {
    public static void checkAge(int userAge) throws InvalidAgeException {
        if (userAge < 18) {
            throw new InvalidAgeException("You must be at least 18 years old.");
        }
        System.out.println("Age is valid.");
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {
            System.out.print("Enter your age: ");
            int userAge = input.nextInt();
            checkAge(userAge);
        } catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        }
        input.close();
    }
}