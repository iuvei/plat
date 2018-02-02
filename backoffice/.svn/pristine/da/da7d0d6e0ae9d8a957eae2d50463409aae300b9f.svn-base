import {Component, OnInit} from '@angular/core';
import {TableWinLostService} from './tablewinlost.service';
import {TranslateService} from "@ngx-translate/core";
import {ToasterService} from "angular2-toaster";
import {CommonUtil} from "../../common/CommonUtil";
import {Common} from "app/common/common";

@Component({
  templateUrl: 'tablewinlost.component.html',
  styleUrls: ['./tablewinlost.component.scss']
})
export class TableWinLostComponent implements  OnInit {
  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  private _maxSize: number = Common.MAX_PAGE_SIZE;

  public tableList: any[] = [];

  //quert param
  public startTime: Date = new Date();
  public endTime: Date = new Date();
  public tid: string = '';
  public bid: string = '';

  constructor(private tableWinLostService: TableWinLostService, private _translate: TranslateService, private _toaster: ToasterService) {
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
    this.search();
  }

  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  public search() {
    this.tableWinLostService.search(CommonUtil.formatDate(this.startTime),CommonUtil.formatDate(this.endTime),
      this.tid, this.bid, this.currentPage, this.pageSize).subscribe(
      rc => {
        this.totalItems = rc.total;
        this.tableList = this.dealListData(rc.data.data);
      },
      err => {
        console.log(err);
      });
  }

  //处理后台返回的数据
  dealListData(datas:any[]) {
    if (datas.length <= 0)
      return datas;
    for (let data of datas) {
      //处理余额
      data.betamount = this.tableWinLostService.formatMoney(data.betAmount, 2);
      data.settleAmount = this.tableWinLostService.formatMoney(data.settleAmount, 2);
      data.winLostAmount = this.tableWinLostService.formatMoney(data.winLostAmount, 2);
    }
    return datas;
  }

}
