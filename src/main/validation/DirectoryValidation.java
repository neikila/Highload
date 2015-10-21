package main.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
 * Created by neikila on 21.10.15.
 */
public class DirectoryValidation implements IParameterValidator {
    @Override
    public void validate(String s, String directoryName) throws ParameterException {
        File dir = new File(directoryName);
        if (!dir.isDirectory()) {
            throw new ParameterException("'" + directoryName + "' is not a directory");
        }
    }
}
