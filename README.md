A full stack project where you can search through PDF documents easily. The system extracts text from PDFs, stores it in OpenSearch, and allows users to query documents from a simple React.js frontend.

 Features :

Upload and index PDF documents

Full-text search on document content

REST API built with Spring Boot

Frontend built in React.js

Uses OpenSearch as the search engine

Handles loading and error states in UI


Exposes REST endpoints:

/api/index → index PDF documents.

/api/search?q=keyword → search documents.

Extracts text from PDFs using Apache PDFBox.

Connects with OpenSearch client to index and search data.


OpenSearch :

Stores and indexes documents.

Handles full-text search queries.

Returns ranked search results.


Tech Stack

Frontend: React.js, CSS

Backend: Java 17, Spring Boot, Maven


Libraries:

Apache PDFBox (text extraction)

Jackson Databind (JSON processing)

HttpClient5 (API requests)

Search Engine: OpenSearch 2.9.0

Version Control: Git + GitHub

Setup & Installation :

1. Clone the Repo
git clone https://github.com/iamrajmani/documentsearch.git
cd documentsearch

2. Backend Setup
   
Go into backend folder:
cd backend

Build and run using Maven:

mvn clean install
mvn spring-boot:run

Backend will start at:
http://localhost:8080

3. Frontend Setup

Go into frontend folder:
cd frontend
Install dependencies:

npm install
Run React app:

npm start

Frontend will run at:
http://localhost:3000

4. OpenSearch Setup

Make sure OpenSearch is running locally (default: http://localhost:9200).
If using Docker:

docker run -d --name opensearch -p 9200:9200 -e "discovery.type=single-node" opensearchproject/opensearch:2.9.0



 How It Works: 

User enters query in frontend search bar.

React sends request → http://localhost:8080/api/search?q=yourKeyword.

Backend searches documents in OpenSearch.

Results are returned as JSON and displayed in React UI.
