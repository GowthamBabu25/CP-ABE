/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abe;

/**
 *
 * @author Gowtham Babu
 */

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        // Load Dropbox API credentials from a properties file
        Properties prop = new Properties();
        // This should only be used for debugging purposes, not in production
        //System.setProperty("com.dropbox.disableStrictSSL", "true");

        try (FileInputStream input = new FileInputStream("C:\\Users\\gowth\\OneDrive\\Documents\\NetBeansProjects\\ABE\\src\\abe\\dropbox.properties")) {
            
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String appKey = prop.getProperty("APP_KEY");
        String appSecret = prop.getProperty("APP_SECRET");
        String accessToken = prop.getProperty("ACCESS_TOKEN");

        // Set up Dropbox API connection
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial")
                .withUserLocaleFrom(Locale.getDefault())
                .build();

        if (accessToken == null || accessToken.isEmpty()) {
            // If access token is not available, authorize the app
            authorizeApp(appKey, appSecret, config);
        } else {
            // If access token is available, upload a file
            uploadFile(accessToken, config);
        }
    }

    private static void authorizeApp(String appKey, String appSecret, DbxRequestConfig config) {
        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);

        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
        String authorizeUrl = webAuth.authorize(DbxWebAuth.newRequestBuilder().build());

        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");

        // Input the authorization code from the user
        String code = System.console().readLine("Enter the authorization code: ");

        try {
            DbxAuthFinish authFinish = webAuth.finishFromCode(code);
            String accessToken = authFinish.getAccessToken();

            System.out.println("Authorization complete. Access token: " + accessToken);

            // Save the access token to a properties file for future use
            Properties prop = new Properties();
            prop.setProperty("ACCESS_TOKEN", accessToken);

            try {
                prop.store(new java.io.FileOutputStream("dropbox.properties"), null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Upload a file after authorization
            uploadFile(accessToken, config);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadFile(String accessToken, DbxRequestConfig config) {
        try{
            // Specify the file path to upload
            DbxClientV2 client = new DbxClientV2(config, accessToken);
            String filePath = "C:\\Users\\gowth\\OneDrive\\Documents\\NetBeansProjects\\ABE\\src\\abe\\input.txt";
            FileInputStream fileInputStream = new FileInputStream(filePath);

            // Specify the destination path in Dropbox
            String destinationPath = "/uploaded_file.txt";

            // Upload the file
            FileMetadata metadata = client.files().uploadBuilder(destinationPath)
                    .uploadAndFinish(fileInputStream);

            System.out.println("File uploaded successfully. Metadata: " + metadata);
        } catch (UploadErrorException e) {
            e.printStackTrace();
        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }
    }
}

