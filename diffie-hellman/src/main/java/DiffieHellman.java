import java.math.BigInteger;
import java.util.Random;


public class DiffieHellman {
    private final Random rnd = new Random();
    public DiffieHellman() { }

    public BigInteger privateKey(BigInteger prime) {
        // Random algorithm https://stackoverflow.com/a/2290089
        BigInteger r;
        do {
            r = new BigInteger(prime.bitLength(), rnd);
        } while (r.compareTo(prime) >= 0 || r.compareTo(BigInteger.ONE) <= 0);
        return r;
    }

    public BigInteger publicKey(BigInteger primeA, BigInteger primeB, BigInteger privateKey) {
        return primeB.modPow(privateKey, primeA);
    }

    public BigInteger secret(BigInteger prime, BigInteger publicKey, BigInteger privateKey) {
        return publicKey.modPow(privateKey, prime);
    }
}