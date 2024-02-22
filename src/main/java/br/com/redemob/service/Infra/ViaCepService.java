package br.com.redemob.service.Infra;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.redemob.exception.ViaCepException;
import br.com.redemob.infra.ViaCepClient;
import br.com.redemob.service.dto.AddressDto;

@Service
public class ViaCepService {
	
	private static Logger logger = LoggerFactory.getLogger(ViaCepService.class);
	
	public String getCidade(String cep) {
		AddressDto addressDto = null;
		try {
	        String response = ViaCepClient.getAddressInfo(cep);
	        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();
	        addressDto = gson.fromJson(response, AddressDto.class);
	    } catch (IOException | ViaCepException e) {
	    	logger.error("There was an error while getting the address: {}", e.getMessage());
	    }
		
		return addressDto.getCidade();
	
	}

}
