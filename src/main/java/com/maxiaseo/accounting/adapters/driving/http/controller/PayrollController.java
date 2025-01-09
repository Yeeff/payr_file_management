package com.maxiaseo.accounting.adapters.driving.http.controller;

import com.maxiaseo.accounting.adapters.driving.http.dto.FileResponseDto;
import com.maxiaseo.accounting.adapters.driving.http.mapper.ExcelMapper;
import com.maxiaseo.accounting.adapters.driving.http.mapper.IFileResponseMapper;
import com.maxiaseo.accounting.domain.api.IPayrollServicesPort;
import com.maxiaseo.accounting.domain.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.io.File;
import java.nio.file.Files;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor

@RequestMapping("/api/file")
public class PayrollController {

    private final IPayrollServicesPort payrollServices;
    private final ExcelMapper excelMapper;
    private final IFileResponseMapper fileResponseMapper;

    private File tempFile; // Store reference to the temp file


    @GetMapping("/processed-info")
    public ResponseEntity<List<Employee>> handleFileUpload(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("day") Integer day) throws IOException {

        List<Employee>  result = payrollServices.handleFileUpload(tempFile.getName() ,year, month, day);

        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<Void> uploadFile(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("day") Integer day,
            @RequestParam("file") MultipartFile file
    ) throws IOException  {

        payrollServices.saveFile(
                excelMapper.fileExcelToInputstream(file)
                ,year, month, day);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity< List<FileResponseDto> > getSavedFiles(){
        return ResponseEntity.ok().body(fileResponseMapper.toListDto(payrollServices.getFiles()));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
        payrollServices.deleteTemporaryFile(fileName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
    @GetMapping("/process-siigoformat")
    public ResponseEntity<String> processSiigoFormat() throws IOException {
        File auxFile;
        auxFile = payrollServices.processSiigoFormat(tempFile.getName(), 2024, 9, 1);
        return ResponseEntity.ok(auxFile.getName());
    }


}
