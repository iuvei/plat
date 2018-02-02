import {Component, OnInit, ViewChild} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';

import {BetLevelService} from "./betlevel.service";
import {TranslateService} from "@ngx-translate/core";
import {ToasterService} from "angular2-toaster";
import {ConfirmationService} from "primeng/primeng";
import {BetLevel} from "app/betlevel/betlevel";
import {Common} from "../common/common";

@Component({
  templateUrl: 'betlevel.component.html',
  styleUrls: ['./betlevel.component.scss']
})
export class BetLevelComponent implements  OnInit {

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  public dataList: any[] = [];

  @ViewChild('createModal')
  public createModal: ModalDirective;

  @ViewChild('modifyModal')
  public modifyModal: ModalDirective;

  public searchForm: BetLevel = new BetLevel();

  public form: BetLevel = new BetLevel();

  constructor(private betLevelService: BetLevelService, private _toaster: ToasterService,
              private _translate: TranslateService, private confirmationService: ConfirmationService) {
  }

  ngOnInit() {
    this.search();
  }

  search() {
    const param = this.searchForm;
    param['page'] = this.currentPage;
    param['rows'] = this.pageSize;
    this.betLevelService.search(param).subscribe(
      data => {
        this.totalItems = data.total;
        this.dataList = data.rows;
      },
      err => {
        console.log(err);
    });
  }

  pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  create() {
    this.betLevelService.create(this.form).subscribe(
      data => {
        if (data.ok) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.closeCreateModal();
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
    return true;
  }

  modify() {
    this.betLevelService.modify(this.form).subscribe(
      data => {
        if (data.ok) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.closeModifyModal();
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  deleteData(id: string) {
    this.confirmationService.confirm({
      header: this._translate.instant('betLevel.deleteBetLevel'),
      message: this._translate.instant('betLevel.confirmDeleteBetLevel'),
      accept : () => {
        this.betLevelService.delete({id: id}).subscribe(
          data => {
            if (data.ok) {
              this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
              this.closeModifyModal();
              this.search();
            } else {
              this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
            }
          },
          err => {
            console.log(err);
          });
      }
    });
  }

  openModifyModal(item: any) {
    this.form.name = item.name;
    this.form.conditions = item.conditions;
    this.modifyModal.show();
  }

  closeCreateModal() {
    this.form = new BetLevel();
    this.createModal.hide();
  }

  closeModifyModal() {
    this.form = new BetLevel();
    this.modifyModal.hide();
  }

}
