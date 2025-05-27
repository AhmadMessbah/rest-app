import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getPerson, createPerson, updatePerson } from '../services/api';

function PersonForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ name: '', email: '' });
  const [error, setError] = useState('');

  useEffect(() => {
    if (id) {
      const fetchPerson = async () => {
        try {
          const data = await getPerson(id);
          setFormData({ name: data.name, email: data.email });
        } catch (err) {
          setError('Failed to load person');
        }
      };
      fetchPerson();
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await updatePerson(id, formData);
      } else {
        await createPerson(formData);
      }
      navigate('/');
    } catch (err) {
      setError('Failed to save person');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-center">{id ? 'Edit Person' : 'Add Person'}</h2>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700 mb-2" htmlFor="name">Name</label>
          <input
            id="name"
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            className="w-full p-2 border rounded"
            required
          />
        </div>
        <div className="mb-6">
          <label className="block text-gray-700 mb-2" htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            className="w-full p-2 border rounded"
            required
          />
        </div>
        <button type="submit" className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">
          Save
        </button>
      </form>
    </div>
  );
}

export default PersonForm;