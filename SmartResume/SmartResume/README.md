# SmartResume – Analyze. Match. Improve.

Final-year B.Tech project: a resume vs job-description analyzer.
The analysis is done by **your existing Java DSA code** (`JobMatchAnalyzer`). Spring Boot only wraps it in a REST API and saves results in PostgreSQL. React only shows the result.

```
React + Vite + CSS + Recharts
          |  REST / JSON  (POST /api/analyze, GET /api/history)
          v
     Spring Boot API
       |            |
       v            v
 JobMatchAnalyzer   PostgreSQL (analysis_records)
 (existing DSA)
```

## 1. How to run

**Requirements:** Java 21, Maven, Node.js 18+, PostgreSQL.

### Database (once)
```sql
CREATE DATABASE smart_resume;
```
Open `backend/src/main/resources/application.properties` and set your PostgreSQL `username` / `password`.
The table `analysis_records` is created automatically on first start.

### Backend
```bash
cd backend
mvn spring-boot:run
```
Expected: log line `Started SmartResumeApplication` and the server on http://localhost:8080.

Quick test without the UI:
```bash
curl -X POST http://localhost:8080/api/analyze -H "Content-Type: application/json" -d @sample-request.json
curl http://localhost:8080/api/history
```

### Frontend (second terminal)
```bash
cd frontend
npm install
npm run dev
```
Open http://localhost:5173

## 2. Project structure

```
backend/src/main/java
  analysis/ algorithms/ model/ processing/ input/ main/   <- YOUR existing DSA code (unchanged)
  api/
    SmartResumeApplication   starts Spring Boot
    AnalysisController       the 2 endpoints
    AnalysisService          request -> Candidate/JobDescription -> JobMatchAnalyzer -> save -> response
    AnalysisRecord           JPA entity = table analysis_records
    AnalysisRecordRepository JPA repository (INSERT / SELECT)
    AnalyzeRequest           shape of the JSON sent by React
frontend/src
  App.jsx                    page switching + loads history once
  api.js                     the two fetch calls
  pages/                     Landing, Dashboard, Analyze (3 steps + loading), Results, Candidates, History, Settings
  components/                Sidebar, TopBar (RA avatar + dropdown), ScoreRing, StatCard, SkillChips, RepeatableList
```

Only change made to your Java project: `module-info.java` was left out (it is an Eclipse-only file and conflicts with Spring Boot). No analyzer/scoring code was touched. The console `Main` class still exists.

## 3. Database table `analysis_records`
Normal columns: candidate name/email/type, job title, company, overall + 5 scores, role relevance, KMP / N-Gram / Trie / keyword counters, `created_at`.
Text columns (JSON text): `matched_skills`, `near_matches`, `missing_skills`, `priority_skills`, `priority_levels`.
Check it with `SELECT * FROM analysis_records;`

## 4. What each chart shows (for the viva)
| Chart | Data source |
|---|---|
| Big score ring | `overallScore` (+ `classification` badge) |
| Score cards / Score breakdown | `skillScore, experienceScore, similarityScore, projectScore, certificationScore` |
| Skill Analysis bars | `exactMatches` (100) / `nearMatches` (60) / `missingSkills` (0). The analyzer has no per-skill percentage, so the bar shows the *category*. 60 is the weight your skill score already gives near matches. |
| Matched vs Missing donut | counts of exact / near / missing |
| Skill Gaps | `prioritySkills` + `priorityLevels` (greedy algorithm) |
| DSA Metrics | `kmpMatches, nGramMatches, trieRecognizedSkills, relevantKeywordCount` |
| Candidate Comparison + radar | stored results of analyses with the same job title (sorted by stored score, no extra ranking logic) |
| Dashboard line chart | overall score of every saved analysis over time |

## 5. Which algorithm does what
- **KMP** – exact skill matching · **Trie** – skill lookup · **N-Gram** – multi-word skills
- **Edit Distance** – near matches (e.g. "Springboot" ≈ "Spring Boot")
- **Cosine Similarity** – resume text vs job text · **Greedy** – skill improvement priority
