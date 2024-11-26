package edu.lehigh.cse216.team23.backend;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;

public class FileUploadService {

    private final Drive driveService;

    /**
     * Constructor for FileUploadService
     * 
     * @param driveService The Google Drive service instance
     */
    public FileUploadService(Drive driveService) {
        this.driveService = driveService;
    }

    /**
     * Uploads a file to Google Drive
     * 
     * @param fileName    The name of the file to upload
     * @param mimeType    The MIME type of the file
     * @param fileContent The content of the file as a byte array
     * @return The public URL of the uploaded file
     * @throws Exception if the upload fails
     */
    public String uploadFile(String fileName, String mimeType, byte[] fileContent) throws Exception {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setMimeType(mimeType);

        // Make the file publicly accessible
        fileMetadata.setParents(Collections.singletonList("appFolder"));

        InputStream fileStream = new ByteArrayInputStream(fileContent);

        File uploadedFile = driveService.files().create(fileMetadata, new com.google.api.client.http.InputStreamContent(mimeType, fileStream))
                .setFields("id,webViewLink")
                .execute();

        // Return the file's public link
        return uploadedFile.getWebViewLink();
    }
}
