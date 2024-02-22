package br.com.redemob.service.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FilesDto {
    
    private MultipartFile biometria;
    private MultipartFile identidade;
    private MultipartFile residencia;

}
