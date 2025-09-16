package com.project.documentsearch.service;

import com.project.documentsearch.model.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class DocumentService {

    private RestHighLevelClient client;

    public DocumentService(RestHighLevelClient client){
        this.client = client;
    }

    public void indexPdfs(String folderPath){
        File folder = new File(folderPath);
        if(!folder.isDirectory()){
            System.out.println("It's Not a valid folder: " + folderPath);
            return;
        }

        File[] pdfFiles = folder.listFiles((f,name) -> name.toLowerCase().endsWith(".pdf"));
        if(pdfFiles != null){
            for(File pdfFile : pdfFiles){
                try(PDDocument pdf = PDDocument.load(pdfFile)){
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(pdf);

                    Map<String,Object> docMap = new HashMap<>();
                    docMap.put("id", UUID.randomUUID().toString());
                    docMap.put("title", pdfFile.getName());
                    docMap.put("content", text);

                    IndexRequest req = new IndexRequest("documents")
                            .id(docMap.get("id").toString())
                            .source(docMap);

                    IndexResponse resp = client.index(req, RequestOptions.DEFAULT);
                    System.out.println("Indexed: " + pdfFile.getName());

                }catch(IOException e){
                    System.err.println("Request Failed: " + pdfFile.getName() + " -> " + e.getMessage());
                }
            }
        }
    }

    public List<Document> searchDocs(String keyword){
        List<Document> resultList = new ArrayList<>();

        SearchRequest searchReq = new SearchRequest("documents");
        SearchSourceBuilder source = new SearchSourceBuilder();
        source.query(QueryBuilders.matchQuery("content", keyword));
        searchReq.source(source);

        try{
            SearchResponse response = client.search(searchReq, RequestOptions.DEFAULT);
            response.getHits().forEach(hit -> {
                Map<String,Object> m = hit.getSourceAsMap();
                Document d = new Document();
                d.setId(m.get("id").toString());
                d.setTitle(m.get("title").toString());
                d.setContent(m.get("content").toString());
                resultList.add(d);
            });
        }catch(IOException e){
            System.err.println("Search failed: " + e.getMessage());
        }

        return resultList;
    }
}
