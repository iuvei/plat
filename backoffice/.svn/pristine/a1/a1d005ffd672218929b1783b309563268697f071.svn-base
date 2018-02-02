import {Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';

import 'rxjs/add/operator/map';
import {CustomHttp} from '../components/customhttp';
import {Resource} from 'app/resource/resource';
import {Common} from "../common/common";


@Injectable()
export class ResourceService {


  private searchUrl = Common.URL + '/sysResourceFunction/searchResource';

  private addUrl = Common.URL + '/sysResourceFunction/createResource';

  private updateUrl = Common.URL + '/sysResourceFunction/updateResource';

  private headers: Headers;

  constructor(private http: CustomHttp) {
  }

  public search(resource: Resource,
                currentPage: number, pageSize: number) {
    let data: string = `page=${currentPage}&rows=${pageSize}`;
    for (let obj in resource) {
      data +=`&`+obj+`=`+resource[obj];
    }
    this.headers = new Headers();
    this.headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.searchUrl, data,{headers: this.headers}).map((res: Response) => res.json());
  }

  public add(resource: Resource) {
    return this.http.post(this.addUrl, resource).map(res => res.json());
  }

  public update(resource: Resource){
    return this.http.post(this.updateUrl, resource).map(res => res.json());
  }





}
