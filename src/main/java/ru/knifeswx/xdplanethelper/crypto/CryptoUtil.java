package ru.knifeswx.xdplanethelper.crypto;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptoUtil {
    
    private static final String PUBLIC_KEY_BASE64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvDBBbTDpoR3cArnwczeVHGeEtfrvQlX+AApyatD/K+S4SZiIT5aDcIp7P/8mdfK7ZrtpuH+mosVm9lOh+pvNrWTbGp6kEzQmF7TiLIrK/rhj17SUEwCMy4jQFSDiIvMioRmYSWb9HG3oSSN6hURp/TW39WzS89tysN+hPxomcPdl3qXql5KIqMFapASJ0uTOJYTS2ZU9QzT/kUlUzi5r+bHKnzrCDSrIGHm/21oTjH/yXjyiQVUWsJuFdDF8cB6W8Y5pSwhpcwQN1kn8TaggImMSxiVgRkXAQPg9si32R+4jEwKm1yEV3LqT5YRxprSeMdKj62w8Witdih8Tik6otwIDAQAB";
    
    private static PublicKey publicKey;
    
    static {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY_BASE64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }
    
    public static byte[] encrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}
