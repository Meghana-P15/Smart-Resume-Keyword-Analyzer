import { useState } from "react";
import Icon from "./Icon";

// The "RA" circle is a placeholder profile (no login in this project).
export default function TopBar({ title, onMenu, onNavigate }) {
  const [open, setOpen] = useState(false);

  function goSettings() {
    setOpen(false);
    onNavigate("settings");
  }

  return (
    <header className="topbar">
      <div className="topbar-left">
        <button className="menu-btn" onClick={onMenu} aria-label="Open menu">
          <Icon name="menu" />
        </button>
        <h1 className="page-title">{title}</h1>
      </div>

      <div className="profile">
        <button className="avatar" onClick={() => setOpen(!open)} aria-label="Profile menu">
          RA
        </button>

        {open && (
          <>
            <div className="dropdown-backdrop" onClick={() => setOpen(false)} />
            <div className="dropdown">
              <div className="dropdown-head">
                <strong>Rahul Sharma</strong>
                <span>Administrator</span>
              </div>
              <button className="dropdown-item disabled" disabled title="Not available yet">
                Profile
              </button>
              <button className="dropdown-item" onClick={goSettings}>
                Settings
              </button>
              <button className="dropdown-item disabled" disabled title="No login in this version">
                Logout
              </button>
            </div>
          </>
        )}
      </div>
    </header>
  );
}
