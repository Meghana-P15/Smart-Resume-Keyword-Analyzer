import { classificationClass, formatDate, round } from "../utils";

// Every row comes from the analysis_records table in PostgreSQL.
export default function History({ history, onOpen }) {
  if (history.length === 0) {
    return (
      <div className="card empty">
        <h2>No history yet</h2>
        <p className="muted">Saved analyses will appear here.</p>
      </div>
    );
  }

  return (
    <div className="card table-card">
      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th>Candidate</th>
              <th>Job</th>
              <th>Score</th>
              <th>Result</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {history.map((h) => (
              <tr key={h.id} className="clickable" onClick={() => onOpen(h)}>
                <td><strong>{h.candidateName}</strong></td>
                <td>{h.jobTitle}{h.company ? " · " + h.company : ""}</td>
                <td><strong>{round(h.result.overallScore)}%</strong></td>
                <td><span className={"badge " + classificationClass(h.result.classification)}>{h.result.classification}</span></td>
                <td className="muted">{formatDate(h.createdAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="muted small hint">Click a row to open the stored analysis.</p>
    </div>
  );
}
