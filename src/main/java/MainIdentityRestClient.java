import com.totvslabs.idm.common.exception.FluigIdentityException;
import com.totvslabs.idm.common.model.UserAccountDTO;
import com.totvslabs.idm.common.model.UserCompanyAccountDTO;
import com.totvslabs.idm.rest.client.FluigIdentityRestClient;
import com.totvslabs.idm.rest.client.credentials.FluigIdentityCredentials;


public class MainIdentityRestClient {

	public static FluigIdentityCredentials credentials;
	public static FluigIdentityRestClient client;
	
	
	public static void main(String[] args) throws FluigIdentityException {
		UserCompanyAccountDTO userInfo = MainIdentityRestClient.getFluigClient().getCompanyUserService().getUserById("9dknst0cywf51hsr1474853531915", "k50294z21nisq81f1474849411098");
		
		System.out.println("Email:"+ userInfo.getEmailAddress());
	}

	private static FluigIdentityCredentials getCredentials() {
		String privatekeyFile = MainIdentityRestClient.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", "").replace("target/classes/", "lib/FluigIdentity.pk8");
		
		if(MainIdentityRestClient.credentials==null){
			try {
				MainIdentityRestClient.credentials = new FluigIdentityCredentials
						("c43d2009-b842-4aff-8cdd-1d28e3fb1304", privatekeyFile, "https://testingsaml.thecloudpass.com");
			} catch (FluigIdentityException e) {
				e.printStackTrace();
			}
		}
		
		return MainIdentityRestClient.credentials;
	}
	
	public static FluigIdentityRestClient getFluigClient(){
		if(MainIdentityRestClient.client==null){
			MainIdentityRestClient.client = new FluigIdentityRestClient(getCredentials());
		}
		return MainIdentityRestClient.client;
	}

}
