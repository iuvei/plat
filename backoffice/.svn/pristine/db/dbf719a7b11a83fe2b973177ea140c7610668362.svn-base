import {Injectable} from "@angular/core";
import {Common} from "./common";
import {ChatMessage, ChatMessageCmd} from "./chat-message";

/**
 * Created by sunny on 2017/4/12 0012.
 */
@Injectable()
export class ChatService {
  private socket : WebSocket;

  constructor() {
    console.info("构造....")
  }

  connect(){
    let currentUser = JSON.parse(localStorage.getItem('loginUser'));
    if (currentUser && currentUser.user && currentUser.user.token){
      console.info("初始化....")

      this.socket=new WebSocket(Common.MESSAGE_URL);
      let _this = this;
      this.socket.onopen = function (evt) {
        console.info("消息发送")
        _this.sendMessage(ChatMessageCmd.login, currentUser.user.token);
      };

      // this.socket.onmessage = function(evt){
      //   console.info("收到消息："+evt.data)
      // };

      // this.socket.co
      // this.socket.emit("login", currentUser.user.token,res=>{
      // });
    }
  }

  close(){
    this.socket.close();
  }

  sendMessage(cmd:ChatMessageCmd,text:string){
    // this.socket.emit("message", msg);
    let msg = `{"cmd":${cmd},"text":"${text}"}`;
    this.socket.send(msg);
  }

  getMessage(listen) {
    this.socket.onmessage=listen;
  }

}
