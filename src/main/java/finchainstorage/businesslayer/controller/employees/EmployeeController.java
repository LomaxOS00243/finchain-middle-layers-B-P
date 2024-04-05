package finchainstorage.businesslayer.controller.employees;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.message.HttpResponse;
import finchainstorage.businesslayer.services.EmployeeService;
import finchainstorage.businesslayer.services.implementaion.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static java.time.LocalTime.now;


@RestController
//@CrossOrigin(origins ="http://localhost:8081")
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

        private final EmployeeService employeeService = new EmployeeServiceImpl();

        @PostMapping("/register")
        public ResponseEntity<String> createEmployeeAccount(@RequestBody Employees employee) {

                //EmployeeDTO employee = employeeService.createAccount(employee);

                HttpResponse response = new HttpResponse(HttpStatus.CREATED,
                        "Employee account created successfully", Map.of("employee", employee.toString()));

                return ResponseEntity.accepted().body(response.toString());
        }
        /*
        @PostMapping("/loginEmployee")
        public void loginEmployee(@RequestBody Employees employee) {
            employeeService.loginEmployee(employee);
        }*/
        /*public String generateEmployeePassword(String id) {
            Random random = new Random();
            StringBuilder password = new StringBuilder(15);
            String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lowerCaseLetters = upperCaseLetters.toLowerCase();
            String numbers = "0123456789";
            String specialCharacters = "!@#$%&*?£";
            String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

            int firstChars = id.length();
            password.append(id);

            for (int i = firstChars; i < 15; i++) {
                password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
            }
            return password.toString();
        }*/
}
