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
            boolean printUserMenu = true;
            while (printUserMenu) {
                printUserMenu = ATM.printUserMenu(currentUser, sc);
            }

            System.out.print("\nIf you want to close programme, enter Q/q.\nEnter: ");
            String quitState = sc.next();
            if ("q".equals(quitState) || "Q".equals(quitState)) {
                break;
            }
        }

        System.out.println("Programme terminated.");
    }

    /**
     * Show menu for user and make some operation
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object used for User input
     * @return              state for printing again
     */
    public static boolean printUserMenu(User currentUser, Scanner sc) {

        // Print a summary of user's accounts
        currentUser.printAccountsSummary();

        // Init
        int choice;

        // User menu
        boolean invalidChoice;
        do {
            System.out.printf("Welcome %s, what would you like to do?\n", currentUser.getFirstName());
            System.out.println("\t1) Show account transaction history.");
            System.out.println("\t2) Withdraw.");
            System.out.println("\t3) Deposit.");
            System.out.println("\t4) Transfer.");
            System.out.println("\t5) Quit.");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            invalidChoice = ((choice < 1) || (choice > 5));

            if (invalidChoice) {
                System.err.println("Invalid choice. Please chose [1-5].");
            }
        } while (invalidChoice);

        // Process the choice
        switch (choice) {
            case 1:
                ATM.showTransHistory(currentUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(currentUser, sc);
                break;
            case 3:
                ATM.depositFunds(currentUser, sc);
                break;
            case 4:
                ATM.transferFunds(currentUser, sc);
                break;
            case 5:
            default:
                // TODO handle this cases
                break;
        }

        // Redisplay this menu unless the user wants to quit
        return choice != 5;
    }

    /**
     * Process transferring funds from one account to another.
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object used for User input
     */
    private static void transferFunds(User currentUser, Scanner sc) {

        // Inits
        int fromAccount;
        int toAccount;

        double amount;
        double acctBalance;

        // Get the account to transfer for
        fromAccount = ATM.getAccountNumber(currentUser, sc, false);

        // Get the account balance
        acctBalance = currentUser.getAccountBalance(fromAccount);

        // Get the account to transfer to
        toAccount = ATM.getAccountNumber(currentUser, sc, true);

        // Get the amount to transfer
        amount = getAmount(acctBalance, sc);

        // Do the transfer
        currentUser.addAccountTransaction(fromAccount, -1 * amount,String.format(
                "Transfer to account %s", currentUser.getAccountUUID(toAccount)));
        currentUser.addAccountTransaction(toAccount, amount,String.format(
                "Transfer to account %s", currentUser.getAccountUUID(fromAccount)));
    }

    /**
     * Process a fund deposit to an account.
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object used for User input
     */
    private static void depositFunds(User currentUser, Scanner sc) {
        makeFundsOperation(currentUser, sc, true);
    }

    /**
     * Process a fund  withdraw from an account.
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object used for User input
     */
    private static void withdrawFunds(User currentUser, Scanner sc) {
        makeFundsOperation(currentUser, sc, false);
    }

    /**
     * Process a fund operation from an account
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object used for User input
     * @param isDeposit     flag which show status of operation: deposit(tue)
     *                      withdraw(false)
     */
    private static void makeFundsOperation(User currentUser, Scanner sc, boolean isDeposit) {

        // Init
        int account;
        double amount;
        double acctBalance;
        String memo;

        // Get the account to transfer for
        account = ATM.getAccountNumber(currentUser, sc, false);
        acctBalance = currentUser.getAccountBalance(account);

        // Get the amount to transfer
        amount = getAmount(acctBalance, sc);

        // User can't withdraw more, than he has on his account
        // Call getAmount func unless condition will be accepted
        while (!isDeposit && amount > acctBalance) {
            System.out.println("Amount must not be greater than account balance.\n");
            amount = getAmount(acctBalance, sc);
        }

        // Gobble up rest of previous input
        sc.nextLine();

        // Get the memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // Do the withdraw
        if (!isDeposit) {
            amount *= -1;
        }
        currentUser.addAccountTransaction(account, amount, memo);
    }

    /**
     * Show the transaction history for an account.
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object used for user Input
     */
    private static void showTransHistory(User currentUser, Scanner sc) {
        int theAcct;
        boolean invalidAcct;

        // Get Account whose transaction history to look at
        do {
            System.out.printf("Enter the number (1-%d) of the account\n " +
                    "whose transaction you want to see: ", currentUser.getNumAccounts());
            theAcct = sc.nextInt() - 1;
            invalidAcct = ((theAcct < 0) || (theAcct >= currentUser.getNumAccounts()));
            if (invalidAcct) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (invalidAcct);

        // Print the transaction history
        currentUser.printAccountTransHistory(theAcct);
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

    /**
     * Get account number from user input.
     * @param currentUser   the logged-in User object
     * @param sc            the Scanner object to use for user input
     * @param transTo       flag which show direction of transaction
     * @return              number of chosen account
     */
    private static int getAccountNumber(User currentUser, Scanner sc, boolean transTo) {
        boolean invalidAccount;
        int account;

        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer ", currentUser.getNumAccounts());
            if (transTo) {
                System.out.print("to: ");
            } else {
                System.out.println("from: ");
            }
            account = sc.nextInt() - 1;
            invalidAccount = ((account < 0) || (account >= currentUser.getNumAccounts()));
            if ( invalidAccount) {
                System.out.println("Invalid account. Please try again.\n");
            }
        } while (invalidAccount);

        return account;
    }

    /**
     * Get amount of transaction from input.
     * @param acctBalance   current balance of account
     * @param sc            the Scanner object to use for user input
     * @return              amount for transaction
     */
    private static double getAmount(double acctBalance, Scanner sc) {
        double amount;
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
                    acctBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);
        return amount;
    }
}
