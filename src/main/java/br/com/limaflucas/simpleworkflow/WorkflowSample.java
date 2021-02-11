package br.com.limaflucas.simpleworkflow;

import static io.nflow.engine.workflow.definition.WorkflowStateType.manual;
import static io.nflow.engine.workflow.definition.WorkflowStateType.start;

import org.joda.time.DateTime;

import io.nflow.engine.workflow.definition.NextAction;
import io.nflow.engine.workflow.definition.StateExecution;
import io.nflow.engine.workflow.definition.WorkflowDefinition;
import io.nflow.engine.workflow.definition.WorkflowStateType;

public class WorkflowSample extends WorkflowDefinition<WorkflowSample.State> {

    public static final String TYPE = "repeatingWorkflow";
    public static final String VAR_COUNTER = "VAR_COUNTER";

    public enum State implements io.nflow.engine.workflow.definition.WorkflowState {
        REPEAT(start, "Repeating state"), ERROR(manual, "Error state");

        private WorkflowStateType type;
        private String description;

        State(WorkflowStateType type, String description) {
            this.type = type;
            this.description = description;
        }

        @Override
        public WorkflowStateType getType() {
            return type;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public WorkflowSample() {
        super(TYPE, State.REPEAT, State.ERROR);
        permit(State.REPEAT, State.REPEAT);
    }

    public NextAction REPEAT(StateExecution execution) {
        System.out.println("Counter: " + execution.getVariable(VAR_COUNTER));
        execution.setVariable(VAR_COUNTER, execution.getVariable(VAR_COUNTER, Integer.class) + 1);
        return NextAction.moveToStateAfter(State.REPEAT, DateTime.now().plusSeconds(10), "Next iteration");
    }

}
