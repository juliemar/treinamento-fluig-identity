package com.fluig.main;
import com.totvslabs.idm.common.exception.FluigIdentityException;
import com.totvslabs.idm.common.model.UserAccountDTO;
import com.totvslabs.idm.common.model.UserCompanyAccountDTO;
import com.totvslabs.idm.rest.client.FluigIdentityRestClient;
import com.totvslabs.idm.rest.client.credentials.FluigIdentityCredentials;


public class MainIdentityRestClient {

	public static FluigIdentityCredentials credentials;
	public static FluigIdentityRestClient client;
	public static String COMPANY_ID = "9dknst0cywf51hsr1474853531915";
	
	public static void main(String[] args) throws FluigIdentityException {
		UserCompanyAccountDTO userInfo = MainIdentityRestClient.getFluigClient().getCompanyUserService().getUserById(MainIdentityRestClient.COMPANY_ID, "k50294z21nisq81f1474849411098");
		System.out.println("Email:"+ userInfo.getEmailAddress());
		
		UserCompanyAccountDTO accountDTO = MainIdentityRestClient.createUser("juliemar.berri@totvs.com.br", "Juliemar", "TOTVS", "Totvs@123");
		System.out.println(accountDTO.getId());
		
		
	}

	
	public static UserCompanyAccountDTO createUser(String mail,String name,String lastName,String password) throws FluigIdentityException{
		UserCompanyAccountDTO accountDTO = new UserCompanyAccountDTO();
		accountDTO.setCompanyId(MainIdentityRestClient.COMPANY_ID);
		accountDTO.setEmailAddress(mail);
		accountDTO.setFirstName(name);
		accountDTO.setLastName(lastName);
		accountDTO.setPassword(password);

		accountDTO = getFluigClient().getCompanyUserService().createUser(MainIdentityRestClient.COMPANY_ID, accountDTO, true);
		
		return accountDTO;
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