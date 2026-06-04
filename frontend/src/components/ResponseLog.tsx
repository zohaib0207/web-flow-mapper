const mockResults = [
  {
    url: "/",
    status: 200,
    responseTime: "120ms",
  },
  {
    url: "/login",
    status: 302,
    responseTime: "45ms",
  },
  {
    url: "/dashboard",
    status: 200,
    responseTime: "180ms",
  },
  {
    url: "/admin",
    status: 403,
    responseTime: "90ms",
  },
];

export default function ResponseLog() {
  return (
    <div className="bg-zinc-900 rounded-xl p-6">
      <h2 className="text-lg font-semibold mb-4">
        Scan Results
      </h2>

      <table className="w-full">
        <thead>
          <tr className="border-b border-zinc-700">
            <th className="text-left py-3">URL</th>
            <th className="text-left py-3">Status</th>
            <th className="text-left py-3">Response Time</th>
          </tr>
        </thead>

        <tbody>
          {mockResults.map((result, index) => (
            <tr
              key={index}
              className="border-b border-zinc-800"
            >
              <td className="py-3">{result.url}</td>
              <td className="py-3">{result.status}</td>
              <td className="py-3">{result.responseTime}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}