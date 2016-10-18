package com.fluig.main;
import java.util.HashMap;
import java.util.Map;

import com.totvslabs.idm.common.exception.FluigIdentityException;
import com.totvslabs.idm.common.extension.Resource;
import com.totvslabs.idm.common.model.UserCompanyAccountDTO;
import com.totvslabs.idm.common.util.ScimResourceTypeEnum;
import com.totvslabs.idm.rest.client.FluigIdentityRestClient;
import com.totvslabs.idm.rest.client.credentials.FluigIdentityCredentials;


public class MainIdentityRestClient {

	public static FluigIdentityCredentials credentials;
	public static FluigIdentityRestClient client;
	public static String COMPANY_ID = "9dknst0cywf51hsr1474853531915";
	public static String CLIENT_ID = "c43d2009-b842-4aff-8cdd-1d28e3fb1304";
	public static String IDENTITY_URI = "https://testingsaml.thecloudpass.com";
	
	
	public static void main(String[] args) throws FluigIdentityException {
//		UserCompanyAccountDTO userInfo = MainIdentityRestClient.getFluigClient().getCompanyUserService().getUserById(MainIdentityRestClient.COMPANY_ID, "k50294z21nisq81f1474849411098");
//		System.out.println("Email:"+ userInfo.getEmailAddress());
//		
//		UserCompanyAccountDTO accountDTO = MainIdentityRestClient.createUser("clientrest@identity.com", "Client", "REST", "Totvs@123");
//		System.out.println(accountDTO.getId());
		
		MainIdentityRestClient.addResource("Gestor");
	}

	
	public static void addResource(String resourceName) throws FluigIdentityException{
		
		Resource resource = new Resource();

		resource.setType("Project");
		resource.setScimResourceTypeEnum(ScimResourceTypeEnum.ROLE);
		
		Map<String, String> name = new HashMap<String, String>();
		
		name.put("en-US", resourceName);
		name.put("pt-BR", resourceName);
		
		resource.setDescription(name);
		resource.setDisplayName(name);
		resource.setName(name);

		
		resource = MainIdentityRestClient.getFluigClient().getScimResourcesService().createApplicationResource(COMPANY_ID, "yso3uyr17vz1rfrv1476669824768", resource);
	
		
		
		System.out.println("Resource-ID:"+ resource.getId());
	
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
	
	/**
	 * Inicializa as credenciais
	 * @return
	 */
	private static FluigIdentityCredentials getCredentials() {
		String privatekeyFile = MainIdentityRestClient.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", "").replace("target/classes/", "lib/FluigIdentity.pk8");
		
		if(MainIdentityRestClient.credentials==null){
			try {
				MainIdentityRestClient.credentials = new FluigIdentityCredentials
						(MainIdentityRestClient.CLIENT_ID, "C:\\FLUIG\\treinamentos\\treinamento-fluig-identity\\lib\\FluigIdentity.pk8", MainIdentityRestClient.IDENTITY_URI);
			} catch (FluigIdentityException e) {
				e.printStackTrace();
			}
		}
		
		return MainIdentityRestClient.credentials;
	}
	
	/**
	 * Inicializa o Client
	 * @return
	 */
	public static FluigIdentityRestClient getFluigClient(){
		if(MainIdentityRestClient.client==null){
			MainIdentityRestClient.client = new FluigIdentityRestClient(getCredentials());
		}
		return MainIdentityRestClient.client;
	}

}
