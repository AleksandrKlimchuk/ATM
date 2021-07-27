import java.util.ArrayList;
import java.util.List;

public class Account implements UUID{

    /**
     * The name of the account.
     * */
    private String name;

    /**
    * The account ID number.
    */
    private String uuid;

    /**
     * The user object that owns this account.
     * */
    private User holder;

    /**
     * The list of transactions for this account.
     * */
    private List<Transaction> transactions;

    /**
     * Create a new Account.
     * @param name      the name of the account
     * @param holder    the User object that holds account
     * @param bank      the bank that issues the account
     */
    Account(String name, User holder, Bank bank) {

        // Set the account name and holder
        this.name = name;
        this.holder = holder;

        // Get new account UUID
        this.uuid = bank.getNewAccountUUID();

        // Create empty list of transactions
        this.transactions = new ArrayList<>();
    }

    @Override
    public String getUUID() {
        return uuid;
    }
}
