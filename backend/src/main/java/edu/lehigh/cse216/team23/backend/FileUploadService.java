package edu.lehigh.cse216.team23.backend;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.Collections;

public class FileUploadService {

    private final Drive driveService;

    public FileUploadService(Drive driveService) {
        this.driveService = driveService;
    }

    public String uploadFile(java.io.File tempFile, String filename, String contentType, String folderId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(filename);
        if (folderId != null && !folderId.isEmpty()) {
            fileMetadata.setParents(Collections.singletonList(folderId));
        }

        FileContent mediaContent = new FileContent(contentType, tempFile);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        return file.getId();
    }

    public static Drive getDriveService() {
        try {
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("config/service-account-key.json"))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("The Buzz")
                    .build();
        } catch (Exception e) {
            System.err.println("Failed to create Drive service: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

