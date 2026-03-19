import { RouterConfigurationBuilder } from '@vaadin/hilla-file-router/runtime.js';
import Flow from 'Frontend/generated/flow/Flow';
import fileRoutes from 'Frontend/generated/file-routes';
import { lazy } from 'react';
import Layout from './views/@layout.js';

const MovieDetailView = lazy(() => import('./views/MovieDetailView.js'));

export const { router, routes } = new RouterConfigurationBuilder()
    .withFileRoutes(fileRoutes)
    .withReactRoutes(
      [
        {
          element: <Layout />,
          children: [
            { path: '/movie/:movieId', element: <MovieDetailView />, handle: { title: 'Movie Detail' } }
          ]
        }
      ]
    )
    .withFallback(Flow)
    .protect()
    .build();
