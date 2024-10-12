import React, { useState } from 'react';
import './IdeaForm.css';

const IdeaForm = ({ onIdeaCreated }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [author, setAuthor] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!title || !description || !author) {
      alert('All fields are required');
      return;
    }
    // Submit the idea ( placeholder)
    console.log({ title, description, author });
    onIdeaCreated();
    setTitle('');
    setDescription('');
    setAuthor('');
  };

  return (
    <form className="idea-form" onSubmit={handleSubmit}>
      <label htmlFor="title">Title</label>
      <input
        type="text"
        id="title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <label htmlFor="description">Description</label>
      <textarea
        id="description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <label htmlFor="author">Author</label>
      <input
        type="text"
        id="author"
        value={author}
        onChange={(e) => setAuthor(e.target.value)}
      />
      <button type="submit">Submit Idea</button>
    </form>
  );
};

export default IdeaForm;