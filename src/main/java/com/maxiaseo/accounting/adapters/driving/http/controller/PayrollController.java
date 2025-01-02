package com.maxiaseo.accounting.adapters.driving.http.controller;

import com.maxiaseo.accounting.adapters.driving.http.mapper.ExcelMapper;
import com.maxiaseo.accounting.domain.api.IPayrollServicesPort;
import com.maxiaseo.accounting.domain.model.Employee;
import com.maxiaseo.accounting.domain.api.usecase.PayrollServices;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.startup.PasswdUserDatabase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class PayrollController {

    private final IPayrollServicesPort payrollServices;
    private final ExcelMapper excelMapper;

    private File tempFile; // Store reference to the temp file


    @GetMapping("/processed-info")
    public ResponseEntity<List<Employee>> handleFileUpload(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("day") Integer day) throws IOException {

        List<Employee>  result = payrollServices.handleFileUpload(tempFile.getName() ,year, month, day);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("day") Integer day,
            @RequestParam("file") MultipartFile file
    ) throws IOException  {

        tempFile = payrollServices.saveFile(
                excelMapper.fileExcelToInputstream(file)
                ,year, month, day);

        Map<String, String> response = new HashMap<>();
        response.put("message", "El archivo se subio satisfactoriamente");
        response.put("name", tempFile.getName());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
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

            payrollServices.deleteTemporaryFile(tempFile);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/process-siigoformat")
    public ResponseEntity<String> processSiigoFormat() throws IOException {
        File auxFile;
        auxFile = payrollServices.processSiigoFormat(tempFile.getName(), 2024, 9, 1);
        return ResponseEntity.ok(auxFile.getName());
    }

}
