/**
 * Created by Administrator
 * on 2017/4/3.
 */
import { Component, OnInit ,ViewChild} from '@angular/core';
import {Http,Headers,Response} from '@angular/http';
import {UserVo} from "../model/uservo";
import {UserVoCondition} from "../model/uservocondition";
import {RoleSelectVo} from "../model/roleselectvo";
import {CustomHttp} from "../components/customhttp";
import {TranslateService} from "@ngx-translate/core";
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';
import {ToasterService} from 'angular2-toaster/angular2-toaster';
import {ConfirmationService} from "primeng/primeng";
import {Common} from "../common/common";
import {Dict} from "../model/dict";


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],

})
export class UserComponent implements OnInit {


  @ViewChild('createUserModal')
  public createUserModal:ModalDirective;
  @ViewChild('passwordModal')
  public passwordModal:ModalDirective;
  @ViewChild('updateUserModal')
  public updateUserModal:ModalDirective;


  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  //字典数据。
  public dicts : Dict[] = [];
  private userUpdateRoleRequest = {loginName:null,userId:null,roleIds:[]} ;

  public userList : UserVo[] = [];
  //默认search初始化条件
  public usercondition: UserVoCondition = new UserVoCondition();
  //服务器的地址
  public url: string = Common.URL;

  constructor( private confirmDialog: ConfirmationService,public http: CustomHttp, private toaster:ToasterService, private _translate: TranslateService) { }

  ngOnInit() {
    this.dicts = JSON.parse(localStorage['loginUser']).dicts;
    this.search();
  }
  public paswcondition = {
    id:0,
    password:'',
    rePassword:''
  }
  public updatecondition = {
    uid:0,
    status:2,
    roleList:""
  }

  updateUserUsername:string = '';
  public assignRceList:any[];
  public assignRceSelectList:string[] = [];
  public unAssignRceList:any[];
  public unAssignRceSelectList:string[] = [];


  updateUserStatus(data:any){
    let status = data.userStatus==1 ? 2 : 1;
    let msg = data.userStatus == 1? this._translate.instant('user.isStopThis'):this._translate.instant('user.isRestartThis');
    let condtion = {
      id:data.id,
      userStatus :status
    }
    this.confirmDialog.confirm({
      message: msg,
      accept: () => {
        this.http.post(this.url + '/systemUser/updateStatus',condtion)
          .map(res =>res.json())
          .subscribe(
          (
            response => {
              this.glaobal_toaster(response);
              this.search();
            })
        );
      }
    });
  }
  goUpdate(data:any){
    this.loadMenuList(data.id)
    this.userUpdateRoleRequest.loginName = data.loginName;
    this.userUpdateRoleRequest.userId = data.id;
    this.updateUserModal.show();
  }
  updateUser(){
    this.userUpdateRoleRequest.roleIds=[];
    this.roleUserList.forEach(role=>{
      this.userUpdateRoleRequest.roleIds.push(role.roleID);
    });
    this.http
        .post(this.url + '/systemUser/updateUserRole', this.userUpdateRoleRequest)
        .map(res =>res.json())
        .subscribe(
            (
                response => {
                  this.glaobal_toaster(response);
                  if(response.status==0) {
                    this.updateUserModal.hide();
                    this.search();
                  }
                })
        );
  }

  //加载菜单列表
  public loadMenuList(uid:number) {
    this.http
        .get(this.url + '/systemUser/findRole/'+uid)
        .map(res =>res.json())
        .subscribe(
            (
                response => {
                  this.roleUserList = response.data.hasRoles  as any[];
                  this.roleAllList = response.data.waitRoles  as any[];
                })
        );
  }
  goPassword(data:any){
    this.paswcondition.id = data.id;
    this.paswcondition.password='';
    this.paswcondition.rePassword='';
    this.passwordModal.show();
  }
  updatePassword(){
    if(this.paswcondition.password!=this.paswcondition.rePassword){
      return;
    }

    this.http
        .post(this.url + '/systemUser/updatePassword', this.paswcondition)
        .map(res =>res.json())
        .subscribe(
            (
                response => {
                  this.passwordModal.hide();
                  this.glaobal_toaster(response);
                })
        );
  }
  goCreateUser(){
    this.addUser = {};
    this.roleUserList = [];
    this.loadingPermission();
    this.createUserModal.show();
  }
  public setPage(pageNo: number): void {
    this.currentPage = pageNo;
  }

  public pageChanged(event: any): void {
    //更新查询页数
    this.usercondition.currentPage = event.page;
    this.search();
  }

  public search() {
    return this.http
      .post(this.url + '/systemUser/search', this.usercondition)
      .subscribe(
        (
          response  =>{
            this.userList = response.json()['data'].data as UserVo[];
            this.totalItems = response.json()['data'].total as number;
          })
      );
  }
  public roleUserList :RoleSelectVo[] =[];
  public roleAllList :RoleSelectVo[] =[];
  public selectAllList:string[] =[];
  public selectUserList:string[] =[];

  public loadingPermission() {
    this.roleUserList =[];
    this.roleAllList =[];
    return this.http
      .get(this.url + '/systemUser/findAllRole/')
      .subscribe(
        (
          response  =>{
            this.roleAllList = response.json().data as RoleSelectVo[]
          })
      );
  }

  //创建用户的数据
  public addUser = {};
  public addUserStatus = {
    hasLoginName : false,
    msg : null
  };

  //创建用户
  public createUser(){
    if(this.addUserStatus.hasLoginName){
      return;
    }
    if(this.addUser['password']!=this.addUser['rePassword']){
      return;
    }

    this.addUser['roleIds']=this.roleUserList;
    this.addUser['roleIds']=[];

    this.roleUserList.forEach(role=>{
      this.addUser['roleIds'].push(role.roleID);
    });

    return this.http
      .post(this.url + '/systemUser/add',this.addUser)
      .map(res =>res.json())
      .subscribe(
        (
          response  => {
            this.glaobal_toaster(response);
            if (response.status == 0) {
              this.createUserModal.hide();
              this.search();
            }
          })
      );
  }

  //检测用户名是否重复
  public checkUser(){
    if(this.addUser['loginName'] == null){
      this.addUserStatus.hasLoginName = false;
      return
    }

    this.http
      .get(this.url +'/systemUser/checkLoginName/'+this.addUser['loginName'])
      .subscribe(
        (
          response  =>{
            let status = response.json().status as number;
            this.addUserStatus.hasLoginName = status!=0;
            this.addUserStatus.msg = response.json()['msg'] as string
          })
      );
  }

  //左移全部
  zuoyiAll(){
    let list = this.roleAllList.splice(0,this.roleAllList.length);
    this.roleUserList = this.roleUserList.concat(list);
  }

  //左移一些
  zuoyiMost(){
    this.selectAllList.forEach(selectRoleId=>{
      this.roleAllList.forEach(role=>{
        if(selectRoleId==role.roleID){
          this.roleUserList.push(role);
          let index = this.roleAllList.indexOf(role);
          this.roleAllList.splice(index,1);
        }
      })
    })
  }
  //右移一些
  youyiMost(){
    this.selectUserList.forEach(selectRoleId=>{
      this.roleUserList.forEach(role=>{
        if(selectRoleId==role.roleID){
          this.roleAllList.push(role);
          let index = this.roleUserList.indexOf(role);
          this.roleUserList.splice(index,1);
        }
      })
    })
  }
  //右移所有
  youyiAll(){
    let list = this.roleUserList.splice(0,this.roleUserList.length);
    this.roleAllList = this.roleAllList.concat(list);
  }

  //获取到选中的权限id
  public getRoleIdList() {
    var dataList:string = "";
    if (this.roleUserList.length <= 0) {
       return dataList;
    }
    for(var i=0;i<this.roleUserList.length;i++){
      if(i == this.roleUserList.length -1){
        dataList += this.roleUserList[i].roleID;
        continue
      }
      dataList = dataList + this.roleUserList[i].roleID + ",";
    }

    return dataList;
  }


  //获取到选中的菜单id
  public getRoleIdList1() {
    var dataList:string = '';
    if (this.assignRceList.length <= 0) {
      return dataList;
    }
    for (var i = 0; i < this.assignRceList.length; i++) {
      dataList = dataList + this.assignRceList[i].roleID + ",";
    }
    return dataList.toString();
  }

  //toaster共用的函数
  public glaobal_toaster(response:Response) {
    let status = response['status'];
    let message = response['msg'] as string;
    if (status==0) {
      this.toaster.pop('success', '成功', message);
    } else {
      this.toaster.pop('error', '错误', message);
    }
  }
}

