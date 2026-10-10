import { useState } from "react";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, Legend, CartesianGrid, ResponsiveContainer,
  RadarChart, Radar, PolarGrid, PolarAngleAxis, PolarRadiusAxis,
} from "recharts";
import { COLORS, classificationClass, formatDate, round } from "../utils";

// Candidate Comparison: shows stored analyses that were done for the same job title.
// It only displays saved results - it does not rank or decide anything.
export default function Candidates({ history, onOpen }) {
  const [jobKey, setJobKey] = useState("");
  const [selectedId, setSelectedId] = useState(null);

  if (history.length === 0) {
    return (
      <div className="card empty">
        <h2>Candidate Comparison</h2>
        <p className="muted">Analyze at least one resume to compare candidates here.</p>
      </div>
    );
  }

  // list of different job titles (case-insensitive), newest first
  const jobs = [];
  history.forEach((h) => {
    const key = h.jobTitle.trim().toLowerCase();
    if (!jobs.some((j) => j.key === key)) jobs.push({ key, title: h.jobTitle });
  });

  const activeKey = jobs.some((j) => j.key === jobKey) ? jobKey : jobs[0].key;

  // Stored analyses for this job, shown from the highest stored score to the lowest
  const rows = history
    .filter((h) => h.jobTitle.trim().toLowerCase() === activeKey)
    .sort((a, b) => b.result.overallScore - a.result.overallScore);

  const chartData = rows.map((h) => ({
    name: h.candidateName,
    Match: round(h.result.overallScore),
    Skills: round(h.result.skillScore),
    Experience: round(h.result.experienceScore),
  }));

  const selected = rows.find((h) => h.id === selectedId);

  return (
    <>
      <div className="toolbar">
        <div>
          <label className="label">Job</label>
          <select value={activeKey} onChange={(e) => { setJobKey(e.target.value); setSelectedId(null); }}>
            {jobs.map((j) => <option key={j.key} value={j.key}>{j.title}</option>)}
          </select>
        </div>
        <p className="muted small toolbar-note">
          Comparison of stored analysis results for the same job. It is not a hiring decision.
        </p>
      </div>

      <div className="card table-card">
        <div className="table-scroll">
          <table>
            <thead>
              <tr>
                <th>Candidate</th>
                <th>Match</th>
                <th>Skills</th>
                <th>Experience</th>
                <th>Similarity</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((h) => (
                <tr
                  key={h.id}
                  className={"clickable" + (h.id === selectedId ? " selected" : "")}
                  onClick={() => setSelectedId(h.id)}
                >
                  <td><strong>{h.candidateName}</strong></td>
                  <td><strong>{round(h.result.overallScore)}%</strong></td>
                  <td>{round(h.result.skillScore)}%</td>
                  <td>{round(h.result.experienceScore)}%</td>
                  <td>{round(h.result.similarityScore)}%</td>
                  <td className="muted">{formatDate(h.createdAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <p className="muted small hint">Click a candidate to see the details.</p>
      </div>

      <div className="card section">
        <h2>Candidate Comparison Chart</h2>
        <div className="chart" style={{ height: 300 }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
              <CartesianGrid stroke="#eef1f8" vertical={false} />
              <XAxis dataKey="name" tickLine={false} axisLine={false} />
              <YAxis domain={[0, 100]} tickLine={false} axisLine={false} />
              <Tooltip formatter={(v) => v + "%"} />
              <Legend />
              <Bar dataKey="Match" fill={COLORS.accent} radius={[6, 6, 0, 0]} />
              <Bar dataKey="Skills" fill={COLORS.accentLight} radius={[6, 6, 0, 0]} />
              <Bar dataKey="Experience" fill={COLORS.navy} radius={[6, 6, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {selected && <CandidateDetail analysis={selected} onOpen={onOpen} />}
    </>
  );
}

function CandidateDetail({ analysis, onOpen }) {
  const r = analysis.result;
  const nearSkills = Object.values(r.nearMatches);

  const radarData = [
    { metric: "Skills", value: round(r.skillScore) },
    { metric: "Experience", value: round(r.experienceScore) },
    { metric: "Similarity", value: round(r.similarityScore) },
    { metric: "Projects", value: round(r.projectScore) },
    { metric: "Certifications", value: round(r.certificationScore) },
  ];

  return (
    <div className="card section detail">
      <div className="detail-head">
        <div>
          <h2>{analysis.candidateName}</h2>
          <div className="muted">{analysis.jobTitle}</div>
        </div>
        <div className="detail-score">
          <strong>{round(r.overallScore)}% Match</strong>
          <span className={"badge " + classificationClass(r.classification)}>{r.classification}</span>
        </div>
      </div>

      <div className="grid grid-2">
        <div>
          <div className="mini-stats">
            <div><b>{round(r.skillScore)}%</b><span>Skill</span></div>
            <div><b>{round(r.experienceScore)}%</b><span>Experience</span></div>
            <div><b>{round(r.similarityScore)}%</b><span>Similarity</span></div>
          </div>

          <h3>Skills</h3>
          <ul className="check-list">
            {r.exactMatches.map((s) => <li key={s} className="ok">✓ {s}</li>)}
            {nearSkills.map((s) => <li key={s} className="near">≈ {s} (near match)</li>)}
            {r.exactMatches.length + nearSkills.length === 0 && <li className="muted">No matched skills</li>}
          </ul>

          <h3>Missing</h3>
          <ul className="check-list">
            {r.missingSkills.map((s) => <li key={s} className="missing">○ {s}</li>)}
            {r.missingSkills.length === 0 && <li className="muted">Nothing missing</li>}
          </ul>

          <h3>Priority Improvements</h3>
          <ol className="priority-list">
            {r.prioritySkills.map((s) => (
              <li key={s}>{s} <span className="muted small">({r.priorityLevels[s]})</span></li>
            ))}
            {r.prioritySkills.length === 0 && <li className="muted">No improvements needed</li>}
          </ol>

          <button className="btn btn-secondary" onClick={() => onOpen(analysis)}>View full analysis</button>
        </div>

        <div className="chart" style={{ height: 320 }}>
          <ResponsiveContainer width="100%" height="100%">
            <RadarChart data={radarData} outerRadius="75%">
              <PolarGrid />
              <PolarAngleAxis dataKey="metric" />
              <PolarRadiusAxis domain={[0, 100]} tick={false} axisLine={false} />
              <Radar dataKey="value" stroke={COLORS.accent} fill={COLORS.accent} fillOpacity={0.3} />
              <Tooltip formatter={(v) => v + "%"} />
            </RadarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}
