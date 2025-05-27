import { useState } from 'react';
import { searchImages } from '../services/api';

function ImageSearch() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await searchImages(query);
      setResults(data);
      setError('');
    } catch (err) {
      setError('Failed to search images');
    }
  };

  return (
    <div className="max-w-2xl mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-center">Search Images</h2>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      <form onSubmit={handleSubmit} className="mb-6">
        <div className="flex">
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Enter search query"
            className="flex-1 p-2 border rounded-l"
          />
          <button type="submit" className="bg-blue-600 text-white p-2 rounded-r hover:bg-blue-700">
            Search
          </button>
        </div>
      </form>
      <div className="grid gap-4">
        {results.map((result) => (
          <div key={result.id} className="p-4 bg-gray-100 rounded">
            <p><strong>Text:</strong> {result.extracted_text}</p>
            <p><strong>Created:</strong> {new Date(result.created_at).toLocaleString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ImageSearch;