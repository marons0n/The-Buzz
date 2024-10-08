import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <h1>Welcome to The Buzz</h1>
        <p>
          Share your ideas and connect with others. Like and share posts to keep the buzz going!
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
      <main>
        <section className="posts">
          <h2>Latest Posts</h2>
          {/* Add components or code to display posts here */}
        </section>
        <section className="share-idea">
          <h2>Share Your Idea</h2>
          {/* Add components or code to share a new idea here */}
        </section>
      </main>
    </div>
  );
}

export default App;
