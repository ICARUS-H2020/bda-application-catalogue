package it.eng.alidalab.applicationcatalogue.domain.workflow;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WorkflowNameValidator implements ConstraintValidator<WorkflowNameConstraint, String> {

    @Override
    public void initialize(WorkflowNameConstraint workflowNameConstraint) {
    }

    @Override
    public boolean isValid(String name,
                           ConstraintValidatorContext cxt) { //STRING OF VARIABLE LENGTH INCLUDING '_' CHARACTER
        return name != null && name.matches("^[a-zA-Z0-9_]*$");
    }

}