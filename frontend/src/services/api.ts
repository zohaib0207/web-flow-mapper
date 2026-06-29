import statesData from "../mock/states.json";
import transitionsData from "../mock/transitions.json";

import type { State } from "../types/State";
import type { Transition } from "../types/Transition";

const API_BASE_URL = "http://localhost:8000";

export async function getStates() {
  return Promise.resolve(statesData as State[]
  );
}

export async function getTransitions() {
  return Promise.resolve(transitionsData as Transition[]);
}

export async function startScan(url: string) {
  const response = await fetch(`${API_BASE_URL}/scan`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ url }),
  });

  if (!response.ok) {
    throw new Error("Failed to start scan");
  }

  return response.json();
}

export async function getScanResults() {
  const response = await fetch(`${API_BASE_URL}/results`);

  if (!response.ok) {
    throw new Error("Failed to fetch results");
  }

  return response.json();
}

export async function getGraphData() {
  const response = await fetch(`${API_BASE_URL}/graph`);

  if (!response.ok) {
    throw new Error("Failed to fetch graph data");
  }

  return response.json();
}

export async function getStats() {
  const response = await fetch(`${API_BASE_URL}/stats`);

  if (!response.ok) {
    throw new Error("Failed to fetch stats");
  }

  return response.json();
}
