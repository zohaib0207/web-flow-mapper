import ReactFlow, {
  Background,
  Controls,
} from "reactflow";

import "reactflow/dist/style.css";

const nodes = [
  {
    id: "1",
    position: { x: 100, y: 100 },
    data: { label: "Homepage" },
  },
  {
    id: "2",
    position: { x: 100, y: 250 },
    data: { label: "Login" },
  },
  {
    id: "3",
    position: { x: 100, y: 400 },
    data: { label: "Dashboard" },
  },
];

const edges = [
  {
    id: "e1-2",
    source: "1",
    target: "2",
  },
  {
    id: "e2-3",
    source: "2",
    target: "3",
  },
];

export default function GraphViewer() {
  return (
    <div className="h-full w-full">
      <ReactFlow nodes={nodes} edges={edges}>
        <Background />
        <Controls />
      </ReactFlow>
    </div>
  );
}