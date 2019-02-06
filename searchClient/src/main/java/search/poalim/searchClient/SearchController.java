package search.poalim.searchClient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
public class SearchController {

    @Autowired
    SolrClient solrClient;

    @GetMapping("/{id}")
    public String getName(@PathVariable String id) throws Exception {
        return solrClient.getCustomer(id);
    }


    @GetMapping("/customer/{id}/{name}")
    public String index(@PathVariable String id, @PathVariable String name) throws Exception{
        solrClient.addCustomer(id,name);
        return "OK";
    }


}
