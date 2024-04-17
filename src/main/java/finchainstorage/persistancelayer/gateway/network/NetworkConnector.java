package finchainstorage.persistancelayer.gateway.network;


import finchainstorage.persistancelayer.gateway.exception.GatewayApiException;
import finchainstorage.persistancelayer.gateway.identity.EmployeeIdentities;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class NetworkConnector {

    public static Contract getConnected(String employeeId) {
        String contractName = "finchaincode";
        Gateway gateway = null;

        try {
            gateway = setup(employeeId).connect();
        } catch (IOException e) {
            throw new GatewayApiException( "Failed to connect to the network ", e);
        }

        if (gateway == null) {
            throw new GatewayApiException( "The network is unreachable", new NullPointerException("The gateway return null"));
        }

        Network network = gateway.getNetwork("insuranceservice1");

        return network.getContract(contractName);
    }
    private static Gateway.Builder setup(String employeeID) throws IOException {

        Gateway.Builder builder = Gateway.createBuilder();

        EmployeeIdentities.storeIdentity(employeeID); //Check if the identity is already stored

        EmployeeIdentities ids = EmployeeIdentities.getIdentity(employeeID);

        Path connectionProfile = Paths.get("connection", "ConnectionProfile.yaml");

        builder.identity(ids.getWallet(), ids.getEmployeeID()).networkConfig(connectionProfile).discovery(true);

        return builder;
    }


}
