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
        private final EmployeeServicesImpl employeeAuthServing;

        private final SessionRegistry sessionRegistry;

        //private final InMemoryServices registeredEmployees;
        //private final  Map<String, EmployeeDTO> registeredEmployees = new ConcurrentHashMap<>();

        @Autowired
        public EmployeeController(EmployeeServicesImpl emplAuthServer, SessionRegistry sessionRegistry) {
                this.employeeAuthServing = emplAuthServer;
                this.sessionRegistry = sessionRegistry;
                //this.registeredEmployees = registeredEmpl;
        }
        @PostMapping("/register")
        public ResponseEntity<?> createEmployeeAccount(@RequestBody Employees employee) {

                //String eRegAccountResponse = emplAuthServer.createAccount(employee); //Return this transaction id from the network

                EmployeeDTO eReqDTO = DTOMapper.fromEmployee(employee);//Map employee to DTO

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("Response Code", HttpStatus.CREATED.value());
                response.put("status", HttpStatus.CREATED);
                response.put("message", "Employee account created successfully");
                response.put("Transaction ID", "eRegAccountResponse78676574646464644646");

                //registeredEmployees.put(employee.getEmployeeId(), eReqDTO);

                //Store registered employees in memory for authorisation
                //registeredEmployees.addEmployee(employee.getName(), eReqDTO);

                employeeAuthServing.addEmployee(employee.getEmployeeId(), eReqDTO);

                System.out.println("EmployeeDTO: " + eReqDTO.toString());

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody EmployeeDTOLogin employeeDtoLogin) {

            //Check if employee already exists in the registered employees data structure
                //EmployeeDTO eReqDto = registeredEmployees.get(employeeDtoLogin.getEmployeeId());



                //if (eReqDto == null) {
                        //return new ResponseEntity<>("Employee not found: You must to register first", HttpStatus.NOT_FOUND);
                        //throw new BusinessApiException("Employee not found: You must to register first");
                        //Redirect to the registration page
                //}

                EmployeeDTO eReqDto = employeeAuthServing.findEmployee(employeeDtoLogin);
                System.out.println("EmployeeDTO: " + eReqDto.toString());

                //emplAuthServer.verifyLoginTransaction(employeeLoginDto); //blockchain network verification

                String sessionId = sessionRegistry.addSession(eReqDto);

                Map<String, Object> response = new LinkedHashMap<>();
                response.put("Response Code", HttpStatus.OK.value());
                response.put("status", HttpStatus.OK);
                response.put("message", "Login successful");
                response.put("employee", eReqDto.getUsername());
                response.put("sessionId", sessionId);

                return new ResponseEntity<>(response, HttpStatus.OK);

        }

}
