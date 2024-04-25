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
        Gateway gateway;

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

    //Construct the gateway object to connect to the network
    private static Gateway.Builder setup(String employeeID) throws IOException {

        Gateway.Builder builder = Gateway.createBuilder();

        //Store the identity if it does not exist
        EmployeeIdentities.storeIdentity(employeeID);

        //Retrieve the identity from the wallet
        EmployeeIdentities ids = EmployeeIdentities.getIdentity(employeeID);

        //Path to the connection profile
        Path connectionProfile = Paths.get("connection", "ConnectionProfile.yaml");

        builder.identity(ids.getWallet(), ids.getEmployeeID()).networkConfig(connectionProfile).discovery(true);

        return builder;
    }


}
