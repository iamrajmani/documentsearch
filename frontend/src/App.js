import React, { useState } from "react";
import SearchBar from "./components/SearchBar/SearchBar";
import SearchResults from "./components/SearchResults/SearchResults";
import "./App.css";

function App() {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [useSemantic, setUseSemantic] = useState(false); 
  const searchDocuments = async (searchText) => {
    if (!searchText.trim()) return;

    setLoading(true);
    setError(null);

    try
     {
      const endpoint = useSemantic 
        ? "http://localhost:8080/api/semantic-search" 
        : "http://localhost:8080/api/search";

      const res = await fetch(`${endpoint}?q=${encodeURIComponent(searchText)}`);

      if (!res.ok) throw new Error("Error connecting to API");

      const data = await res.json();
      setResults(data);
    } 
    catch (error) 
    {
      console.log(error);
      setError("Something went wrong");
    } finally 
    {
      setLoading(false);
    }
  };

  return (
    <div className="app-container">
      <h1>Search Documents...</h1>

      <div style={{ marginBottom: "20px" }}>
        <label>
          <input
            type="checkbox"
            checked={useSemantic}
            onChange={() => setUseSemantic(!useSemantic)}
          />
          {" "}Tick here to Use Semantic Search
        </label>
      </div>

      <div className="search-results-wrapper">
        <SearchBar onSearch={searchDocuments} />
        {loading && <p className="loading">Loading...</p>}
        {error && <p className="error">{error}</p>}
        <SearchResults results={results} />
      </div>
    </div>
  );
}

export default App;

