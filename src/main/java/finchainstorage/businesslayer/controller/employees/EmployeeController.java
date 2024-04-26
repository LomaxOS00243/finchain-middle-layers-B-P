package finchainstorage.businesslayer.controller.employees;

import finchainstorage.businesslayer.dto.EmployeeDTOLogin;
import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.dto.DTOMapper;
import finchainstorage.businesslayer.inmemory.SessionRegistry;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.services.implementaion.EmployeeServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins ="*", maxAge = 3500)
@RequestMapping("/api")
public class EmployeeController {
        private final EmployeeServicesImpl employeeAuthService;

        private final SessionRegistry sessionRegistry;
        @Autowired
        public EmployeeController(EmployeeServicesImpl emplAuthServer, SessionRegistry sessionRegistry) {
                this.employeeAuthService = emplAuthServer;
                this.sessionRegistry = sessionRegistry;
        }
        @PostMapping("/register")
        public ResponseEntity<?> createEmployeeAccount(@RequestBody Employees employee) {


                //Issue blockchain transaction, which returns this transaction id
                String eRegAccountResponse = employeeAuthService.createAccount(employee);

                //Map Employees class to EmployeeDTO
                EmployeeDTO eDTO = DTOMapper.fromEmployee(employee);

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("Response", HttpStatus.CREATED.value());
                response.put("status", HttpStatus.CREATED);
                response.put("TransactionID", eRegAccountResponse);

                //Store registered employees in memory for authentication
                employeeAuthService.addEmployee(employee.getEmployeeId(), eDTO);

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody EmployeeDTOLogin employeeDtoLogin) {

                //Find employee in memory for authentication
                EmployeeDTO eDTO = employeeAuthService.findEmployee(employeeDtoLogin);

                //Issue blockchain transaction to verify login credentials
                employeeAuthService.verifyLoginTransaction(employeeDtoLogin);

                //Create a session ID for the authenticated employee
                String sessionId = sessionRegistry.addSession(eDTO);

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("Response", HttpStatus.OK.value());
                response.put("status", HttpStatus.OK);
                response.put("employee", eDTO.getUsername());
                response.put("sessionId", sessionId);

                return new ResponseEntity<>(response, HttpStatus.OK);

        }

}
