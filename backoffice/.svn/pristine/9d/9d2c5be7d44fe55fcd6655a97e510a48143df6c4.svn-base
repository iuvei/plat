import {Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';


import {Role} from "../model/role";

import 'rxjs/add/operator/map';
import {Common} from "../common/common";

@Injectable()
export class AccountTableService {
  private host = Common.URL;
  private url = this.host+'/accountManager/search';
  private addUrl = this.host+'/roleManage/create';
  private modifyUrl = this.host+'/roleManage/update';
  private checkRoleNameUrl = this.host+'/roleManage/check';
  private loadPermissionUrl = this.host+'/roleManage/loadPermission';
  private changeStatusUrl = this.host+'/roleManage/changeStatus';

  private headers: Headers;

  constructor(private http: Http) {
  }

  public search(loginName: String,  levelId: String, status: String, isBet: String, currentPage: number, pageSize: number) {
    const data = `loginName=${loginName}&levelId=${levelId}&status=${status}&isBet=${isBet}&page=${currentPage}&rows=${pageSize}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.url, data, {headers: this.headers}).map((res: Response) => res.json());
  }

  public create(role: Role, permissionList: String) {
    const data = `roleName=${role.roleName}&roleDesc=${role.roleDesc}&permissionList=${permissionList}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.addUrl, data, {headers: this.headers}).map((res: Response) => res.json());

  }

  public modify(role: Role, permissionList: String) {
    const data = `roleID=${role.roleID}&roleName=${role.roleName}&roleDesc=${role.roleDesc}&permissionList=${permissionList}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.modifyUrl, data, {headers: this.headers}).map((res: Response) => res.json());

  }

  public changeStatus(role: Role) {
    const data = `roleID=${role.roleID}&status=${role.status}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.changeStatusUrl, data, {headers: this.headers}).map((res: Response) => res.json());

  }

  public checkRoleName(roleName: String) {
    const data = `roleName=${roleName}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.checkRoleNameUrl, data, {headers: this.headers}).map((res: Response) => res.json());

  }

  public loadPermission(roleId: string) {
    const data = `roleID=${roleId}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.loadPermissionUrl, data, {headers: this.headers}).map((res: Response) => res.json());

  }


}
