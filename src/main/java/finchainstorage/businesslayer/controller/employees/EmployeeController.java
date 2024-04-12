package finchainstorage.businesslayer.controller.employees;

import finchainstorage.businesslayer.dto.EmployeeDTOLogin;
import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.dto.DTOMapper;
import finchainstorage.businesslayer.inmemory.InMemoryServices;
import finchainstorage.businesslayer.inmemory.SessionRegistry;
import finchainstorage.businesslayer.model.Employees;
import finchainstorage.businesslayer.services.EmployeeAuthenticationService;
import finchainstorage.businesslayer.services.implementaion.EmployeeAuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins ="*", maxAge = 3500)
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {
        private final EmployeeAuthenticationService emplAuthServer = new EmployeeAuthenticationServiceImpl();

        @PostMapping("/register")
        public ResponseEntity<?> createEmployeeAccount(@RequestBody Employees employee) {

                //String eRegAccountResponse = emplAuthServer.createAccount(employee); //Return this transaction id

                EmployeeDTO eReqDTO = DTOMapper.fromEmployee(employee);//Map employee to DTO

                Map<String, Object> response = Map.of(
                        "Response Code", HttpStatus.CREATED.value(),
                        "status", HttpStatus.CREATED,
                        "message", "Employee account created successfully",
                        "Transaction ID", "eRegAccountResponse78676574646464644646"
                );

                //Store registered employees in memory for authentication
                InMemoryServices.addEmployee(employee.getName(), eReqDTO);

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody EmployeeDTOLogin employeeDtoLogin) {

            //get login body and check if the employee exists in the memory
                EmployeeDTO eReqDto = emplAuthServer.findEmployee(employeeDtoLogin);

                //if yes, forward the request to the blockchain network for verification
                        //blockchain checks: permission and password validation
                //emplAuthServer.verifyLoginTransaction(employeeLoginDto);
                        //redirect to the registration page
                //take employee name and password for authenticationManagement



                String sessionId = SessionRegistry.addSession(eReqDto);

                Map<String, Object> response = Map.of(
                        "Response Code", HttpStatus.OK.value(),
                        "status", HttpStatus.OK,
                        "message", "Login successful",
                        "employee", eReqDto.getUsername(),
                        "sessionId", sessionId
                );

                return new ResponseEntity<>(response, HttpStatus.OK);
                // if the response is true, return the employee details and the token
                        //return response of the employee, token, and the status
        }

}
