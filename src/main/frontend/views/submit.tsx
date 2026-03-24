import { useState } from 'react';
import { useNavigate } from 'react-router';
import { Button } from '@vaadin/react-components/Button';
import { TextField } from '@vaadin/react-components/TextField';
import { TextArea } from '@vaadin/react-components/TextArea';
import { Select } from '@vaadin/react-components/Select';
import { Notification } from '@vaadin/react-components/Notification';
import { TicketEndpoint } from 'Frontend/generated/endpoints';
import Category from 'Frontend/generated/com/example/specdriven/domain/Category';
import Priority from 'Frontend/generated/com/example/specdriven/domain/Priority';

export default function SubmitTicketView() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState<string>('GENERAL');
  const [priority, setPriority] = useState<string>('MEDIUM');
  const [titleError, setTitleError] = useState('');
  const [descriptionError, setDescriptionError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const categoryItems = Object.values(Category).map(c => ({ label: c.charAt(0) + c.slice(1).toLowerCase(), value: c }));
  const priorityItems = Object.values(Priority).map(p => ({ label: p.charAt(0) + p.slice(1).toLowerCase(), value: p }));

  async function handleSubmit() {
    let valid = true;

    if (!title.trim()) {
      setTitleError('Title is required');
      valid = false;
    } else {
      setTitleError('');
    }

    if (!description.trim()) {
      setDescriptionError('Description is required');
      valid = false;
    } else {
      setDescriptionError('');
    }

    if (!valid) return;

    setSubmitting(true);
    try {
      const ticket = await TicketEndpoint.submitTicket({
        title: title.trim(),
        description: description.trim(),
        category: category as Category,
        priority: priority as Priority,
      });
      Notification.show(`Ticket #${ticket.id} created successfully`, {
        position: 'top-center',
        duration: 3000,
        theme: 'success',
      });
      navigate('/tickets');
    } catch (error) {
      Notification.show('Failed to create ticket', {
        position: 'top-center',
        duration: 5000,
        theme: 'error',
      });
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div style={{ maxWidth: '600px' }}>
      <h2 style={{ fontSize: 'var(--aura-font-size-xl)', marginTop: 0 }}>Submit a Ticket</h2>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--vaadin-space-m)' }}>
        <TextField
          label="Title"
          required
          value={title}
          onValueChanged={(e) => { setTitle(e.detail.value); setTitleError(''); }}
          errorMessage={titleError}
          invalid={!!titleError}
          placeholder="Brief summary of your issue"
          style={{ width: '100%' }}
        />

        <Select
          label="Category"
          items={categoryItems}
          value={category}
          onValueChanged={(e) => setCategory(e.detail.value)}
          style={{ width: '100%' }}
        />

        <Select
          label="Priority"
          items={priorityItems}
          value={priority}
          onValueChanged={(e) => setPriority(e.detail.value)}
          style={{ width: '100%' }}
        />

        <TextArea
          label="Description"
          required
          value={description}
          onValueChanged={(e) => { setDescription(e.detail.value); setDescriptionError(''); }}
          errorMessage={descriptionError}
          invalid={!!descriptionError}
          placeholder="Describe your issue in detail"
          style={{ width: '100%', minHeight: '150px' }}
        />

        <Button
          theme="primary"
          onClick={handleSubmit}
          disabled={submitting}
        >
          Submit
        </Button>
      </div>
    </div>
  );
}
