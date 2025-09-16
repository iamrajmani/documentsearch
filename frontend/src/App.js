import SearchBar from "./searchPage"; 

function App() {
   const searchDocuments = (query) => {
      console.log("Searching: ", query)
      
   }
   return (
     <div>
        <SearchBar onSearch={searchDocuments} />
     </div>
   )
}

export default App;


