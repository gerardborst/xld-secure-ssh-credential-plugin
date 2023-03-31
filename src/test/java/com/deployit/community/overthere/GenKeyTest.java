package com.deployit.community.overthere;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import com.xebialabs.deployit.community.overthere.SecureSshCredentialHandler;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.jupiter.api.Test;


public class GenKeyTest {

    @Test
    public void generateKeysTest()
            throws IOException, GeneralSecurityException {
        
        String pem = SecureSshCredentialHandler.generateKeys();
        assertNotNull(pem);
        SSHClient client = new SSHClient();
        KeyProvider keys = client.loadKeys(pem, null, null);
        RSAPublicKey pubkey = (RSAPublicKey) keys.getPublic();
        RSAPrivateKey privkey = (RSAPrivateKey) keys.getPrivate();
        assertNotNull(pubkey);
        assertNotNull(privkey);
        client.close();
    }
}
