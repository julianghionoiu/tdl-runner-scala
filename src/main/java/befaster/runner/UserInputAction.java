package befaster.runner;

import tdl.client.runner.ActionProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputAction implements ActionProvider {
    private String[] args;

    public UserInputAction(String[] args) {
        this.args = args;
    }

    @Override
    public String get() {
        String returnValue = null;
        try {
            returnValue = args.length > 0 ? args[0] : readInputFromConsole();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private String readInputFromConsole() throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        return buffer.readLine();
    }
}
