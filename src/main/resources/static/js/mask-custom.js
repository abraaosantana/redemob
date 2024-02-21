$('document').ready(function(){
	$('.mask-cpf').mask('000.000.000-00', {reverse: true});
	$('.mask-cnpj').mask('00.000.000/0000-00', {reverse: true});
	$('.mask-cep').mask('00000-000');
	$('.mask-telefone').mask('(00) 0000-0000');
	$('.mask-celular').mask('(00) 0 0000-0000');
});

$(document).ready(function() {

    function limpa_formulário_cep() {
        // Limpa valores do formulário de cep.
        $("#logradouro").val("");
        $("#complemento").val("");
        $("#bairro").val("");
        $("#localidade").val("");
        $("#uf").val("");
        $("#unidade").val("");
        $("#ibge").val("");
        $("#gia").val("");
    }
    
    //Quando o campo cep perde o foco.
    $("#cep").blur(function() {

        //Nova variável "cep" somente com dígitos.
        var cep = $(this).val().replace(/\D/g, '');

        //Verifica se campo cep possui valor informado.
        if (cep != "") {

            //Expressão regular para validar o CEP.
            var validacep = /^[0-9]{8}$/;

            //Valida o formato do CEP.
            if(validacep.test(cep)) {

                //Preenche os campos com "..." enquanto consulta webservice.
                $("#logradouro").val(" ... ");
                $("#complemento").val(" ... ");
                $("#bairro").val(" ... ");
                $("#localidade").val(" ... ");
                $("#uf").val(" ... ");
                $("#unidade").val(" ... ");
                $("#ibge").val(" ... ");
                $("#gia").val(" ... ");

                //Consulta o webservice viacep.com.br/
                $.getJSON("//viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {

                    if (!("erro" in dados)) {
                        //Atualiza os campos com os valores da consulta.
                        $("#logradouro").val(dados.logradouro);
                        $("#complemento").val(dados.complemento);
                        $("#bairro").val(dados.bairro);
                        $("#localidade").val(dados.localidade);                               
                        $("#uf").val(dados.uf);
                        $("#unidade").val(dados.unidade);
                        $("#ibge").val(dados.ibge);
                        $("#gia").val(dados.gia);
                    } //end if.
                    else {
                        //CEP pesquisado não foi encontrado.
                        limpa_formulário_cep();
                        alert("CEP não encontrado.");
                    }
                });
            } //end if.
            else {
                //cep é inválido.
                limpa_formulário_cep();
                alert("Formato de CEP inválido.");
            }
        } //end if.
        else {
            //cep sem valor, limpa formulário.
            limpa_formulário_cep();
        }
    });
});
