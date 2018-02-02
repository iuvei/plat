import {Injectable } from '@angular/core';
import { Http, Response, Headers,RequestOptions } from '@angular/http';
import 'rxjs/add/operator/map';
import {Common} from "../../common/common";
import {CustomHttp} from "../../components/customhttp";


@Injectable()
export class GameTableService {

  private host = Common.URL;

  private url = this.host + '/gametable/search';
  private addUrl = this.host + '/gametable/create';
  private modifyUrl = this.host + '/gametable/modify';
  private deleteUrl = this.host + '/gametable/updateTableStatus';

  private getGameTypeUrl = this.host + '/gametable/getGameType';
  private clearOnlineUserUrl = this.host + '/gametable/clearOnlineUser';
  private getPlatFlagUrl = this.host + '/gametable/getPlatFlag';
  private platMaintenanceUrl = this.host + '/gametable/platMaintenance';
  private options = new RequestOptions({});
  constructor(private http: CustomHttp) {

  }

  clearOnlineUser(){
    return this.http.post(this.clearOnlineUserUrl, null).map((res: Response) => res.json());
  }

  getPlatFlag(){
    return this.http.post(this.getPlatFlagUrl, null).map((res: Response) => res.json());
  }

  platMaintenance(){
    return this.http.post(this.platMaintenanceUrl, null).map((res: Response) => res.json());
  }

  search(data: any) {
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post(this.url,this.transformRequest(data), this.options).map((res: Response) => res.json());
  }

  create(data: any) {
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post(this.addUrl, this.transformRequest(data), this.options).map((res: Response) => res.json());

  }

  modify(data: any) {
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post(this.modifyUrl, this.transformRequest(data), this.options).map((res: Response) => res.json());

  }

  updateTableStatus(data: any) {
    this.options.headers = new Headers();
    this.options.headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post(this.deleteUrl, this.transformRequest(data), this.options).map((res: Response) => res.json());
  }

  getGameType() {
    return this.http.post(this.getGameTypeUrl, null).map((res: Response) => res.json());
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
