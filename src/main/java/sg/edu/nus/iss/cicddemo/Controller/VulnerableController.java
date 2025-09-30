package sg.edu.nus.iss.cicddemo.Controller;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.logging.Logger;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Demo controller with intentional security vulnerabilities for SonarCloud testing
 * WARNING: This code contains security issues for educational purposes only!
 */
@RestController
@RequestMapping("/demo")
public class VulnerableController {
    
    private static final Logger logger = Logger.getLogger(VulnerableController.class.getName());
    
    // Security Hotspot: Hard-coded credentials
    private static final String DB_PASSWORD = "admin123"; // NOSONAR - Intentional for demo
    private static final String API_KEY = "sk-1234567890abcdef"; // NOSONAR - Intentional for demo
    
    /**
     * Vulnerability: SQL Injection
     * This method is vulnerable to SQL injection attacks
     */
    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable String id) {
        try {
            // Vulnerable SQL query - concatenating user input directly
            String sql = "SELECT * FROM users WHERE id = " + id;
            logger.info("Executing SQL: " + sql);
            return "User data for ID: " + id;
        } catch (Exception e) {
            // Security Hotspot: Information disclosure in error messages
            return "Database error: " + e.getMessage();
        }
    }
    
    /**
     * Vulnerability: Path Traversal
     * This method allows directory traversal attacks
     */
    @GetMapping("/file")
    public String readFile(@RequestParam String fileName) {
        try {
            // Vulnerable file access - no input validation
            Path filePath = Paths.get("/app/files/" + fileName);
            String content = Files.readString(filePath);
            return content;
        } catch (IOException e) {
            return "Error reading file: " + fileName;
        }
    }
    
    /**
     * Security Hotspot: Weak cryptographic algorithm
     * Using MD5 which is cryptographically broken
     */
    @PostMapping("/hash")
    public String hashPassword(@RequestBody String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // NOSONAR - Intentional for demo
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "Hashing failed";
        }
    }
    
    /**
     * Security Hotspot: Command injection potential
     * This method could be vulnerable to command injection
     */
    @GetMapping("/system")
    public String executeCommand(@RequestParam String command) {
        try {
            // Potentially dangerous - executing user input as system command
            Process process = Runtime.getRuntime().exec("echo " + command); // NOSONAR - Intentional for demo
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        } catch (IOException e) {
            return "Command execution failed";
        }
    }
    
    /**
     * Security Hotspot: Information disclosure
     * This method exposes sensitive system information
     */
    @GetMapping("/info")
    public String getSystemInfo(HttpServletRequest request) {
        StringBuilder info = new StringBuilder();
        info.append("System Properties:\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("OS Name: ").append(System.getProperty("os.name")).append("\n");
        info.append("User Home: ").append(System.getProperty("user.home")).append("\n");
        info.append("User Name: ").append(System.getProperty("user.name")).append("\n");
        
        // Security issue: Exposing client information
        info.append("Client IP: ").append(request.getRemoteAddr()).append("\n");
        info.append("User Agent: ").append(request.getHeader("User-Agent")).append("\n");
        
        return info.toString();
    }
    
    /**
     * Code Smell: Empty catch block
     */
    @GetMapping("/risky")
    public String riskyOperation() {
        try {
            // Simulating risky operation
            int result = 10 / 0;
            return "Result: " + result;
        } catch (Exception e) {
            // Empty catch block - code smell
        }
        return "Operation completed";
    }
    
    /**
     * Security Hotspot: Hard-coded credentials usage
     */
    @GetMapping("/config")
    public String getConfig() {
        // Using hard-coded credentials - security hotspot
        return "Database password length: " + DB_PASSWORD.length() + 
               ", API key starts with: " + API_KEY.substring(0, 5);
    }
}
