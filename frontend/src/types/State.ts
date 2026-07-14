export interface State {
  stateId: string;
  url: string;

  depth: number;
  statusCode: number;

  hasLoginForm: boolean;
  authenticationRequired: boolean;

  discoveredByMutation: boolean;

  isUnknownState: boolean;
  discoveryReason: string;

  timestamp: string;

  forms: any[];
  links: string[];
}