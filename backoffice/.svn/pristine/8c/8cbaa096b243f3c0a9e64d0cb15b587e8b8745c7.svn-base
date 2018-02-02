import {Component, OnDestroy, OnInit} from '@angular/core';
import {LogRecordService} from './logrecord.service';
import {TranslateService} from "@ngx-translate/core";
import {CommonUtil} from "../../common/CommonUtil";
import {SearchModel} from "./search.model";
import {Common} from "app/common/common";

@Component({
  templateUrl: 'logrecord.component.html',
  styleUrls: ['./logrecord.component.scss']
})
export class LogRecordComponent implements  OnInit,OnDestroy {
  private tableList: any[] = [];
  public maxSize: number = Common.MAX_PAGE_SIZE;
  private searchModel : SearchModel = new SearchModel();
  private totalItems;

  constructor(private logRecordService: LogRecordService, private _translate: TranslateService) {

  }

  ngOnInit() {
    let obj = JSON.parse(sessionStorage.getItem("logrecord-component"));
    if(obj){
      this.tableList = obj.tableList;
      this.searchModel = SearchModel.create(obj.searchModel);
      this.totalItems = obj.totalItems;
    }else {
      this.searchModel.startTime = CommonUtil.getStartDate();
      this.searchModel.endTime = CommonUtil.getEndDate();
      this.search();
    }
  }
  ngOnDestroy(): void {
    let json = {"tableList":this.tableList,"searchModel":this.searchModel,"totalItems":this.totalItems}
    sessionStorage.setItem("logrecord-component",JSON.stringify(json));
  }

  public pageChanged(event: any): void {
    this.searchModel.currentPage =  event.page;
    this.search();
  }

  public search() {
    this.logRecordService.search(CommonUtil.formatDate(this.searchModel.startTime),CommonUtil.formatDate(this.searchModel.endTime),
      this.searchModel.state, this.searchModel.userName, this.searchModel.ip, this.searchModel.loginType,this.searchModel.currentPage, this.searchModel.pageSize).subscribe(
      log => {
        this.totalItems = log.data.total;
        this.tableList = log.data.data;
      },
      err => {
        console.log(err);
      });
  }

}
