import {Component, OnInit, ViewChild} from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';


import {Role} from "../model/role";
import {RoleService} from "./role.service";
import {Permission} from "../model/permission";
import {TranslateService} from "@ngx-translate/core";
import {ToasterService} from "angular2-toaster";
import {ConfirmationService} from "primeng/primeng";
import {LoadingAnimateService} from "ng2-loading-animate";
import {Common} from "../common/common";

@Component({
  templateUrl: 'role.component.html',
  styleUrls: ['./role.component.scss']
})
export class RoleComponent implements  OnInit{

  public currentPage: number = 1;

  // fired when total pages count changes, $event:number equals to total pages count
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;

  public searchRoleName: string = '';
  public searchRoleStatus: string = '';

  public roleList: Role[] = [];


  public role: Role = new Role();

  // 未配置权限列表
  public unAssignPermissionList: Permission[] = [];
  // 已配置权限列表
  public assignPermissionList: Permission[] = [];

  public selectedUnAssignPermission: string[] = [];

  public selectedAssignPermission: string[] = [];

  @ViewChild('createRoleModal')
  public createRoleModal: ModalDirective;

  @ViewChild('modifyRoleModal')
  public modifyRoleModal: ModalDirective;

  backColor: string;

  constructor(private roleService: RoleService, private _toaster: ToasterService,
              private _translate: TranslateService, private confirmationService: ConfirmationService) {
  }

  ngOnInit() {
    this.search();
  }

  public search() {
    this.roleService.search( this.searchRoleName, this.searchRoleStatus, this.currentPage, this.pageSize).subscribe(
      role => {
        this.totalItems = role.total;
        this.roleList = role.data.data;
      },
      err => {
        console.log(err);
    });
  }

  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  public create() {
    const permissionList: string[] = [];
    this.assignPermissionList.forEach(p => {
      permissionList.push(p['permissionID']);
    });
    this.role.permissionList = permissionList;
    this.roleService.create(this.role).subscribe(
      data => {
        if (data.status==0) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.role = new Role();
          this.assignPermissionList.splice(0, this.assignPermissionList.length);
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

  public modify() {
    const permissionList: string[] = [];
    this.assignPermissionList.forEach(p => {
      permissionList.push(p['permissionID']);
    });
    this.role.permissionList = permissionList;
    this.roleService.modify(this.role).subscribe(
      data => {
        if (data.status==0) {
          this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
          this.role = new Role();
          this.assignPermissionList.splice(0, this.assignPermissionList.length);
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

  openModifyModal(id: string, name: string, desc: string) {
    this.role.roleID = id;
    this.role.roleName = name;
    this.role.roleDesc = desc;
    this.loadPermission(id);
    this.modifyRoleModal.show();
  }

  public loadPermission(roleId: string) {
    this.roleService.loadPermission(roleId).subscribe(
      data => {
        this.unAssignPermissionList = data.data.unAssignPermissionList;
        this.assignPermissionList = data.data.assignPermissionList;
      },
      err => {
        console.log(err);
      });
  }

  addPermission() {
    const tempPermissionList = this.unAssignPermissionList.slice(0);
    const tempSelectList = this.selectedUnAssignPermission.slice(0);

    tempPermissionList.forEach(p => {
      tempSelectList.forEach(i => {
        if (p['permissionID'] == i) {
          this.assignPermissionList.push(p);
          const index = this.unAssignPermissionList.indexOf(p);
          this.unAssignPermissionList.splice(index, 1);

          const selectIndex = this.selectedUnAssignPermission.indexOf(i);
          this.selectedUnAssignPermission.splice(selectIndex, 1);
        }
      });
    });
  }

  addAllPermission() {
    const list = this.unAssignPermissionList.splice(0, this.unAssignPermissionList.length);
    this.assignPermissionList = this.assignPermissionList.concat(list);
  }

  removePermission() {
    const tempPermissionList = this.assignPermissionList.slice(0);
    const tempSelectList = this.selectedAssignPermission.slice(0);

    tempPermissionList.forEach(p => {
      tempSelectList.forEach(i => {
        if (p['permissionID'] == i) {
          this.unAssignPermissionList.push(p);
          const index = this.assignPermissionList.indexOf(p);
          this.assignPermissionList.splice(index, 1);

          const selectIndex = this.selectedAssignPermission.indexOf(i);
          this.selectedAssignPermission.splice(selectIndex, 1);
        }
      });
    });

  }

  removeAllPermission() {
    const list = this.assignPermissionList.splice(0, this.assignPermissionList.length);
    this.unAssignPermissionList = this.unAssignPermissionList.concat(list);
  }

  changeStatus(id: string, status: number) {
    const role = new Role();
    role.roleID = id;
    let header = '';
    let message = '';
    if (status==1) {
      role.status = '2';
      header = this._translate.instant('role.modifyRoleStatus');
      message = this._translate.instant('role.confirmDisableRoleStatus');
    } else {
      role.status = '1';
      header = this._translate.instant('role.modifyRoleStatus');
      message = this._translate.instant('role.confirmEnableRoleStatus');
    }
    this.confirmationService.confirm({
      //header: header,
      message: message,
      accept : () => {
        this.roleService.changeStatus(role).subscribe(
          data => {
            if (data.status==0) {
              this._toaster.pop('success', this._translate.instant('common.success'), data.msg);
            } else {
              this._toaster.pop('error', this._translate.instant('common.error'), data.msg);
            }
            this.search();
          },
          err => {
            console.log(err);
          });
      }
    });
  }

  closeCreateModal() {
    this.role = new Role();
    this.createRoleModal.hide();
  }

  closeModifyModal() {
    this.role = new Role();
    this.modifyRoleModal.hide();
  }

  onMouseover(event) {
    event.target.style = 'background-color: #3c763d';
  }

  onMouseLeave(event) {
    event.target.style = '';
  }


}
