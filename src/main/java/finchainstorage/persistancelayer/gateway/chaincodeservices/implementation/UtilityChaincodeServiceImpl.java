package finchainstorage.persistancelayer.gateway.chaincodeservices.implementation;

import finchainstorage.businesslayer.model.Employees;
import finchainstorage.persistancelayer.gateway.chaincodeservices.UtilityChaincodeService;
import finchainstorage.persistancelayer.gateway.exception.GatewayApiException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Repository
public class UtilityChaincodeServiceImpl implements UtilityChaincodeService {
    @Override
    public void verifyDuplication(String employeeId, Contract contract) {

        try {
            contract.evaluateTransaction("checkIfAlreadyExists", employeeId);
        } catch (ContractException e) {
            if (e.getMessage().contains("EMPLOYEE_ALREADY_EXISTS")) {
                throw new GatewayApiException("Error - "+ e.getMessage());
            } else {
                throw new GatewayApiException("Duplicate Verification - Blockchain contract error. ", e);
            }
        } catch (Exception e) {
            throw new GatewayApiException("Duplicate Verification - Unexpected error occurred. ", e);
        }
    }
    @Override
    public void verifyAccessPermission(Contract contract) {
        try {
            contract.evaluateTransaction("unauthorisedIfNoPermits");
        } catch (ContractException e) {
            if (e.getMessage().contains("NO_AUTHORISED_REQUEST")) {
                throw new GatewayApiException("Error: "+ e.getMessage());
            } else {
                throw new GatewayApiException("Permission Verification - Blockchain contract error. ", e);
            }
        } catch (Exception e) {
            throw new GatewayApiException("Permission Verification - Unexpected error occurred.", e);
        }
    }

    @Override
    public void eventsListener(Contract contract) {
        contract.addContractListener(contractEvent -> {

            var txBlock = contractEvent.getTransactionEvent();

            Employees employee = Employees.deserialize(contractEvent.getPayload().toString());

            System.out.println("======================= Event ========================");
            System.out.println("Transaction event: " + contractEvent.getName() + "-" + employee.display());
            System.out.println("Transaction ID: " + txBlock.getTransactionID()+" - Status: "+txBlock.isValid());
            System.out.println("Block No: " + txBlock.getBlockEvent().getBlockNumber());
            System.out.println("=================== We did it! =================");
        });
    }

    //Hash function to digest into a hash value
    public static String generateHashValue(String ValueToDigest) {
        byte[] convertValueAsByte = ValueToDigest.getBytes();
        String digestedInHex;
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] digest = messageDigest.digest(convertValueAsByte);
            //Convert bitwise AND operation into a hexadecimal string
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            digestedInHex = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new GatewayApiException("Hash Value Generation - Wrong Algorithm.", e);
        }
        return digestedInHex;
    }

    public void verifyPassword(Contract contract, String employeeId, String password) {
        try {
            contract.evaluateTransaction("checkIfPasswordIsValid", employeeId, password);
        } catch (ContractException e) {
            if (e.getMessage().contains("INVALID_EMPLOYEE_PASSWORD")) {
                throw new GatewayApiException("Error: "+ e.getMessage());
            } else {
                throw new GatewayApiException("Password Verification - Blockchain contract error. ", e);
            }
        } catch (Exception e) {
            throw new GatewayApiException("Password Verification - Unexpected error occurred.", e);
        }
    }

}
