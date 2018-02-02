import {Component, OnInit, ViewChild} from '@angular/core';
import {ModalDirective} from 'ng2-bootstrap/modal/modal.component';

import {IpBlackListService} from "./ipblacklist.service";
import {IpBlackListSearchForm} from "./ipblacklistsearchform";
import {IpBlackListForm} from "./ipblacklistfrom";
import {ToasterService} from "angular2-toaster";
import {ConfirmationService} from "primeng/primeng";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "../common/common";
import {isUndefined} from "util";

@Component({
  templateUrl: 'ipblacklist.component.html',
  styleUrls: ['./ipblacklist.component.scss']
})
export class IpBlackListComponent implements OnInit {

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;
  private uploadurl = Common.URL+ '/ipBlackWhiteList/uploadExcel';

  dataList: any[] = [];

  @ViewChild('createModal')
  public createModal: ModalDirective;

  @ViewChild('batchCreateModal')
  public batchCreateModal: ModalDirective;

  @ViewChild('removeMultipleModal')
  public removeMultipleModal: ModalDirective;
  // 搜索参数
  searchForm: IpBlackListSearchForm = new IpBlackListSearchForm();
  // 创建模型
  form: IpBlackListForm = new IpBlackListForm();

  // 多选框数据
  unRemoveBlackList: any[] = [];
  removeBlackList: any[] = [];

  selectedUnRemoveBlack: string[] = [];

  selectedRemoveBlack: string[] = [];

  constructor(private ipBlackListService: IpBlackListService, private _toaster: ToasterService,
              private confirmationService: ConfirmationService, private _translate: TranslateService) {
  }

  ngOnInit() {
    this.search();
  }

  public search() {
    if (this.searchForm.ip !='' && !this.isIP(this.searchForm.ip.trim())) {
      this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('ipBlackList.ipFormatError') + this.searchForm.ip.trim());
      return;
    }
    this.ipBlackListService.search(JSON.stringify(this.searchForm)).subscribe(
      ips => {
        this.totalItems = ips.data.total;
        this.dataList = ips.data.data;
      });
  }

  create() {
    /**
     this.form.ips=[];
     const arrIps = this.form.ip.trim().split(';');
     if (arrIps.length > 10) {
      this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('ipBlackList.maxAddIpAddress'));
      return;
    }
     for (const index in arrIps) {
      if (!this.isIP(arrIps[index].trim())) {
          this._toaster.pop('error', this._translate.instant('common.error'),
          this._translate.instant('ipBlackList.ipFormatError') + arrIps[index]);
          return;
      }
      this.form.ips.push(arrIps[index].trim());
    }
     */
    if (!this.isIP(this.form.start.trim())) {
      this._toaster.pop('error', this._translate.instant('common.error'),
        this._translate.instant('ipBlackList.ipFormatError') + this.form.start);
      return;
    }
    if (!this.isIP(this.form.end.trim())) {
      this._toaster.pop('error', this._translate.instant('common.error'),
        this._translate.instant('ipBlackList.ipFormatError') + this.form.end);
      return;
    }
    this.ipBlackListService.create(JSON.stringify(this.form)).subscribe(
      data => {
        if (data.status == 0) {
          this.closeCreateModal();
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }
  batchCreateModalShow(){
    this.uploadedFiles=[];
    this.batchCreateModal.show();
  }

  batchCreate() {
    if(isUndefined(this.form.filePath) || this.form.filePath==''){
      this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('ipBlackList.dealerpicVaild'));
      return;
    }
    this.ipBlackListService.batchCreate(JSON.stringify(this.form)).subscribe(
      data => {
        if (data.status == 0) {
          this.closeBatchCreateModal();
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  uploadedFiles: any[] = [];
  onUpload(event) {
    for(let file of event.files) {
      this.uploadedFiles.push(file);
    }
    let responseBody = JSON.parse(event.xhr.response);
    if(responseBody.status==0){
      this._toaster.pop('success', this._translate.instant('common.success'), responseBody.msg);
      this.form.filePath = responseBody.data;
    }else{
      this._toaster.pop('error', this._translate.instant('common.error'), responseBody.msg);
      this.uploadedFiles = [];
    }
  }

  pageChanged(event: any): void {
    this.currentPage = event.page;
    this.searchForm.currentPage =event.page;
    this.search();
  }

  closeCreateModal() {
    this.form = new IpBlackListForm();
    this.createModal.hide();
  }

  closeBatchCreateModal() {
    this.form = new IpBlackListForm();
    this.batchCreateModal.hide();
  }

  openRemoveMultipleModal() {
    this.unRemoveBlackList = this.dataList.slice(0);
    this.removeMultipleModal.show();
  }

  closeRemoveMultipleModal() {
    this.selectedRemoveBlack = [];
    this.selectedUnRemoveBlack = [];
    this.removeBlackList = [];
    this.removeMultipleModal.hide();
  }

  removeMultiple() {
    const list = [];
    for (const item of this.removeBlackList) {
      list.push(item.id);
    }
    this.ipBlackListService.remove({
      ids: list
    }).subscribe(
      data => {
        if (data.status == 0) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.closeRemoveMultipleModal();
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  remove(item: any) {
    const list = [];
    list.push(item.id);
    this.confirmationService.confirm({
      message: this._translate.instant('ipBlackList.isRemoveBlack'),
      accept: () => {
        this.ipBlackListService.remove({
          ids: list
        }).subscribe(
          data => {
            if (data.status == 0) {
              this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
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

  isIP(ipaddress) {
    if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddress)) {
      return true;
    }
    return false;
  }

  addBlack() {
    const tempPermissionList = this.removeBlackList.slice(0);
    const tempSelectList = this.selectedRemoveBlack.slice(0);

    tempPermissionList.forEach(p => {
      tempSelectList.forEach(i => {
        if (p.id === i) {
          this.unRemoveBlackList.push(p);
          const index = this.removeBlackList.indexOf(p);
          this.removeBlackList.splice(index, 1);

          const selectIndex = this.selectedRemoveBlack.indexOf(i);
          this.selectedRemoveBlack.splice(selectIndex, 1);
        }
      });
    });
  }

  addAllBlack() {
    const list = this.removeBlackList.splice(0, this.removeBlackList.length);
    this.unRemoveBlackList = this.unRemoveBlackList.concat(list);
  }

  removeBlack() {
    const tempPermissionList = this.unRemoveBlackList.slice(0);
    const tempSelectList = this.selectedUnRemoveBlack.slice(0);

    tempPermissionList.forEach(p => {
      tempSelectList.forEach(i => {
        if (p.id === i) {
          this.removeBlackList.push(p);
          const index = this.unRemoveBlackList.indexOf(p);
          this.unRemoveBlackList.splice(index, 1);

          const selectIndex = this.selectedUnRemoveBlack.indexOf(i);
          this.selectedUnRemoveBlack.splice(selectIndex, 1);
        }
      });
    });
  }

  removeAllBlack() {
    const list = this.unRemoveBlackList.splice(0, this.unRemoveBlackList.length);
    this.removeBlackList = this.removeBlackList.concat(list);
  }

}
