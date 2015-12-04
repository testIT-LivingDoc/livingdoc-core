package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Example;


public class AnnotateSetup implements Stub {
    private final Example row;

    public AnnotateSetup(Example row) {
        this.row = row;
    }

    @Override
    public void call(Result result) {
        Example newLastCell = row.addChild();

        if (result.isRight()) {
            Annotate.entered(newLastCell).call(result);
        }
        if (result.isWrong()) {
            Annotate.notEntered(newLastCell).call(result);
        }
        if (result.isException()) {
            Annotate.exception(newLastCell).call(result);
        }
        if (result.isIgnored()) {
            Annotate.entered(newLastCell).call(result);
        }
    }
}
