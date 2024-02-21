package br.com.redemob.enums;

import lombok.Getter;

@Getter
public enum SituacaoEnum {
	ANALISE("Em Análise"),
	APROVADO("Aprovado"),
	REPROVADO("Reprovado");

	String descricao;

	SituacaoEnum(String descricao) {
		this.descricao = descricao;
	}
}
