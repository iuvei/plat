///<reference path="../components/customhttp.ts"/>
import {Common} from "../common/common";
import {Response} from "@angular/http";
import {CustomHttp} from "../components/customhttp";
import {Injectable} from "@angular/core";

@Injectable()
export class RoomSerice{

  private host = Common.URL;
  private url = this.host + '/privateRoom/search';
  private createUrl = this.host + '/privateRoom/create';
  private deleteUrl = this.host + '/privateRoom/deleteById';
  private updateUrl = this.host + '/privateRoom/update';
  private modifyPasswordUrl = this.host + '/privateRoom/modifyPassword';
  private getTableUrl = this.host + '/privateRoom/gametables';
  private getPercentageConfigUrl = this.host + '/privateRoom/getPercentageConfigList';

  constructor(private http: CustomHttp) {
  }

  search(data: any) {
    return this.http.post(this.url, data).map((res: Response) => res.json());
  }

  create(data: any) {
    return this.http.post(this.createUrl, data).map((res: Response) => res.json());
  }

  delete(id:number) {
    return this.http.get(this.deleteUrl+"/"+id).map((res: Response) => res.json());
  }

  update(data: any) {
    return this.http.post(this.updateUrl, data).map((res: Response) => res.json());
  }

  modifyPassword(data: any) {
    return this.http.post(this.modifyPasswordUrl, data).map((res: Response) => res.json());
  }

  getGameTable() {
    return this.http.post(this.getTableUrl, null).map((res: Response) => res.json());
  }

  getPercentageConfig() {
    return this.http.post(this.getPercentageConfigUrl, null).map((res: Response) => res.json());
  }

}
