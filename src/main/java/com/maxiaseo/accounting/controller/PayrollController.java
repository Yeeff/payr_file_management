package com.maxiaseo.accounting.controller;

import com.maxiaseo.accounting.domain.Employee;
import com.maxiaseo.accounting.services.PayrollServices;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class PayrollController {

    PayrollServices payrollServices;

    private File tempFile; // Store reference to the temp file


    public PayrollController(PayrollServices payrollServices) {
        this.payrollServices = payrollServices;
    }

    @GetMapping("/processed-info")
    public ResponseEntity<List<Employee>> handleFileUpload() throws IOException {

        List<Employee>  result = payrollServices.handleFileUpload(tempFile);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        tempFile = payrollServices.saveTemporaryFile(file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "File uploaded and stored temporarily.");
        response.put("name", tempFile.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete")
    public String deleteFile() {
        if (tempFile != null) {
            payrollServices.deleteTemporaryFile(tempFile);
            return "Temporary file deleted successfully.";
        }
        return "No temporary file to delete.";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        if (tempFile != null && tempFile.exists()) {
            byte[] fileContent = Files.readAllBytes(tempFile.toPath());

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", tempFile.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        }
        return ResponseEntity.notFound().build();
    }

}
