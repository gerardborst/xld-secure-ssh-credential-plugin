package com.deployit.community.overthere;

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
import java.util.concurrent.TimeUnit;

import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import com.xebialabs.deployit.community.overthere.SecureSshCredentialHandler;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import net.schmizz.sshj.connection.channel.direct.Session;

public class GenKey {
    public static String localKey = "-----BEGIN RSA PRIVATE KEY-----\n" + 
            "MIIEowIBAAKCAQEA2oVW1RH5qkWawK2OXEEu1yX0wYwT+7QaNB+AtCamW+5aWybm\n" + 
            "e/HFUTMi85noukGvHrVM8lX9SG9TiTY0xV8cJSgIa8R21dc5v2XW+cXON2/BIMZq\n" + 
            "yDExME9weXHk1GGwfuCTJ4gMlAjMrZIZL7eDaIPr884g2/Yi+wJQ0H3oJysucgH5\n" + 
            "7GdCZRfDDgYBdcge4rLIFifVLccyI+k3rE7NIanEabQ5/ke6jwbdaDCxw0qv3Xdw\n" + 
            "CchPA7+o7aP1CoeUPVi0FprtV32Afn9Mqk8rZJr1Wa4yqPxc/jfXFlLDUWzdjZ20\n" + 
            "Ns5hDx+KxOB21Pe9N1zPht5ofPc8Z9Ot3MWg1QIDAQABAoIBAEMSsnhsMAoHm1yQ\n" + 
            "6SWADHSVfe76pBeaTIC7OrIROvb0zl80LYLVzw8BdzT2hOzGxI3UcG3h3wCgyy9O\n" + 
            "qBv8+ZKeGxeZEjDJAzywca/w+z8g2lLJFHWmQ8bkW02rm+VkptwK6qAzrkix+sE8\n" + 
            "Gub+mSM3evE2z/c0McCCEXQtcViz4ypqu/F+5uXmTpOLW/Og68piaoCCNqNJf5g/\n" + 
            "HkThfwUN9rJcPooYaHZLJtEpoi9ctUDsS4tQwtuZD8uoIlIn9S7NAz2EHE0gktEq\n" + 
            "snJ+UQiNL8PbdssKIjXh4cVV1e+O0tnwTNhGPt2fw0IGgqFzEfgkOJyGVnLf0rEG\n" + 
            "tuwpEoECgYEA8s+aHVnB/QXoY690N3mZabRPqbQEoCoVga6AF+l1yzJQ8EtyqebW\n" + 
            "RjFO9c9IRwlOxoXvtf7/d9SMdLgII1alniPNrqDSW92ZS1MFYs9d34EkjDopdidH\n" + 
            "OikEjYFFZ/j+ez1I0IXe3MXiDkmhRY0LTpq+qhMDbc9/7z9zbmhiXXkCgYEA5mP4\n" + 
            "6P2cdZ6LzzwIDBKEvoFvhzW9Tn+lO1sB6HqD2F084pIuAQT6aB1UKwrGrOPXgbrg\n" + 
            "ZZiZ7fwxyUzff09EEC3Gc70O8kf+X5osvy6RwRXuirT8EwNMnNnu7UA/9yWrY1+D\n" + 
            "E1SoKMLRdIR9L6cK+3mrUjA3YmfzvlvssdoYcz0CgYEA0/5DfSq3vvTgf+BZJHIr\n" + 
            "amm+P9lnTHtRyaaUOcxgRueIWTRWSVa8cA5u9Rhj73mF6LhZoNmhLHtLokBnAouN\n" + 
            "D/QMiVHqN8DTLTuv8SRBvnkfcrr5JiqG+pWDXxSlh3CSpwDLyRuIh9LRWk4Y2wOZ\n" + 
            "AqkCnLT7Am3vMfCBFL1YXGkCgYBr3SRdGNeWrbrxND7kQAtMEdeAayWNSe+3AV28\n" + 
            "VRfDRavKu/uz2HvQIOMWH75ylV2BTFfg2PhAVpGMmGQO+7J3DKdb6ojD3/b1FX6b\n" + 
            "p5bv2m/LGv7le+bzjIjzJOCNumIqbT413623H7bmoVzm4YMfx4/xM8fKV7bjSbBg\n" + 
            "eCZ4VQKBgG9Y48bW1wFg/392wpQc3o3FRXh7u4o4yzc5S+U1sNAdIlispzzBfPKY\n" + 
            "ui0/iRmYcEyNFAQNCuTRClUOwYqIT/apSoVD1m8NkQWBWmdcNpHyiOkeJ9W60HsP\n" + 
            "K6ufylhwYVs3UTejRwjeKP+L9NVTAiowoRW4btcJZAyMIer/N2X7\n" + 
            "-----END RSA PRIVATE KEY-----\n" + 
            "";
    
    public static String generatePKCS5String() throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return encodePrivateKey((RSAPrivateKey) keyPair.getPrivate());
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

    private static String encodePublicKey(RSAPublicKey rsaPublicKey, String user) throws IOException {
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
        return "ssh-rsa " + publicKeyEncoded + " " + user;
    }

    public static void main(String[] args) {
        try {
            String key = SecureSshCredentialHandler.generateKeys();
            System.out.print(key);
            System.out.println("\n");

            
            SSHClient client = new SSHClient();
            client.addHostKeyVerifier((HostKeyVerifier)new PromiscuousVerifier());
            client.connect("gerards-laptop", 2022);
            KeyProvider keys = client.loadKeys(localKey, null, null);
            KeyProvider[] providers = new KeyProvider[]{keys};
            System.out.print(encodePublicKey((RSAPublicKey) keys.getPublic(), "zomaar"));
            System.out.println();
            System.out.print(encodePrivateKey((RSAPrivateKey) keys.getPrivate()));
            System.out.println();
            client.authPublickey("root", providers);
            Session session = client.startSession();
            final Command cmd = session.exec("ls -al");
            System.out.print(IOUtils.readFully(cmd.getInputStream()).toString());
            cmd.join(5, TimeUnit.SECONDS);
            System.out.print("\n** exit status: " + cmd.getExitStatus());
            client.close();

            //System.out.print(generatePKCS5String());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
