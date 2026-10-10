import Logo from "../components/Logo";

const ALGORITHMS = ["KMP", "Trie", "N-Gram", "Cosine Similarity", "Edit Distance", "Greedy Priority"];

export default function Landing({ onNavigate }) {
  return (
    <div className="landing">
      <nav className="landing-nav">
        <Logo />
        <div className="landing-links">
          <button className="link-btn active" onClick={() => onNavigate("landing")}>Home</button>
          <button className="link-btn" onClick={() => onNavigate("analyze")}>Analyze</button>
          <button className="link-btn" onClick={() => onNavigate("history")}>History</button>
        </div>
      </nav>

      <section className="hero">
        <div className="hero-text">
          <span className="eyebrow">ANALYZE. MATCH. IMPROVE.</span>
          <h1>Understand how well a resume matches a job description.</h1>
          <p>DSA-powered resume analysis with intelligent skill matching and insights.</p>
          <div className="hero-buttons">
            <button className="btn btn-primary btn-lg" onClick={() => onNavigate("analyze")}>
              Start Analysis
            </button>
            <button className="btn btn-secondary btn-lg" onClick={() => onNavigate("dashboard")}>
              View Dashboard
            </button>
          </div>
          <div className="algo-tags">
            {ALGORITHMS.map((a) => (
              <span key={a} className="tag">{a}</span>
            ))}
          </div>
        </div>

        {/* Decorative preview card (sample values, not real data) */}
        <div className="hero-preview">
          <div className="card preview-card">
            <div className="preview-score">82%</div>
            <div className="muted">Match Score</div>
            <ul className="preview-list">
              <li><span>Java</span><b className="ok">✓</b></li>
              <li><span>SQL</span><b className="ok">✓</b></li>
              <li><span>Docker</span><b className="warn">!</b></li>
            </ul>
            <div className="preview-note">Sample preview</div>
          </div>
        </div>
      </section>
    </div>
  );
}
