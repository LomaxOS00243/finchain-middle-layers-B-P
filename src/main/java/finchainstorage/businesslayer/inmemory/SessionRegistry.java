package finchainstorage.businesslayer.inmemory;

import finchainstorage.businesslayer.dto.EmployeeDTO;
import finchainstorage.businesslayer.exception.BusinessApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionRegistry {

    private static final HashMap<String, String> session_registry = new HashMap<>();

    //store user session once successfully logged in
    public String addSession(EmployeeDTO session_data) {//session data: id, name, password

        if (session_data == null) {
            throw new BusinessApiException("session data is null");
        }
        String session_id = generateSessionId();

        session_registry.put(session_id, session_data.getUsername());//set employee name as value

        return session_id;
    }

    //Generate a session id to be used for access token
    private String generateSessionId() {
        return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
    }

    //Check if the session id exists for front end requests after login
    public boolean checkSession(String session_id) {
        return session_registry.containsKey(session_id);
    }

    //Return the employee name for the login response
    public String getEmployeeNameBySessionId(String session_id) {
        return session_registry.get(session_id);
    }


    public void clear() {
        session_registry.clear();
    }

}
