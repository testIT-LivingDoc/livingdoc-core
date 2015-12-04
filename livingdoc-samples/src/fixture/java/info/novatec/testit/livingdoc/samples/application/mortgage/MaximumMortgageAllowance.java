package info.novatec.testit.livingdoc.samples.application.mortgage;

public class MaximumMortgageAllowance
{
    private float commercialEvaluation;
    private float purchasedPrice;
    
    public float mortgageAllowance()
    {
        return MortgageBalanceCalculator.getMortgageAllowance(commercialEvaluation, purchasedPrice);
    }
    
    public void setCommercialEvaluation(float commercialEvaluation)
    {
        this.commercialEvaluation = commercialEvaluation;
    }
    
    public void setPurchasedPrice(float purchasedPrice)
    {
        this.purchasedPrice = purchasedPrice;
    }
}
