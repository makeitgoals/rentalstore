export interface ICompany {
  id?: number;
  companyName?: string;
  dealsIn?: string;
  officeAddress?: string | null;
  companyPhoneNumber?: string | null;
  email?: string | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public companyName?: string,
    public dealsIn?: string,
    public officeAddress?: string | null,
    public companyPhoneNumber?: string | null,
    public email?: string | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
