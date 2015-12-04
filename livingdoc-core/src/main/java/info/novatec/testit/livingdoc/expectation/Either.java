package info.novatec.testit.livingdoc.expectation;

public class Either implements Collator {
    private Expectation expectation;

    public Either(Expectation expectation) {
        this.expectation = expectation;
    }

    public Either or(Expectation other) {
        this.expectation = new OrExpectation(this.expectation, other);
        return this;
    }

    public Either negate() {
        this.expectation = new NotExpectation(this.expectation);
        return this;
    }

    @Override
    public Expectation toExpectation() {
        return expectation;
    }
}
