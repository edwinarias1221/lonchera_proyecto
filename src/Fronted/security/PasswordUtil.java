
package Fronted.security;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Utilidad para crear/validar hashes de contraseña con PBKDF2-HMAC-SHA256. */
public class PasswordUtil {
    private static final int ITER = 65536;  // iteraciones
    private static final int KEYLEN = 256;  // bits
    private static final int SALT_BYTES = 16;

    /** Devuelve "iteraciones:saltBase64:hashBase64" */
    public static String hash(String plain) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(plain.toCharArray(), salt, ITER, KEYLEN);
        return ITER + ":" +
               Base64.getEncoder().encodeToString(salt) + ":" +
               Base64.getEncoder().encodeToString(hash);
    }

    /** Verifica una contraseña contra el formato almacenado. */
    public static boolean verify(String plain, String stored) {
        try {
            String[] p = stored.split(":");
            int it = Integer.parseInt(p[0]);
            byte[] salt = Base64.getDecoder().decode(p[1]);
            byte[] expected = Base64.getDecoder().decode(p[2]);
            byte[] test = pbkdf2(plain.toCharArray(), salt, it, expected.length * 8);

            int diff = 0; // comparación constante
            for (int i = 0; i < expected.length; i++) diff |= expected[i] ^ test[i];
            return diff == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] pwd, byte[] salt, int iter, int keyLenBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pwd, salt, iter, keyLenBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
