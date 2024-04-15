package finchainstorage.businesslayer.model;

import lombok.Setter;
import org.json.JSONObject;

import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Employees {
    @Setter
    private String employeeId;
    @Setter
    private String name;

    @Setter
    private String password;

    private String recoveryPhrase;


    public Employees(String employeeId, String name, String password, String recoveryPhrase) {
        this.employeeId = employeeId;
        this.name = name;
        this.password = password;
        this.recoveryPhrase = recoveryPhrase;
    }

    public String getEmployeeId() {
        return employeeId;
    }


    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }


    public String getRecoveryPhrase() {
        return recoveryPhrase;
    }


    public static Employees deserialize(final byte[] employeeJSON) {
        return deserialize(new String(employeeJSON, UTF_8));
    }

    public static Employees deserialize(final String employeeJSON) {

        JSONObject json = new JSONObject(employeeJSON);
        Map<String, Object> eMap = json.toMap();
        final String id = (String) eMap.get("employeeId");
        final String name = (String) eMap.get("name");
        final String password = (String) eMap.get("password");
        final String phrase = (String) eMap.get("recoveryPhrase");

        return new Employees(id, name, password, phrase);
    }


    public String display() {
        return "{" +
                " employeeId:'" + employeeId + '\'' +
                " name:'" + name + '\'' +
                " password:'" + password + '\'' +
                " recoveryPhrase:'" + recoveryPhrase + '\'' +
                '}';
    }
}
