export default function Settings({ history, historyError }) {
  return (
    <div className="grid grid-2">
      <div className="card section">
        <h2>Connection</h2>
        <ul className="plain-list">
          <li><span>Backend (Spring Boot)</span><b className={historyError ? "bad" : "ok"}>{historyError ? "Not reachable" : "Connected"}</b></li>
          <li><span>Saved analyses (PostgreSQL)</span><b>{history.length}</b></li>
          <li><span>API address</span><b>/api → localhost:8080</b></li>
        </ul>
      </div>

      <div className="card section">
        <h2>About SmartResume</h2>
        <p className="muted">
          The score, matches and priorities are calculated by the Java DSA analyzer in the backend.
          This React app only shows the result.
        </p>
        <ul className="plain-list">
          <li><span>Exact skill match</span><b>KMP + N-Gram</b></li>
          <li><span>Skill lookup</span><b>Trie</b></li>
          <li><span>Near match</span><b>Edit Distance</b></li>
          <li><span>Text similarity</span><b>Cosine Similarity</b></li>
          <li><span>Skill priority</span><b>Greedy</b></li>
        </ul>
      </div>
    </div>
  );
}
