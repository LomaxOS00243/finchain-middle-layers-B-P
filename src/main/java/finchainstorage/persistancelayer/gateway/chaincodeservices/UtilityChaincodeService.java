package finchainstorage.persistancelayer.gateway.chaincodeservices;

import org.hyperledger.fabric.gateway.Contract;

public interface UtilityChaincodeService {
    void verifyDuplication(String employeeId, Contract contract);

    void verifyAccessPermission(Contract contract);

    void eventsListener(Contract contract);

}
