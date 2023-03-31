/**
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
 * FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
 */
package com.xebialabs.deployit.community.overthere;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.xebialabs.deployit.plugin.api.flow.ExecutionContext;
import com.xebialabs.deployit.plugin.api.flow.Step;
import com.xebialabs.deployit.plugin.api.flow.StepExitCode;
import com.xebialabs.deployit.plugin.api.udm.ControlTask;
import com.xebialabs.deployit.plugin.api.udm.Metadata;
import com.xebialabs.deployit.plugin.api.udm.Property;
import com.xebialabs.deployit.plugin.api.udm.TypeIcon;
import com.xebialabs.deployit.plugin.api.udm.base.BaseConfigurationItem;

@SuppressWarnings("serial")
@Metadata(root = Metadata.ConfigurationItemRoot.ENVIRONMENTS, description = "A overthere.SshCredential with generated private and public keys")
@TypeIcon(value="icons/types/overthere.SecureSshCredential.svg")
public class SecureSshCredential extends BaseConfigurationItem {
    public static final String defaultValue = "Not yet generated";
    
    @Property
    private String username;

    @Property(required = false, label = "Public Key (Value managed by the server. Use this value in the authorized_keys file)", category = "Managed by the Server", 
            defaultValue = defaultValue)
    private String publicKey;

    @Property(required = false, password = true, label = "Private Key (Value managed by the server)",
            category = "Managed by the Server")
    private String privateKey;
    
    public String getUsername() {
        return username;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Generates {@code ControlTask} to a new SSH key pair.
     * 
     * @return {@code List<Step> displaySteps}
     */
    @ControlTask(label="Generate new SSH Keys")
    public List<Step> displayPublicKey() {
        
        Step step = new Step(){

            public int getOrder() {
                return 1;
            }

            public String getDescription() {
                return "Generate and save new SSH Keys";
            }

            public StepExitCode execute(ExecutionContext ctx) throws NoSuchAlgorithmException, IOException {
                SecureSshCredential.this.setPrivateKey(SecureSshCredentialHandler.generateKeys());
                SecureSshCredential.this.setPublicKey(SecureSshCredentialHandler
                        .getPublicKey(SecureSshCredential.this));
                ctx.getRepository().update(SecureSshCredential.this);
                ctx.logOutput(SecureSshCredential.this.getPublicKey());
                return StepExitCode.SUCCESS;
            }
        };
        List<Step> stepList = new ArrayList<>();
        stepList.add(step);
        return stepList;
    }
    

}
