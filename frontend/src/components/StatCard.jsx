import { round } from "../utils";

export default function StatCard({ label, value, suffix = "%" }) {
  return (
    <div className="card stat">
      <div className="stat-label">{label}</div>
      <div className="stat-value">
        {round(value)}
        {suffix}
      </div>
    </div>
  );
}
