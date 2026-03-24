import { LoginOverlay } from '@vaadin/react-components/LoginOverlay';
import { useState } from 'react';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  menu: { exclude: true },
  title: 'Login',
};

const demoCredentials = 'Demo accounts (password = email): customer@test.com (Customer) · agent@test.com (Agent) · manager@test.com (Manager)';

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
          forgotPassword: 'Forgot password',
        },
        additionalInformation: showCredentials ? demoCredentials : '',
      }}
      onForgotPassword={() => setShowCredentials(true)}
    />
  );
}
