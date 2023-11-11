package edu.cs.student.studentassignment.student;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/storeData")
    public ResponseEntity<Map<String, String>> storeData(@RequestBody Map<String, String> formData) {
        try {

            System.out.println(formData.size());
            System.out.println(formData);
            studentService.storeData(formData);
            // You can return a response message or status if needed
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Data stored successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions or validation errors
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error storing data: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, String>>> search(@RequestParam String keyword, @RequestParam String searchBy) {
        try {
            System.out.println(keyword + " " + searchBy);
            // Assuming 'searchBy' is either "GPA" or "FirstName"
            List<Map<String, String>> searchResults = studentService.search(keyword, searchBy);
            System.out.println(searchResults);
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            // Handle exceptions or validation errors
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteStudent(@RequestParam String studentId) {

        studentService.delete(studentId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
