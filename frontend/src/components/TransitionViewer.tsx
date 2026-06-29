import transitionsData from "../mock/transitions.json";
import statesData from "../mock/states.json";

function getStateName(id: string) {
  const state = statesData.find((s) => s.id === id);
  return state ? state.state : "UNKNOWN";
}

export default function TransitionViewer() {
  return (
    <div className="bg-zinc-900 rounded-xl p-6 mt-6">
      <h2 className="text-lg font-semibold mb-4">
        State Transitions
      </h2>

      <div className="overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="border-b border-zinc-700 text-left">
              <th className="py-2">FROM</th>
              <th className="py-2">TO</th>
              <th className="py-2">METHOD</th>
              <th className="py-2">TRIGGER</th>
            </tr>
          </thead>

          <tbody>
            {transitionsData.map((transition, index) => (
              <tr
                key={index}
                className="border-b border-zinc-800 hover:bg-zinc-800"
              >
                <td className="py-3">
                  {getStateName(transition.from)}
                </td>

                <td>
                  {getStateName(transition.to)}
                </td>
                <td>{transition.method}</td>
                <td>{transition.trigger}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}