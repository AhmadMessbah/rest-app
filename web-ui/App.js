import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Table, Button, Modal, TextField, Box } from '@mui/material';
import { useForm } from 'react-hook-form';

function App() {
    const [persons, setPersons] = useState([]);
    const [requests, setRequests] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState(null);
    const { register, handleSubmit, reset } = useForm();

    useEffect(() => {
        axios.get('http://localhost:8080/api/persons').then(res => setPersons(res.data));
        axios.get('http://localhost:8080/api/image-requests').then(res => setRequests(res.data));
    }, []);

    const handleShowModal = (item, type) => {
        setSelectedItem({ ...item, type });
        setShowModal(true);
    };

    const onSubmitPerson = async (data) => {
        await axios.post('http://localhost:8080/api/persons', data);
        reset();
        const res = await axios.get('http://localhost:8080/api/persons');
        setPersons(res.data);
    };

    const onSubmitImage = async (data) => {
        const formData = new FormData();
        formData.append('file', data.file[0]);
        formData.append('person_id', data.person_id);
        await axios.post('http://localhost:8080/api/image-requests', formData);
        const res = await axios.get('http://localhost:8080/api/image-requests');
        setRequests(res.data);
    };

    return (
        <Container>
            <h1>Persons</h1>
            <Box component="form" onSubmit={handleSubmit(onSubmitPerson)} sx={{ mb: 2 }}>
                <TextField label="Name" {...register('name')} required />
                <TextField label="Email" {...register('email')} required />
                <Button type="submit" variant="contained">Add Person</Button>
            </Box>
            <Table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                {persons.map(person => (
                    <tr key={person.id}>
                        <td>{person.id}</td>
                        <td>{person.name}</td>
                        <td>{person.email}</td>
                        <td>
                            <Button onClick={() => handleShowModal(person, 'person')}>View</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <h1>Image Requests</h1>
            <Box component="form" onSubmit={handleSubmit(onSubmitImage)} sx={{ mb: 2 }}>
                <TextField label="Person ID" {...register('person_id')} type="number" required />
                <input type="file" {...register('file')} accept="image/*" />
                <Button type="submit" variant="contained">Upload Image</Button>
            </Box>
            <Table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Person ID</th>
                    <th>Text</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                {requests.map(request => (
                    <tr key={request.id}>
                        <td>{request.id}</td>
                        <td>{request.person_id}</td>
                        <td>{request.text}</td>
                        <td>
                            <Button onClick={() => handleShowModal(request, 'request')}>View</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal open={showModal} onClose={() => setShowModal(false)}>
                <Box sx={{ p: 4, bgcolor: 'background.paper', m: 'auto', mt: 10, width: 400 }}>
                    <h2>{selectedItem?.type === 'person' ? 'Person Details' : 'Request Details'}</h2>
                    {selectedItem?.type === 'person' ? (
                        <div>
                            <p><strong>ID:</strong> {selectedItem.id}</p>
                            <p><strong>Name:</strong> {selectedItem.name}</p>
                            <p><strong>Email:</strong> {selectedItem.email}</p>
                        </div>
                    ) : (
                        <div>
                            <p><strong>ID:</strong> {selectedItem.id}</p>
                            <p><strong>Person ID:</strong> {selectedItem.person_id}</p>
                            <p><strong>Text:</strong> {selectedItem.text}</p>
                            <p><strong>Image:</strong> {selectedItem.image}</p>
                        </div>
                    )}
                    <Button onClick={() => setShowModal(false)}>Close</Button>
                </Box>
            </Modal>
        </Container>
    );
}

export default App;