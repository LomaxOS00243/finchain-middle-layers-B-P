package finchainstorage.persistancelayer.gateway.chaincodeservices.implementation;

import finchainstorage.businesslayer.model.Employees;
import finchainstorage.persistancelayer.gateway.chaincodeservices.DataChaincodeService;
import finchainstorage.persistancelayer.gateway.chaincodeservices.EmployeeChaincodeService;
import finchainstorage.persistancelayer.gateway.exception.GatewayApiException;
import finchainstorage.persistancelayer.gateway.network.NetworkConnector;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * ChaincodeAgent is a class that is used to interact with the chaincode. It is used to submit transactions to the ledger.
 * It is used to submit a create employee account transaction to the ledger.
 * It is used to submit a login employee transaction to the ledger.
 */
@Repository
public class ChaincodeServiceImpl implements EmployeeChaincodeService, DataChaincodeService {
    private Contract getConnected(String employeeID) throws ContractException, TimeoutException, InterruptedException, IOException{
        String contractName = "finchaincode";
        Gateway gateway = NetworkConnector.Setup(employeeID).connect();
        Network network = gateway.getNetwork("insuranceservice1");
        return network.getContract(contractName);
    }
    @Override
    public Employees createEmployeeAccount(Employees employee) {

        try {
            Contract contract = getConnected(employee.getEmployeeId());

            byte[] response = contract.submitTransaction("createEmployeeAccount", employee.getEmployeeId(), employee.getName(), employee.getPassword(), employee.getRecoveryPhrase());

            return Employees.deserialize(response);

        } catch (ContractException | TimeoutException | InterruptedException | IOException e) {
            throw new GatewayApiException( "Failed to create employee account for " + employee.getEmployeeId(), e);
        }

    }
    @Override
    public void loginEmployee(Employees employee) {
        try {
            Contract contract = getConnected(employee.getEmployeeId());
            byte[] response = contract.submitTransaction("loginEmployee", employee.getEmployeeId(), employee.getPassword());
            String jsonString = new String(response, StandardCharsets.UTF_8);
            System.out.println(jsonString);
        } catch (ContractException | TimeoutException | InterruptedException | IOException e) {
            throw new GatewayApiException( "Failed to login to the account ", e);
        }
    }
    public boolean employeeExists(String employeeID) {
        boolean result = true;
        try {
            Contract contract = getConnected(employeeID);
            byte[] response = contract.submitTransaction("employeeExists", employeeID);
            String jsonString = new String(response, StandardCharsets.UTF_8);
            if(jsonString.equals("false")){
                result = false;
            }
        } catch (ContractException | TimeoutException | InterruptedException | IOException e) {
            throw new GatewayApiException( "Failed to connect or send employee existing request ", e);
        }
        return result;
    }
    //method to ulpoad data to S3: verifies the uploading operation and returns true if this operation must be performed
        //the upload verify the issuer of this transaction, in this case is employeeId
            //the upload verify the existance of this employee and the permission.
    //Method Upload: it takes a
    // method to download data to s3: verifies the downloading operation and returns true if operation must be performed

}