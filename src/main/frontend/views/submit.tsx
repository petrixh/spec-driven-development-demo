import { useState } from 'react';
import { useNavigate } from 'react-router';
import { Button } from '@vaadin/react-components/Button';
import { TextField } from '@vaadin/react-components/TextField';
import { TextArea } from '@vaadin/react-components/TextArea';
import { Select } from '@vaadin/react-components/Select';
import { Notification } from '@vaadin/react-components/Notification';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  title: 'Submit Ticket',
};

export default function SubmitView() {
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState('GENERAL');
  const [priority, setPriority] = useState('MEDIUM');
  const [description, setDescription] = useState('');
  const [titleInvalid, setTitleInvalid] = useState(false);
  const [descInvalid, setDescInvalid] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async () => {
    const titleEmpty = !title.trim();
    const descEmpty = !description.trim();
    setTitleInvalid(titleEmpty);
    setDescInvalid(descEmpty);
    if (titleEmpty || descEmpty) return;

    try {
      const ticket = await TicketEndpoint.submitTicket({
        title: title.trim(),
        category,
        priority,
        description: description.trim(),
      });
      Notification.show(`Ticket #${ticket.id} submitted successfully!`, {
        position: 'top-center',
        duration: 3000,
        theme: 'success',
      });
      navigate('/tickets');
    } catch (error: any) {
      if (error?.message?.includes('401') || error?.message?.includes('403')) {
        window.location.href = '/login';
        return;
      }
      Notification.show('Failed to submit ticket. Please try again.', {
        position: 'top-center',
        duration: 5000,
        theme: 'error',
      });
    }
  };

  return (
    <div style={{ maxWidth: '600px' }}>
      <h2>Submit a Ticket</h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-m)' }}>
        <TextField
          label="Title"
          placeholder="Brief summary of your issue"
          value={title}
          onValueChanged={(e) => { setTitle(e.detail.value); setTitleInvalid(false); }}
          required
          errorMessage="Title is required"
          invalid={titleInvalid}
        />
        <Select
          label="Category"
          value={category}
          onValueChanged={(e) => setCategory(e.detail.value)}
          items={[
            { label: 'General', value: 'GENERAL' },
            { label: 'Technical', value: 'TECHNICAL' },
            { label: 'Billing', value: 'BILLING' },
            { label: 'Access', value: 'ACCESS' },
          ]}
        />
        <Select
          label="Priority"
          value={priority}
          onValueChanged={(e) => setPriority(e.detail.value)}
          items={[
            { label: 'Low', value: 'LOW' },
            { label: 'Medium', value: 'MEDIUM' },
            { label: 'High', value: 'HIGH' },
            { label: 'Critical', value: 'CRITICAL' },
          ]}
        />
        <TextArea
          label="Description"
          placeholder="Describe your issue in detail"
          value={description}
          onValueChanged={(e) => { setDescription(e.detail.value); setDescInvalid(false); }}
          required
          errorMessage="Description is required"
          invalid={descInvalid}
          style={{ minHeight: '150px' }}
        />
        <Button theme="primary" onClick={handleSubmit}>
          Submit
        </Button>
      </div>
    </div>
  );
}
