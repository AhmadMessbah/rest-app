import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getPersons, deletePerson } from '../services/api';

function PersonList() {
    const [persons, setPersons] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchPersons = async () => {
            try {
                const data = await getPersons();
                setPersons(data);
            } catch (err) {
                setError('Failed to load persons');
            }
        };
        fetchPersons();
    }, []);

    const handleDelete = async (id) => {
        try {
            await deletePerson(id);
            setPersons(persons.filter((person) => person.id !== id));
        } catch (err) {
            setError('Failed to delete person');
        }
    };

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Persons</h2>
            <Link to="/persons/new" className="inline-block bg-blue-600 text-white p-2 rounded mb-4">
                Add Person
            </Link>
            {error && <p className="text-red-500 mb-4">{error}</p>}
            <div className="grid gap-4">
                {persons.map((person) => (
                    <div key={person.id} className="p-4 bg-white rounded shadow">
                        <p><strong>Name:</strong> {person.name}</p>
                        <p><strong>Email:</strong> {person.email}</p>
                        <div className="mt-2 space-x-2">
                            <Link to={`/persons/edit/${person.id}`} className="text-blue-600 hover:underline">Edit</Link>
                            <button onClick={() => handleDelete(person.id)} className="text-red-600 hover:underline">
                                Delete
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default PersonList;