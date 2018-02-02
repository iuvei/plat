import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Http,Headers,RequestOptions} from '@angular/http';
import * as moment from 'moment';
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {TranslateService} from "@ngx-translate/core";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {GameType} from "../../model/gametype";
import {GameResultVoCondition} from "../../model/gameresultvocondition";
import {Common} from "../../common/common";
import {CustomHttp} from "../../components/customhttp";
import {WindowRef} from "../../common/window-ref";
import {CommonUtil} from "../../common/CommonUtil";
@Component({
  selector: 'app-gameresult',
  templateUrl: './gameresult.component.html',
  styleUrls: ['./gameresult.component.scss']
})

export class GameResultComponent implements OnInit {

  //时间控制
  public endTime:Date = new Date();
  public startTime:Date=new Date();
  public showStartDatePicker:boolean = false;
  public showEndDatePicker:boolean = false;
  public gameTypeList:GameType[] = [];
  public gameResultList:any[] = [];
  //public gameTypeCondion:string = "";
  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  //初始化查询条件
  public gameresultvocondition: GameResultVoCondition = new GameResultVoCondition("","","",1,10);
  //服务端返回的状态
  public status:boolean = true;
  public msg:string;
  //
  public flashUrl:string;

  //
  public flashlandtid:string;
  public swfList:number[];
  @ViewChild('luziModal')
  public modifyModal:ModalDirective;


  //服务器的地址
  public url:string = Common.URL;

  public constructor( private toaster:ToasterService,private http:CustomHttp,private winRef: WindowRef, private _translate: TranslateService) {
    if(new Date().getHours()<12){
      this.startTime = new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate()-1);
      this.endTime = new Date(this.endTime.getFullYear(), this.endTime.getMonth(), this.endTime.getDate());
    }else{
      this.startTime = new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate());
      this.endTime = new Date(this.endTime.getFullYear(), this.endTime.getMonth(), this.endTime.getDate()+1);
    }
    this.startTime.setHours(12,0,0);
    this.endTime.setHours(11,59,59);
  }

  ngOnInit() {
    this.getGameType();
    this.updateCondition();
  }

  public getFlash() {

  }

  public flashShow(bid:number) {
    let geturl = this.url + '/gameresult/gameLand?bid=' + bid + "&gtid=" + this.gameresultvocondition.gtid;
    this.http
      .get(geturl)
      .subscribe(
        (
          response => {
            // console.info("==================="+this.window['swfList']);

            this.swfList = response.json()['data']['list']  as any[];
            this.winRef.nativeWindow.window['swfList']=this.swfList;
            this.flashlandtid = response.json()['data']['viewlandtid']  as string
            this.winRef.nativeWindow.window['intSwf'](this.flashlandtid,this.url);
          })
      );

    this.modifyModal.show();
    this.initSwf();
  }

  //提示信息控制
  public updateStatus() {
    this.status = true;
    this.msg = "";
    return;
  }

  public setPage(pageNo:number):void {
    this.currentPage = pageNo;
  }

  public pageChanged(event:any):void {
    //更新查询页数
    this.currentPage = event.page;
    this.query();
  }

  //查询游戏结果
  public query() {
    this.updateCondition();
    if (this.gameresultvocondition.gtid == null || this.gameresultvocondition.gtid == '') {
      this.toaster.pop('error', this._translate.instant("common.warning"), this._translate.instant('gameResult.plsSelectTableNo'));
      return;
    }
    let options = new RequestOptions({});
    options.headers = new Headers();
    options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    this.http
      .post(this.url + '/gameresult/gameResultList',this.transformRequest(this.gameresultvocondition),options)
      .subscribe(
        (
          response => {
            this.gameResultList = response.json()['data']['rows']  as any[];
            this.totalItems = response.json()['data']['total']  as number
          })
      );
  }

  //获取游戏桌子
  public getGameType() {
    this.http
      .post(this.url + '/gameresult/gametables', '')
      .subscribe(
        (
          response => {
            this.gameTypeList = response.json()['data'] as GameType[];
          })
      );
  }

  public closeEnd() {
    this.showEndDatePicker = false;
  }

  public closeStart() {
    this.showStartDatePicker = false;
  }
  public updateCondition(){
    this.gameresultvocondition.startDt = CommonUtil.formatDate(this.startTime);
    this.gameresultvocondition.endDt = CommonUtil.formatDate(this.endTime);
    this.gameresultvocondition.page = this.currentPage;
    this.gameresultvocondition.rows = this.pageSize;
  }

  public initSwf() {

  }

 public transformRequest(data:any){
    var str =[];
    for(var p in data){
      str.push(p+'='+data[p]);
    }
    return str.join('&');
  }
}


