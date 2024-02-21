package br.com.redemob.enums;

import lombok.Getter;

@Getter
public enum CidadeEnum {

	GOIANIA("Goiânia");

	String descricao;

	CidadeEnum(String descricao) {
		this.descricao = descricao;
	}

}
