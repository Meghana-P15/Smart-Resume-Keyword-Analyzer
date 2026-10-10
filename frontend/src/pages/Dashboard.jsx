import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";
import StatCard from "../components/StatCard";
import { COLORS, firstName, round } from "../utils";

export default function Dashboard({ history, onOpen, onNavigate }) {
  if (history.length === 0) {
    return (
      <div className="card empty">
        <h2>Welcome back</h2>
        <p className="muted">No analyses saved yet. Analyze your first resume to fill this dashboard.</p>
        <button className="btn btn-primary" onClick={() => onNavigate("analyze")}>Start Analysis</button>
      </div>
    );
  }

  // average of the stored scores (only for display)
  const average = (key) => history.reduce((sum, h) => sum + h.result[key], 0) / history.length;

  // history comes newest-first, the line chart should go oldest -> newest
  const chartData = [...history].reverse().map((h) => ({
    name: firstName(h.candidateName),
    score: round(h.result.overallScore),
  }));

  return (
    <>
      <p className="subtitle">Welcome back. Here is a summary of your {history.length} saved analyses.</p>

      <div className="grid grid-4">
        <StatCard label="Avg. Match" value={average("overallScore")} />
        <StatCard label="Avg. Skills" value={average("skillScore")} />
        <StatCard label="Avg. Experience" value={average("experienceScore")} />
        <StatCard label="Avg. Similarity" value={average("similarityScore")} />
      </div>

      <div className="card section">
        <h2>Candidate Performance</h2>
        <p className="muted small">Overall match score of each analysis, oldest to newest</p>
        <div className="chart" style={{ height: 280 }}>
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={chartData} margin={{ top: 10, right: 20, left: -10, bottom: 0 }}>
              <CartesianGrid stroke="#eef1f8" vertical={false} />
              <XAxis dataKey="name" tickLine={false} axisLine={false} />
              <YAxis domain={[0, 100]} tickLine={false} axisLine={false} />
              <Tooltip formatter={(v) => [v + "%", "Overall score"]} />
              <Line type="monotone" dataKey="score" stroke={COLORS.accent} strokeWidth={3} dot={{ r: 4 }} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="card section">
        <h2>Recent Analyses</h2>
        <div className="list">
          {history.slice(0, 5).map((h) => (
            <button key={h.id} className="list-row" onClick={() => onOpen(h)}>
              <span className="list-name">{h.candidateName}</span>
              <span className="list-job">{h.jobTitle}</span>
              <span className="list-score">{round(h.result.overallScore)}%</span>
            </button>
          ))}
        </div>
      </div>
    </>
  );
}
