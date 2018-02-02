///<reference path="../components/customhttp.ts"/>
import {Common} from "../common/common";
import {Response} from "@angular/http";
import {CustomHttp} from "../components/customhttp";
import {Injectable} from "@angular/core";

@Injectable()
export class AnnounceSerice{

  private host = Common.URL;

  private url = this.host + '/announcemanage/search';
  private createUrl = this.host + '/announcemanage/create';
  private deleteUrl = this.host + '/announcemanage/deleteById';
  private updateUrl = this.host + '/announcemanage/updateAnnounce';

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
}
