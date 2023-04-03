package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.value.PageIndex;

import java.util.List;

public class TransactionsPage {

    private PageIndex paging;
    private TransactionAggregation aggregated;
    private List<Transaction> values;

    public PageIndex getPaging() {
        return paging;
    }

    public void setPaging(PageIndex paging) {
        this.paging = paging;
    }

    public TransactionAggregation getAggregated() {
        return aggregated;
    }

    public void setAggregated(TransactionAggregation aggregated) {
        this.aggregated = aggregated;
    }

    public List<Transaction> getValues() {
        return values;
    }

    public void setValues(List<Transaction> values) {
        this.values = values;
    }
}
