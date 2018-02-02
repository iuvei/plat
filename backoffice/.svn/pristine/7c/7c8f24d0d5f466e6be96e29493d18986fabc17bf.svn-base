import {Injectable } from '@angular/core';
import { Response, Headers } from '@angular/http';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import {CustomHttp} from "../components/customhttp";
import {AccountVO} from "./accountvo";
import {Common} from "../common/common";
import {PercentageConfig} from "./percentageconfig";

@Injectable()
export class AccountService {

  private host = Common.URL;

  private url = this.host + '/accountManager/search';
  private getLowerUserUrl = this.host + '/accountManager/getLowerUser';
  private getLimitDataUrl = this.host + '/accountManager/getChips';
  private modifyPasswordUrl = this.host + '/accountManager/modifyPassword';
  private modifyAccountStatusUrl = this.host + '/accountManager/modifyStatus';
  private modifyBetStatusUrl = this.host + '/accountManager/modifyBetStatus';
  private modifyBalanceUrl = this.host + '/accountManager/modifyBalance';
  private getUserTypeUrl = this.host + '/accountManager/getUserType';
  private getUserByUidUrl = this.host + '/accountManager/getUserById';
  private refreshAllUserChipsDataUrl = this.host + '/accountManager/refreshAllUserChipsData';
  private searchPercentageConfigUrl = this.host + '/percentageConfig/search';
  private deletePercentageConfigUrl = this.host + '/percentageConfig/deleteById';
  private addPercentageConfigUrl = this.host + '/percentageConfig/create';
  private updatePercentageConfigUrl = this.host + '/percentageConfig/update';
  private addUrl = this.host + '/accountManager/create';
  private modifyUrl = this.host + '/accountManager/modify';

  private modifyTitleStatusUrl = this.host + '/accountManager/modifyTitleStatus';

  private headers: Headers;

  constructor(private http: CustomHttp) {
  }

  public search(data: any) {
    // let data = `loginName=${loginName}&levelId=${levelId}&status=${status}&isBet=${isBet}&page=${currentPage}&rows=${pageSize}`;
    // if (id) {
    //   data += `&id=${id}`;
    // } else if (superiorId) {
    //   data += `&superiorId=${superiorId}`;
    // }
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

  public getLowerUser(data: any) {
    return this.http.post(this.getLowerUserUrl, data).map((res: Response) => res.json());
  }

  create(data: AccountVO) {
    return this.http.post(this.addUrl, data).map((res: Response) => res.json());

  }

  getUserType(userType: any) {
    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    return this.http.post(this.getUserTypeUrl, userType, {headers: this.headers}).map((res: Response) => res.json());

  }

  modify(data: AccountVO) {
    return this.http.post(this.modifyUrl, data).map((res: Response) => res.json());

  }

  getLimitData(data: any) {
    // const data = `id=${id}&isVip=${isVip}&isMobile=${isMobile}&isPhone=${isPhone}&islp=${islp}`;

    return this.http.post(this.getLimitDataUrl, data).map((res: Response) => res.json());

  }

  modifyPassword(data: any) {
    return this.http.post(this.modifyPasswordUrl, data).map((res: Response) => res.json());

  }

  modifyAccountStatus(data: any) {
    return this.http.post(this.modifyAccountStatusUrl, data).map((res: Response) => res.json());

  }

  modifyBetStatus(data: any) {
    return this.http.post(this.modifyBetStatusUrl, data).map((res: Response) => res.json());
  }

  modifyBalance(data: any) {
    return this.http.post(this.modifyBalanceUrl, data).map((res: Response) => res.json());
  }

  getUserByUid(data: any) {
    return this.http.post(this.getUserByUidUrl, data).map((res: Response) => res.json());
  }

  refreshAllUserChipsData() {
    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    return this.http.post(this.refreshAllUserChipsDataUrl, null, {headers: this.headers}).map((res: Response) => res.json());
  }

  modifyTitleStatus(data: any) {
    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    return this.http.post(this.modifyTitleStatusUrl, data, {headers: this.headers}).map((res: Response) => res.json());
  }

  searchPercentageConfig(id:number) {
    return this.http.post(this.searchPercentageConfigUrl+'/'+id, null).map((res: Response) => res.json());
  }

  addPercentageConfig(data: PercentageConfig) {
    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    return this.http.post(this.addPercentageConfigUrl, data, {headers: this.headers}).map((res: Response) => res.json());
  }

  updatePercentageConfig(data: PercentageConfig) {
    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/json');
    return this.http.post(this.updatePercentageConfigUrl, data, {headers: this.headers}).map((res: Response) => res.json());
  }

  deletePercentageConfig(id: number) {
    return this.http.post(this.deletePercentageConfigUrl+'/'+id, null).map((res: Response) => res.json());
  }
}
