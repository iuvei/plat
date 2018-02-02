/**
 * Created by Administrator
 * on 2017/4/3.
 */
import { Component, OnInit, ViewChild} from '@angular/core';
import {Http,Response,Headers} from '@angular/http';
import {CustomHttp} from '../../components/customhttp';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';
import {ToasterService} from 'angular2-toaster/angular2-toaster';
import {CommonUtil} from '../../common/CommonUtil';
import {TranslateService} from "@ngx-translate/core";
import {ConfirmationService} from "primeng/components/common/api";
import {Message} from "primeng/primeng";
import {Common} from "../../common/common";

@Component({
    selector: 'urlconfig',
    templateUrl: 'urlconfig.component.html',
    styleUrls: ['./urlconfig.component.scss'],

})
//url配置管理
export class UrlConfigComponent implements OnInit {
    @ViewChild('updateModal')
    public updateModal:ModalDirective;
    //游戏数据
    public dataList:any[];
    //服务器的地址
    public url:string = Common.URL;

  public updateData = {
    id: null,
    url: null,
  }

    constructor(private confirmDialog: ConfirmationService,public http:CustomHttp, private toaster:ToasterService, private _translate: TranslateService) {
    }

    ngOnInit() {
      let obj = JSON.parse(sessionStorage.getItem("urlConfig-component"));
      if(obj){
        this.dataList = obj.dataList;
      }else {
        this.search();
      }
    }

  ngOnDestroy(): void {
    let json = {"dataList":this.dataList};
    sessionStorage.setItem("urlConfig-component",JSON.stringify(json));
  }

    goUpdateGameConfig(data:any) {
        this.glaobal_clearObjectVal(this.updateData);
        this.updateData.id = data.id;
        this.updateData.url = data.url;
        this.updateModal.show();
    }
    //修该相关信息
    updateGameConfig() {
        return this.http
            .post(this.url + '/urlConfig/update', JSON.stringify(this.updateData))
            .subscribe(
                (
                    response => {
                        if(response.json()['status'] == 0){
                          this.search();
                          this.updateModal.hide();
                        }
                        this.glaobal_toaster(response);
                    })
            );
    }
    //查询数据
    search() {
        return this.http.post(this.url + '/urlConfig/search',null).subscribe(
            (
                response => {
                    this.dataList = response.json()['data'] as any[];
                })
        );
    }



    //toaster共用的函数
    public glaobal_toaster(response:Response) {
        let ok = response.json()['status'] as number;
        let message = response.json()['msg'] as string;
        if (ok === 0) {
            this.toaster.pop('success', this._translate.instant('common.success'), message);
        } else {
            this.toaster.pop('error', this._translate.instant('common.error'), message);
        }
    }

    //清除双向绑定对象的值
    public glaobal_clearObjectVal(obj:any) {
        let keys:any[] = Object.keys(obj);
        for (let key of keys) {
            obj[key] = null;
        }
    }
}

