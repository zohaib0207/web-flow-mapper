export interface Transition {
  transitionId: string;

  from: string;
  to: string;

  method: string;
  trigger: string;

  depth: number;

  discoveredByMutation: boolean;

  priority: number;
  priorityReason: string;
}