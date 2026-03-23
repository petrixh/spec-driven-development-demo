import { LoginOverlay } from '@vaadin/react-components/LoginOverlay';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  menu: { exclude: true },
  title: 'Login',
};

export default function LoginView() {
  return (
    <LoginOverlay
      opened
      title="re:solve"
      description="Help desk, simplified."
      action="login"
    />
  );
}
