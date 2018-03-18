package befaster.runner;

import tdl.client.queue.ImplementationRunnerConfig;
import tdl.client.runner.ChallengeSessionConfig;

import static befaster.runner.CredentialsConfigFile.readFromConfigFile;

public class Utils {
    public static ChallengeSessionConfig getConfig() throws ConfigNotFoundException {
        return ChallengeSessionConfig.forJourneyId(readFromConfigFile("tdl_journey_id"))
                .withServerHostname(readFromConfigFile("tdl_hostname"))
                .withColours(Boolean.parseBoolean(readFromConfigFile("tdl_use_coloured_output", "true")))
                .withRecordingSystemShouldBeOn(Boolean.parseBoolean(readFromConfigFile("tdl_require_rec", "true")));
    }

    public static ImplementationRunnerConfig getRunnerConfig() throws ConfigNotFoundException {
        return new ImplementationRunnerConfig()
                .setUniqueId(readFromConfigFile("tdl_username"))
                .setHostname(readFromConfigFile("tdl_hostname"));
    }
}
