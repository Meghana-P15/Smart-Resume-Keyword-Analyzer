import { useEffect, useState } from "react";
import SkillChips from "../components/SkillChips";
import RepeatableList from "../components/RepeatableList";
import { analyzeResume } from "../api";

const STEP_NAMES = ["Candidate", "Skills & Experience", "Job Description"];
const LOADING_STEPS = ["Skills", "Experience", "Similarity", "Projects", "Certifications"];
const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

export default function Analyze({ onFinished }) {
  const [step, setStep] = useState(1);
  const [analyzing, setAnalyzing] = useState(false);
  const [error, setError] = useState("");

  // Step 1
  const [candidate, setCandidate] = useState({
    name: "", email: "", age: "", gender: "Male", candidateType: "Fresher",
    degree: "", university: "", graduationYear: "", cgpa: "", summary: "",
  });
  // Step 2
  const [skills, setSkills] = useState([]);
  const [experiences, setExperiences] = useState([]);
  const [internship, setInternship] = useState("");
  const [projects, setProjects] = useState([]);
  const [certifications, setCertifications] = useState([]);
  const [achievements, setAchievements] = useState("");
  // Step 3
  const [job, setJob] = useState({ jobTitle: "", company: "", requiredExperience: "", description: "" });
  const [requiredSkills, setRequiredSkills] = useState([]);

  // returns an onChange handler that updates one field of an object state
  const update = (state, setState, key) => (e) => setState({ ...state, [key]: e.target.value });

  function checkStep(number) {
    if (number === 1) {
      if (!candidate.name.trim()) return "Please enter the candidate's full name.";
      if (!candidate.email.includes("@")) return "Please enter a valid email.";
    }
    if (number === 2 && skills.length === 0) return "Please add at least one skill.";
    if (number === 3) {
      if (!job.jobTitle.trim()) return "Please enter the job title.";
      if (requiredSkills.length === 0) return "Please add at least one required skill.";
    }
    return "";
  }

  function next() {
    const problem = checkStep(step);
    setError(problem);
    if (!problem) setStep(step + 1);
  }

  async function handleAnalyze() {
    const problem = checkStep(3);
    if (problem) {
      setError(problem);
      return;
    }
    setError("");
    setAnalyzing(true);

    // This is the JSON that Spring Boot receives at POST /api/analyze
    const payload = {
      candidate: {
        name: candidate.name,
        email: candidate.email,
        age: Number(candidate.age) || 0,
        gender: candidate.gender,
        candidateType: candidate.candidateType,
        degree: candidate.degree,
        university: candidate.university,
        graduationYear: Number(candidate.graduationYear) || 0,
        cgpa: Number(candidate.cgpa) || 0,
        summary: candidate.summary,
        skills,
        experiences: experiences
          .filter((e) => e.role.trim())
          .map((e) => ({ role: e.role, company: e.company, years: Number(e.years) || 0 })),
        internship,
        projects: projects.filter((p) => p.title.trim()),
        certifications: certifications.filter((c) => c.name.trim()),
        achievements,
      },
      job: {
        jobTitle: job.jobTitle,
        company: job.company,
        requiredExperience: Number(job.requiredExperience) || 0,
        requiredSkills,
        description: job.description,
      },
    };

    try {
      // wait(3200) only keeps the loading screen visible long enough to read
      const [saved] = await Promise.all([analyzeResume(payload), wait(3200)]);
      onFinished(saved);
    } catch (e) {
      setError(e.message);
      setAnalyzing(false);
    }
  }

  if (analyzing) return <AnalyzingScreen />;

  return (
    <div className="card form-card">
      <div className="stepper">
        {STEP_NAMES.map((name, i) => (
          <div key={name} className={"step" + (step === i + 1 ? " active" : "") + (step > i + 1 ? " done" : "")}>
            <span className="step-number">{step > i + 1 ? "✓" : i + 1}</span>
            <span className="step-name">{name}</span>
          </div>
        ))}
      </div>

      {step === 1 && (
        <div>
          <h2>Candidate Information</h2>
          <div className="form-grid">
            <div className="field">
              <label className="label">Full Name</label>
              <input value={candidate.name} onChange={update(candidate, setCandidate, "name")} placeholder="Rahul Sharma" />
            </div>
            <div className="field">
              <label className="label">Email</label>
              <input type="email" value={candidate.email} onChange={update(candidate, setCandidate, "email")} placeholder="rahul@example.com" />
            </div>
            <div className="field">
              <label className="label">Age</label>
              <input type="number" value={candidate.age} onChange={update(candidate, setCandidate, "age")} placeholder="22" />
            </div>
            <div className="field">
              <label className="label">Gender</label>
              <select value={candidate.gender} onChange={update(candidate, setCandidate, "gender")}>
                <option>Male</option>
                <option>Female</option>
                <option>Other</option>
              </select>
            </div>
            <div className="field">
              <label className="label">Candidate Type</label>
              <select value={candidate.candidateType} onChange={update(candidate, setCandidate, "candidateType")}>
                <option>Fresher</option>
                <option>Experienced Professional</option>
              </select>
            </div>
            <div className="field">
              <label className="label">Degree</label>
              <input value={candidate.degree} onChange={update(candidate, setCandidate, "degree")} placeholder="B.Tech Computer Science" />
            </div>
            <div className="field">
              <label className="label">University</label>
              <input value={candidate.university} onChange={update(candidate, setCandidate, "university")} />
            </div>
            <div className="field">
              <label className="label">Graduation Year</label>
              <input type="number" value={candidate.graduationYear} onChange={update(candidate, setCandidate, "graduationYear")} placeholder="2026" />
            </div>
            <div className="field">
              <label className="label">CGPA</label>
              <input type="number" step="0.01" value={candidate.cgpa} onChange={update(candidate, setCandidate, "cgpa")} placeholder="8.4" />
            </div>
          </div>
          <div className="field">
            <label className="label">Summary</label>
            <textarea rows="3" value={candidate.summary} onChange={update(candidate, setCandidate, "summary")} placeholder="A short professional summary" />
          </div>
        </div>
      )}

      {step === 2 && (
        <div>
          <h2>Skills &amp; Experience</h2>
          <SkillChips label="Skills" skills={skills} setSkills={setSkills} placeholder="e.g. Python" />

          <RepeatableList
            title="Work Experience (leave empty for freshers)"
            items={experiences}
            setItems={setExperiences}
            emptyItem={{ role: "", company: "", years: "" }}
            addLabel="Add experience"
            fields={[
              { key: "role", placeholder: "Role, e.g. Software Engineer" },
              { key: "company", placeholder: "Company" },
              { key: "years", placeholder: "Years", type: "number" },
            ]}
          />

          <div className="field">
            <label className="label">Internship</label>
            <input value={internship} onChange={(e) => setInternship(e.target.value)} placeholder="e.g. Backend intern building REST APIs" />
          </div>

          <RepeatableList
            title="Projects"
            items={projects}
            setItems={setProjects}
            emptyItem={{ title: "", description: "", technologies: "" }}
            addLabel="Add project"
            fields={[
              { key: "title", placeholder: "Project title" },
              { key: "description", placeholder: "What does it do?" },
              { key: "technologies", placeholder: "Technologies used" },
            ]}
          />

          <RepeatableList
            title="Certifications"
            items={certifications}
            setItems={setCertifications}
            emptyItem={{ name: "", issuer: "" }}
            addLabel="Add certification"
            fields={[
              { key: "name", placeholder: "Certification name" },
              { key: "issuer", placeholder: "Issued by" },
            ]}
          />

          <div className="field">
            <label className="label">Achievements</label>
            <textarea rows="2" value={achievements} onChange={(e) => setAchievements(e.target.value)} />
          </div>
        </div>
      )}

      {step === 3 && (
        <div>
          <h2>Job Description</h2>
          <div className="form-grid">
            <div className="field">
              <label className="label">Job Title</label>
              <input value={job.jobTitle} onChange={update(job, setJob, "jobTitle")} placeholder="Software Engineer" />
            </div>
            <div className="field">
              <label className="label">Company</label>
              <input value={job.company} onChange={update(job, setJob, "company")} />
            </div>
            <div className="field">
              <label className="label">Required Experience (years)</label>
              <input type="number" step="0.5" value={job.requiredExperience} onChange={update(job, setJob, "requiredExperience")} placeholder="0" />
            </div>
          </div>
          <SkillChips label="Required Skills" skills={requiredSkills} setSkills={setRequiredSkills} placeholder="e.g. Spring Boot" />
          <div className="field">
            <label className="label">Job Description</label>
            <textarea rows="5" value={job.description} onChange={update(job, setJob, "description")} placeholder="Paste the job description here" />
          </div>
        </div>
      )}

      {error && <div className="alert">{error}</div>}

      <div className="form-buttons">
        {step > 1 ? (
          <button className="btn btn-secondary" onClick={() => { setError(""); setStep(step - 1); }}>← Back</button>
        ) : (
          <span />
        )}
        {step < 3 ? (
          <button className="btn btn-primary" onClick={next}>Next →</button>
        ) : (
          <button className="btn btn-primary" onClick={handleAnalyze}>Analyze Resume →</button>
        )}
      </div>
    </div>
  );
}

// Simple loading screen: the ticks move one by one while the request is running
function AnalyzingScreen() {
  const [done, setDone] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => setDone((d) => Math.min(d + 1, LOADING_STEPS.length)), 600);
    return () => clearInterval(timer);
  }, []);

  return (
    <div className="card analyzing">
      <h2>Analyzing your resume...</h2>
      <div className="spinner" />
      <p className="muted">Processing</p>
      <ul className="loading-steps">
        {LOADING_STEPS.map((name, i) => {
          const state = i < done ? "done" : i === done ? "active" : "todo";
          const icon = state === "done" ? "✓" : state === "active" ? "◌" : "○";
          return (
            <li key={name} className={state}>
              <span className="step-icon">{icon}</span>
              {name}
            </li>
          );
        })}
      </ul>
      <p className="muted small">The analysis runs in the Java backend (KMP, Trie, N-Gram, Cosine Similarity).</p>
    </div>
  );
}
