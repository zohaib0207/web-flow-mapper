export default function Navbar() {
  return (
    <header className="h-16 border-b border-zinc-800 bg-zinc-900 flex items-center justify-between px-6">
      <h1 className="text-xl font-semibold text-white">
        Dashboard
      </h1>

      <div className="text-sm text-zinc-400">
        Web Flow Mapper v1.0
      </div>
    </header>
  );
}