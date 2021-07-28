import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Bank {
    private static final int USER_UUID_LENGTH    = 6;
    private static final int ACCOUNT_UUID_LENGTH = 10;

    private final String name;

    private final List<User> users;

    private final List<Account> accounts;

    /**
     * Create a new Bank object with empty lists of users and accounts
     * @param name  the name of the Bank
     */
    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getNumOfUsers() {
        return users.size();
    }

    public int getNumOfAccounts() {
        return accounts.size();
    }

    /**
     * Create a new universally unique ID for a user.
     * @return the UUID
     */
    public String getNewUserUUID() {
        return generateUUID(this.users, USER_UUID_LENGTH);
    }

    /**
     * Create a new universally unique ID for an account.
     * @return the UUID
     */
    public String getNewAccountUUID() {
        return generateUUID(this.accounts, ACCOUNT_UUID_LENGTH);
    }

    /**
     * Generate a new random unique ID for object which implements UUID.
     * @param uniqueList list of unique objects for type T
     * @param length length of uuid
     * @param <T> type which implement UUID interface. User/Account, as example
     * @return the uuid
     */
    private <T extends UUID> String generateUUID(List<T> uniqueList, int length) {
        // Inits
        StringBuilder uuidBuilder;
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        boolean nonUnique;

        // Continue looping until we get a unique ID
        do {
            // Generate number
            uuidBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                uuidBuilder.append(((Integer) rnd.nextInt(10)));
            }

            // Check to make sure it's unique
            nonUnique = false;
            for (T element : uniqueList) {
                if (uuidBuilder.toString().compareTo(element.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuidBuilder.toString();
    }

    /**
     * Add an account.
     * @param account the account to add
     */
    public void addAccount(Account account) {
        accounts.add(account);
    }

    /**
     * Create a new user of bank
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param pin       the user's pin
     * @return          the new User object
     */
    public User addUser(String firstName, String lastName, String pin) {

        // Create a new User object and add it to out list
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // Create a savings account for the user and add to User and Bank
        // accounts lists
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);

        return newUser;
    }

    /**
     * Get the User object associated with a particular userID and pin,
     * if the are valid
     * @param userID    the UUID of the user to log in
     * @param pin       the pin of the user to log in
     * @return          the User object, if the login is successful, or
     *                  null, if it is not
     */
    public User userLogin(String userID, String pin) {

        // Search through list of users
        for (User user : users) {

            // Check user ID is correct
            if (user.getUUID().compareTo(userID) == 0 && user.validatePin(pin)) {
                return user;
            }
        }

        // If we have not fount the user or have an incorrect pin
        return null;
    }
}
