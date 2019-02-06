package search.poalim.searchClient;

import org.apache.solr.client.solrj.beans.Field;

public class Customer {

    String id;
    String name ;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Field("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Field("name")
    public void setName(String name) {
        this.name = name;
    }
}
