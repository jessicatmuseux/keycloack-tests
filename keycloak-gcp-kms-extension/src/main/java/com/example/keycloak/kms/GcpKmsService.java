package com.example.keycloak.kms; 
import com.google.cloud.kms.v1.*; 
import com.google.protobuf.ByteString; 
import java.io.IOException; 
public class GcpKmsService { 
private final KeyManagementServiceClient client; 
private final String keyName; 
public GcpKmsService(String projectId, String locationId, String keyRingId, String keyId) throws IOException { 
this.client = KeyManagementServiceClient.create(); 
this.keyName = KeyManagementServiceClient.formatCryptoKeyName(projectId, locationId, keyRingId, keyId); 
} 
public byte[] encrypt(byte[] plaintext) throws IOException { 
EncryptResponse response = client.encrypt(keyName, ByteString.copyFrom(plaintext)); 
return response.getCiphertext().toByteArray(); 
} 
public byte[] decrypt(byte[] ciphertext) throws IOException { 
DecryptResponse response = client.decrypt(keyName, ByteString.copyFrom(ciphertext)); 
return response.getPlaintext().toByteArray(); 
} 
} 
