import { useEffect, useState } from "react";
import { fetchHistory } from "./api";
import Sidebar from "./components/Sidebar";
import TopBar from "./components/TopBar";
import Landing from "./pages/Landing";
import Dashboard from "./pages/Dashboard";
import Analyze from "./pages/Analyze";
import Results from "./pages/Results";
import Candidates from "./pages/Candidates";
import History from "./pages/History";
import Settings from "./pages/Settings";

const TITLES = {
  dashboard: "Dashboard",
  analyze: "Analyze Resume",
  candidates: "Candidate Comparison",
  history: "History",
  settings: "Settings",
  results: "Analysis Result",
};

export default function App() {
  const [page, setPage] = useState("landing"); // which screen is visible
  const [history, setHistory] = useState([]); // saved analyses from PostgreSQL
  const [historyError, setHistoryError] = useState("");
  const [selected, setSelected] = useState(null); // analysis shown on the Results page
  const [origin, setOrigin] = useState("history"); // which sidebar item stays highlighted on Results
  const [menuOpen, setMenuOpen] = useState(false); // mobile sidebar

  function loadHistory() {
    return fetchHistory()
      .then((data) => {
        setHistory(data);
        setHistoryError("");
      })
      .catch((e) => setHistoryError(e.message));
  }

  useEffect(() => {
    loadHistory();
  }, []);

  function go(nextPage) {
    setPage(nextPage);
    setMenuOpen(false);
    window.scrollTo(0, 0);
  }

  function openResult(analysis, from) {
    setSelected(analysis);
    setOrigin(from);
    go("results");
  }

  function handleAnalysisFinished(saved) {
    loadHistory();
    openResult(saved, "analyze");
  }

  if (page === "landing") {
    return <Landing onNavigate={go} />;
  }

  const activeNav = page === "results" ? origin : page;

  return (
    <div className="app">
      <Sidebar active={activeNav} onNavigate={go} open={menuOpen} />
      {menuOpen && <div className="backdrop" onClick={() => setMenuOpen(false)} />}

      <div className="main">
        <TopBar title={TITLES[page]} onMenu={() => setMenuOpen(true)} onNavigate={go} />

        <div className="content">
          {historyError && <div className="alert">{historyError}</div>}

          {page === "dashboard" && (
            <Dashboard history={history} onOpen={(a) => openResult(a, "dashboard")} onNavigate={go} />
          )}
          {page === "analyze" && <Analyze onFinished={handleAnalysisFinished} />}
          {page === "results" && (
            <Results analysis={selected} onBack={() => go(origin)} onAnalyzeAnother={() => go("analyze")} />
          )}
          {page === "candidates" && (
            <Candidates history={history} onOpen={(a) => openResult(a, "candidates")} />
          )}
          {page === "history" && <History history={history} onOpen={(a) => openResult(a, "history")} />}
          {page === "settings" && <Settings history={history} historyError={historyError} />}
        </div>
      </div>
    </div>
  );
}
