package finchainstorage.persistancelayer.gateway.identity;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;




public class EmployeeIdentities {


    private final String employeeID;

    private static final Path walletPath = Paths.get( "wallet");

    private final Wallet wallet;


    private EmployeeIdentities(String employeeID){
        this.employeeID = employeeID;
        try{
            wallet = Wallets.newFileSystemWallet(walletPath);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public String getEmployeeID() {
        return employeeID;
    }

    public Wallet getWallet() {
        return wallet;
    }

    //retrieve the private key from the file system
    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }
    //retrieve the certificate from the file system
    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    //Store user identity in the wallet
    public static void storeIdentity(String employeeID) {

        try {
            Path credentialPath = Paths.get(  "..", "data-layer", "blockchain", "test-network",
                    "organizations", "peerOrganizations", "org1.example.com", "users", employeeID+"@finchain.com", "msp");

            Path privateKeyPath = credentialPath.resolve(Paths.get("keystore"));

            //Get the private key file
            try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(privateKeyPath, path -> path.toString().endsWith("_sk"))) {
                for (Path path : directoryStream) {
                    privateKeyPath = path;
                }
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
            Path certificatePath = credentialPath.resolve(Paths.get("signcerts", "cert.pem"));

            X509Certificate certificate = readX509Certificate(certificatePath);
            PrivateKey privateKey = getPrivateKey(privateKeyPath);

            Identity identity = Identities.newX509Identity("Org1MSP", certificate, privateKey);

            //Get the wallet path to store identities
            Wallet wallet = Wallets.newFileSystemWallet(walletPath);
            String idLabel = employeeID+"@finchain.com";
            wallet.put(idLabel, identity);

        } catch (IOException | CertificateException | InvalidKeyException e) {
            System.err.println("Error adding to wallet");
            throw new RuntimeException(e);
        }
    }

    //Retrieve an employeeID from the wallet
    public static EmployeeIdentities getIdentity(String employeeID) {
            return new EmployeeIdentities(employeeID+"@finchain.com");
    }
    //Delete an employeeID from the wallet
    public  void deleteIdentity(String employeeID){

        try{

            String idLabel = employeeID+"@finchain.com";
            this.wallet.remove(idLabel);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}


