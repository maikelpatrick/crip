export interface ISaque {
  id?: number;
  volume?: number | null;
  client_account?: string | null;
  entidade_account?: string | null;
}

export class Saque implements ISaque {
  constructor(
    public id?: number,
    public volume?: number | null,
    public client_account?: string | null,
    public entidade_account?: string | null
  ) {}
}

export function getSaqueIdentifier(saque: ISaque): number | undefined {
  return saque.id;
}
