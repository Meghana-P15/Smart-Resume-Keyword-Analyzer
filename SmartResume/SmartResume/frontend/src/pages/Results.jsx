import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell, LabelList, PieChart, Pie,
} from "recharts";
import ScoreRing from "../components/ScoreRing";
import StatCard from "../components/StatCard";
import { COLORS, LEVEL_COLORS, LEVEL_RANK, classificationClass, formatDate, round } from "../utils";

export default function Results({ analysis, onBack, onAnalyzeAnother }) {
  if (!analysis) {
    return <div className="card empty"><p className="muted">No analysis selected.</p></div>;
  }

  // r has the same fields as the Java JobMatchResult
  const r = analysis.result;

  // ---------- 1. Skill coverage (one bar per required skill) ----------
  // The analyzer only says exact / near / missing, so the bar length shows that category:
  // exact = 100, near = 60 (the weight the analyzer's skill score gives near matches), missing = 0
  const skillRows = [
    ...r.exactMatches.map((s) => ({ skill: s, strength: 100, type: "Exact match", color: COLORS.accent })),
    ...Object.entries(r.nearMatches).map(([written, required]) => ({
      skill: required, strength: 60, type: 'Near match (written as "' + written + '")', color: COLORS.amber,
    })),
    ...r.missingSkills.map((s) => ({ skill: s, strength: 0, type: "Missing", color: COLORS.grey })),
  ];
  const totalRequired = skillRows.length;

  // ---------- 2. Matched vs missing donut ----------
  const nearCount = Object.keys(r.nearMatches).length;
  const donutData = [
    { name: "Exact match", value: r.exactMatches.length, color: COLORS.accent },
    { name: "Near match", value: nearCount, color: COLORS.amber },
    { name: "Missing", value: r.missingSkills.length, color: COLORS.red },
  ].filter((d) => d.value > 0);

  // ---------- 3. Skill gaps (from the greedy priority algorithm) ----------
  const gapRows = r.prioritySkills.map((skill) => {
    const level = r.priorityLevels[skill] || "LOW";
    return { skill, level, rank: LEVEL_RANK[level] || 1, color: LEVEL_COLORS[level] };
  });

  // ---------- 4. Score breakdown ----------
  const scoreRows = [
    { name: "Skill", value: round(r.skillScore) },
    { name: "Experience", value: round(r.experienceScore) },
    { name: "Similarity", value: round(r.similarityScore) },
    { name: "Projects", value: round(r.projectScore) },
    { name: "Certification", value: round(r.certificationScore) },
  ];

  // ---------- 5. DSA metrics ----------
  const dsaRows = [
    { name: "KMP matches", value: r.kmpMatches, info: "Exact skill search with the KMP string-matching algorithm" },
    { name: "N-Gram matches", value: r.nGramMatches, info: "Multi-word skills found anywhere in the profile" },
    { name: "Trie recognized skills", value: r.trieRecognizedSkills, info: "Skills found through the Trie (prefix tree) lookup" },
    { name: "Relevant keywords", value: r.relevantKeywordCount, info: "Job-skill keywords counted in the candidate profile" },
  ];

  return (
    <>
      <div className="results-actions">
        <button className="btn btn-ghost" onClick={onBack}>← Back</button>
        <button className="btn btn-secondary" onClick={onAnalyzeAnother}>Analyze another resume</button>
      </div>

      {/* Top section */}
      <div className="card result-hero">
        <div className="result-hero-head">
          <h2>Analysis Result</h2>
          <div className="muted">
            {analysis.jobTitle}{analysis.company ? " · " + analysis.company : ""}
          </div>
        </div>
        <div className="result-hero-body">
          <ScoreRing value={r.overallScore} size={190} />
          <div>
            <span className={"badge badge-lg " + classificationClass(r.classification)}>{r.classification}</span>
            <h3 className="candidate-name">{analysis.candidateName}</h3>
            <div className="muted">{analysis.candidateType} · {analysis.candidateEmail}</div>
            <div className="muted small">Analyzed {formatDate(analysis.createdAt)}</div>
          </div>
        </div>
      </div>

      {/* Score cards */}
      <div className="grid grid-5">
        <StatCard label="Skill" value={r.skillScore} />
        <StatCard label="Experience" value={r.experienceScore} />
        <StatCard label="Similarity" value={r.similarityScore} />
        <StatCard label="Projects" value={r.projectScore} />
        <StatCard label="Certification" value={r.certificationScore} />
      </div>

      <div className="grid grid-2">
        {/* Skill analysis */}
        <div className="card section">
          <h2>Skill Analysis</h2>
          <p className="muted small">Bar length shows how each required skill was matched</p>
          {totalRequired === 0 ? (
            <p className="muted">No required skills were given.</p>
          ) : (
            <div className="chart" style={{ height: Math.max(220, totalRequired * 40) }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={skillRows} layout="vertical" margin={{ top: 0, right: 40, left: 10, bottom: 0 }}>
                  <XAxis type="number" domain={[0, 100]} hide />
                  <YAxis type="category" dataKey="skill" width={95} tickLine={false} axisLine={false} />
                  <Tooltip formatter={(v, n, item) => [v + "%", item.payload.type]} />
                  <Bar dataKey="strength" barSize={16} radius={8} background={{ fill: "#eef1f8", radius: 8 }}>
                    {skillRows.map((row) => <Cell key={row.skill} fill={row.color} />)}
                    <LabelList dataKey="strength" position="right" formatter={(v) => v + "%"} />
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>
          )}
        </div>

        {/* Matched vs missing */}
        <div className="card section">
          <h2>Matched vs Missing</h2>
          <p className="muted small">Required skills grouped by how they matched</p>
          {totalRequired === 0 ? (
            <p className="muted">No required skills were given.</p>
          ) : (
            <>
              <div className="chart donut" style={{ height: 240 }}>
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie data={donutData} dataKey="value" nameKey="name" innerRadius="62%" outerRadius="90%" paddingAngle={2}>
                      {donutData.map((d) => <Cell key={d.name} fill={d.color} />)}
                    </Pie>
                    <Tooltip />
                  </PieChart>
                </ResponsiveContainer>
                <div className="donut-center">
                  <strong>{r.exactMatches.length}/{totalRequired}</strong>
                  <span>exact matches</span>
                </div>
              </div>
              <div className="legend">
                {donutData.map((d) => (
                  <span key={d.name}><i style={{ background: d.color }} />{d.name} ({d.value})</span>
                ))}
              </div>
            </>
          )}
        </div>
      </div>

      {/* Skill gaps */}
      <div className="card section">
        <h2>Skill Gaps</h2>
        <p className="muted small">Skills to improve, ordered by the greedy priority algorithm</p>
        {gapRows.length === 0 ? (
          <p className="muted">No skill gaps found - every required skill is matched.</p>
        ) : (
          <div className="chart" style={{ height: Math.max(140, gapRows.length * 46) }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={gapRows} layout="vertical" margin={{ top: 0, right: 70, left: 10, bottom: 0 }}>
                <XAxis type="number" domain={[0, 3]} hide />
                <YAxis type="category" dataKey="skill" width={110} tickLine={false} axisLine={false} />
                <Tooltip formatter={(v, n, item) => [item.payload.level, "Priority"]} />
                <Bar dataKey="rank" barSize={18} radius={9}>
                  {gapRows.map((row) => <Cell key={row.skill} fill={row.color} />)}
                  <LabelList dataKey="level" position="right" />
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}
      </div>

      <div className="grid grid-2">
        {/* Score breakdown */}
        <div className="card section">
          <h2>Score Breakdown</h2>
          <p className="muted small">The five scores calculated by the analyzer</p>
          <div className="chart" style={{ height: 260 }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={scoreRows} margin={{ top: 20, right: 10, left: -20, bottom: 0 }}>
                <XAxis dataKey="name" tickLine={false} axisLine={false} />
                <YAxis domain={[0, 100]} tickLine={false} axisLine={false} />
                <Tooltip formatter={(v) => [v + "%", "Score"]} />
                <Bar dataKey="value" fill={COLORS.accent} radius={[8, 8, 0, 0]} barSize={34}>
                  <LabelList dataKey="value" position="top" formatter={(v) => v + "%"} />
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* DSA metrics */}
        <div className="card section">
          <h2>DSA Metrics</h2>
          <p className="muted small">What the data-structure algorithms found in this resume</p>
          <div className="chart" style={{ height: 170 }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={dsaRows} layout="vertical" margin={{ top: 0, right: 30, left: 10, bottom: 0 }}>
                <XAxis type="number" hide />
                <YAxis type="category" dataKey="name" width={130} tickLine={false} axisLine={false} />
                <Tooltip />
                <Bar dataKey="value" fill={COLORS.navy} barSize={14} radius={7}>
                  <LabelList dataKey="value" position="right" />
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
          <ul className="dsa-info">
            {dsaRows.map((d) => (
              <li key={d.name}><b>{d.name}:</b> {d.info}</li>
            ))}
          </ul>
        </div>
      </div>
    </>
  );
}
