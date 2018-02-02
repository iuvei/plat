import {Component, OnInit, ViewChild} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';
import { DatePipe } from '@angular/common';
import {DomSanitizer} from '@angular/platform-browser';
import {AbnormalTableService} from "./abnormaltable.service";
import {AbnormalTableForm} from "./abnormaltablefrom";
import {AbnormalTableSearchForm} from "./abnormaltablesearchform";
import {AbnormalTableModalConfig} from "./abnormaltable-modal-config";
import {ToasterService} from "angular2-toaster";
import {TranslateService} from "@ngx-translate/core";
import {SelectItem} from 'primeng/primeng';

@Component({
  templateUrl: 'abnormaltable.component.html',
  styleUrls: ['./abnormaltable.component.scss']
})
export class AbnormalTableComponent implements  OnInit {

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = 10;
  // 表格数据
  dataList: any[] = [];
  // 搜索参数
  searchForm: AbnormalTableSearchForm = new AbnormalTableSearchForm();
  // 创建模型
  form: AbnormalTableForm = new AbnormalTableForm();

  @ViewChild('modifyModal')
  public modifyModal: ModalDirective;

  @ViewChild('rouletteModifyModal')
  public rouletteModifyModal: ModalDirective;

  modalConfig: AbnormalTableModalConfig = new AbnormalTableModalConfig();

  // 游戏类型
  gameTypeList: string[] = [];

  onSubmitFlag: number = 0;

  constructor(private abnormalTableService: AbnormalTableService, private datepipe: DatePipe,
              private _toaster: ToasterService, private _translate: TranslateService, private domSani:DomSanitizer) {

    this.cardColors = [];
    this.cardColors.push({label:'♠', value:'S'})
    this.cardColors.push({label:'♣', value:'C'})
    this.cardColors.push({label:'♦', value:'D'})
    this.cardColors.push({label:'♥', value:'H'})
    this.cardNumbers = [];
    for(let i = 1; i<=10;i++){
      this.cardNumbers.push({label:i+'', value:i});
    }
    this.rouletteNumbers = [];
    for(let i = 0; i<=36;i++){
      this.rouletteNumbers.push({label:i+'', value:i+''});
    }

    this.cardNumbers.push({label:'J', value:11});
    this.cardNumbers.push({label:'Q', value:12});
    this.cardNumbers.push({label:'K', value:13});

  }

  ngOnInit() {
    this.getGameType();
    this.search();

  }

  //牌处理
  cardColors:SelectItem[];
  cardNumbers:SelectItem[];
  rouletteNumbers:SelectItem[];


  clear(cardNum:any) {
    switch (cardNum){
      case 11:
            this.form.bankCard1Mode = null;
            this.form.bankCard1Number = null;
            break;
      case 12:
        this.form.bankCard2Mode = null;
        this.form.bankCard2Number = null;
        break;
      case 13:
        this.form.bankCard3Mode = null;
        this.form.bankCard3Number = null;
        break;
      case 21:
        this.form.playerCard1Mode = null;
        this.form.playerCard1Number = null;
        break;
      case 22:
        this.form.playerCard2Mode = null;
        this.form.playerCard2Number = null;
        break;
      case 23:
        this.form.playerCard3Mode = null;
        this.form.playerCard3Number = null;
        break;
      case 88:
        this.form.result = null;
        break;
    }
  }
  search() {
    const param = this.searchForm;
    param['page'] = this.currentPage;
    param['rows'] = this.pageSize;
    this.abnormalTableService.search(param).subscribe(
      data => {
        this.dataList = data.data;

        this.dataList.forEach(function(item){
          item.data.status = "";
        });


      },
      err => {
        console.log(err);
    });
  }

  pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  openModifyModal(item: any) {
    this.modalConfig.result = true;

    this.form.gameId = item.game_id;
    this.form.rid = item.round_id;
    if(this.form.rid == null){
      this.form.rid = item._id;
    }
    this.form.gameTableId = item.gametable_id;
    this.form.bootNumber = item.boot_number;
    this.form.bootId = item.boot_id;
    this.form.startTime = item.startTime;
    this.form.showStatus = item.status;
    this.form.status = item._status;

    if (typeof(item.result) !== 'undefined' && item.result != null) {
      this.form.result = item.result;
      this.modalConfig.result = false;
      this.modalConfig.saveBtn = true;
      this.modalConfig.resultBtn = true;
      this.modalConfig.cancleBetBtn = true;
    } else {
      this.modalConfig.saveBtn = true;
      this.modalConfig.resultBtn = true;
      this.modalConfig.cancleBetBtn = true;
    }

    if(item.game_id == 1){
      this.form.bankCard1Mode = item.bankCard1_mode===undefined?null:item.bankCard1_mode;
      this.form.bankCard1Number = item.bankCard1_number ===0?null:item.bankCard1_number;
      this.form.bankCard1Number = this.form.bankCard1Number ===undefined?null:this.form.bankCard1Number;
      this.form.bankCard2Mode = item.bankCard2_mode===undefined?null:item.bankCard2_mode;
      this.form.bankCard2Number = item.bankCard2_number===0?null:item.bankCard2_number;
      this.form.bankCard2Number = this.form.bankCard2Number===undefined?null:this.form.bankCard2Number;
      this.form.bankCard3Mode = item.bankCard3_mode===undefined?null:item.bankCard3_mode;
      this.form.bankCard3Number = item.bankCard3_number===0?null:item.bankCard3_number;
      this.form.bankCard3Number = this.form.bankCard3Number===undefined?null:this.form.bankCard3Number;

      this.form.playerCard1Mode = item.playerCard1_mode===undefined?null:item.playerCard1_mode;
      this.form.playerCard1Number = item.playerCard1_number===0?null:item.playerCard1_number;
      this.form.playerCard1Number = this.form.playerCard1Number===undefined?null:this.form.playerCard1Number;
      this.form.playerCard2Mode = item.playerCard2_mode===undefined?null:item.playerCard2_mode;
      this.form.playerCard2Number = item.playerCard2_number===0?null:item.playerCard2_number;
      this.form.playerCard2Number = this.form.playerCard2Number===undefined?null:this.form.playerCard2Number;
      this.form.playerCard3Mode = item.playerCard3_mode===undefined?null:item.playerCard3_mode;
      this.form.playerCard3Number = item.playerCard3_number===0?null:item.playerCard3_number;
      this.form.playerCard3Number = this.form.playerCard3Number===undefined?null:this.form.playerCard3Number;
      this.modifyModal.show();
    }if(item.game_id == 3){
      this.form.result = item.result + '';
      this.rouletteModifyModal.show();
    }
  }

  onSubmit() {
    this.form.endTime = this.datepipe.transform(this.form.endTime, 'yyyy-MM-dd HH:mm:ss');
    if (this.onSubmitFlag === 1) {
      this.correctData();
    } else if (this.onSubmitFlag === 2) {
      this.settlement();
    } else if (this.onSubmitFlag === 3) {
      this.refund();
    }
  }

  correctData() {
    let index = this.checkForm();
    if(!index) return;
    this.abnormalTableService.correctData(this.form).subscribe(
      data => {
        if (data.status == 0) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.closeModifyModal();
          this.closeRouttleModifyModal();
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  settlement() {
    let index = this.checkForm();
    if(!index) return;
    this.abnormalTableService.settlement(this.form).subscribe(
      data => {
        if (data.status == 0) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.closeModifyModal();
          this.closeRouttleModifyModal();
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  refund() {
    this.abnormalTableService.refund(this.form).subscribe(
      data => {
        if (data.status == 0) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.closeModifyModal();
          this.closeRouttleModifyModal();
          this.search();
        } else {
          this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  getGameType() {
    this.abnormalTableService.getGameType().subscribe(
      data => {
        this.gameTypeList = data.data;
      },
      err => {
        console.log(err);
      });
  }

  closeModifyModal() {
    this.modifyModal.hide();
    this.form = new AbnormalTableForm();
  }

  closeRouttleModifyModal() {
    this.rouletteModifyModal.hide();
    this.form = new AbnormalTableForm();
  }

  loadNode(event) {
    this.abnormalTableService.searchRound({
      tid: event.node.data.id
    }).subscribe(
      data => {
        const childrenData = [];
        let children, tempData;
        for (const item of data.data) {
          let itemStatus = '';
          if (item.status === 3) {
            itemStatus = this._translate.instant('abnormaltable.beginGame');
          }else if (item.status === 4) {
            itemStatus = this._translate.instant('abnormaltable.betting');
          }else if (item.status === 5) {
            itemStatus = this._translate.instant('abnormaltable.waitingResult');
          }
          tempData = Object.assign({}, item);
          //tempData.name = this._translate.instant('common.startTime') + ':' + item.309;
          tempData._id = item.id;
          tempData.id = this._translate.instant('abnormaltable.roundNum') + ':'+item.id;
          tempData.status = itemStatus;
          tempData._status = item.status;
          tempData.round_id = item.round_id;
          children = {data: tempData};
          childrenData.push(children);
        }
        event.node.children = childrenData;
      },
      err => {
        console.log(err);
      });
  }

  checkForm(){
    let index = true;
    if(this.form.gameId == 1){
      if(this.form.bankCard1Mode === null|| this.form.bankCard2Mode === null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.bankCard1Mode === undefined || this.form.bankCard2Mode === undefined ){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.playerCard1Mode === null || this.form.playerCard2Mode === null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.playerCard1Mode === undefined || this.form.playerCard2Mode === undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.bankCard1Number === null || this.form.bankCard2Number === null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.bankCard1Number === undefined || this.form.bankCard2Number === undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.playerCard1Number === null || this.form.playerCard2Number === null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.playerCard1Number === undefined || this.form.playerCard2Number === undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.bankCard3Mode !== null && this.form.bankCard3Number === null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.bankCard3Mode !== undefined && this.form.bankCard3Number === undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.bankCard3Mode === null && this.form.bankCard3Number !== null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.bankCard3Mode === undefined && this.form.bankCard3Number !== undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.playerCard3Mode !== null && this.form.bankCard3Number === null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.playerCard3Mode !== undefined && this.form.bankCard3Number === undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
        index = false;
        return index;
      }
      if(this.form.playerCard3Mode === null && this.form.playerCard3Number !== null){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
      if(this.form.playerCard3Mode === undefined && this.form.playerCard3Number !== undefined){
        this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectMode'));
        index = false;
        return index;
      }
    }

    if(this.form.gameId == 3 && this.form.result === null){
      this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
      index = false;
      return index;
    }
    if(this.form.gameId == 3 && this.form.result === undefined){
      this._toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('abnormaltable.selectNumber'));
      index = false;
      return index;
    }
    return index;
  }

}
