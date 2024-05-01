package assignments.assignment3.payment;

public interface DepeFoodPaymentSystem {
    /*
     * Metode memproses pembayaran sesuai dengan metode pembayaran yang digunakan.
     *
     * @param amount Jumlah pembayaran yang harus dibayar.
     * @return Jumlah pembayaran yang berhasil diproses.
     * @throws Exception Jika terjadi kesalahan dalam proses pembayaran. 
     */
    long processPayment(long amount) throws Exception;
}