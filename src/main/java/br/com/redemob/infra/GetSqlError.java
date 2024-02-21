package br.com.redemob.infra;


import org.apache.commons.lang.exception.ExceptionUtils;

public class GetSqlError {

    public static String getErro(Exception e) {
        String erro = ExceptionUtils.getRootCauseMessage(e);

        //faz tratamento para pegar só o primeiro erro do Oracle
        if (erro.contains("ORA-")) {
            try {
                erro = erro.substring(erro.indexOf("ORA-"));
                erro = erro.substring(0,erro.indexOf("\nORA-"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        //faz tratamento para pegar só o primeiro erro do postgresql
        } else if (erro.contains("ERROR:") || erro.contains("PSQLException:")) {
            try {
                erro = erro.substring(erro.indexOf("ERROR:"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

        return erro;
    }

}
