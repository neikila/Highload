package main;

import com.beust.jcommander.Parameter;
import main.validation.*;
/**
 * Created by neikila on 21.10.15.
 */
public class ArgumentsParser {
    @Parameter(names = { "-r"}, description = "root directory", validateWith = DirectoryValidation.class)
    private String rootDir = "/home/neikila/Study/HigloadProject/static";

    @Parameter(names = { "-port"}, description = "port", validateWith = PortValidation.class)
    private Integer port = 8081;

    @Parameter(names = { "-pullSize"}, description = "pull size", validateWith = ThreadAmountValidation.class)
    private Integer pullSize = 1;

    public String getRootDir() {
        return rootDir;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getPullSize() {
        return pullSize;
    }
}
