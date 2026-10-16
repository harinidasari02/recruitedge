import { useEffect, useState } from "react";
import "./styles.css";

const STAGES = ["APPLIED", "SCREENING", "INTERVIEW", "OFFER", "HIRED", "REJECTED"];

async function api(path, method = "GET", body) {
  const res = await fetch("/api" + path, {
    method,
    headers: body ? { "Content-Type": "application/json" } : {},
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    const details = err.errors ? Object.values(err.errors).join(" ") : "";
    throw new Error(`${err.message || "Request failed."} ${details}`.trim());
  }
  return res.status === 204 ? null : res.json();
}

const formData = (e) => Object.fromEntries(new FormData(e.target));
const fmt = (d) => new Date(d).toLocaleString([], { dateStyle: "medium", timeStyle: "short" });

export default function App() {
  const [tab, setTab] = useState("jobs");
  const [jobs, setJobs] = useState([]);
  const [candidates, setCandidates] = useState([]);
  const [interviews, setInterviews] = useState([]);
  const [error, setError] = useState("");

  const load = async () => {
    try {
      const [j, c, i] = await Promise.all([api("/jobs"), api("/candidates"), api("/interviews")]);
      setJobs(j);
      setCandidates(c);
      setInterviews(i);
    } catch (e) {
      setError(e.message);
    }
  };
  useEffect(() => { load(); }, []);

  // Runs an action, then refreshes data; shows the server's message on failure.
  const run = (fn, reset) => async (e) => {
    e?.preventDefault?.();
    setError("");
    try {
      await fn(e);
      if (reset) e.target.reset();
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  const openJobs = jobs.filter((j) => j.status === "OPEN");
  const activeCandidates = candidates.filter((c) => !["HIRED", "REJECTED"].includes(c.stage));

  return (
    <div className="app">
      <header>
        <h1>RecruitEdge</h1>
        <nav>
          {["jobs", "candidates", "interviews"].map((t) => (
            <button key={t} className={tab === t ? "on" : ""} onClick={() => { setTab(t); setError(""); }}>
              {t[0].toUpperCase() + t.slice(1)}
            </button>
          ))}
        </nav>
      </header>

      {error && <p className="error" role="alert">{error}</p>}

      {tab === "jobs" && (
        <section>
          <form onSubmit={run((e) => api("/jobs", "POST", formData(e)), true)}>
            <input name="title" placeholder="Job title" />
            <input name="department" placeholder="Department" />
            <input name="location" placeholder="Location" />
            <input name="description" placeholder="Short description" />
            <button>Post job</button>
          </form>
          {jobs.length === 0 && <p className="empty">No jobs yet. Post your first opening above.</p>}
          <ul>
            {jobs.map((j) => (
              <li key={j.id}>
                <div>
                  <strong>{j.title}</strong> <span className={`tag ${j.status}`}>{j.status}</span>
                  <small>{j.department}{j.location && ` in ${j.location}`}</small>
                </div>
                <div className="actions">
                  {j.status === "OPEN" && <button onClick={run(() => api(`/jobs/${j.id}/close`, "PATCH"))}>Close job</button>}
                  <button className="danger" onClick={run(() => api(`/jobs/${j.id}`, "DELETE"))}>Delete</button>
                </div>
              </li>
            ))}
          </ul>
        </section>
      )}

      {tab === "candidates" && (
        <section>
          <form onSubmit={run((e) => { const d = formData(e); return api("/candidates", "POST", { ...d, jobId: d.jobId ? Number(d.jobId) : null }); }, true)}>
            <input name="name" placeholder="Full name" />
            <input name="email" placeholder="Email" />
            <input name="phone" placeholder="Phone" />
            <select name="jobId" defaultValue="">
              <option value="" disabled>Select an open job</option>
              {openJobs.map((j) => <option key={j.id} value={j.id}>{j.title}</option>)}
            </select>
            <button>Add candidate</button>
          </form>
          {candidates.length === 0 && <p className="empty">No candidates yet. Add one to start tracking.</p>}
          <ul>
            {candidates.map((c) => (
              <li key={c.id}>
                <div>
                  <strong>{c.name}</strong>
                  <small>{c.email} for {c.job.title}</small>
                </div>
                <select value={c.stage} onChange={(e) => run(() => api(`/candidates/${c.id}/stage`, "PATCH", { stage: e.target.value }))()}>
                  {STAGES.map((s) => <option key={s}>{s}</option>)}
                </select>
              </li>
            ))}
          </ul>
        </section>
      )}

      {tab === "interviews" && (
        <section>
          <form onSubmit={run((e) => { const d = formData(e); return api("/interviews", "POST", { ...d, candidateId: d.candidateId ? Number(d.candidateId) : null, scheduledAt: d.scheduledAt || null }); }, true)}>
            <select name="candidateId" defaultValue="">
              <option value="" disabled>Select a candidate</option>
              {activeCandidates.map((c) => <option key={c.id} value={c.id}>{c.name} ({c.job.title})</option>)}
            </select>
            <input name="scheduledAt" type="datetime-local" />
            <input name="interviewer" placeholder="Interviewer" />
            <input name="mode" placeholder="Mode (online, on-site)" />
            <button>Schedule interview</button>
          </form>
          {interviews.length === 0 && <p className="empty">No interviews scheduled.</p>}
          <ul>
            {[...interviews].sort((a, b) => a.scheduledAt.localeCompare(b.scheduledAt)).map((i) => (
              <li key={i.id}>
                <div>
                  <strong>{fmt(i.scheduledAt)}</strong>
                  <small>{i.candidate.name} with {i.interviewer}{i.mode && `, ${i.mode}`}</small>
                </div>
                <button className="danger" onClick={run(() => api(`/interviews/${i.id}`, "DELETE"))}>Cancel</button>
              </li>
            ))}
          </ul>
        </section>
      )}
    </div>
  );
}
