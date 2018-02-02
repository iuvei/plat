/**
 * 发送消息数据结构。
 * Created by sunny on 2017/6/27 0027.
 */
export class ChatMessage{

  constructor(private _cmd:number, private _text:string) {
  }


  get cmd(): number {
    return this._cmd;
  }

  set cmd(value: number) {
    this._cmd = value;
  }

  get text(): string {
    return this._text;
  }

  set text(value: string) {
    this._text = value;
  }
}

export enum ChatMessageCmd {
  login = 1,
  logout = 2
};
