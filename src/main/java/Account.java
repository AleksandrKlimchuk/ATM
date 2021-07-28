import java.util.ArrayList;
import java.util.List;

public class Account implements UUID{

    /**
     * The name of the account.
     * */
    private final String name;

    /**
    * The account ID number.
    */
    private final String uuid;

    /**
     * The user object that owns this account.
     * */
    private final User holder;

    /**
     * The list of transactions for this account.
     * */
    private final List<Transaction> transactions;

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

    /**
     * Return UUID of current account.
     * @return UUID
     */
    @Override
    public String getUUID() {
        return uuid;
    }

    /**
     * Get summary line for the account.
     * @return the string summary
     */
    public String getSummaryLine() {

        // Get the account's balance
        double balance = this.getBalance();

        // for the summary line, depending on the whether the balance
        // is negative
        return String.format("%s\t:\t$%s\t:\t%s", this.uuid,
                getBalanceLine(balance), this.name);
    }

    /**
     * Get converted balance in string. Negative balance add in rounded parentheses.
     * @param balance balance which will be converted to string
     * @return string of balance
     */
    private String getBalanceLine(double balance) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%.02f", balance));
        if (balance < 0) {
            builder.insert(0, "(").append(")");
        }
        return builder.toString();
    }

    /**
     * Get the balance of this account by adding the amount of the each transaction
     * @return the balance value
     */
    public double getBalance() {
        double balance = 0.0;

        for (Transaction transaction : this.transactions) {
            balance += transaction.getAmount();
        }

        return balance;
    }

    /**
     * Print the transaction history of the account
     */
    public void printTransHistory() {

        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        if (this.transactions.size() == 0) {
            System.out.println("Account has not any transactions.");
        }
        for (int i = this.transactions.size() - 1; i >= 0; i--) {
            System.out.print(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Add the new transaction in this account.
     * @param amount    the amount transacted
     * @param memo      the transaction memo
     */
    public void addTransaction(double amount, String memo) {

        // Create a new transaction object and add it in list
        Transaction newTransaction = new Transaction(amount, memo, this);
        this.transactions.add(newTransaction);
    }

    public User getHolder() {
        return holder;
    }
}
