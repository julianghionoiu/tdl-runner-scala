package befaster.runner;

import befaster.solutions.Checkout;
import befaster.solutions.FizzBuzz;
import befaster.solutions.Hello;
import befaster.solutions.Sum;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import tdl.client.Client;
import tdl.client.ProcessingRules;

import static tdl.client.actions.ClientActions.publish;

public class ClientRunner {
    private String hostname;
    private RunnerAction defaultRunnerAction;
    private final String userId;

    public static ClientRunner forUser(String userId) {
        return new ClientRunner(userId);
    }

    private ClientRunner(String userId) {
        this.userId = userId;
    }

    public ClientRunner withServerHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public ClientRunner withActionIfNoArgs(RunnerAction actionIfNoArgs) {
        defaultRunnerAction = actionIfNoArgs;
        return this;
    }

    public void start(String[] args) {
        RunnerAction runnerAction = extractActionFrom(args).orElse(defaultRunnerAction);
        System.out.println("Chosen action is: "+runnerAction.name());

        Client client = new Client.Builder()
                .setHostname(hostname)
                .setUniqueId(userId)
                .create();

        ProcessingRules processingRules = new ProcessingRules() {{
            on("display_description").call(p -> displayAndSaveDescription(asString(p[0]), asString(p[1]))).then(publish());
            on("sum").call(p -> Sum.sum(asInt(p[0]), asInt(p[1]))).then(runnerAction.getClientAction());
            on("hello").call(p -> Hello.hello(asString(p[0]))).then(runnerAction.getClientAction());
            on("fizz_buzz").call(p -> FizzBuzz.fizzBuzz(asInt(p[0]))).then(runnerAction.getClientAction());
            on("checkout").call(p -> Checkout.checkout(asString(p[0]))).then(runnerAction.getClientAction());
        }};
        client.goLiveWith(processingRules);
    }

    private static Optional<RunnerAction> extractActionFrom(String[] args) {
        String firstArg = args.length > 0 ? args[0] : null;
        return Arrays.stream(RunnerAction.values())
                .filter(runnerAction -> runnerAction.name().equalsIgnoreCase(firstArg))
                .findFirst();
    }

    //~~~~~~~ Provided implementations ~~~~~~~~~~~~~~

    private static String displayAndSaveDescription(String label, String description) {
        System.out.println("Starting round: "+label);
        System.out.println(description);

        //Save description
        File output = new File("challenges" + File.separator + label + ".txt");
        try {
            Files.write(description.getBytes(), output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Challenge description saved to file: "+output.getPath()+".");

        return "OK";
    }

    private static String asString(String s) {
        return s;
    }

    private static int asInt(String s) {
        return Integer.parseInt(s);
    }

}
