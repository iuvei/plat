/**
 * Created by Administrator
 * on 2017/4/3.
 */
import { Component, OnInit, ViewChild} from '@angular/core';
import {Http,Response,Headers} from '@angular/http';
import {CustomHttp} from "../components/customhttp";
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';
import {ToasterService} from 'angular2-toaster/angular2-toaster';
import {CommonUtil} from '../common/CommonUtil';
import {TranslateService} from "@ngx-translate/core";
import {ConfirmationService} from "primeng/components/common/api";
import {Message} from "primeng/primeng";
import {Common} from "../common/common";
import {Advertise} from "app/model/Advertise";
import {logger} from "codelyzer/util/logger";
import {log} from "util";
import {AdvertisePicture} from "../model/AdvertisePicture";


@Component({
    selector: 'gameconfig',
    templateUrl: 'advertise.component.html',
    styleUrls: ['./advertise.component.scss'],

})
//游戏配置管理
export class AdvertiseComponent implements OnInit {


    @ViewChild('createModal')
    public createModal:ModalDirective;
    @ViewChild('updateModal')
    public updateModal:ModalDirective;

    public dataList = [];
    public createAdvertise:Advertise = new Advertise();
    public updateAdvertise:Advertise = new Advertise();
    public uploadurl = Common.URL+"/uploadPhotos"
    public createUploadedFiles: any[] = [];
    public updateUploadedFiles: any[] = [];
    public orders:number[];
    public types;
    public platforms;
    //服务器的地址
    public url:string = Common.URL;

    constructor(private confirmDialog: ConfirmationService,public http:CustomHttp, private toaster:ToasterService, private _translate: TranslateService) {
    }

    ngOnInit() {
      this.types = [{'name': this.ts('advertise.initGame'), 'value': 1}];
      this.platforms = [{'name': this.ts('advertise.pc'), 'value': 1},{'name': this.ts('advertise.phone'), 'value': 2}];
      this.search();
    }
    public delete(){
      this.http
        .post(this.url + '/advertise/delete/'+this.updateAdvertise.id, null)
        .subscribe(
          (
            response => {
              let ok = response.json()['status'] as number;
              let message = response.json()['msg'] as string;
              if (ok === 0) {
                this.toaster.pop('success', this._translate.instant('common.success'), message);
                this.updateModal.hide();
                this.search();
              } else {
                this.toaster.pop('error', this._translate.instant('common.error'), message);
              }

            })
        );
    }
    public search(){
      this.http
        .post(this.url + '/advertise/search', null)
        .subscribe(
          (
            response => {
              let ok = response.json()['status'] as number;
              let message = response.json()['msg'] as string;
              if (ok === 0) {
                this.toaster.pop('success', this._translate.instant('common.success'), message);
                let _dataList = response.json().data;
                for(let item of _dataList){
                  for(let temp of item.advertisePictures){
                    item.url=temp.url;
                    break;
                  }

                }
                this.dataList = _dataList;

              } else {
                this.toaster.pop('error', this._translate.instant('common.error'), message);
              }

            })
        );
    }

    public openupdate(data:any){
      this.updateAdvertise = data;
      this.updateUploadedFiles = [];
      this.updateModal.show();
      this.orders = [];
      for(let index in data.advertisePictures){
        this.orders.push(Number(index) + 1);
      }
    }

    public update(){
      if(this.updateAdvertise.platform === null || this.updateAdvertise.platform === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleasePlatform'));
        return;
      }
      if(this.updateAdvertise.type === null || this.updateAdvertise.type === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleaseType'));
        return;
      }
      if(this.updateAdvertise.remark === null || this.updateAdvertise.remark === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleaseRemark'));
        return;
      }
      if(this.updateAdvertise.advertisePictures === null || this.updateAdvertise.advertisePictures === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleasePhoto'));
        return;
      }
      let lengthPictures = this.updateAdvertise.advertisePictures.length;
      if(lengthPictures > 1){
        for(let index in this.updateAdvertise.advertisePictures){
          let _index = Number(index);
          for(var i=_index;i<lengthPictures -1; i++){
            if(this.updateAdvertise.advertisePictures[_index].order == this.updateAdvertise.advertisePictures[i+1].order){
              this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleasePhotoOrder'));
              return;
            }
          }
        }
      }

      this.http
        .post(this.url + '/advertise/update', this.updateAdvertise)
        .subscribe(
          (
            response => {
              let ok = response.json()['status'] as number;
              let message = response.json()['msg'] as string;
              if (ok === 0) {
                this.toaster.pop('success', this._translate.instant('common.success'), message);
                this.updateModal.hide();
                this.search();
              } else {
                this.toaster.pop('error', this._translate.instant('common.error'), message);
              }

            })
        );
    }
    public opencreate(){
      this.createAdvertise = new Advertise();
      this.createUploadedFiles = [];
      this.createModal.show();
    }
    public create(){
      if(this.createAdvertise.platform === null || this.createAdvertise.platform === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleasePlatform'));
        return;
      }
      if(this.createAdvertise.type === null || this.createAdvertise.type === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleaseType'));
        return;
      }
      if(this.createAdvertise.remark === null || this.createAdvertise.remark === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleaseRemark'));
        return;
      }
      if(this.createAdvertise.advertisePictures === null || this.createAdvertise.advertisePictures === undefined){
        this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleasePhoto'));
        return;
      }
      let lengthPictures = this.createAdvertise.advertisePictures.length;
      if(lengthPictures > 1){
        for(let index in this.createAdvertise.advertisePictures){
          let _index = Number(index);
          for(var i=_index;i<lengthPictures -1; i++){
              if(this.createAdvertise.advertisePictures[_index].order == this.createAdvertise.advertisePictures[i+1].order){
                this.toaster.pop('error', this._translate.instant('common.error'), this.ts('advertise.pleasePhotoOrder'));
                return;
              }
          }
        }
      }

      this.http
        .post(this.url + '/advertise/create', this.createAdvertise)
        .subscribe(
          (
            response => {
              let ok = response.json()['status'] as number;
              let message = response.json()['msg'] as string;
              if (ok === 0) {
                this.toaster.pop('success', this._translate.instant('common.success'), message);
                this.createModal.hide();
                this.search();
              } else {
                this.toaster.pop('error', this._translate.instant('common.error'), message);
              }

            })
        );
    }

  createOnUpload(event) {
    for (let file of event.files) {
     this.createUploadedFiles.push(file);
    }
    let responseBody = JSON.parse(event.xhr.response);
    if (responseBody.status == 0) {
      this.toaster.pop('success', this._translate.instant('common.success'), responseBody.msg);
      this.createAdvertise.advertisePictures = [];
      this.orders = [];
      for(let index in responseBody.data){
        let item= new AdvertisePicture();
        item.url = responseBody.data[index];
        item.order = Number(index) + 1;
        this.orders.push(Number(index) + 1);
        this.createAdvertise.advertisePictures.push(item);
      }
    }else {
      this.toaster.pop('error', this._translate.instant('common.error'), responseBody.msg);
      this.createUploadedFiles = [];
    }
  }

  updateOnUpload(event) {
    for (let file of event.files) {
      this.updateUploadedFiles.push(file);
    }
    let responseBody = JSON.parse(event.xhr.response);
    if (responseBody.status == 0) {
      this.toaster.pop('success', this._translate.instant('common.success'), responseBody.msg);
      this.updateAdvertise.advertisePictures = [];
      this.orders = [];
      for(let index in responseBody.data){
        let item= new AdvertisePicture();
        item.url = responseBody.data[index];
        item.order = Number(index) + 1;
        this.orders.push(Number(index) + 1);
        this.updateAdvertise.advertisePictures.push(item);
      }
    }else {
      this.toaster.pop('error', this._translate.instant('common.error'), responseBody.msg);
      this.updateUploadedFiles = [];
    }
  }

  ts(data:string){
      return this._translate.instant(data);
  }

}

