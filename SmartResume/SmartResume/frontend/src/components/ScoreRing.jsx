import { RadialBarChart, RadialBar, PolarAngleAxis, ResponsiveContainer } from "recharts";
import { COLORS, round } from "../utils";

// Big circular score indicator. The value is passed in from the backend result.
export default function ScoreRing({ value, label = "Match Score", size = 200 }) {
  const data = [{ name: "score", value: round(value) }];

  return (
    <div className="score-ring" style={{ width: size, height: size }}>
      <ResponsiveContainer width="100%" height="100%">
        <RadialBarChart
          data={data}
          innerRadius="78%"
          outerRadius="100%"
          startAngle={90}
          endAngle={-270}
        >
          <PolarAngleAxis type="number" domain={[0, 100]} tick={false} />
          <RadialBar dataKey="value" cornerRadius={20} fill={COLORS.accent} background={{ fill: "#e8ecf7" }} />
        </RadialBarChart>
      </ResponsiveContainer>
      <div className="score-ring-text">
        <strong>{round(value)}%</strong>
        <span>{label}</span>
      </div>
    </div>
  );
}
