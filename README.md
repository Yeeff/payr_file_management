Employee Schedule File Validator

This microservice validates schedule files for employees before sending them to the payroll overtime and surcharge calculation service.

Purpose:

Ensure the incoming file meets the required format and data rules before processing.
Prevent errors in overtime and surcharge calculations caused by incorrect or incomplete schedules.

Features:

Accepts a predefined file format (e.g., CSV, XLSX) containing employee schedules.

Validates:

File type and structure.
Required columns (e.g., Employee ID, date, start time, end time).
Correct date and time formats.
No missing or duplicate entries.
Generates a report of errors if the file is invalid.

If valid, sends the file to the overtime & surcharge calculation microservice.

Tech stack:

Java 17
Spring Boot
Graddle
