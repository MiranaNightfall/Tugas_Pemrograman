package assignments.assignment3.payment;

public class DebitPayment implements DepeFoodPaymentSystem {
    private static final double MINIMUM_TOTAL_PRICE = 50000.0;
    private long saldo;

    public DebitPayment(long saldo) {
        this.saldo = saldo;
    }

    @Override
    public long processPayment(long amount) throws Exception {
        if (amount < MINIMUM_TOTAL_PRICE) {
            throw new Exception("Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain");
        }
        if (amount > saldo) {
            throw new Exception("Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain");
        }
        saldo -= amount;
        return amount;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }
}