import {Component, OnInit, ViewChild} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {CustomHttp} from "../components/customhttp";
import {ModalDirective} from "ng2-bootstrap/modal/modal.component";
import {ConfirmationService} from "primeng/components/common/api";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {RoomForm} from "./roomform";
import {RoomSerice} from "./room.service";
import {Common} from "../common/common";
import {SearchRoomForm} from "./search.model";
import {CommonUtil} from "../common/CommonUtil";

@Component({
  templateUrl: 'room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements  OnInit {

  @ViewChild('addRoomDialog')
  public addRoomDialog:ModalDirective;

  @ViewChild('updateRoomDialog')
  public updateRoomDialog:ModalDirective;

  @ViewChild('modifyPasswordModal')
  public modifyPasswordModal: ModalDirective;

  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  public tableList: any[] = [];
  //query param
  public searchForm:SearchRoomForm =new SearchRoomForm();

  public datalist:any[] = [];

  public roomForm: RoomForm = new RoomForm();

  gameTableList: any[] = [];

  selectRowData: any;

  percentageConfigList: any[];

  selectedValue:string='';

  hasAuth:boolean = true;

  // 当前登录用户
  currentUser: any;

  constructor(private _roomSerice:RoomSerice,private _translate: TranslateService,private http: CustomHttp,private confirmDialog: ConfirmationService, private toaster:ToasterService) {
  }

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('loginUser'));
    this.currentUser = user.user;
    if(this.currentUser.userType ==3){
      this.hasAuth =false;
    }else{
      this.hasAuth = true;
    }
    this.getGameTable();
    let obj = JSON.parse(sessionStorage.getItem("room-component-list"));
    if(obj){
      this.searchForm =SearchRoomForm.create(obj.searchModel);
      this.datalist = obj.datalist;
    }else {
      this.searchForm.startTime = CommonUtil.getStartDateByDay(30);
      this.searchForm.endTime = CommonUtil.getEndDate();
      this.search();
    }
  }

  ngOnDestroy(): void {
    let json = {"searchModel":this.searchForm,"datalist":this.datalist};
    sessionStorage.setItem("room-component-list",JSON.stringify(json));
  }

  getGameTable() {
    this._roomSerice.getGameTable().subscribe(
      data => {
        this.gameTableList =data.data;
      },
      err => {
        console.log(err);
      });
  }

  getPercentageConfig() {
    this._roomSerice.getPercentageConfig().subscribe(
      data => {
        for(const config of data.data){
          config['label']=this._translate.instant("privateRoom.hedgePercentage")+":"+config.hedgePercentage*100+"% "
            +this._translate.instant("privateRoom.noHedgePercentage")+":"+config.noHedgePercentage*100+"% "
            +this._translate.instant("privateRoom.waterPercentage")+":"+config.waterPercentage*100+"%";
        }
        this.percentageConfigList = data.data;
        if(this.percentageConfigList.length >0){
          this.selectedValue=this.percentageConfigList[0].lid;
        }else{
          this.selectedValue='';
        }
      },
      err => {
        console.log(err);
      });
  }

  gotoDelete(data:any){
    let msg =  this._translate.instant('privateRoom.cancelRoom');
    if(data.status ==0){
      msg =  this._translate.instant('privateRoom.normalRoom');
    }
    this.confirmDialog.confirm({
      message: msg,
      accept: () => {
        this._roomSerice.delete(data.id).subscribe(data=>{
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

  gotoUpdateRoom(data:any){
    this.roomForm.id=data.id;
    this.roomForm.roomName=data.roomName;
    this.roomForm.maxMembers=data.maxMembers;
    this.roomForm.minBalance=data.minBalance;
    this.roomForm.gameTableId =data.gameTableId;
    //this.selectedValue=data.lid;
    // this.getPercentageConfig();
    this.updateRoomDialog.show();
  }
  updateRoom(){
    if (!/^[a-zA-Z0-9\u4e00-\u9fa5]{1,5}$/.test(this.roomForm.roomName)) {
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.roomName.error'));
      return false;
    }
    if(parseFloat(this.roomForm.maxMembers.toString())<0){
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.maxMembers.error'));
      return false;
    }
    if(parseFloat(this.roomForm.minBalance.toString())<=0){
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.minBalance.error'));
      return false;
    }
    // if(this.selectedValue ==''){
    //   this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.percentage.error'));
    //   return false;
    // }
    //this.roomForm.hedgePercentage = parseFloat(this.selectedValue.split("-")[0]) as number;
    //this.roomForm.noHedgePercentage = parseFloat(this.selectedValue.split("-")[1]) as number;
    //this.roomForm.waterPercentage = parseFloat(this.selectedValue.split("-")[2]) as number;
    this._roomSerice.update(this.roomForm).subscribe(data=>{
        if (data.status ==0) {
          this.updateRoomDialog.hide();
          this.toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.search();
        } else {
          this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      });
  }
  gotoCreateRoom(){
    //this.getGameTable();
    this.getPercentageConfig();
    this.glaobal_clearObjectVal(this.roomForm);
    this.roomForm.maxMembers=0;
    this.roomForm.gameTableId=null;
    this.addRoomDialog.show();
  }
  createRoom(){
    if (!/^[a-zA-Z0-9\u4e00-\u9fa5]{1,5}$/.test(this.roomForm.roomName)) {
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.roomName.error'));
      return false;
    }
    if (!/^[A-Za-z0-9]{6,15}$/.test(this.roomForm.password)) {
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.password.error'));
      return false;
    }
    if (this.roomForm.password !== this.roomForm.confirmPassword) {
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('common.confirmPasswordFailure'));
      return false;
    }
    if(parseFloat(this.roomForm.maxMembers.toString())<0){
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.maxMembers.error'));
      return false;
    }
    if(parseFloat(this.roomForm.minBalance.toString())<=0){
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.minBalance.error'));
      return false;
    }

    if(this.roomForm.type==''){
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.select.roomType'));
      return false;
    }

    if(this.roomForm.type=='2'){ //房间类型 1.虚拟房间 2.VIP对冲房 3 非对冲房
      if(this.percentageConfigList.length ==0){
        this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.percentage.tip'));
        return false;
      }
      if(this.selectedValue ==''){
        this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.percentage.error'));
        return false;
      }
      this.roomForm.hedgePercentage = parseFloat(this.selectedValue.split("-")[0]) as number;
      this.roomForm.noHedgePercentage = parseFloat(this.selectedValue.split("-")[1]) as number;
      this.roomForm.waterPercentage = parseFloat(this.selectedValue.split("-")[2]) as number;
    }
    this._roomSerice.create(this.roomForm).subscribe(data=>{
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
    this.search();
  }

  public search() {
    let condition = {
      startTime: CommonUtil.formatDate(this.searchForm.startTime),
      endTime: CommonUtil.formatDate(this.searchForm.endTime),
      roomName:this.searchForm.roomName,
      createUser:this.searchForm.createUser,
      status:this.searchForm.searchRoomStatus,
      type:this.searchForm.type,
      currentPage: this.currentPage,
      pageSize: this.pageSize
    };
    this._roomSerice.search(condition).subscribe(rec=>{
      this.datalist = rec.data.data;
      this.totalItems = rec.data.total;
    });
  }


  //清除双向绑定对象的值
  public glaobal_clearObjectVal(obj:any) {
    let keys:any[] = Object.keys(obj);
    for (let key of keys) {
      obj[key] = '';
    }
  }
  openModifyPasswordModal(item:any) {
    this.selectRowData = item;
    this.roomForm.password = '';
    this.roomForm.confirmPassword = '';
    this.modifyPasswordModal.show();
  }

  closeModifyPasswordModal() {
    this.modifyPasswordModal.hide();
  }

  // 修改密码
  modifyPassword() {
    if (!/^[A-Za-z0-9]{6,15}$/.test(this.roomForm.password)) {
      this.toaster.pop('error', this._translate.instant('common.warning'), this._translate.instant('privateRoom.password.error'));
      return false;
    }
    if (this.roomForm.password !== this.roomForm.confirmPassword) {
      this.toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('common.confirmPasswordFailure'));
      return false;
    }

    this._roomSerice.modifyPassword({
      id: this.selectRowData.id,
      password: this.roomForm.password
    }).subscribe(
      data => {
        if (data.status === 0) {
          this.closeModifyPasswordModal();
          this.toaster.pop('success', this._translate.instant('common.success'), data.msg);
        } else {
          this.toaster.pop('error', this._translate.instant('common.error'), data.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

}
