package search.poalim.searchClient;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SolrClient {

    @Value("${solr.url}")
    String url;

    private HttpSolrClient solrClient;

    HttpSolrClient solr = new HttpSolrClient.Builder(url).build();

    public SolrClient() {

        solrClient = new HttpSolrClient.Builder(url).build();
        solrClient.setParser(new XMLResponseParser());
    }


    public String addCustomer(String id,String name) throws Exception {

            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", id);
            document.addField("name", name);
            solrClient.add(document);
            solrClient.commit();
            return "OK";

    }

    public String getCustomer(String id) throws Exception{

        try {
            SolrQuery query = new SolrQuery();
            query.set("q", "price:599.99");
            QueryResponse response = solr.query(query);

            SolrDocumentList docList = response.getResults();

            if (docList.isEmpty())
                return null;

            return (String) docList.get(0).getFieldValue("name");
        }
        catch (Exception e)
        {
            throw new Exception("error insert to db");
        }

    }


}
