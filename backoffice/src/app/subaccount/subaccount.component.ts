import {Component, ComponentFactoryResolver, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';
import {Tree,TreeModule, TreeNode, SharedModule, ConfirmationService, Message} from 'primeng/primeng';
import { Http, Response, Headers ,RequestOptions} from '@angular/http';
import {Role} from "../model/role";
import {MenuSelectVo} from "../model/menuselectvo";
import {CustomHttp} from "../components/customhttp";
import {ToasterService} from "angular2-toaster/angular2-toaster";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "../common/common";


@Component({
  templateUrl: 'subaccount.component.html',
  styleUrls: ['./subaccount.component.scss'],
})
export class SubAccountComponent implements OnInit {

  public currentPage:number = 1;

  // fired when total pages count changes, $event:number equals to total pages count
  public smallnumPages:number = 1;
  public totalItems:number = 0;
  public pageSize:number = 20;
  public maxSize:number = Common.MAX_PAGE_SIZE;

  // query param
  public searchUserName:string = '';
  public searchUserLevel:string = '0';
  public searchAccountStatus:string = '-1';
  public searchBetStatus:string = '-1';

  //更新用户信息的窗口
  @ViewChild('updateUserModule')
  public updateUserModule:ModalDirective;
  //更新用户密码的窗口
  @ViewChild('updatePasswrodModal')
  public updatePasswrodModal:ModalDirective;
  //新建子账号的窗口
  @ViewChild('createSubUserModal')
  public createSubUserModal:ModalDirective;

  public currentUser:any;

  //当前用户的信息
  public loginUser = {
    uid: 0,
    nickName: ""
  }

  //当前选中的节点

  //父节点的ID
  uid:string;

  //可能是登陆的ID
  aid:string;
  //存放树的数据
  filesTree10:TreeNode[];
  //列表数据
  tablelist:any[];


  //url
  public url:string =Common.URL+"/childAccountUser";

  //双select
  public assignRceList:any[] = [];
  public unAssignRceList:any[] = [];
  public unAssignRceSelectList:number[] = [];
  public assignRceSelectList:number[] = [];
  private options = new RequestOptions({});

  //新增子账号的信息
  public createSubUser = {
    loginName: null,
    nickName: null,
    password: null,
    rePassword: null,
    parentId:null,
    permissions: []
  }

  //上级用户
  public global_parentuser = {
    name: "",
    uid: null
  };
  //修改用户信息
  public updateuser = {
    userId: -1,
    loginName: null,
    nickName: null,
    permissions: [],
  }

  //修改用户状态信息
  public updatestatus = {
    id: 0,
    status: 0
  }

  //修改用户密码的信息
  public updatepassword = {
    id: -1,
    password: null,
    rePassword: null
  }
  //public repassword:string = "";

  //修改用户密码

  //tree
  public lazyFiles:TreeNode[];


  //模块构造
  constructor(private confirmDialog:ConfirmationService, private http:CustomHttp, private toaster:ToasterService, private _translate:TranslateService) {
  }

  //初始化操作
  ngOnInit() {
    this.currentUser = JSON.parse(localStorage.getItem('loginUser'));
    this.loginUser.uid = this.currentUser.user.id;
    this.loginUser.nickName = this.currentUser.user.loginName;
    this.global_parentuser.name = this.loginUser.nickName;
    this.global_parentuser.uid = this.loginUser.uid ;
    this.http.post(this.url + '/getLocalUserNode', null)
      .toPromise()
      .then(response => {
        this.lazyFiles = <TreeNode[]>[
          {
            "label": response.json()['data']['loginName'],
            "data": response.json()['data']['id'],
            "expandedIcon": "fa-folder-open",
            "collapsedIcon": "fa-folder",
            "leaf": false
          },
        ];

      });

    this.getSubAccountData(this.loginUser.uid);

    //this.getAgentAccount();
    //先做个假数据

  }

  //树展开时调用
  nodeExpand(event) {
    if (event.node) {
      this.options.headers = new Headers();
      this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
      //in a real application, make a call to a remote url to load children of the current node and add the new nodes as children
      this.http.post(this.url + '/getAgentAccount', this.transformRequest({id:event.node.data}),this.options)
        .toPromise()
        .then(response => {
          event.node.children = response.json()['data'] as TreeNode[];
          this.getSubAccountData(event.node.data);
          this.global_parentuser.name = event.node.label;
          this.global_parentuser.uid = event.node.data;
        });
    }
  }

  //当节点被选择时,触发
  nodeSelect(event) {
    this.getSubAccountData(event.node.data);
    this.global_parentuser.name = event.node.label;
    this.global_parentuser.uid = event.node.data;
  }

  //进入创建子账号子窗口
  public gotoCreateSubAccount() {
    if(this.global_parentuser.uid !== this.loginUser.uid){
      this.toaster.pop('error', this._translate.instant('common.error'), this._translate.instant('subaccount.createLiveUserPermissionDeny'));
      return;
    }
    this.loadCreateMenuList();
    this.assignRceList = [];
    this.unAssignRceList = [];
    this.createSubUserModal.show();
    //初始化输入的条件
    this.createSubUser.loginName = "";
    this.createSubUser.nickName = "";
    this.createSubUser.password = "";
    this.createSubUser.rePassword = "";
  }

  //创建子账号
  public createSubAccount() {
    this.createSubUser.permissions = this.getRoleIdList();
    this.createSubUser.parentId = this.global_parentuser.uid;
    if (this.createSubUser.password != this.createSubUser.rePassword) {
      return;
    }
    if (this.createSubUser.password == "") {
      return;
    }
    this.http
      .post(this.url + '/create', this.createSubUser)
      .subscribe(
        (
          response => {
            if(response.json()['status'] == 0){
              this.createSubUserModal.hide();
              this.getSubAccountData(this.createSubUser.parentId);
            }
            this.glaobal_toaster(response);
          })
      );
  }

  //进入修改密码的窗口
  public gotoUserPassword(user:any) {
    this.updatepassword.id = user.id;
    this.updatepassword.password = "";
    this.updatepassword.rePassword = "";
    this.updatePasswrodModal.show();
  }

  //修改密码
  public updateUserPassword() {
    if (this.updatepassword.password != this.updatepassword.rePassword) {
      return;
    }
    if (this.updatepassword.password == "") {
      return;
    }
    this.http
      .post(this.url + '/changePassword', this.updatepassword)
      .subscribe(
        (
          response => {
            this.updatePasswrodModal.hide();
            this.glaobal_toaster(response);
          })
      );
  }

  //停用信息的控制判断
  public isStop:boolean;
  public isReStart:boolean;
  //进入更改用户状态弹窗
  public gotoUserStatus(user:any) {
    this.updatestatus.id = user.id;
    this.updatestatus.status = user.userStatus;

    let msg = user.userStatus == 1 ? this._translate.instant('subaccount.confirmDisableAccount') : this._translate.instant('subaccount.confirmEnableAccount');
    this.confirmDialog.confirm({
      message: msg,
      accept: () => {
        let index:number;
        if (this.updatestatus.status == 1) {
          index = 2;
        } else if (this.updatestatus.status == 2) {
          index = 1;
        }
        this.updatestatus.status = index;
        this.http
          .post(this.url + '/setStatus', this.updatestatus)
          .subscribe(
            (
              response => {
                this.getSubAccountData(this.global_parentuser.uid);
                this.glaobal_toaster(response);
              })
          );
      }
    });
  }


  //修改子账号信息
  public updateUserMessage() {
    this.updateuser.permissions = this.getRoleIdList();

    this.http
      .post(this.url + '/update', this.updateuser)
      .subscribe(
        (
          response => {
            if(response.json()['status'] == 0){
              this.getSubAccountData(this.global_parentuser.uid);
              this.updateUserModule.hide();
            }
            this.glaobal_toaster(response);

          })
      );

  }

  //加载菜单列表
  public loadMenuList(uid:number) {

    this.http
      .post(this.url + '/getPermission/'+uid+'/'+this.global_parentuser.uid, null)
      .subscribe(
        (
          response => {
            this.assignRceList = response.json()['data']['assignRce']  as any[];
            this.unAssignRceList = response.json()['data']['unAssignRce']  as any[];
          })
      );
  }

  //加载创建子账号的权限菜单
  public loadCreateMenuList() {

    this.http
      .post(this.url + '/getSelfPermission/'+this.global_parentuser.uid,null)
      .subscribe(
        (
          response => {
            this.unAssignRceList = response.json()['data']['unAssignRce'] as any[];
          })
      );
  }

  //进入修改角色信息页面
  public gotoUserMessage(user:any) {
    this.loadMenuList(user.id);
    this.updateuser.userId = user.id;
    //this.updateuser.parentId = this.global_parentuser.uid;
    this.updateuser.loginName = user.loginName;
    this.updateuser.nickName = user.nickName;
    this.updateUserModule.show();
  }

  //获取列表数据
  public getSubAccountData(uid:number) {
    if (uid == 0 || uid == null) {
      uid = this.loginUser.uid ;
    }
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    let condition = {
      uid: uid,
    }
    this.http
      .post(this.url + '/getChild', this.transformRequest(condition),this.options)
      .subscribe(
        (
          response => {
            this.tablelist = response.json().data  as any[];
          })
      );
  }

  //获取tree数据
  public getAgentAccount() {
    this.aid = '570';
    let condition = {
      id: this.aid
    }
    this.http
      .post(this.url + '/getAgentAccount', condition)
      .subscribe(
        (
          response => {
            this.filesTree10 = response.json()  as any[];
          })
      );
  }


  //左移全部
  zuoyiAll() {
    this.unAssignRceSelectList = [];
    this.assignRceSelectList = [];
    var arrSelpermissionid = new Array();
    var arrSelpermissionname = new Array();

    //存储所有源列表框数据到缓存数组
    for (var i = 0; i < this.unAssignRceList.length; i++) {
      arrSelpermissionid[i] = this.unAssignRceList[i].permissionID;
      arrSelpermissionname[i] = this.unAssignRceList[i].permissionDesc;
    }

    //将缓存数组的数据增加到目的select中
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      this.assignRceList.push(oOption)
    }

    //清空源列表框数据，完成移动
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      for (var j = 0; j < this.unAssignRceList.length; j++) {
        if (this.unAssignRceList[j].permissionID == oOption.permissionID) {
          this.unAssignRceList.splice(j, 1);
          break;
        }
      }
    }
  }

  //左移一些
  zuoyiMost() {
    //建立存储value和text的缓存数组
    var arrSelpermissionid = new Array();
    var arrSelpermissionname = new Array();

    //存储所有源列表框数据到缓存数组
    for (var i = 0; i < this.unAssignRceSelectList.length; i++) {
      arrSelpermissionid[i] = this.unAssignRceSelectList[i];
      for (var j = 0; j < this.unAssignRceList.length; j++) {
        if (this.unAssignRceList[j].permissionID == this.unAssignRceSelectList[i]) {
          arrSelpermissionname[i] = this.unAssignRceList[j].permissionDesc;
          break;
        }
      }

    }

    //将缓存数组的数据增加到目的select中
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      this.assignRceList.push(oOption)
    }

    //清空源列表框数据，完成移动
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      for (var j = 0; j < this.unAssignRceList.length; j++) {
        if (this.unAssignRceList[j].permissionID == oOption.permissionID) {
          this.unAssignRceList.splice(j, 1);
          break;
        }
      }
    }
    this.unAssignRceSelectList = [];
  }

  //右移一些
  youyiMost() {
    //建立存储value和text的缓存数组
    var arrSelpermissionid = new Array();
    var arrSelpermissionname = new Array();

    //存储所有源列表框数据到缓存数组
    for (var i = 0; i < this.assignRceSelectList.length; i++) {
      arrSelpermissionid[i] = this.assignRceSelectList[i];
      for (var j = 0; j < this.assignRceList.length; j++) {
        if (this.assignRceList[j].permissionID == this.assignRceSelectList[i]) {
          arrSelpermissionname[i] = this.assignRceList[j].permissionDesc;
          break;
        }
      }

    }

    //将缓存数组的数据增加到目的select中
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      this.unAssignRceList.push(oOption)
    }

    //清空源列表框数据，完成移动
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      for (var j = 0; j < this.assignRceList.length; j++) {
        if (this.assignRceList[j].permissionID == oOption.permissionID) {
          this.assignRceList.splice(j, 1);
          break;
        }
      }
    }
    this.assignRceSelectList = [];
  }

  //右移所有
  youyiAll() {
    this.unAssignRceSelectList = [];
    this.assignRceSelectList = [];
    //建立存储value和text的缓存数组
    var arrSelpermissionid = new Array();
    var arrSelpermissionname = new Array();

    //存储所有源列表框数据到缓存数组
    for (var i = 0; i < this.assignRceList.length; i++) {
      arrSelpermissionid[i] = this.assignRceList[i].permissionID;
      arrSelpermissionname[i] = this.assignRceList[i].permissionDesc;
    }

    //将缓存数组的数据增加到目的select中
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      this.unAssignRceList.push(oOption)
    }

    //清空源列表框数据，完成移动
    for (var i = 0; i < arrSelpermissionname.length; i++) {
      var oOption = new MenuSelectVo();
      oOption.permissionID = arrSelpermissionid[i];
      oOption.permissionDesc = arrSelpermissionname[i];
      for (var j = 0; j < this.assignRceList.length; j++) {
        if (this.assignRceList[j].permissionID == oOption.permissionID) {
          this.assignRceList.splice(j, 1);
          break;
        }
      }
    }
  }

  //获取到选中的菜单id
  public getRoleIdList() {
    var dataList = new Array();
    if (this.assignRceList.length <= 0) {
      return dataList;
    }
    for (var i = 0; i < this.assignRceList.length; i++) {
      dataList.push(this.assignRceList[i].permissionID);
    }
    return dataList;
  }

  //toaster共用的函数
  public glaobal_toaster(response:Response) {
    let ok = response.json()['status'] as number;
    let message = response.json()['msg'] as string;
    if (ok == 0) {
      this.toaster.pop('success', this._translate.instant('common.success'), message);
    } else {
      this.toaster.pop('error', this._translate.instant('common.error'), message);
    }
  }


  public transformRequest(data:any){
    var str =[];
    for(var p in data){
      if(data[p] == null || data[p]=='null'){
        continue;
      }
      str.push(p+'='+data[p]);
    }
    return str.join('&');
  }
}
