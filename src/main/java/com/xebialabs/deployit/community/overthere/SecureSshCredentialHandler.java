/**
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
 * FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
 */
package com.xebialabs.deployit.community.overthere;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;


import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.*;

public class SecureSshCredentialHandler {

    public static String generateKeys() throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return encodePrivateKey((RSAPrivateKey) keyPair.getPrivate());
    }
    
    public static String getPublicKey(SecureSshCredential cred) {
        String publicKey = null;
        if (cred.getPrivateKey() == null)
            return null;
        @SuppressWarnings("resource")
        SSHClient client = new SSHClient();
        KeyProvider keys;
        try {
            keys = client.loadKeys(cred.getPrivateKey(), null, null);
            publicKey = encodePublicKey((RSAPublicKey) keys.getPublic(), cred);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private static String getPubKeyUser(SecureSshCredential cred) {
        String[] dirs = cred.getId().split("/");
        String domain = cred.getUsername() + "@xld";
        return domain;
    }
    
    private static String encodePrivateKey(RSAPrivateKey rsaPrivateKey) throws IOException {
        final StringWriter writer = new StringWriter();
        final JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(writer);

        final JcaMiscPEMGenerator pemGenerator = new JcaMiscPEMGenerator(rsaPrivateKey);
        jcaPEMWriter.writeObject(pemGenerator);

        jcaPEMWriter.flush();
        jcaPEMWriter.close();
        return writer.toString();
    }

    private static String encodePublicKey(RSAPublicKey rsaPublicKey, SecureSshCredential cred) throws IOException {
        String publicKeyEncoded;

        ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteOs);
        dos.writeInt("ssh-rsa".getBytes().length);
        dos.write("ssh-rsa".getBytes());
        dos.writeInt(rsaPublicKey.getPublicExponent().toByteArray().length);
        dos.write(rsaPublicKey.getPublicExponent().toByteArray());
        dos.writeInt(rsaPublicKey.getModulus().toByteArray().length);
        dos.write(rsaPublicKey.getModulus().toByteArray());
        publicKeyEncoded = new String(Base64.getEncoder().encodeToString(byteOs.toByteArray()));
        return "ssh-rsa " + publicKeyEncoded + " " + getPubKeyUser(cred);
    }
    
}
