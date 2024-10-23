
// apiTest.js
import api from './api';

const testApi = async () => {
  try {
    console.log('Testing GET all ideas...');
    const ideas = await api.ideas.getAll();
    console.log('GET success:', ideas);

    console.log('Testing POST new idea...');
    const newIdea = await api.ideas.create('Test idea ' + new Date().toISOString());
    console.log('POST success:', newIdea);

    if (ideas.mData && ideas.mData.length > 0) {
      const ideaId = ideas.mData[0].mId;
      console.log('Testing PUT like idea...', ideaId);
      const likeResult = await api.ideas.like(ideaId);
      console.log('PUT success:', likeResult);
    }
  } catch (error) {
    console.error('Test failed:', error);
  }
};

export { testApi };