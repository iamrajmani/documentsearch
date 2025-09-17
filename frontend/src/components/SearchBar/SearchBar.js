import React, { useState } from "react";
import "./SearchBar.css";

const SearchBar = ({ onSearch }) => {
  const [searchTexts, setSearchTexts] = useState("");

  const search = () => {
    if (!searchTexts.trim()) return;
    onSearch(searchTexts);
  };

  const onEnterPress = (e) => {
    if (e.key === "Enter") 
      {
      search();
      }
  };

  return (
    <div className="search-page">
      <div className="search-box">
        <input
          type="text"
          value={searchTexts}
          placeholder="Enter texts to search..."
          onChange={(e) => setSearchTexts(e.target.value)}
          onKeyDown={onEnterPress}
        />
        <button onClick={search}>Search</button>
      </div>
    </div>
  );
};

export default SearchBar;
