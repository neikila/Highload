package main.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by neikila on 21.10.15.
 */
public class PortValidation implements IParameterValidator {
    @Override
    public void validate(String s, String s1) throws ParameterException {
        Integer portNum = null;
        try {
            portNum = Integer.parseInt(s1);
        } catch (Exception e) {
            throw new ParameterException(s + " should be integer.");
        }
        if (portNum < 1024) {
            throw new ParameterException(s + " should be over 1024.");
        }
    }
}
