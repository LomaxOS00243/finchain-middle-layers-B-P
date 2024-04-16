package finchainstorage.businesslayer.controller.data;


import finchainstorage.businesslayer.model.Employees;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins ="*", maxAge = 3500)
@RequestMapping("/api")
public class DocumentController {

    @PostMapping("/datamanager")
    public ResponseEntity<?> uploadDocument(@RequestBody Employees employee) {



        return null;
    }
}
