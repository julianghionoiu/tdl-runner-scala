package befaster.runner;

import tdl.client.actions.ClientAction;
import tdl.client.actions.ClientActions;

public enum RunnerAction {
    getNewRoundDescription(ClientActions.stop()),
    testConnectivity(ClientActions.stop()),
    deployToProduction(ClientActions.publish());

    private ClientAction clientAction;

    RunnerAction(ClientAction clientAction) {
        this.clientAction = clientAction;
    }

    public ClientAction getClientAction() {
        return clientAction;
    }
}
