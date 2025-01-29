export interface IDepartment {
  id?: string;
  name?: string | null;
  university?: string | null;
}

export const defaultValue: Readonly<IDepartment> = {};
