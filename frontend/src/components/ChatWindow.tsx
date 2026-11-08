import React, { useState } from 'react';

export const ChatWindow: React.FC = () => {
  const [document, setDocument] = useState('');
  const [query, setQuery] = useState('');
  const [answer, setAnswer] = useState('');
  const [context, setContext] = useState<string[]>([]);
  const [ingestStatus, setIngestStatus] = useState('');

  const handleIngest = async () => {
    const res = await fetch('http://localhost:8080/api/rag/ingest', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ document }),
    });
    const data = await res.json();
    setIngestStatus(`Indexed ${data.totalChunksIndexed} total chunks successfully!`);
  };

  const handleQuery = async (e: React.FormEvent) => {
    e.preventDefault();
    const res = await fetch('http://localhost:8080/api/rag/query', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query }),
    });
    const data = await res.json();
    setAnswer(data.groundedAnswer);
    setContext(data.retrievedContext);
  };

  return (
    <div style={{ maxWidth: '700px', margin: '2rem auto', fontFamily: 'Arial, sans-serif' }}>
      <h2>DocIntellect: Enterprise RAG Demo</h2>

      {/* Ingestion Section */}
      <div style={{ border: '1px solid #ccc', padding: '1rem', borderRadius: '8px', marginBottom: '1.5rem' }}>
        <h3>1. Document Ingestion Pipeline</h3>
        <textarea
          rows={4}
          style={{ width: '100%', padding: '0.5rem' }}
          placeholder="Paste raw text or policy document here..."
          value={document}
          onChange={(e) => setDocument(e.target.value)}
        />
        <br />
        <button onClick={handleIngest} style={{ marginTop: '0.5rem', padding: '0.5rem 1rem' }}>
          Chunk & Index Document
        </button>
        {ingestStatus && <p style={{ color: 'green' }}>{ingestStatus}</p>}
      </div>

      {/* Querying Section */}
      <div style={{ border: '1px solid #ccc', padding: '1rem', borderRadius: '8px' }}>
        <h3>2. Grounded Query & Retrieval</h3>
        <form onSubmit={handleQuery}>
          <input
            type="text"
            style={{ width: '75%', padding: '0.5rem', marginRight: '0.5rem' }}
            placeholder="Ask a question..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button type="submit" style={{ padding: '0.5rem 1rem' }}>Ask AI</button>
        </form>

        {answer && (
          <div style={{ marginTop: '1rem', padding: '1rem', backgroundColor: '#f0f7ff', borderLeft: '4px solid #0056b3' }}>
            <strong>Response:</strong>
            <p>{answer}</p>
            {context.length > 0 && (
              <div>
                <small><strong>Retrieved Context Chunks:</strong></small>
                <ul>
                  {context.map((c, i) => <li key={i}><small>{c}</small></li>)}
                </ul>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};