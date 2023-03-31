package com.xebialabs.deployit.community.overthere;

import com.xebialabs.deployit.plugin.api.udm.Metadata;
import com.xebialabs.deployit.plugin.api.udm.Property;
import com.xebialabs.overthere.OverthereConnection;

@SuppressWarnings("serial")
@Metadata(root = Metadata.ConfigurationItemRoot.INFRASTRUCTURE, description = "Machine that can be connected to using SSH")
public class SecureSshHost extends SecureRemoteHost {
    
    @Property
    private SecureSshCredential credential;
    

    @Property(required=true, defaultValue="NOTUSED", hidden=true)
    String username;
    @Property(password = true, required=true, defaultValue="NOTUSED", hidden=true)
    String password;
    @Property(required=false, hidden=true)
    String privateKeyFile;
    @Property(password = true, required=false, hidden=true)
    String privateKey;
    @Property(password = true, required=false, hidden=true)
    String passphrase;
    
    @Override
    public OverthereConnection getConnection() {
        this.username = credential.getUsername();
        this.privateKey = credential.getPrivateKey();
        this.passphrase = null;
        OverthereConnection conn = super.getConnection();
        return conn;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public SecureSshCredential getCredential() {
        return credential;
    }

    public void setCredential(SecureSshCredential credential) {
        this.credential = credential;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
    
    
}
