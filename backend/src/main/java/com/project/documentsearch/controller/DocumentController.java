package com.project.documentsearch.controller;
import com.project.documentsearch.model.Document;
import com.project.documentsearch.service.DocumentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService documentService;
    public DocumentController(DocumentService documentService)
    {
        this.documentService = documentService;
    }

    @PostMapping("/index")
    public String indexAllDocuments(@RequestParam("folder") String folderPath)
    {
        try
        {
            documentService.indexPdfs(folderPath);
            return "Indexing completed successfully.";
        }
        catch (Exception e)
        {
            return "Indexing failed: " + e.getMessage();
        }
    }
    @GetMapping("/search")
    public List<Document> searchDocuments(@RequestParam("q") String keyword)
    {
        return documentService.searchDocs(keyword);
    }
    @GetMapping("/semantic-search")
    public List<Document> semanticSearch(@RequestParam("q") String query)
    {
        return documentService.semanticSearch(query);
    }
}


