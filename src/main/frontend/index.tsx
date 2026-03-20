import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router';
import { router } from 'Frontend/generated/routes';

createRoot(document.getElementById('outline')!).render(
  <RouterProvider router={router} />
);
