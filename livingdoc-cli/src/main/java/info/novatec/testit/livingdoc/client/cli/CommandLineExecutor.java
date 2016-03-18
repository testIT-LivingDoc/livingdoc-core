package info.novatec.testit.livingdoc.client.cli;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CommandLineExecutor {
    private static final Logger log = LogManager.getLogger(CommandLineExecutor.class);
    private static final int SUCCESS = 0;
    private StreamGobbler gobbler;
    private String[] cmdLine;

    public CommandLineExecutor(String[] cmdLine) {
        this.cmdLine = cmdLine.clone();
    }

    public void executeAndWait() throws IOException, InterruptedException, LivingDocCLIException {
        Process p = launchProcess();
        checkForErrors(p);

        if (log.isDebugEnabled()) {
            // Keep trace of outputs
            if ( ! StringUtils.isEmpty(getOutput())) {
                log.debug("System Output during execution : \n" + getOutput());
            }

            if (gobbler.hasErrors()) {
                log.debug("System Error Output during execution : \n" + gobbler.getError());
            }
        }
    }

    private Process launchProcess() throws IOException, InterruptedException {
        if (log.isDebugEnabled()) {
            log.debug("Launching cmd: " + getCmdLineToString());
        }

        Process p = Runtime.getRuntime().exec(cmdLine);
        gobbler = new StreamGobblerImpl(p);
        Thread reader = new Thread(gobbler);
        reader.start();
        p.waitFor();
        return p;
    }

    public String getOutput() {
        return gobbler.getOutput();
    }

    // TODO exc: find fitting Exception for method 'checkForErrors(Process p)'
    private void checkForErrors(Process p) throws LivingDocCLIException {
        if (p.exitValue() != SUCCESS) {
            if (gobbler.hasErrors()) {
                throw new LivingDocCLIException(gobbler.getError());
            }

            throw new LivingDocCLIException("Process was terminated abnormally");
        }
    }

    private String getCmdLineToString() {
        StringBuilder sb = new StringBuilder();
        for (String cmd : cmdLine) {
            sb.append(cmd).append(' ');
        }

        return sb.toString();
    }
}


class LivingDocCLIException extends Exception {
    private static final long serialVersionUID = 211101020465499526L;

    public LivingDocCLIException() {
        super();
    }

    public LivingDocCLIException(String message) {
        super(message);
    }
}
