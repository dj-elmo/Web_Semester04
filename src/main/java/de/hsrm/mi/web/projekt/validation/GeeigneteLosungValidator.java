package de.hsrm.mi.web.projekt.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GeeigneteLosungValidator implements ConstraintValidator<GeeigneteLosung, String> {
    private static final Logger logger = LoggerFactory.getLogger(GeeigneteLosungValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        String lower = value.toLowerCase();

        logger.info("Validating password: " + lower);
        return !(lower.equals("42") || lower.equals("zweiundvierzig"));
    }

    
}
