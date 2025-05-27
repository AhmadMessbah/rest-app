import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../services/auth';

function Header() {
    const { token, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="bg-blue-600 p-4 shadow-md">
            <div className="container mx-auto flex justify-between items-center">
                <Link to="/" className="text-white text-2xl font-bold">Web UI</Link>
                <div className="space-x-4">
                    {token ? (
                        <>
                            <Link to="/" className="text-white hover:text-gray-200">Persons</Link>
                            <Link to="/images/upload" className="text-white hover:text-gray-200">Upload Image</Link>
                            <Link to="/images/search" className="text-white hover:text-gray-200">Search Images</Link>
                            <button onClick={handleLogout} className="text-white hover:text-gray-200">Logout</button>
                        </>
                    ) : (
                        <Link to="/login" className="text-white hover:text-gray-200">Login</Link>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Header;