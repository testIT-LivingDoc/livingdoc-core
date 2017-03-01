package info.novatec.testit.livingdoc.client.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 * Basic stream manager for a process.
 * <p>
 * 
 * @author JCHUET
 */
public class StreamGobblerImpl implements StreamGobbler {
    private String input = "";
    private OutputStream stdin;
    private InputStream stdout;
    private InputStream stderr;
    private StringBuffer outBuffer = new StringBuffer();
    private StringBuffer errBuffer = new StringBuffer();
    private List<Exception> exceptions = new ArrayList<Exception>();

    public StreamGobblerImpl(Process process) {
        stdin = process.getOutputStream();
        stdout = process.getInputStream();
        stderr = process.getErrorStream();
    }

    @Override
    public void run() {
        new Thread(new OuputReadingRunnable(stdout, outBuffer), "Process standard out").start();
        new Thread(new OuputReadingRunnable(stderr, errBuffer), "Process error").start();
        sendInput();
    }

    @Override
    public String getOutput() {
        return outBuffer.toString();
    }

    @Override
    public String getError() {
        return errBuffer.toString();
    }

    @Override
    public boolean hasErrors() {
        return ! StringUtils.isEmpty(errBuffer.toString());
    }

    @Override
    public List<Exception> getExceptions() {
        return exceptions;
    }

    @Override
    public boolean hasExceptions() {
        return exceptions.size() > 0;
    }

    @Override
    public void exceptionCaught(Exception e) {
        exceptions.add(e);
    }

    protected void sendInput() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    stdin.write(input.getBytes("UTF-8"));
                    stdin.flush();
                    stdin.close();
                } catch (IOException e) {
                    exceptionCaught(e);
                }
            }
        };

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            exceptionCaught(e);
        }
    }

    private void readOutput(InputStream inputStream, StringBuffer buffer) {
        try {
            int c;
            while ( ( c = inputStream.read() ) != - 1) {
                buffer.append(( char ) c);
            }
        } catch (IOException e) {
            exceptionCaught(e);
        }
    }

    private class OuputReadingRunnable implements Runnable {
        public InputStream runnableInput;
        public StringBuffer buffer;

        public OuputReadingRunnable(InputStream input, StringBuffer buffer) {
            this.runnableInput = input;
            this.buffer = buffer;
        }

        @Override
        public void run() {
            readOutput(runnableInput, buffer);
        }
    }
}
