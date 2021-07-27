import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        // Init Scanner
        Scanner sc = new Scanner(System.in);

        // Init bank
        Bank theBank = new Bank("Sberbank");

        // Add a user which also creates a savings account
        User aUser = theBank.addUser("John", "Doe", "1234");

        // Add a checking account for out user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User currentUser;
        while (true) {

            // Stay in  the login prompt until successful login
            currentUser = ATM.MainMenuPrompt(theBank, sc);

            // Stay in main menu until user quits
            ATM.printUserMenu(currentUser, sc);
        }
    }

    public static void printUserMenu(User currentUser, Scanner sc) {
        
    }

    /**
     * Print the ATM's login menu
     * @param theBank   the Bank object whose accounts to use
     * @param sc        the Scanner object to use for user input
     * @return          the authenticated User object
     */
    public static User MainMenuPrompt(Bank theBank, Scanner sc) {

        // Inits
        String userID;
        String pin;
        User authUser;

        // Prompt the user for User ID/Pin combo until a correct one is reached
        do {
            System.out.printf("\n\nWelcome message to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();

            // Try to get the User object corresponding to the ID
            // and pin combo
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combination." +
                        " Please try again.");
            }
        } while (authUser == null); // Continue looping until successful login

        return authUser;
    }
}
