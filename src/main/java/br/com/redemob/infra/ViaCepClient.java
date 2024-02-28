package br.com.redemob.infra;

import br.com.redemob.exception.ViaCepException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Component
public class ViaCepClient {

    @Value("${app.ssl.trust-store-type}")
    private String trustStoreType;
    @Value("${app.ssl.trust-store}")
    private Path trustStore;
    @Value("${app.ssl.trust-secret}")
    private String trustStoreSecret;

    private static final String BASE_URL = "https://viacep.com.br/ws/";

    public String getAddressInfo(String address) throws ViaCepException, IOException {
        try {

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore ks = KeyStore.getInstance(trustStoreType);

            FileInputStream fis = new FileInputStream(trustStore.toFile());

            ks.load(fis, trustStoreSecret.toCharArray());
            fis.close();

            tmf.init(ks);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Cria a URL para a consulta do CEP
            URL url = new URL(BASE_URL + address + "/json");

            // Abre a conexão HTTP
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setSSLSocketFactory(sslContext.getSocketFactory());

            // Verifica se a requisição foi bem sucedida (código 200)
            if (connection.getResponseCode() == 200) {
                // Lê os dados da resposta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                if (response.toString().contains("\"erro\": true")) {
                    throw new ViaCepException("CEP not found: " + address + ". Try again.");
                }

                reader.close();
                connection.disconnect();

                return response.toString();
            } else {
                // Caso a requisição tenha falhado, lance uma exceção com o código de erro:
                throw new ViaCepException("Failed to connect. Error code: " + connection.getResponseCode());
            }
        } catch (IOException | KeyStoreException e) {
            throw new ViaCepException("Error while accessing ViaCEP API: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
