import type { State } from "../types/State";
import type { Transition } from "../types/Transition";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import StatsCard from "../components/StatsCard";
import GraphViewer from "../components/GraphViewer";
import TransitionViewer from "../components/TransitionViewer";
import LoadingSpinner from "../components/LoadingSpinner";
import {
  startScan,
  getStates,
  getTransitions,
} from "../services/api";

import { useEffect, useState } from "react";


export default function Dashboard() {
  const [url, setUrl] = useState("");
  const [loading, setLoading] = useState(false);

  const [states, setStates] = useState<State[]>([]);
  const [transitions, setTransitions] = useState<Transition[]>([]);

  const [stats, setStats] = useState({
  totalStates: 0,
  totalTransitions: 0,
  totalForms: 0,
  unknownStates: 0,
});

  const [error, setError] = useState("");

  const [selectedNode, setSelectedNode] = useState<any>(null);

  const graphNodes = states.map((state, index) => ({
  id: state.id,
  position: {
    x: 200,
    y: index * 150 + 50,
  },
  data: {
    label: state.state,
    source_url: state.source_url,
    forms: state.forms,
    links: state.links,
  },
}));

const graphEdges = transitions.map((transition, index) => ({
  id: `edge-${index}`,
  source: transition.from,
  target: transition.to,
}));

useEffect(() => {
  async function loadData() {
    const stateData = await getStates();
    const transitionData = await getTransitions();

    setStates(stateData);
    setTransitions(transitionData);
    const totalForms = stateData.reduce(
  (sum, state) => sum + state.forms,
  0
);

const unknownStates = transitionData.filter(
  (transition) => transition.to === "UNKNOWN"
).length;

setStats({
  totalStates: stateData.length,
  totalTransitions: transitionData.length,
  totalForms,
  unknownStates,
});
  }

  loadData();
}, []);

  const handleScan = async () => {
    setError("");

  try {
    setLoading(true);

    if (!url.trim()) {
      setError("Please enter a URL.");
      return;
    }

    setLoading(true);

    await startScan(url);

    alert("Scan started!");
  } catch (error) {
    setError("Failed to start scan.");
    console.error(error);
  } finally {
    setLoading(false);
  }
};

  return (
    <div className="h-screen bg-zinc-950 text-white flex">
      
      <Sidebar />

      <div className="flex-1 flex flex-col">
        
        <Navbar />

        <main className="p-6 flex-1 overflow-auto">

          {/* URL Input */}
          <div className="bg-zinc-900 rounded-xl p-6 mb-6">
            <h2 className="text-lg font-semibold mb-4">
              Start Exploration
            </h2>

            <div className="flex gap-3">
              <input
                type="text"
                value={url}
                onChange={(e) => setUrl(e.target.value)}
                placeholder="https://example.com"
                className="flex-1 bg-zinc-800 border border-zinc-700 rounded-lg px-4 py-2"
              />

              <button
                onClick={handleScan}
                disabled={loading}
                className="bg-blue-600 px-5 py-2 rounded-lg hover:bg-blue-500 disabled:opacity-50"
              >
                {loading ? "Scanning..." : "Start Scan"}
              </button>
              
            </div>

            {loading && <LoadingSpinner />}

            {error && (
              <div className="mt-4 bg-red-900 border border-red-600 text-red-200 rounded-lg p-3">
                <strong>Error:</strong> {error}
              </div>
            )}
          </div>

          {/* Stats */}
          <div className="grid grid-cols-4 gap-4 mb-6">
            <StatsCard title="Total States" value={stats.totalStates}/>
            <StatsCard title="Total Transitions" value={stats.totalTransitions} />
            <StatsCard title="Total Forms" value={stats.totalForms} />
            <StatsCard title="Unknown States" value={stats.unknownStates} />
          </div>

            

          {/* Graph Area */}
          <div className="mt-6">
            <TransitionViewer />
            </div>
            
          <div className="bg-zinc-900 rounded-xl p-6 h-96">
            <h2 className="text-lg font-semibold mb-4">
              Flow Graph
            </h2>

            <GraphViewer
              nodes={graphNodes}
              edges={graphEdges}
              onNodeSelect={setSelectedNode}   
            />
          </div>

          <div className="flex gap-4 mt-4 text-sm">

            <div className="flex items-center gap-2">
             <div className="w-3 h-3 bg-green-500 rounded-full"></div>
             <span>200 OK</span>
            </div>

            <div className="flex items-center gap-2">
             <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
             <span>302 Redirect</span>
            </div>

           <div className="flex items-center gap-2">
             <div className="w-3 h-3 bg-red-500 rounded-full"></div>
             <span>403/404 Error</span>
           </div>

          </div>

          {selectedNode && (
            <div className="bg-zinc-900 rounded-xl p-4 mt-4">
              <h3 className="text-lg font-semibold mb-3">
                State Information
              </h3>

              <p>
                <strong>State:</strong> {selectedNode.data.label}
              </p>

              <p>
                <strong>Source URL:</strong> {selectedNode.data.source_url}
              </p>

              <p>
                <strong>Forms:</strong> {selectedNode.data.forms}
              </p>

              <p>
                <strong>Links:</strong> {selectedNode.data.links}
              </p>
            
             <div className="flex gap-2 mt-3">

             <span className="bg-blue-600 px-3 py-1 rounded-full text-sm">
               Login Form Detected
             </span>

             <span className="bg-yellow-600 px-3 py-1 rounded-full text-sm">
               Authentication Required
             </span>

            </div>
            <hr className="my-4 border-zinc-700" />

            <h4 className="text-md font-semibold mb-2">
              Raw JSON
            </h4>

            <pre className="bg-zinc-950 p-4 rounded-lg overflow-auto text-sm">
              {JSON.stringify(selectedNode.data, null, 2)}
            </pre>
          </div>
          )}

        </main>
      </div>
    </div>
  );
}