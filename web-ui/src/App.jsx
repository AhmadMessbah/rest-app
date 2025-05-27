import { Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header';
import LoginForm from './components/LoginForm';
import PersonList from './components/PersonList';
import PersonForm from './components/PersonForm';
import ImageUpload from './components/ImageUpload';
import ImageSearch from './components/ImageSearch';
import { useAuth } from './services/auth';

function App() {
    const { token } = useAuth();

    return (
        <div className="min-h-screen bg-gray-100">
            <Header />
            <main className="container mx-auto p-4">
                <Routes>
                    <Route path="/login" element={token ? <Navigate to="/" /> : <LoginForm />} />
                    <Route path="/" element={token ? <PersonList /> : <Navigate to="/login" />} />
                    <Route path="/persons/new" element={token ? <PersonForm /> : <Navigate to="/login" />} />
                    <Route path="/persons/edit/:id" element={token ? <PersonForm /> : <Navigate to="/login" />} />
                    <Route path="/images/upload" element={token ? <ImageUpload /> : <Navigate to="/login" />} />
                    <Route path="/images/search" element={token ? <ImageSearch /> : <Navigate to="/login" />} />
                </Routes>
            </main>
        </div>
    );
}

export default App;