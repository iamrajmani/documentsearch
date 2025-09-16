package com.project.documentsearch.controller;

import com.project.documentsearch.model.Document;
import com.project.documentsearch.service.DocumentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @PostMapping("/index")
    public String indexAllDocuments(){
        documentService.indexAllPdfs("/Users/rajmani/Desktop/Document search/pdf files");
        return "Indexing successfully done.";
    }

    @GetMapping("/search")
    public List<Document> searchDocumentsByKeyword(@RequestParam("q") String keyword){
        return documentService.searchByKeyword(keyword);
    }
}

