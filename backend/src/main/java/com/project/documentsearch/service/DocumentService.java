package com.project.documentsearch.service;
import com.project.documentsearch.model.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.Embedding;

@Service
public class DocumentService {

    private final RestHighLevelClient client;
    private final OpenAiService openAiService;

    public DocumentService(RestHighLevelClient client){
        this.client = client;
        String apiKey = System.getenv(API_KEY);
        this.openAiService = new OpenAiService(System.getenv("API_KEY"));;
    }

    public void indexPdfs(String folderPath){
        File folder = new File(folderPath);
        if(!folder.exists() || !folder.isDirectory()){
            System.out.println("Folder not found " + folderPath);
            return;
        }
        File[] pdfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".pdf");
            }});
        if(pdfFiles != null){
            for(File file : pdfFiles){
                try(PDDocument pdf = PDDocument.load(file)){
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(pdf);
                    Map<String,Object> docMap = new HashMap<>();
                    docMap.put("id", UUID.randomUUID().toString());
                    docMap.put("title", file.getName());
                    docMap.put("content", text);
                    docMap.put("path", file.getAbsolutePath());
                    List<Float> embedding = generateEmbedding(text);
                    docMap.put("embedding", embedding);
                    IndexRequest req = new IndexRequest("documents")
                            .id(docMap.get("id").toString())
                            .source(docMap);
                    client.index(req, RequestOptions.DEFAULT);
                    System.out.println("Indexed: " + file.getName());
                } catch(IOException e){
                    System.err.println("Can't able to index file: " + file.getName() + " -> " + e.getMessage());
                }
            }
        }
    }
    public List<Document> searchDocs(String keyword){
        List<Document> docs = new ArrayList<>();
        SearchRequest searchReq = new SearchRequest("documents");
        SearchSourceBuilder source = new SearchSourceBuilder();
        source.query(QueryBuilders.matchQuery("content", keyword));
        searchReq.source(source);
        try
        {
            SearchResponse resp = client.search(searchReq, RequestOptions.DEFAULT);
            resp.getHits().forEach(hit -> {
                Map<String,Object> m = hit.getSourceAsMap();
                Document d = new Document();
                d.setId(m.get("id").toString());
                d.setTitle(m.get("title").toString());
                d.setContent(m.get("content").toString());
                d.setPath(m.get("path") != null ? m.get("path").toString() : "");
                docs.add(d);
            });
        }
        catch(IOException e)
        {
            System.err.println("Keyword search failed: " + e.getMessage());
        }
        return docs;
    }

    public List<Document> semanticSearch(String query)
    {
        List<Document> docs = new ArrayList<>();
        List<Float> queryEmbedding = generateEmbedding(query);

        SearchRequest searchReq = new SearchRequest("documents");
        SearchSourceBuilder source = new SearchSourceBuilder();
        source.query(QueryBuilders
                .scriptScoreQuery(
                        QueryBuilders.matchAllQuery(),
                        "cosineSimilarity(params.query_vector, 'embedding') + 1.0"
                )
                .scriptParams(Collections.singletonMap("query_vector", queryEmbedding))
        );
        searchReq.source(source);

        try
        {
            SearchResponse resp = client.search(searchReq, RequestOptions.DEFAULT);
            resp.getHits().forEach(hit -> {
                Map<String,Object> m = hit.getSourceAsMap();
                Document d = new Document();
                d.setId(m.get("id").toString());
                d.setTitle(m.get("title").toString());
                d.setContent(m.get("content").toString());
                d.setPath(m.get("path") != null ? m.get("path").toString() : "");
                docs.add(d);
            });
        }
        catch(IOException e)
        {
            System.err.println("Semantic search failed: " + e.getMessage());
        }

        return docs;
    }

    private List<Float> generateEmbedding(String text){
        EmbeddingRequest req = EmbeddingRequest.builder()
                .model("text-embedding-3-small")
                .input(text)
                .build();
        List<Embedding> resp = openAiService.createEmbeddings(req).getData();
        return resp.get(0).getEmbedding();
    }
}



