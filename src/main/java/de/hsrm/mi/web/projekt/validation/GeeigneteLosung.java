package de.hsrm.mi.web.projekt.validation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = GeeigneteLosungValidator.class)
@Target({ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface GeeigneteLosung {
    String message() default "Benutzer losung gültig";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}