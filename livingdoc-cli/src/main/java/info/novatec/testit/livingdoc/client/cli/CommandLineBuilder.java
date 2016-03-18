package info.novatec.testit.livingdoc.client.cli;

import java.io.File;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;


public class CommandLineBuilder {
    private String[] cmdLine;

    public CommandLineBuilder(String cmdLineTemplate) {
        this.cmdLine = cmdLineTemplate.split(" ");
    }

    public String[] getCmdLine() {
        return cmdLine.clone();
    }

    public void setMainClass(String mainClass) {
        replace("${mainClass}", mainClass);
    }

    public void setInputPath(String inputPath) {
        replace("${inputPath}", inputPath);
    }

    public void setOutpuPath(String outputPath) {
        replace("${outputPath}", outputPath);
    }

    public void setDependencies(Collection<String> dependencies) {
        StringBuilder sb = new StringBuilder("");
        for (String dependency : dependencies) {
            sb.append(dependency);
            sb.append(File.pathSeparator);
        }

        replace("${classpaths}", sb.toString());
    }

    public void setSections(String sections) {
        if ( ! StringUtils.isEmpty(sections)) {
            addOption("-t", sections);
        }
    }

    public void setLocale(String locale) {
        replace("${locale}", locale);
    }

    public void setRepository(String repository) {
        replace("${repository}", repository);
    }

    public void setFixtureFactory(String fixtureFactory) {
        replace("${fixtureFactory}", fixtureFactory);
    }

    public void setProjectDependencyDescriptor(String projectDependencyDescriptor) {
        replace("${projectDependencyDescriptor}", projectDependencyDescriptor);
    }

    private void addOption(String cmd, String args) {
        String[] newCmdLine = new String[cmdLine.length + 2];
        System.arraycopy(cmdLine, 0, newCmdLine, 0, cmdLine.length);

        newCmdLine[newCmdLine.length - 2] = cmd;
        newCmdLine[newCmdLine.length - 1] = args;
        cmdLine = newCmdLine;
    }

    private void replace(String name, String value) {
        for (int i = 0; i < cmdLine.length; i ++ ) {
            if (cmdLine[i].equals(name)) {
                cmdLine[i] = value == null ? "" : value;
            }
        }
    }
}
