import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {CustomHttp} from "../components/customhttp";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {ConfirmationService} from "primeng/components/common/api";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {Common} from "../common/common";
import {SearchVGameTableForm} from "./vsearch.model";
import {VGameTableSerice} from "./virtual-game-table.service";
import {VGameTableForm} from "./vtableform";

@Component({
  templateUrl: 'virtual-game-table.component.html',
  styleUrls: ['./virtual-game-table.component.scss']
})
export class VGameTableComponent implements  OnInit {

  @ViewChild('addRoomDialog')
  public addRoomDialog:ModalDirective;

  @ViewChild('updateRoomDialog')
  public updateRoomDialog:ModalDirective;

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  public tableList: any[] = [];
  //query param
  private _searchForm:SearchVGameTableForm =new SearchVGameTableForm();

  public datalist:any[] = [];

  public roomForm: VGameTableForm = new VGameTableForm();

  gameTableList: any[] = [];

  gameList: any[] = [];

  public itemList: Array<any>=[];
  public selected: Array<any>=[];
  public allList: Array<any>=[];
  public idListAll: Array<any>=[];

  // 当前登录用户
  currentUser: any;

  constructor(private _vGameTableSerice:VGameTableSerice,private _translate: TranslateService,private http: CustomHttp,private confirmDialog: ConfirmationService, private toaster:ToasterService) {
  }

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('loginUser'));
    this.currentUser = user.user;
    this.getGameTable();
    this.getGame();
    let obj = JSON.parse(sessionStorage.getItem("vroom-component-list"));
    if(obj){
      this._searchForm =SearchVGameTableForm.create(obj.searchModel);
      this.datalist = obj.datalist;
    }else {
      this.search();
    }
  }

  ngOnDestroy(): void {
    let json = {"searchModel":this._searchForm,"datalist":this.datalist};
    sessionStorage.setItem("vroom-component-list",JSON.stringify(json));
  }

  //   点击时执行
  clickItem(e, item) {
    const checkbox = e.target;
    const action = (checkbox.checked ? 'add' : 'remove');
    this.updateSelected(action, item);
  }
  // 用来判断input的checked
  isCheck(item) {
    return this.selected.findIndex(value => value == item) >= 0;
  }
  // 执行禁用
  private updateSelected(action, item) {
    if (action == 'add' && this.selected.findIndex(value => value == item) == -1){
      console.log('执行添加');
      this.selected.push(item);
    }
    if (action == 'remove' && this.selected.findIndex(value => value == item) != -1){
      console.log('执行删除');
      this.selected.splice(this.selected.findIndex(value => value == item), 1);
    }
    console.log(this.selected);
  }
  // 全选点击事件
  selectAll(e) {
    const checkbox = e.target;
    const action = (checkbox.checked ? 'add' : 'remove');
    this.allList.forEach((elt, i, array) => {
      this.updateSelected(action, elt);
    });
  }

  // 判断是否全选
  isSelectedAll() {
    return this.isContained(this.selected, this.idListAll);
  }
  // 判断b数组是否包含在a数组中
  private isContained(a, b) {
    if (!(a instanceof Array) || !(b instanceof Array)) return false;
    if (a.length < b.length) return false;
    const aStr = a.toString();
    for (let i = 0, len = b.length; i < len; i++) {
      if (aStr.indexOf(b[i]) == -1) {
        return false;
      }
    }
    return true;
  }

  getGameTable() {
    this._vGameTableSerice.getGameTable().subscribe(
      data => {
        this.gameTableList =data.data;
      },
      err => {
        console.log(err);
      });
  }

  getGame() {
    this._vGameTableSerice.getGame().subscribe(
      data => {
        this.gameList =data.data;
      },
      err => {
        console.log(err);
      });
  }

  gotoDelete(data:any){
    let msg =  this._translate.instant('virtualGameTable.cancelTable');
    if(data.status ==0){
      msg =  this._translate.instant('virtualGameTable.normalTable');
    }
    this.confirmDialog.confirm({
      message: msg,
      accept: () => {
        this._vGameTableSerice.delete(data.id).subscribe(data=>{
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

  batchDelete(status){
    if(this.selected.length==0){
        this.toaster.pop('error', this._translate.instant('common.validateFail'), this._translate.instant('virtualGameTable.selected'));
        return;
    }

    let msg =  this._translate.instant('virtualGameTable.cancelTable');
    if(status ==1){
      msg =  this._translate.instant('virtualGameTable.normalTable');
    }
    let data={
      'status':status,
      'ids':this.selected.join(',')
    }
    this.confirmDialog.confirm({
      message: msg,
      accept: () => {
        this._vGameTableSerice.batchDelete(data).subscribe(data=>{
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


  gotoCreateRoom(){
    this.glaobal_clearObjectVal(this.roomForm);
    this.addRoomDialog.show();
  }
  createRoom(){
    this._vGameTableSerice.create(this.roomForm).subscribe(data=>{
      if (data.status ==0) {
        this.addRoomDialog.hide();
        this.toaster.pop('success', this._translate.instant('common.success'), data.data.id+" "+this._translate.instant('privateRoom.add.success'));
      } else {
        this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
      }
      this.search();
    });
  }


  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.selected =[];
    this.isSelectedAll();
    this.search();
  }

  public search() {
    let condition = {
      gameId:this._searchForm.gameId,
      gameTableId:this._searchForm.gameTableId,
      status:this._searchForm.status,
      currentPage: this.currentPage,
      pageSize: this.pageSize
    };
    this._vGameTableSerice.search(condition).subscribe(rec=>{
      this.datalist = rec.data.data;
      this.totalItems = rec.data.total;
      for (let data of rec.data.data) {
        this.itemList.push(data.id);
        this.allList.push(data.id);
        this.idListAll.push(data.id);
      }
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
