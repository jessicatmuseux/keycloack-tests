package com.example.keycloak.kms; 
import org.keycloak.Config.Scope; 
import org.keycloak.models.KeycloakSession; 
import org.keycloak.models.KeycloakSessionFactory; 
import org.keycloak.vault.VaultProvider; 
import org.keycloak.vault.VaultProviderFactory; 
import java.io.IOException; 
public class GcpKmsProviderFactory implements VaultProviderFactory { 
@Override 
public VaultProvider create(KeycloakSession session) { 
Scope config = session.getContext().getRealm().getConfig(); 
String projectId = config.get("projectId"); 
String locationId = config.get("locationId"); 
String keyRingId = config.get("keyRingId"); 
String keyId = config.get("keyId"); 
try { 
GcpKmsService kmsService = new GcpKmsService(projectId, locationId, keyRingId, keyId); 
return new GcpKmsProvider(kmsService); 
} catch (IOException e) { 
throw new RuntimeException("Failed to create GCP KMS service", e); 
} 
} 
@Override 
public void init(Scope config) {} 
@Override 
public void postInit(KeycloakSessionFactory factory) {} 
@Override 
public void close() {} 
@Override 
public String getId() { 
return "gcp-kms"; 
} 
} 
