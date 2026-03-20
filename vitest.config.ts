import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
import { fileURLToPath } from 'url';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      'Frontend': fileURLToPath(new URL('./src/main/frontend', import.meta.url)),
    },
  },
  test: {
    environment: 'jsdom',
    include: ['src/test/frontend/**/*.test.{ts,tsx}'],
    globals: true,
    setupFiles: ['src/test/frontend/setup.ts'],
  },
});
