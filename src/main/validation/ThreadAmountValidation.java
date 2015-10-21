package main.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Created by neikila on 21.10.15.
 */
public class ThreadAmountValidation implements IParameterValidator {
    @Override
    public void validate(String s, String s1) throws ParameterException {
        Integer threadAmount = null;
        try {
            threadAmount = Integer.parseInt(s1);
        } catch (Exception e) {
            throw new ParameterException(s + "should be integer.");
        }
        if (threadAmount < 1) {
            throw new ParameterException(s + " value should be positive.");
        }
    }
}
