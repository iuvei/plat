///<reference path="../components/customhttp.ts"/>
import {Common} from "../common/common";
import {Response} from "@angular/http";
import {CustomHttp} from "../components/customhttp";
import {Injectable} from "@angular/core";

@Injectable()
export class VGameTableSerice{

  private host = Common.URL;
  private url = this.host + '/virtualGameTable/search';
  private createUrl = this.host + '/virtualGameTable/create';
  private deleteUrl = this.host + '/virtualGameTable/deleteById';
  private batchDeleteUrl = this.host + '/virtualGameTable/batchDelete';
  private getTableUrl = this.host + '/virtualGameTable/gametables';
  private getGameUrl = this.host + '/virtualGameTable/games';

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

  batchDelete(data:any) {
    return this.http.post(this.batchDeleteUrl,data).map((res: Response) => res.json());
  }

  getGameTable() {
    return this.http.post(this.getTableUrl, null).map((res: Response) => res.json());
  }

  getGame() {
    return this.http.post(this.getGameUrl, null).map((res: Response) => res.json());
  }

}
