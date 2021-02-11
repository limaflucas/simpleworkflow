package br.com.limaflucas.simpleworkflow;

import static io.nflow.engine.workflow.definition.WorkflowStateType.end;
import static io.nflow.engine.workflow.definition.WorkflowStateType.normal;
import static io.nflow.engine.workflow.definition.WorkflowStateType.start;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import br.com.limaflucas.simpleworkflow.AliasAccountOperationWorkflow.States;
import io.nflow.engine.workflow.definition.NextAction;
import io.nflow.engine.workflow.definition.StateExecution;
import io.nflow.engine.workflow.definition.WorkflowDefinition;
import io.nflow.engine.workflow.definition.WorkflowState;
import io.nflow.engine.workflow.definition.WorkflowStateType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AliasAccountOperationWorkflow extends WorkflowDefinition<States> {

    public static final String TYPE = "repeatingWorkflow";

    private final GetAccountPort getAccountPort;

    @RequiredArgsConstructor
    public enum States implements WorkflowState {

        getAccountData(start, "Retrieve account data from database"),
        requestOperation(normal, "Send the operation to the partner"),
        validateResponses(normal, "Validate whether both operations have been made"),
        completedOperation(end, "Final stage"),
        errorState(start, "Final error stage");

        private final WorkflowStateType type;
        private final String description;

        @Override
        public WorkflowStateType getType() {
            return this.type;
        }
    }

    public AliasAccountOperationWorkflow(GetAccountPort getAccountPort) {

        super(TYPE, States.getAccountData, States.errorState);
        permit(States.getAccountData, States.requestOperation);
        permit(States.requestOperation, States.validateResponses);
        permit(States.validateResponses, States.completedOperation);
        permit(States.errorState, States.getAccountData);

        this.getAccountPort = getAccountPort;
    }

    public NextAction getAccountData(StateExecution stateExecution) {

        return this.getAccountPort.get(UUID.fromString("79afd7d7-4c24-4af5-9e45-89cc24a4ac2d")).map(account -> {
            log.info("Account data: {}", account);
            return NextAction.moveToState(States.requestOperation, "Next state");
        }).orElseGet(() -> {
            log.error("Acount not found");
            return NextAction.moveToState(States.errorState, "Error state");
        });
    }

    public NextAction requestOperation(StateExecution stateExecution) {

        log.info("Send doc request");
        log.info("Send main account request");
        return NextAction.moveToState(States.validateResponses, "operation requests sent");
    }

    public NextAction validateResponses(StateExecution stateExecution) {

        log.info("valida both requests");
        return NextAction.moveToState(States.completedOperation, "account data retrieved");
    }

    public void completedOperation(StateExecution stateExecution) {

        log.info("processing finished");
    }

    public NextAction errorState(StateExecution stateExecution) {
        log.info("some error. Starting over");
        return NextAction.moveToStateAfter(States.getAccountData, DateTime.now().plusSeconds(3), "start over on error");
    }
    
}
