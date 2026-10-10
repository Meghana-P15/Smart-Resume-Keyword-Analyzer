// All calls to the Spring Boot backend live here.

async function request(url, options) {
  let response;
  try {
    response = await fetch(url, options);
  } catch (e) {
    throw new Error("Cannot reach the backend. Is Spring Boot running on port 8080?");
  }

  if (!response.ok) {
    let message = "Request failed (" + response.status + ")";
    try {
      const body = await response.json();
      if (body.message) message = body.message;
    } catch (e) {
      // response had no JSON body - keep the default message
    }
    throw new Error(message);
  }
  return response.json();
}

// POST /api/analyze  -> runs the Java DSA analyzer, saves to PostgreSQL, returns the result
export function analyzeResume(payload) {
  return request("/api/analyze", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
}

// GET /api/history  -> all saved analyses (newest first)
export function fetchHistory() {
  return request("/api/history");
}
