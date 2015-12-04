package info.novatec.testit.livingdoc.fixture.ognl;

import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.ognl.OgnlResolution;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class MemberResolutionPrecedenceFixture {
    private List<String> list = new ArrayList<String>();

    public MemberResolutionPrecedenceFixture(String param) {
        OgnlResolution resolver = new OgnlResolution(param);
        list.addAll(resolver.expressionsListToResolve());
    }

    public List<String> query() {
        return list;
    }
}
