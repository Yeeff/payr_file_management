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


    @GetMapping("/processed-info/{fileName}")
    public ResponseEntity<List<Employee>> handleFileUpload(
            @PathVariable String fileName) throws IOException {

        List<Employee>  result = payrollServices.handleFileUpload(fileName);

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

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) throws IOException {

        byte[] fileContent = payrollServices.getTempFile(fileName);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @GetMapping("/process-siigoformat")
    public ResponseEntity<String> processSiigoFormat() throws IOException {
        File auxFile;
        auxFile = payrollServices.processSiigoFormat(tempFile.getName());
        return ResponseEntity.ok(auxFile.getName());
    }

    @GetMapping("/download-siigo/{fileName}")
    public ResponseEntity<byte[]> downloadSiigoFile(@PathVariable String fileName) throws IOException {

        File siigoFile = payrollServices.processSiigoFormat(fileName);

        byte[] fileContent = payrollServices.getTempFile(siigoFile.getName());

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

}
