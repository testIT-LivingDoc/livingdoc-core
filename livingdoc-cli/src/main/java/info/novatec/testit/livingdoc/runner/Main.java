package info.novatec.testit.livingdoc.runner;

import info.novatec.testit.livingdoc.util.cli.ParseException;


public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        CommandLineRunner runner = new CommandLineRunner();
        LoggingMonitor monitor = new LoggingMonitor();
        runner.setMonitor(monitor);
        try {
            runner.run(args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.err.println("Try '--help' for more information.");

            if (e.getCause() != null) {
                System.err.println("Caused by:");
                e.getCause().printStackTrace(System.err);
            }

            System.exit(1);
        } catch (Throwable t) {
            monitor.exceptionOccurred(t);
            System.exit(1);
        }
    }
}
