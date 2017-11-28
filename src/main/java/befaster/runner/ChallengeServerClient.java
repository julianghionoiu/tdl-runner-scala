package befaster.runner;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


class ChallengeServerClient {
    private final Logger LOG = LoggerFactory.getLogger(ChallengeServerClient.class);
    private String url;
    private String journeyId;
    private int port = 8222;
    private String acceptHeader;

    ChallengeServerClient(String url, String journeyId, boolean useColours) {
        this.url = url;
        this.journeyId = journeyId;
        this.acceptHeader = useColours ? "text/coloured" : "text/not-coloured";
    }

    //~~~~~~~ GET ~~~~~~~~

    String getJourneyProgress() throws OtherCommunicationException, ServerErrorException, ClientErrorException {
        return get("journeyProgress");
    }

    String getAvailableActions() throws OtherCommunicationException, ServerErrorException, ClientErrorException {
        return get("availableActions");
    }

    String getRoundDescription() throws OtherCommunicationException, ServerErrorException, ClientErrorException {
        return get("roundDescription");
    }

    private String get(String name) throws OtherCommunicationException, ServerErrorException, ClientErrorException {
        try {
            String encodedPath = URLEncoder.encode(this.journeyId, "UTF-8");
            String url = String.format("http://%s:%d/%s/%s", this.url, port, name, encodedPath);
            HttpResponse<String> response = Unirest.get(url)
                    .header("Accept", this.acceptHeader)
                    .header("Accept-Charset", "UTF-8")
                    .asString();
            ensureStatusOk(response);
            return response.getBody();
        } catch (UnirestException | UnsupportedEncodingException e ) {
            throw new OtherCommunicationException("Could not perform GET request",e);
        }
    }

    //~~~~~~~ POST ~~~~~~~~

    String sendAction(String action) throws
            ClientErrorException, ServerErrorException, OtherCommunicationException {
        try {
            String encodedPath = URLEncoder.encode(this.journeyId, "UTF-8");
            String url = String.format("http://%s:%d/action/%s/%s", this.url, port, action, encodedPath);
            HttpResponse<String> response =  Unirest.post(url)
                    .header("Accept", this.acceptHeader)
                    .header("Accept-Charset", "UTF-8")
                    .asString();
            ensureStatusOk(response);
            return response.getBody();
        } catch (UnirestException | UnsupportedEncodingException e ) {
            throw new OtherCommunicationException("Could not perform POST request",e);
        }
    }


    //~~~~~~~ Error handling ~~~~~~~~~

    private static void ensureStatusOk(HttpResponse<String> response) throws ClientErrorException,
            ServerErrorException, OtherCommunicationException {
        int responseStatus = response.getStatus();
        if (isClientError(responseStatus)) {
            throw new ClientErrorException(response.getBody());
        } else if (isServerError(responseStatus)) {
            throw new ServerErrorException(response.getStatusText());
        } else if (isOtherErrorResponse(responseStatus)) {
            throw new OtherCommunicationException(response.getStatusText());
        }
    }

    private static boolean isClientError(int responseStatus) {
        return responseStatus >= 400 && responseStatus < 500;
    }

    private static boolean isServerError(int responseStatus) {
        return responseStatus >= 500 && responseStatus < 600;
    }

    private static boolean isOtherErrorResponse(int responseStatus) {
        return responseStatus < 200 || responseStatus > 300;
    }

    static class ClientErrorException extends Exception {

        String responseMessage;
        ClientErrorException(String message) {
            this.responseMessage = message;
        }

        String getResponseMessage() {
            return responseMessage;
        }


    }

    static class ServerErrorException extends Exception {
        ServerErrorException(String statusText) {
            super(statusText);
        }
    }

    static class OtherCommunicationException extends Exception {

        OtherCommunicationException(String message, Exception e) {
            super(message,e);
        }

        OtherCommunicationException(String statusText) {
            super(statusText);
        }
    }
}
