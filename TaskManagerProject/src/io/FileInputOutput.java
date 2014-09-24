package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileInputOutput {
    private String fileName;
    private String fileHash;
    
    /**
     * @return true iff there is a change in the file.
     */
    public boolean read() {
        if (fileHashMatchesPrevious())
            return false;
        
        
        return true;
    }

    /**
     * @return true for success.
     */
    public boolean write() {
        
        
        fileHash = computeHash(fileName);
        return true;
    }
    
    
    
    
    
    
    
    private boolean fileHashMatchesPrevious() {
        String currentHash = computeHash(fileName);
        return fileHash.equals(currentHash);
    }
    
    public static String computeHash(String nameOfFile) {
        try {
            MessageDigest md5er = generateMessageDigest(nameOfFile);

            if (md5er == null) {
                return null;
            }
            
            byte[] digest = md5er.digest();
            
            if (digest == null) {
                return null;
            }
            
            StringBuilder result = new StringBuilder();
            
            for (int i = 0; i < digest.length; i++) {
                result.append(byteToHex(digest[i]));
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return null;
        }
    }

    private static String byteToHex(byte b) {
        return Integer.toString((b & 0xff) + 0x100, 16).substring(1);
    }

    private static MessageDigest generateMessageDigest(String nameOfFile) {
        MessageDigest md5er;
        
        try {
            InputStream fin = new FileInputStream(nameOfFile);
            md5er = MessageDigest.getInstance("MD5");
            
            byte[] buffer = new byte[1024];
            
            int read = 0;
            while (read != -1) {
                read = fin.read(buffer);
                if (read > 0) {
                    md5er.update(buffer, 0, read);
                }
            }
            
            fin.close();
            
        } catch (IOException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return md5er;
    }
}
