package de.hm.edu.carads.models.util;

import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Das Hashen von Passwoertern wird geprueft.
 * @author FS
 */
public class HashTest {
    
    private static final String PASSWORD = "bla";
    private static final String PASSWORDHASH = "4df3c3f68fcc83b27e9d42c90431a72499f17875c81a599b566c9889b9696703";
    
	
    @Test
    public void testGetShaHash() throws NoSuchAlgorithmException    {
            String passwordHash = Hasher.getShaHash(PASSWORD);
            assertEquals(PASSWORDHASH, passwordHash);
            
    }
    
}
