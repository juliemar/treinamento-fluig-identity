package com.fluig.main;

import com.totvslabs.idm.common.exception.FluigIdentityException;
import com.totvslabs.idm.common.model.UserCompanyAccountDTO;
import com.totvslabs.idm.rest.client.FluigIdentityRestClient;
import com.totvslabs.idm.rest.client.credentials.FluigIdentityCredentials;

public class MainIdentityConector {

	public static void main(String[] args) throws FluigIdentityException {
		
		FluigIdentityCredentials credentials = new FluigIdentityCredentials(
				"c43d2009-b842-4aff-8cdd-1d28e3fb1304", 
				"C:\\FLUIG\\treinamentos\\treinamento-fluig-identity\\src\\main\\resources\\FluigIdentity-chave-privada.pk8", 
				"https://testingsaml.thecloudpass.com");
		
		
		FluigIdentityRestClient client = new FluigIdentityRestClient(credentials);
		
		UserCompanyAccountDTO userInfo= client.getCompanyUserService().getUserById("9dknst0cywf51hsr1474853531915", "vplphpc6dovi6uci1476732148598");
		
		System.out.println("Email do usuário: "+ userInfo.getEmailAddress());
		
	}
}
