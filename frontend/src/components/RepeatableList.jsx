// A list of small rows (used for experience, projects and certifications).
// fields = [{ key: "title", placeholder: "Project title" }, ...]
export default function RepeatableList({ title, items, setItems, fields, emptyItem, addLabel }) {
  function changeItem(index, key, value) {
    setItems(items.map((item, i) => (i === index ? { ...item, [key]: value } : item)));
  }

  function removeItem(index) {
    setItems(items.filter((_, i) => i !== index));
  }

  return (
    <div className="field">
      <label className="label">{title}</label>
      {items.map((item, index) => (
        <div className="repeat-row" key={index}>
          {fields.map((f) => (
            <input
              key={f.key}
              type={f.type || "text"}
              placeholder={f.placeholder}
              value={item[f.key]}
              onChange={(e) => changeItem(index, f.key, e.target.value)}
            />
          ))}
          <button type="button" className="icon-btn" onClick={() => removeItem(index)} aria-label="Remove">
            ×
          </button>
        </div>
      ))}
      <button type="button" className="btn btn-ghost" onClick={() => setItems([...items, { ...emptyItem }])}>
        + {addLabel}
      </button>
    </div>
  );
}
