package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.value.PageIndex;

import java.util.List;

public class AccountsPage {

    private PageIndex paging;
    private List<AccountBalance> values;

    public PageIndex getPaging() {
        return paging;
    }

    public void setPaging(PageIndex paging) {
        this.paging = paging;
    }

    public List<AccountBalance> getValues() {
        return values;
    }

    public void setValues(List<AccountBalance> values) {
        this.values = values;
    }
}


