import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import StatsCard from "../components/StatsCard";
import GraphViewer from "../components/GraphViewer";
import ResponseLog from "../components/ResponseLog";

export default function Dashboard() {
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
                placeholder="https://example.com"
                className="flex-1 bg-zinc-800 border border-zinc-700 rounded-lg px-4 py-2"
              />

              <button className="bg-blue-600 px-5 py-2 rounded-lg hover:bg-blue-500">
                Start Scan
              </button>
            </div>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-4 gap-4 mb-6">
            <StatsCard title="Nodes" value={0} />
            <StatsCard title="Edges" value={0} />
            <StatsCard title="Requests" value={0} />
            <StatsCard title="Redirects" value={0} />
          </div>

            

          {/* Graph Area */}
          <div className="mt-6">
            <ResponseLog />
            </div>
            
          <div className="bg-zinc-900 rounded-xl p-6 h-96">
            <h2 className="text-lg font-semibold mb-4">
              Flow Graph
            </h2>

            <GraphViewer />
          </div>

        </main>
      </div>
    </div>
  );
}