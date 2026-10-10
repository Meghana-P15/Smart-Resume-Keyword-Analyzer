import { useState } from "react";

// Shows skills as chips: [ Java × ] and lets the user add more.
export default function SkillChips({ label, skills, setSkills, placeholder }) {
  const [text, setText] = useState("");

  function addSkill() {
    const skill = text.trim();
    const alreadyThere = skills.some((s) => s.toLowerCase() === skill.toLowerCase());
    if (skill && !alreadyThere) setSkills([...skills, skill]);
    setText("");
  }

  function removeSkill(skill) {
    setSkills(skills.filter((s) => s !== skill));
  }

  return (
    <div className="field">
      <label className="label">{label}</label>
      <div className="chips">
        {skills.length === 0 && <span className="muted small">No skills added yet</span>}
        {skills.map((skill) => (
          <span className="chip" key={skill}>
            {skill}
            <button type="button" onClick={() => removeSkill(skill)} aria-label={"Remove " + skill}>
              ×
            </button>
          </span>
        ))}
      </div>
      <div className="chip-input">
        <input
          value={text}
          placeholder={placeholder}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              e.preventDefault();
              addSkill();
            }
          }}
        />
        <button type="button" className="btn btn-secondary" onClick={addSkill}>
          + Add
        </button>
      </div>
    </div>
  );
}
