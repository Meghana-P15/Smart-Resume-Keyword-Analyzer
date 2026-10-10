import Icon from "./Icon";
import Logo from "./Logo";

const ITEMS = [
  { id: "dashboard", label: "Dashboard" },
  { id: "analyze", label: "Analyze" },
  { id: "candidates", label: "Candidates" },
  { id: "history", label: "History" },
  { id: "settings", label: "Settings" },
];

export default function Sidebar({ active, onNavigate, open }) {
  return (
    <aside className={"sidebar" + (open ? " open" : "")}>
      <button className="logo-button" onClick={() => onNavigate("landing")} title="Back to home">
        <Logo />
      </button>
      <nav className="nav">
        {ITEMS.map((item) => (
          <button
            key={item.id}
            className={"nav-item" + (active === item.id ? " active" : "")}
            onClick={() => onNavigate(item.id)}
            title={item.label}
          >
            <Icon name={item.id} />
            <span className="nav-label">{item.label}</span>
          </button>
        ))}
      </nav>
    </aside>
  );
}
