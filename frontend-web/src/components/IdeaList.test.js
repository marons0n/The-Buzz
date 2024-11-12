import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import IdeaList from './IdeaList';
import { getIdeas } from '../services/api';

jest.mock('../services/api');

describe('IdeaList', () => {
  test('renders ideas', async () => {
    const mockIdeas = [
      { id: 1, title: 'Test Idea 1', description: 'Description 1', likes: 0 },
      { id: 2, title: 'Test Idea 2', description: 'Description 2', likes: 1 },
    ];

    getIdeas.mockResolvedValue({ data: mockIdeas });

    render(<IdeaList />);

    await waitFor(() => {
      expect(screen.getByText('Test Idea 1')).toBeInTheDocument();
      expect(screen.getByText('Test Idea 2')).toBeInTheDocument();
    });
  });
});