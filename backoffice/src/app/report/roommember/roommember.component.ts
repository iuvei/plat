/**
 * Created by admin on 2017/8/21.
 */
import {Component, OnDestroy, OnInit} from "@angular/core";
import {RoomMemberService} from "./roommember.service";
import {TranslateService} from "@ngx-translate/core";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {DomSanitizer} from "@angular/platform-browser";
import {CommonUtil} from "../../common/CommonUtil";
import {SearchModel} from "app/report/roommember/search.model";
import {Common} from "app/common/common";


@Component({
  templateUrl: 'roommember.component.html',
  styleUrls: ['./roommember.component.scss']
})
export class RoomMemberComponent implements  OnInit,OnDestroy {
  private roomTableList: any[] ;
  public totalItems: number = 0;
  public maxSize: number = Common.MAX_PAGE_SIZE;
  private searchModel : SearchModel = new SearchModel();

  constructor(private roomMemberService:RoomMemberService, private _translate:TranslateService, private toaster:ToasterService, private domSani:DomSanitizer) {
  }

  ngOnInit() {
    let obj = JSON.parse(sessionStorage.getItem("roommember-component"));
    if(obj){
      this.searchModel = SearchModel.create(obj.searchModel);
      this.roomTableList = obj.roomTableList;
    }else {
      this.searchModel.startTime = CommonUtil.getStartDate();
      this.searchModel.endTime = CommonUtil.getEndDate();
      this.search();
    }
  }


  ngOnDestroy(): void {
    let json = {"searchModel":this.searchModel,"roomTableList":this.roomTableList};
    sessionStorage.setItem("roommember-component",JSON.stringify(json));
  }

  public pageChanged(event:any):void {
    this.searchModel.currentPage = event.page;
    this.search();
  }

  public search() {
    this.roomMemberService.search(this.searchModel).subscribe(
      betOrder => {
        if(betOrder.data.total ==0){
          this.roomTableList = [];
          this.totalItems = 0;
        }else{
          this.roomTableList = betOrder.data.data;
          this.totalItems = betOrder.data.total;
        }
      },
      err => {
        this.toaster.pop('error', this._translate.instant('common.error'), err);
      });
  }


  //toaster共用的函数
  public glaobal_toaster(response:Response) {
    let ok = response.json()['ok'] as boolean;
    let message = response.json()['msg'] as string;
    if (ok) {
      this.toaster.pop('success', this._translate.instant('common.success'), message);
    } else {
      this.toaster.pop('error', this._translate.instant('common.error'), message);
    }
  }
}

