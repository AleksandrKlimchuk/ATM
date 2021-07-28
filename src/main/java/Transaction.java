import java.util.Date;

public class Transaction {

    /**
     * The amount of this transaction.
     * */
    private final double amount;

    /**
     * The time and date of this transaction.
     */
    private final Date timestamp;

    /**
     * A memo for this transaction.
     */
    private String memo;

    /**
     * The account in which the transaction was performed.
     */
    private final Account inAccount;

    /**
     * Create a new transaction without memo.
     * @param amount    the account transacted
     * @param inAccount the account the transaction belongs to
     */
    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    /**
     * Create a new transaction without memo.
     * @param amount    the account transacted
     * @param memo      the memo for the transaction
     * @param inAccount the account the transaction belongs to
     */
    public Transaction(double amount, String memo, Account inAccount) {
        // Call two-args constructor first
        this(amount, inAccount);

        // Set the memo
        this.memo = memo;
    }

    /**
     * Get the amount of the transaction.
     * @return the amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Get a string summarizing the transaction
     * @return the summary string
     */
    public String getSummaryLine() {
        return String.format("%s\t:\t$%s\t:\t%s\n", this.timestamp.toString(),
                getAmountLine(), this.memo);
    }

    /**
     * Get a string of amount. Negative amount add in rounded parentheses.
     * @return string of amount
     */
    private String getAmountLine() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.amount);
        if (amount < 0) {
            builder.insert(0, "(").append(")");
        }
        return builder.toString();
    }
}
