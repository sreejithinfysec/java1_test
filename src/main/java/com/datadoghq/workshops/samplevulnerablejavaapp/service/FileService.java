package com.datadoghq.workshops.samplevulnerablejavaapp.service;

import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileForbiddenFileException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileReadException;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileService {
    final static String ALLOWED_PREFIX = "/tmp/files/";

public String readFile(String path) throws FileForbiddenFileException, FileReadException {
    // Check if the path starts with the allowed prefix
    if(!path.startsWith(ALLOWED_PREFIX)) {
        throw new FileForbiddenFileException("You are not allowed to read " + path);
    }
    
    // Normalize the path to remove any relative path elements
    Path normalizedPath = Paths.get(path).normalize();
    
    // Check if the normalized path still starts with the allowed prefix
    if(!normalizedPath.startsWith(ALLOWED_PREFIX)) {
        throw new FileForbiddenFileException("You are not allowed to read " + path);
    }
    
    // Continue with the file reading logic
    try (BufferedReader br = new BufferedReader(new FileReader(normalizedPath.toString()))) {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        return sb.toString();
    } catch (IOException e) {
        throw new FileReadException(e.getMessage());
    }
}

    }
}
