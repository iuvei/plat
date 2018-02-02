import {Component, OnInit} from '@angular/core';
import {HabaBetRecordService} from './hababetrecord.service';
import {HabaBetRecordSearchForm} from "./hababetrecordsearchform";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "../../common/common";

@Component({
  templateUrl: 'hababetrecord.component.html',
  styleUrls: ['./hababetrecord.component.scss']
})
export class HabaBetRecordComponent implements  OnInit {
  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  private maxSize: number = Common.MAX_PAGE_SIZE;

  public tableList: any[] = [];

  // quert param
  searchForm: HabaBetRecordSearchForm = new HabaBetRecordSearchForm();

  constructor(private habaBetRecordService: HabaBetRecordService, private _translate: TranslateService) {
  }

  ngOnInit() {
    this.search();
  }

  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  public search() {
    this.habaBetRecordService.search({
      SbetTime: this.searchForm.SbetTime.toLocaleDateString(),
      EbetTime: this.searchForm.EbetTime.toLocaleDateString(),
      Username: this.searchForm.Username,
      page: this.currentPage,
      rows: this.pageSize
    }).subscribe(
      data => {
        this.totalItems = data.total;
        this.tableList = data.rows;
      },
      err => {
        console.log(err);
      });
  }

}
