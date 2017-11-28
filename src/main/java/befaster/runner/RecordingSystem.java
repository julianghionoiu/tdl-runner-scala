package befaster.runner;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static befaster.runner.CredentialsConfigFile.readFromConfigFile;

class RecordingSystem {
    private static final String RECORDING_SYSTEM_ENDPOINT = "http://localhost:41375";

    static boolean isRecordingSystemOk() {
        //noinspection SimplifiableConditionalExpression
        return RecordingSystem.isRecordingRequired() ? RecordingSystem.isRunning() : true;
    }

    private static boolean isRunning() {
        try {
            HttpResponse<String> stringHttpResponse = Unirest.get(RECORDING_SYSTEM_ENDPOINT + "/status").asString();
            if (stringHttpResponse.getStatus() == 200 && stringHttpResponse.getBody().startsWith("OK")) {
                return true;
            }
        } catch (UnirestException e) {
            System.err.println("Could not reach recording system: " + e.getMessage());
        }

        return false;
    }

    private static boolean isRecordingRequired() {
        return Boolean.parseBoolean(readFromConfigFile("tdl_require_rec", "true"));
    }

    static void notifyEvent(String lastFetchedRound, String shortName) {
        System.out.printf("Notify round \"%s\", event \"%s\"%n", lastFetchedRound, shortName);

        if (!isRecordingRequired()) {
            return;
        }

        try {
            HttpResponse<String> stringHttpResponse = Unirest.post(RECORDING_SYSTEM_ENDPOINT + "/notify")
                    .body(lastFetchedRound+"/"+shortName)
                    .asString();
            if (stringHttpResponse.getStatus() != 200) {
                System.err.println("Recording system returned code: "+stringHttpResponse.getStatus());
                return;
            }

            if (!stringHttpResponse.getBody().startsWith("ACK")) {
                System.err.println("Recording system returned body: "+stringHttpResponse.getStatus());
            }
        } catch (UnirestException e) {
            System.err.println("Could not reach recording system: " + e.getMessage());
        }
    }
}
