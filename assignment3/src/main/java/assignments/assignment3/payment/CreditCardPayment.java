package assignments.assignment3.payment;

public class CreditCardPayment implements DepeFoodPaymentSystem {
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.02; // 2%

    @Override
    public long processPayment(long amount) throws Exception {
        long transactionFee = countTransactionFee(amount);
        long totalPayment = amount + transactionFee;
        return totalPayment;
    }

    private long countTransactionFee(long amount) {
        return (long) (amount * TRANSACTION_FEE_PERCENTAGE);
    }
}