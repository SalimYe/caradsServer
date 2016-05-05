package de.hm.edu.carads.models.util;

import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author florian
 */
public class HelperTest {
    
    private static final String PASSWORD = "1*äA-_ ";
    private static final String PASSWORDHASH = "f4c1172748a9b346ebdbbb7476e65657915bf358e849eb3683b2afb248f1adfc";
    
    @Test
    public void testGetShaHash() throws NoSuchAlgorithmException    {
            String passwordHash = Helper.getShaHash(PASSWORD);
            assertEquals(PASSWORDHASH, passwordHash);
            
    }
    
}
