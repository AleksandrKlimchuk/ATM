import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class User implements UUID {

    private static final String SEC_HASH_ALG = "MD5";

    /**
     * The first name of user.
     * */
    private String firstName;

    /**
     * The last name of user.
     * */
    private String lastName;

    /**
     * The ID number of the user.
     * */
    private String uuid;

    /**
     * The MD5 hash of the user's pin number.
     * */
    private byte[] pinHash;

    /**
     * The list accounts for this user.
     * */
    private List<Account> accounts;

    /**
     * Create a new User.
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param pin       the user's account pin number
     * @param bank      the Bank object that the user is a customer of
     */
    public User(String firstName, String lastName, String pin, Bank bank) {
        // Set user's name
        this.firstName = firstName;
        this.lastName  = lastName;

        // Store the pin's MD5 hash, rather than original value, for security
        // reasons
        try {
            MessageDigest md = MessageDigest.getInstance(SEC_HASH_ALG);
            this.pinHash = md.digest(pin.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException.");
            e.printStackTrace();
            System.exit(1);
        }

        // Get a new unique universal ID for the user
        this.uuid = bank.getNewUserUUID();

        // Create empty list of accounts
        this.accounts = new ArrayList<>();

        // Print log message
        System.out.printf("New user %s %s with ID %s created.\n",
                this.lastName, this.firstName, this.uuid);
    }

    /**
     * Add an account for the user.
     * @param account   the account to add
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
     * Return the user's UUID
     * @return the UUID
     */
    @Override
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Check whether a given pin matches the true User pin
     * @param aPin  the Pin to check
     * @return      whether the pin is valid or not
     */
    public boolean validatePin(String aPin) {
        try {
            MessageDigest md = MessageDigest.getInstance(SEC_HASH_ALG);
            return MessageDigest.isEqual(md.digest(aPin.getBytes(StandardCharsets.UTF_8)),
                    this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error, caught NoSuchAlgorithmException.");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
}
