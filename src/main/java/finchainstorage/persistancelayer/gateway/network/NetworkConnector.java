package finchainstorage.persistancelayer.gateway.network;


import finchainstorage.persistancelayer.gateway.identity.EmployeeIdentities;
import org.hyperledger.fabric.gateway.Gateway;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NetworkConnector {

    public static Gateway.Builder Setup(String employeeID) throws IOException {

        Gateway.Builder builder = Gateway.createBuilder();

        EmployeeIdentities.storeIdentity(employeeID);

        //Select this request identity
        EmployeeIdentities ids = EmployeeIdentities.getIdentity(employeeID);

        //Get the Gateway connection profile
        Path connectionProfile = Paths.get("connection", "ConnectionProfile.yaml");

        // Set connection options on the gateway builder
        builder.identity(ids.getwallet(), ids.getEmployeeID()).networkConfig(connectionProfile).discovery(false);

        //Return the identity and connection profile to the gateway
        return builder;

    }

}
