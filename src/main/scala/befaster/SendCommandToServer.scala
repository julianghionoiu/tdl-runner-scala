package befaster

import befaster.runner.UserInputAction
import befaster.runner.Utils.{getConfig, getRunnerConfig}
import tdl.client.queue.QueueBasedImplementationRunner
import tdl.client.runner.ChallengeSession

object SendCommandToServer extends App {
  /**
    * ~~~~~~~~~~ Running the system: ~~~~~~~~~~~~~
    *
    *   From IDE:
    *      Run this file from the IDE.
    *
    *   From command line:
    *      sbt run
    *
    *   To run your unit tests locally:
    *      sbt test
    *
    * ~~~~~~~~~~ The workflow ~~~~~~~~~~~~~
    *
    *   By running this file you interact with a challenge server.
    *   The interaction follows a request-response pattern:
    *        * You are presented with your current progress and a list of actions.
    *        * You trigger one of the actions by typing it on the console.
    *        * After the action feedback is presented, the execution will stop.
    *
    *   +------+-----------------------------------------------------------------------+
    *   | Step | The usual workflow                                                    |
    *   +------+-----------------------------------------------------------------------+
    *   |  1.  | Run this file.                                                        |
    *   |  2.  | Start a challenge by typing "start".                                  |
    *   |  3.  | Read the description from the "challenges" folder.                    |
    *   |  4.  | Locate the file corresponding to your current challenge in:           |
    *   |      |   ./src/main/scala/befaster/solutions                                 |
    *   |  5.  | Replace the following placeholder exception with your solution:       |
    *   |      |   throw new SolutionNotImplementedException()                         |
    *   |  6.  | Deploy to production by typing "deploy".                              |
    *   |  7.  | Observe the output, check for failed requests.                        |
    *   |  8.  | If passed, go to step 1.                                              |
    *   +------+-----------------------------------------------------------------------+
    *
    *   You are encouraged to change this project as you please:
    *        * You can use your preferred libraries.
    *        * You can use your own test framework.
    *        * You can change the file structure.
    *        * Anything really, provided that this file stays runnable.
    *
    **/
  val entry = EntryPointMapping
  val runner: QueueBasedImplementationRunner = new QueueBasedImplementationRunner.Builder()
    .setConfig(getRunnerConfig)
    .withSolutionFor("sum", entry.sum)
    .withSolutionFor("hello", entry.hello)
    .withSolutionFor("array_sum", entry.array_sum)
    .withSolutionFor("int_range", entry.int_range)
    .withSolutionFor("fizz_buzz", entry.fizz_buzz)
    .withSolutionFor("checkout", entry.checkout)
    .withSolutionFor("checklite", entry.checklite)
    .create

  ChallengeSession.forRunner(runner)
    .withConfig(getConfig)
    .withActionProvider(new UserInputAction(args))
    .start()
}
