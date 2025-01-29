export interface IStudent {
  id?: string;
  name?: string | null;
  email?: string | null;
  phoneNo?: number | null;
  percentage?: number | null;
  course?: string | null;
}

export const defaultValue: Readonly<IStudent> = {};
