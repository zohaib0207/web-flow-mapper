import ReactFlow, {
  Background,
  Controls,
} from "reactflow";

import "reactflow/dist/style.css";

type GraphViewerProps = {
  nodes: any[];
  edges: any[];
  onNodeSelect: (node: any) => void;
};

export default function GraphViewer({
  nodes,
  edges,
  onNodeSelect,  
}: GraphViewerProps) {
  return (
    <div className="h-full w-full">
      <ReactFlow
         nodes={nodes} 
         edges={edges}
         onNodeClick={(_, node) => onNodeSelect(node)}
         >
        <Background />
        <Controls />
      </ReactFlow>
    </div>
  );
}