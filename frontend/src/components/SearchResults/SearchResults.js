import React from "react";
import "./SearchResults.css";

const SearchResults = ({ results }) => {
  return (
    <div className="results-container">
      {(!results || results.length === 0) ? (
        <p className="no-results">No results found</p>
      ) : (
        <table className="results-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>Content</th>
            </tr>
          </thead>
          <tbody>
            {results.map((item, index) => (
              <tr key={index}>
                <td>{index + 1}</td>
                <td>{item.title}</td>
                <td>{item.content}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default SearchResults;

