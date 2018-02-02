export class GameTableForm {
  id:number = null;
  gameId: number = null;
  name: string = null;
  status: string = '';
  countDownSeconds:  number = null;
  type?: string = '';
  isMany: number = 0;
  miCountdownSeconds: number = 0;
  min:number;
  max:number;
}
