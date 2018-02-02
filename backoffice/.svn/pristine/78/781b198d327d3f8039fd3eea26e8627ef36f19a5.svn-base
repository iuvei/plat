import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {CustomHttp} from "../components/customhttp";
import {Common} from "../common/common";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {ConfirmationService} from "primeng/components/common/api";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {Response} from "@angular/http";
import {AnnounceSerice} from "./sysannounce.service";
import {CommonUtil} from "../common/CommonUtil";

@Component({
  templateUrl: 'sysannounce.component.html',
  styleUrls: ['./sysannounce.component.scss']
})
export class SysAnnounceComponent implements  OnInit {

  @ViewChild('addAnnounceDialog')
  public addAnnounceDialog:ModalDirective;

  @ViewChild('updateAnnounceDialog')
  public updateAnnounceDialog:ModalDirective;

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  public tableList: any[] = [];

  //quert param
  public startTime: Date = new Date();
  public endTime: Date = new Date();
  public loginName: string = '';

  public datalist:any[] = [];



  public currentUser:any;
  public status:boolean = true;
  public msg:string;

  constructor(private _announceSerice:AnnounceSerice,private _translate: TranslateService,private http: CustomHttp,private confirmDialog: ConfirmationService, private toaster:ToasterService) {
    (this.startTime = new Date()).setDate(this.endTime.getDate()-30);
    (this.endTime = new Date()).setDate(this.endTime.getDate()+1);
  }

  ngOnInit() {
    //this.initTree();
    this.currentUser = JSON.parse(localStorage.getItem('loginUser')).user;
    this.search();
  }


  //检测用户名是否重复
  public checkUser(){
    var data = {
      loginName:this.createAnnounceCon.loginName
    }
    if(this.createAnnounceCon.loginName == null){
      this.status = true;
      return;
    }

    this.http
      .post(Common.URL +'/announcemanage/checkSysUser',data)
      .subscribe(
        (
          response  =>{
            this.status = response.json()['ok'] as boolean;
            this.msg = response.json()['msg'] as string
          })
      );
  }

  gotoDelete(data:any){
    let msg =  this._translate.instant('sysannounce.deleteData');
    this.confirmDialog.confirm({
      message: msg,
      accept: () => {
        this._announceSerice.delete(data.announceId).subscribe(data=>{
          if (data.status ==0) {
            this.toaster.pop('success', this._translate.instant('common.success'), data.msg);
            this.search();
          } else {
            this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
          }
        });
      }
    });
  }

  public updateAnnounceCon = {
    announceDesc:'',
    announceTitle:'',
    userName:'',
    type:0,
    contentId:0,
    id:0
  };
  gotoUpdateAnnounce(data:any){
    this.updateAnnounceCon.announceDesc = data.conentDesc;
    this.updateAnnounceCon.announceTitle = data.contentTitle;
    this.updateAnnounceCon.userName = data.userName;
    this.updateAnnounceCon.type = data.type;
    this.updateAnnounceCon.contentId = data.announceId;
    this.updateAnnounceCon.id=data.id;
    this.updateAnnounceDialog.show();
  }
  updateAnnounceDo(){
    this._announceSerice.update(this.updateAnnounceCon).subscribe(data=>{
        if (data.status ==0) {
          this.updateAnnounceDialog.hide();
          this.toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.search();
        } else {
          this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      });
  }

  public createAnnounceCon = {
    contentDesc:'',
    contentTitle:'',
    loginName:'',
    type:0
  };
  gotoCreateAnnounce(){
    this.glaobal_clearObjectVal(this.createAnnounceCon);
    this.status = true;
    this.msg = '';
    this.createAnnounceCon.type = 0 as number;
    this.addAnnounceDialog.show();
  }
  createAnnounceDo(){
    if(this.createAnnounceCon.type == 0){
      //this.addAnnounceDialog.hide();
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('sysannounce.chooseType'));
      return;
    }
    this._announceSerice.create(this.createAnnounceCon).subscribe(data=>{
      if (data.status ==0) {
        this.addAnnounceDialog.hide();
        this.toaster.pop('success', this._translate.instant('common.success'), data.msg);
      } else {
        this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
      }
      this.search();
    });
  }


  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }


  public search() {
    let condition = {
      userName:this.loginName,
      startDate:CommonUtil.getDate(this.startTime),
      endDate:CommonUtil.getDate(this.endTime),
      currentPage: this.currentPage,
      pageSize: this.pageSize
    };
    this._announceSerice.search(condition).subscribe(rec=>{
      this.datalist = rec.data.data;
      this.totalItems = rec.data.total;
    });
  }


  //清除双向绑定对象的值
  public glaobal_clearObjectVal(obj:any) {
    let keys:any[] = Object.keys(obj);
    for (let key of keys) {
      obj[key] = '';
    }
  }

}
