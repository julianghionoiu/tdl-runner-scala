package befaster.runner;

import befaster.solutions.Checkout;
import befaster.solutions.FizzBuzz;
import befaster.solutions.Hello;
import befaster.solutions.Sum;
import tdl.client.Client;
import tdl.client.ProcessingRules;

import java.util.Arrays;
import java.util.Optional;

import static befaster.runner.CredentialsConfigFile.readFromConfigFile;
import static tdl.client.actions.ClientActions.publish;

public class ClientRunner {
    private String hostname;
    private RunnerAction defaultRunnerAction;
    private final String username;

    public static ClientRunner forUsername(@SuppressWarnings("SameParameterValue") String username) {
        return new ClientRunner(username);
    }

    private ClientRunner(String username) {
        this.username = username;
    }

    public ClientRunner withServerHostname(@SuppressWarnings("SameParameterValue") String hostname) {
        this.hostname = hostname;
        return this;
    }

    public ClientRunner withActionIfNoArgs(RunnerAction actionIfNoArgs) {
        defaultRunnerAction = actionIfNoArgs;
        return this;
    }

    public void start(String[] args) {
        if(!isRecordingSystemOk()) {
            System.out.println("Please run `record_screen_and_upload` before continuing.");
            return;
        }

        RunnerAction runnerAction = extractActionFrom(args).orElse(defaultRunnerAction);
        System.out.println("Chosen action is: "+runnerAction.name());

        Client client = new Client.Builder()
                .setHostname(hostname)
                .setUniqueId(username)
                .create();

        ProcessingRules processingRules = new ProcessingRules() {{
            on("display_description").call(p -> RoundManagement.displayAndSaveDescription(asString(p[0]), asString(p[1]))).then(publish());
            on("sum").call(p -> Sum.sum(asInt(p[0]), asInt(p[1]))).then(runnerAction.getClientAction());
            on("hello").call(p -> Hello.hello(asString(p[0]))).then(runnerAction.getClientAction());
            on("fizz_buzz").call(p -> FizzBuzz.fizzBuzz(asInt(p[0]))).then(runnerAction.getClientAction());
            on("checkout").call(p -> Checkout.checkout(asString(p[0]))).then(runnerAction.getClientAction());
        }};
        client.goLiveWith(processingRules);

        RecordingSystem.notifyEvent(RoundManagement.getLastFetchedRound(), runnerAction.getShortName());
    }

    private static Optional<RunnerAction> extractActionFrom(String[] args) {
        String firstArg = args.length > 0 ? args[0] : null;
        return Arrays.stream(RunnerAction.values())
                .filter(runnerAction -> runnerAction.name().equalsIgnoreCase(firstArg))
                .findFirst();
    }


    private boolean isRecordingSystemOk() {
        boolean requireRecording = Boolean.parseBoolean(readFromConfigFile("tdl_require_rec", "true"));

        //noinspection SimplifiableIfStatement
        if (requireRecording) {
            return RecordingSystem.isRunning();
        } else {
            return true;
        }
    }


    //~~~~~~~ Provided implementations ~~~~~~~~~~~~~~

    private static String asString(String s) {
        return s;
    }

    private static int asInt(String s) {
        return Integer.parseInt(s);
    }

}
