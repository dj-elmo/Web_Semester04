import type { IZutatDTD } from './IZutatDTD';

export interface IDoenerDTD {
  id: number
  bezeichnung: string
  preis: number
  vegetarizitaet: number
  zutaten: Array<IZutatDTD>
  verfuegbar: number
}