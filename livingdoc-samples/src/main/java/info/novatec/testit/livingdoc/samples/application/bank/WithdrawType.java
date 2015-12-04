package info.novatec.testit.livingdoc.samples.application.bank;

public enum WithdrawType {
    ATM ( "ATM" ), INTERAC ( "Interact" ), PERSONAL_CHECK ( "Personal Check" );

    private final String description;

    private WithdrawType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
