package br.com.limaflucas.simpleworkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

import io.nflow.engine.config.EngineConfiguration;
import io.nflow.engine.service.WorkflowInstanceService;
import io.nflow.engine.workflow.instance.WorkflowInstanceFactory;
import lombok.RequiredArgsConstructor;

@Import(EngineConfiguration.class)
@SpringBootApplication
@RequiredArgsConstructor
public class SimpleworkflowApplication {

	private final WorkflowInstanceService workflowInstances;
	private final WorkflowInstanceFactory workflowInstanceFactory;

	public static void main(String[] args) {
		SpringApplication.run(SimpleworkflowApplication.class, args);
	}

	// @Bean
	// public WorkflowSample exampleWorkflow() {
	// 	return new WorkflowSample();
	// }

	@EventListener(ApplicationReadyEvent.class)
	public void insertWorkflowInstance() {
		workflowInstances.insertWorkflowInstance(
			workflowInstanceFactory.newWorkflowInstanceBuilder()
				.setType(AliasAccountOperationWorkflow.TYPE)
				.setExternalId("sample")
				.build()
		);
	}

}
