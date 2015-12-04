package info.novatec.testit.livingdoc.extensions.guice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import info.novatec.testit.livingdoc.reflect.DefaultFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;

/**
 * Taken from {livingdoc-extensions-guice} to not have a direct depencency...
 */
public class GuiceSystemUnderDevelopment extends DefaultSystemUnderDevelopment {

	private Injector injector;
	private List<String> moduleNames = new ArrayList<String>();

	public GuiceSystemUnderDevelopment() {
	}

	public GuiceSystemUnderDevelopment(String... moduleNames) {
		Collections.addAll(this.moduleNames, moduleNames);
	}

	@Override
	public Fixture getFixture(String name, String... params) throws Throwable {
		lazilyInstantiateModulesAndInjector();

		Fixture fixture;

		if (params.length != 0) {
			// When params are used, we use the PlainOldSystemUnderDevelopment to instantiate the fixture
			// these params are passed to the constructor
			// Then we use Guice to inject members
			fixture = super.getFixture(name, params);
			injector.injectMembers(fixture.getTarget());
		} else {
			// Use Guice to instantiate
			Class<?> klass = loadType(name).getUnderlyingClass();
			Object target = injector.getInstance(klass);
			fixture = new DefaultFixture(target);
		}

		return fixture;
	}

	private void lazilyInstantiateModulesAndInjector() throws ReflectiveOperationException {
		if (injector == null) {
			List<Module> modules = convertModuleNamesToModules();
			injector = Guice.createInjector(modules);
		}
	}

	private List<Module> convertModuleNamesToModules() throws ReflectiveOperationException {
		List<Module> modules = new ArrayList<Module>(moduleNames.size());
		for (String moduleName : moduleNames) {
			Class<?> klass = loadType(moduleName).getUnderlyingClass();
			modules.add((Module)klass.newInstance());
		}
		return modules;
	}

	public void addModules(String... moduleNames) {
		if (injector == null) {
			Collections.addAll(this.moduleNames, moduleNames);
		} else {
			throw new IllegalStateException("Cannot add module after a fixture has been instantiated");
		}
	}
}
