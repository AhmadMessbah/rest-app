import { useState } from 'react';
import { uploadImage } from '../services/api';

function ImageUpload() {
  const [file, setFile] = useState(null);
  const [extractedText, setExtractedText] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      setError('Please select an image');
      return;
    }
    try {
      const formData = new FormData();
      formData.append('file', file);
      const data = await uploadImage(formData);
      setExtractedText(data.extracted_text);
      setError('');
    } catch (err) {
      setError('Failed to process image');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-center">Upload Image</h2>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700 mb-2" htmlFor="file">Select Image</label>
          <input
            id="file"
            type="file"
            accept="image/*"
            onChange={(e) => setFile(e.target.files[0])}
            className="w-full p-2 border rounded"
          />
        </div>
        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">
          Upload
        </button>
      </form>
      {extractedText && (
        <div className="mt-6">
          <h3 className="text-lg font-semibold">Extracted Text:</h3>
          <p className="p-2 bg-gray-100 rounded">{extractedText}</p>
        </div>
      )}
    </div>
  );
}

export default ImageUpload;