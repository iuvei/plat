import {Component, OnInit, ViewChild} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';

import {ApiLogService} from "./apilog.service";
import {ApiLogSearchForm} from "./apilogsearchform";
import {ApiLogForm} from "./apilogform";
import {TranslateService} from "@ngx-translate/core";
import {CommonUtil} from "../common/CommonUtil";
import {Common} from "../common/common";

@Component({
  templateUrl: 'apilog.component.html',
  styleUrls: ['./apilog.component.scss']
})
export class ApiLogComponent implements  OnInit {
  public startTime: Date = new Date();
  public endTime: Date = new Date();
  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  dataList: any[] = [];

  @ViewChild('requestDataModal')
  public requestDataModal: ModalDirective;

  // 搜索参数
  searchForm: ApiLogSearchForm = new ApiLogSearchForm();
  // 创建模型
  form: ApiLogForm = new ApiLogForm();

  constructor(private apiLogService: ApiLogService,  private _translate: TranslateService) {
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

  public search() {
    this.apiLogService.search(CommonUtil.formatDate(this.startTime), CommonUtil.formatDate(this.endTime),
      this.searchForm.merchantNo,this.searchForm.ip,
      parseInt(this.searchForm.status),this.currentPage,this.pageSize
  ).subscribe(
      api => {
        this.totalItems = api.data.total;
        if(this.totalItems ==0){
          this.dataList=[];
        }else{
          this.dataList = api.data.data;
        }
      },
      err => {
        console.log(err);
    });
  }

  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  openRequestDataModal(item: any) {
    this.form.encryptData = item.encryptData;
    this.form.decryptData = item.decryptData;
    this.requestDataModal.show();
  }

  closeRequestDataModal() {
    this.form = new ApiLogForm();
    this.requestDataModal.hide();
  }
}
