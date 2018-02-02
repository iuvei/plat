import {Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';


import {Role} from "../model/role";

import 'rxjs/add/operator/map';
import {Common} from "../common/common";
import {CustomHttp} from "../components/customhttp";

@Injectable()
export class RoleService {

  private host = Common.URL;

  private url = this.host + '/role/search';
  private addUrl = this.host + '/role/add';
  private modifyUrl = this.host + '/role/update';
  private checkRoleNameUrl = this.host + '/role/check';
  private loadPermissionUrl = this.host + '/role/';
  private changeStatusUrl = this.host + '/role/changeStatus';

  private headers: Headers;

  constructor(private http: CustomHttp) {
  }

  public search(roleName: String, status: String , currentPage: number, pageSize: number) {
    let data = {'roleName':roleName,'status':status,'currentPage':currentPage,'pageSize':pageSize};

    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

  public create(role: Role) {
    return this.http.post(this.addUrl, role).map((res: Response) => res.json());

  }

  public modify(role: Role) {
    return this.http.post(this.modifyUrl, role).map((res: Response) => res.json());

  }

  public changeStatus(role: Role) {
    return this.http.post(this.changeStatusUrl, role).map((res: Response) => res.json());

  }

  public checkRoleName(roleName: String) {
    const data = `roleName=${roleName}`;

    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.checkRoleNameUrl, data, {headers: this.headers}).map((res: Response) => res.json());

  }

  public loadPermission(roleId: string) {
    return this.http.get(this.loadPermissionUrl+roleId).map((res: Response) => res.json());
  }


}
