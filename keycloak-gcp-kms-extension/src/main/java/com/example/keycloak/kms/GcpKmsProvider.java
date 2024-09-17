package com.example.keycloak.kms; 
import org.keycloak.vault.VaultProvider; 
import org.keycloak.vault.VaultRawSecret; 
import java.io.IOException; 
public class GcpKmsProvider implements VaultProvider { 
private final GcpKmsService kmsService; 
public GcpKmsProvider(GcpKmsService kmsService) { 
this.kmsService = kmsService; 
} 
@Override 
public VaultRawSecret obtainSecret(String vaultSecretId) { 
try { 
byte[] encryptedSecret = kmsService.encrypt(vaultSecretId.getBytes()); 
return new VaultRawSecret() { 
@Override 
public byte[] get() { 
return encryptedSecret; 
} 
@Override 
public void close() {} 
}; 
} catch (IOException e) { 
throw new RuntimeException("Failed to encrypt secret", e); 
} 
} 
@Override 
public void close() {} 
} 
