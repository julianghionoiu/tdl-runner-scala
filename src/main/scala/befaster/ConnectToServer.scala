package befaster

import befaster.runner.{ClientRunner, RunnerAction}
import befaster.runner.CredentialsConfigFile.readFromConfigFile
import befaster.runner.TypeConversion.asInt
import befaster.solutions.{Checkout, FizzBuzz, Hello, Sum}

object ConnectToServer extends App {
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
    *   +------+-------------------------------------------------------------+
    *   | Step | The usual workflow                                          |
    *   +------+-------------------------------------------------------------+
    *   |  1.  | Run this file.                                              |
    *   |  2.  | Start a challenge by typing "start".                        |
    *   |  3.  | Read description from the "challenges" folder               |
    *   |  4.  | Implement the required method in                            |
    *   |      |   ./src/main/scala/befaster/solutions                       |
    *   |  5.  | Deploy to production by typing "deploy".                    |
    *   |  6.  | Observe output, check for failed requests.                  |
    *   |  7.  | If passed, go to step 3.                                    |
    *   +------+-------------------------------------------------------------+
    *
    *   You are encouraged to change this project as you please:
    *        * You can use your preferred libraries.
    *        * You can use your own test framework.
    *        * You can change the file structure.
    *        * Anything really, provided that this file stays runnable.
    *
    **/
  ClientRunner.forUsername(readFromConfigFile("tdl_username"))
    .withServerHostname(readFromConfigFile("tdl_hostname"))
    .withActionIfNoArgs(RunnerAction.testConnectivity)
    .withSolutionFor("sum", (p: Array[String]) => Sum.sum(asInt(p(0)), asInt(p(1))).asInstanceOf[AnyRef])
    .withSolutionFor("hello", (p: Array[String]) => Hello.hello(p(0)).asInstanceOf[AnyRef])
    .withSolutionFor("fizz_buzz", (p: Array[String]) => FizzBuzz.fizzBuzz(asInt(p(0))).asInstanceOf[AnyRef])
    .withSolutionFor("checkout", (p: Array[String]) => Checkout.checkout(p(0)).asInstanceOf[AnyRef])
    .start(args)
}
