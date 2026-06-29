import type { FieldSchema } from "./FieldSchema";

export interface FormSchema {
  id: string;
  action: string;
  method: string;
  fields: FieldSchema[];
}