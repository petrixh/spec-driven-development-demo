import { LoginOverlay } from '@vaadin/react-components/LoginOverlay';
import { useState } from 'react';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  menu: { exclude: true },
  title: 'Login',
};

const demoCredentials = `Demo accounts:
  customer@test.com / password (Customer)
  agent@test.com / password (Agent)
  manager@test.com / password (Manager)`;

export default function LoginView() {
  const [showCredentials, setShowCredentials] = useState(false);

  return (
    <LoginOverlay
      opened
      title="re:solve"
      description="Help desk, simplified."
      action="login"
      i18n={{
        form: {
          title: 'Log in',
          username: 'Username',
          password: 'Password',
          submit: 'Log in',
          forgotPassword: 'Show demo credentials',
        },
        additionalInformation: showCredentials ? demoCredentials : '',
      }}
      onForgotPassword={() => setShowCredentials(true)}
    />
  );
}
