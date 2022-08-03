export interface IDeposito {
  id?: number;
  volume?: number | null;
  id_deposito?: string | null;
  client_account?: string | null;
  entidade_account?: string | null;
}

export class Deposito implements IDeposito {
  constructor(
    public id?: number,
    public volume?: number | null,
    public id_deposito?: string | null,
    public client_account?: string | null,
    public entidade_account?: string | null
  ) {}
}

export function getDepositoIdentifier(deposito: IDeposito): number | undefined {
  return deposito.id;
}
