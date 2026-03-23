import { useEffect } from 'react';
import { useNavigate } from 'react-router';

export default function IndexView() {
  const navigate = useNavigate();
  useEffect(() => { navigate('/submit'); }, [navigate]);
  return null;
}
