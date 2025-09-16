package com.project.documentsearch.service;

import com.project.documentsearch.model.Document;
import com.project.documentsearch.repository.DocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository repo;

    public DocumentService(DocumentRepository repo){
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        String folderPath = "/Users/rajmani/Desktop/Document search/pdf files";
        indexPdfs(folderPath);
    }

    public void indexPdfs(String folderPath){
        File dir = new File(folderPath);
        if(!dir.isDirectory()){
            System.out.println("It's Not a valid folder : " + folderPath);
            return;
        }

        File[] pdfs = dir.listFiles((d,name)-> name.toLowerCase().endsWith(".pdf"));
        if(pdfs!=null){
            for(File file: pdfs){
                try(PDDocument pdf = PDDocument.load(file)){
                    PDFTextStripper st = new PDFTextStripper();
                    String txt=st.getText(pdf);

                    Document doc=new Document();
                    doc.setId(UUID.randomUUID().toString());
                    doc.setTitle(file.getName());
                    doc.setContent(txt);

                    repo.save(doc);
                    System.out.println("done -> "+file.getName());
                } catch(IOException e){
                    System.err.println("fail on "+file.getName()+" : "+e.getMessage());
                }
            }
        }
    }
}

