package it.eng.alidalab.applicationcatalogue.domain.workflow;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WorkflowNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface WorkflowNameConstraint {
    String message() default "Invalid workflow name";
}
