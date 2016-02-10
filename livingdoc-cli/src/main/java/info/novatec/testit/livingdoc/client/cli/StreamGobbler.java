package info.novatec.testit.livingdoc.client.cli;

import java.util.List;


public interface StreamGobbler extends Runnable {
    String getOutput();

    String getError();

    List<Exception> getExceptions();

    boolean hasErrors();

    boolean hasExceptions();

    void exceptionCaught(Exception e);
}
