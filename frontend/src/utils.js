// Colours used inside the charts (same palette as styles.css)
export const COLORS = {
  accent: "#4361ee",
  accentLight: "#a5b4fc",
  amber: "#f59e0b",
  red: "#e5484d",
  green: "#16a34a",
  grey: "#cbd5e1",
  navy: "#0b1b3f",
};

export const LEVEL_COLORS = { HIGH: COLORS.red, MEDIUM: COLORS.amber, LOW: "#94a3b8" };
export const LEVEL_RANK = { HIGH: 3, MEDIUM: 2, LOW: 1 };

// Scores from the backend are decimals (e.g. 59.49). We only round them for display.
export function round(n) {
  return Math.round(Number(n) || 0);
}

export function firstName(name) {
  return (name || "").split(" ")[0];
}

// "Today", "Yesterday", "3 days ago" or a normal date
export function formatDate(iso) {
  const date = new Date(iso);
  const startOfDay = (d) => new Date(d.getFullYear(), d.getMonth(), d.getDate());
  const days = Math.round((startOfDay(new Date()) - startOfDay(date)) / 86400000);
  if (days <= 0) return "Today";
  if (days === 1) return "Yesterday";
  if (days < 7) return days + " days ago";
  return date.toLocaleDateString();
}

// CSS class for the classification badge (text itself comes from the backend)
export function classificationClass(classification) {
  const c = (classification || "").toUpperCase();
  if (c.startsWith("STRONG")) return "badge-green";
  if (c.startsWith("GOOD")) return "badge-blue";
  if (c.startsWith("MODERATE")) return "badge-amber";
  return "badge-red";
}
