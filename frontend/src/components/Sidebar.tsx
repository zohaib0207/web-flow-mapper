export default function Sidebar() {
  return (
    <aside className="w-64 bg-zinc-900 border-r border-zinc-800 p-4">
      <h2 className="text-xl font-bold mb-8 text-white">
        Web Flow Mapper
      </h2>

      <nav className="space-y-2">
        <button className="w-full text-left px-4 py-3 rounded-lg bg-zinc-800 hover:bg-zinc-700">
          Dashboard
        </button>

        <button className="w-full text-left px-4 py-3 rounded-lg hover:bg-zinc-800">
          Scan Results
        </button>

        <button className="w-full text-left px-4 py-3 rounded-lg hover:bg-zinc-800">
          Graph Viewer
        </button>

        <button className="w-full text-left px-4 py-3 rounded-lg hover:bg-zinc-800">
          Exports
        </button>

        <button className="w-full text-left px-4 py-3 rounded-lg hover:bg-zinc-800">
          Settings
        </button>
      </nav>
    </aside>
  );
}