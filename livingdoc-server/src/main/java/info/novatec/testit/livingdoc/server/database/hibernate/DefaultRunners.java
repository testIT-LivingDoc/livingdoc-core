package info.novatec.testit.livingdoc.server.database.hibernate;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.database.SessionService;
import info.novatec.testit.livingdoc.server.domain.ClasspathSet;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.dao.SystemUnderTestDao;
import info.novatec.testit.livingdoc.server.domain.dao.hibernate.HibernateSystemUnderTestDao;
import info.novatec.testit.livingdoc.util.URIUtil;


public class DefaultRunners {

    private static final Logger log = LoggerFactory.getLogger(DefaultRunners.class);

    private final SystemUnderTestDao sutDao;
    private final Properties properties;

    private String version;
    private String jarFile;

    public DefaultRunners(SessionService sessionService, Properties properties) {
        this(new HibernateSystemUnderTestDao(sessionService), properties);
    }

    public DefaultRunners(SystemUnderTestDao systemUnderTestDao, Properties properties) {
        this.sutDao = systemUnderTestDao;
        this.properties = properties;
    }

    public void insertJavaRunner() {
        // Used for OSGI bundles (supported by LD 4.x.x and greater).
        String livingDocPath = properties.getProperty("livingdoc.path");
        if (livingDocPath != null) {
            File livingDocJarFile = new File(livingDocPath);
            insertJavaRunnerFromFile(livingDocJarFile);
        }

        String livingDocHome = properties.getProperty("livingdoc.home");
        if (livingDocHome != null) {
            File livingDocHomeDir = new File(livingDocHome);
            insertJavaRunnerFromHome(livingDocHomeDir);
        }

        String basePath = properties.getProperty("baseUrl", null);
        if (basePath != null) {
            File libDir = new File(basePath, "WEB-INF/lib");
            insertJavaRunnerFromDir(libDir);
        }

        String confluenceHome = properties.getProperty("confluence.home", null);
        if (confluenceHome != null) {
            File pluginCacheDir = new File(confluenceHome, "plugin-cache");
            insertJavaRunnerFromDir(pluginCacheDir);
        }
    }

    private boolean shouldCreateJavaRunner() {
        return version != null && sutDao.getRunnerByName("LDCore JAVA v. " + version) == null;
    }

    private void insertJavaRunnerFromFile(File runnerJarFile) {
        try {
            version = detectVersion(runnerJarFile);
            jarFile = runnerJarFile.getAbsolutePath();
            ClasspathSet classpaths = new ClasspathSet();
            classpaths.add(normalize(runnerJarFile));
            File runnerDir = runnerJarFile.getParentFile();
            createJavaRunner(classpaths, normalize(runnerDir));
        } catch (IOException e) {
            log.warn("Runner registration failed: " + e.getMessage());
        } catch (LivingDocServerException e) {
            log.warn("Runner registration failed: " + e.getMessage());
        }
    }

    private void insertJavaRunnerFromDir(File dir) {
        try {
            detect(dir, "^livingdoc\\-(confluence|xwiki)4??\\-plugin\\-(.+)\\-complete\\.jar$");

            if (shouldCreateJavaRunner()) {
                createJavaRunner(getJavaRunnerClassPathsFromDir(dir), jarFile);
            }
        } catch (IOException e) {
            log.warn("Runner registration failed: " + e.getMessage());
        } catch (LivingDocServerException e) {
            log.warn("Runner registration failed: " + e.getMessage());
        }
    }

    private ClasspathSet getJavaRunnerClassPathsFromDir(File directory) throws IOException {

        ClasspathSet paths = new ClasspathSet();
        paths.add(String.format("%s/%s", normalize(directory), jarFile));
        return paths;
    }

    private void detect(File srcDir, String regExLib) {
        version = null;
        jarFile = null;

        Pattern pattern = Pattern.compile(regExLib);

        String[] files = srcDir.list();
        if (files != null) {

            for (String file : files) {
                Matcher matcher = pattern.matcher(file);

                if (matcher.find()) {
                    int groupCount = matcher.groupCount();
                    version = matcher.group(groupCount); // always use the last
                                                         // group
                    jarFile = file;
                    break;
                }
            }
        }
    }

    private String detectVersion(File livingDocJarFile) {
        String livingDocJarFileName = livingDocJarFile.getName();
        // E.g. livingdoc-confluence-plugin-1.0-SNAPSHOT.jar
        return livingDocJarFileName.replace("livingdoc-confluence-plugin-", "").replace(".jar", "");
    }

    private void createJavaRunner(ClasspathSet classpaths, String hint) throws LivingDocServerException {

        log.info(String.format("Registrating Runner: LDCore JAVA v. %s (%s)", version, hint));
        Runner runner = Runner.newInstance("LDCore JAVA v. " + version);
        runner.setClasspaths(classpaths);
        sutDao.create(runner);
    }

    private void insertJavaRunnerFromHome(File homeDir) {

        try {
            File runnerDir = new File(homeDir, "java/runner");
            detect(runnerDir, "^livingdoc\\-core\\-(.+)\\.jar$");

            if (shouldCreateJavaRunner()) {
                ClasspathSet classpaths = new ClasspathSet();

                File coreFile = new File(runnerDir, String.format("livingdoc-core-%s.jar", version));
                File codecFile = new File(runnerDir, "commons-codec-1.3.jar");
                File xmlrpcFile = new File(runnerDir, "xmlrpc-2.0.1.jar");

                if (codecFile.exists() && xmlrpcFile.exists()) {
                    classpaths.add(normalize(coreFile));
                    classpaths.add(normalize(codecFile));
                    classpaths.add(normalize(xmlrpcFile));
                    createJavaRunner(classpaths, normalize(runnerDir));
                }
            }
        } catch (IOException e) {
            log.warn("Runner registration failed: " + e.getMessage());
        } catch (LivingDocServerException e) {
            log.warn("Runner registration failed: " + e.getMessage());
        }
    }

    private String normalize(File file) throws IOException {
        return URIUtil.decoded(file.getCanonicalPath());
    }
}
