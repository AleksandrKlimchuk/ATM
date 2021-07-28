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
    private final String firstName;

    /**
     * The last name of user.
     * */
    private final String lastName;

    /**
     * The ID number of the user.
     * */
    private final String uuid;

    /**
     * The MD5 hash of the user's pin number.
     * */
    private byte[] pinHash;

    /**
     * The list accounts for this user.
     * */
    private final List<Account> accounts;

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
     * Check whether a given pin matches the true User pin.
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

    /**
     * Return the user's first name.
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Print summaries for the accounts of this user.
     */
    public void printAccountsSummary() {

        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, this.accounts.get(i).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Get the number of accounts of the user.
     * @return the number of accounts
     */
    public int getNumAccounts() {
        return this.accounts.size();
    }

    /**
     * Print transaction history for a particular account.
     * @param acctIndex the index of the account use
     */
    public void printAccountTransHistory(int acctIndex) {
        this.accounts.get(acctIndex).printTransHistory();
    }

    /**
     * Get the balance of a particular account.
     * @param accountIndex  the index of the account to use
     * @return              the balance of the account
     */
    public double getAccountBalance(int accountIndex) {
        return this.accounts.get(accountIndex).getBalance();
    }

    /**
     * Get the UUID of a particular account
     * @param accountIndex      the index of the account to use
     * @return                  the UUID of the account
     */
    public String getAccountUUID(int accountIndex) {
        return this.accounts.get(accountIndex).getUUID();
    }

    /**
     * Add the transaction to a particular account
     * @param accountIndex      the index of the account
     * @param amount            the amount of the transaction
     * @param memo              the memo of the transaction
     */
    public void addAccountTransaction(int accountIndex, double amount, String memo) {
        this.accounts.get(accountIndex).addTransaction(amount, memo);
    }
}
