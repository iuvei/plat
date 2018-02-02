import {Component, OnInit, ViewChild} from "@angular/core";
import {Router} from "@angular/router";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {PubSubService} from "../components/pubsup.service";
import {ToasterConfig, ToasterService} from "angular2-toaster";
import {CustomHttp} from "../components/customhttp";
import {ChatService} from "../common/chat-service";
import {TranslateService} from "@ngx-translate/core";
import {MenuData} from "../model/MenuData";
import {Common} from "../common/common";
import {ChatMessage, ChatMessageCmd} from "../common/chat-message";

@Component({
  selector: 'app-dashboard',
  styleUrls: ['./full-layout.component.scss','../../assets/css/toaster.css'],
  templateUrl: './full-layout.component.html'

})
export class FullLayoutComponent implements OnInit {
  //url
  public url = Common.URL;

  public oldPwd : string;
  public newPwd : string;
  public receptPwd : string;
  @ViewChild('passwordModal')
  public passwordModal:ModalDirective;
  public currentUser : any;

  public disabled: boolean = false;
  public status: {isopen: boolean} = {isopen: false};
  //放大倍数
  private zoomes = ['zoom_100','zoom_90','zoom_80','zoom_70','zoom_60'];
  private zoomIndex = 0;
  private currentZoom = this.zoomes[this.zoomIndex];


  public toasterconfig : ToasterConfig = new ToasterConfig({newestOnTop: true,timeout:3000});

  constructor(private http: CustomHttp, private route: Router, private pubsub: PubSubService,
              private toaster: ToasterService, private chat: ChatService, private _translate: TranslateService){
    // chat.connect();
  }
  //设置菜单按钮显示
  //====菜单加载状态
  public menu_status:boolean = false;
  public _menu_data:MenuData[] = [];
  public _permissions = [];


  ngOnInit(): void {
    this.chat.connect();
    this.pubsub.errorToast.subscribe(data => {
      console.info("异常:"+data);
      this.toaster.pop('error', this._translate.instant('common.error'), data + '');
    });
    this.currentUser = JSON.parse(localStorage.getItem('loginUser'));
    this.chat.getMessage(data=>{
      let msg : ChatMessage = JSON.parse(data.data) as ChatMessage;
      if(msg.cmd==ChatMessageCmd.logout){
        alert(msg.text);
        this.logout();
      }
    });
    if(this.menu_status == false){
      this._menu_data = this.currentUser.user.menus;
      this._permissions = this.currentUser.user.permissions;
      this.menu_status = true;
    }

    this.goPage();
  }


  public toggled(open: boolean): void {
    console.log('Dropdown is now: ', open);
  }

  public toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isopen = !this.status.isopen;
  }



  logout(){
    localStorage.clear();
    this.chat.close();
    this.route.navigate(['/login']);
  }

  checkHasPermission  (name : string){
    for(let i=0;i<this.currentUser.user.roles.length;i++){
      let role = this.currentUser.user.roles[i];
      for (let k=0;k<role.permissions.length;k++){
        if(role.permissions[k].permissionName==name){
          return true;
        }
      }
    }
    return false;
  }

  modifyPassword(){
    if( this.newPwd !=this.receptPwd ) return;
    let condition = {
      oldpwd:this.oldPwd,
      newpwd:this.newPwd
    }
    this.http.post(this.url + '/modifyPassword',condition)
        .toPromise()
        .then(response => {
          let status = response.json()['status'];
          let message = response.json()['msg'] as string;
          if(status==0){
            this.toaster.pop('success', this._translate.instant('common.success'), message);
            this.passwordModal.hide();
          } else {
            this.toaster.pop('error', this._translate.instant('common.error'), message);
          }
          this.oldPwd = '';
          this.newPwd = '';
          this.receptPwd = '';
        } );

  }

  goPage(){
    let currentUser = JSON.parse(localStorage['loginUser']);
    //区分首页
    let roles = currentUser.user.roles as any[];
    for (let role of roles) {
      if (role.roleName == 'API' || role.roleName == 'ApiAgent') {
        this.route.navigate(['/api']);
        return;
      } else if (role.roleName == 'Agent') {
        this.route.navigate(['/agent']);
        return;
      }
    }
    this.route.navigate(['/sys']);
  }
  zoomSmall(){
    if(this.zoomIndex<this.zoomes.length-1){
      this.zoomIndex++;
      this.currentZoom = this.zoomes[this.zoomIndex];
    }
  }

  zoomBig(){
    if(this.zoomIndex>0){
      this.zoomIndex--;
      this.currentZoom = this.zoomes[this.zoomIndex];
    }
  }

}
