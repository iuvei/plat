/**
 * Created by Administrator
 * on 2017/4/3.
 */
import { Component, OnInit, ViewChild} from '@angular/core';
import {Http,Response,Headers} from '@angular/http';
import {CustomHttp} from '../components/customhttp';
import {ModalDirective} from 'ng2-bootstrap/modal/modal.component';
import {ToasterService} from 'angular2-toaster/angular2-toaster';
import {TranslateService} from "@ngx-translate/core";
import {ConfirmationService} from "primeng/components/common/api";
import {Message} from "primeng/primeng";
import {Common} from "../common/common";

@Component({
    selector: 'voiceuser',
    templateUrl: 'voiceuser.component.html',
    styleUrls: ['./voiceuser.component.scss'],
})
export class VoiceUserComponent implements OnInit {


    @ViewChild('addPhoneBetterModal')
    public addPhoneBetterModal:ModalDirective;




    public currentPage:number = 1;
    public smallnumPages:number = 1;
    public totalItems:number = 0;
    public pageSize:number = 20;
    public maxSize:number = 10;


    //服务器的地址
    public url:string = Common.URL;

    constructor( private confirmDialog: ConfirmationService,public http:CustomHttp, private toaster:ToasterService, private _translate: TranslateService) {
    }

    ngOnInit() {
        this.getBetterUserList()
        this.search();
    }
    goRemoveAdd(data:any){
        let msg = this._translate.instant("voiceuser.isDeleteThisMessge");
        this.confirmDialog.confirm({
          message: msg,
          accept: () => {
            const condition = {
              uid: data.uid,
              bid: data.betteruseruid
            }
            this.http
              .post(this.url + '/sysPhoneUserFunction/deletePhoneBetterUser', condition)
              .subscribe(
                (
                  response => {
                    //this.removeAddModal.hide();
                    this.glaobal_toaster(response);
                    this.search();
                  })
              );
          }
        });
        //this.removeAddVoiceUser = data;
        //this.removeAddModal.show();
    }

    goForceOld(data:any){
      let msg = this._translate.instant("voiceuser.isOldThisMessge");
      this.confirmDialog.confirm({
        message: msg,
        accept: () => {
          const condition = {
            uid: data.uid,
            bid: data.betteruseruid
          }
          this.http
            .post(this.url + '/sysPhoneUserFunction/updatePhoneBetterUser', condition)
            .subscribe(
              (
                response => {
                  this.glaobal_toaster(response);
                  this.search();
                })
            );
        }
      });

    }

    public addeVoiceUser={
        username:'',
        uid:''
    }
    public addBid:number;

    goAdd(data:any) {
        this.addeVoiceUser.username = data.username;
        this.addeVoiceUser.uid = data.uid;
        this.addPhoneBetterModal.show();
    }

    addPhoneBetter() {
        const condition = {
            uid: this.addeVoiceUser.uid,
            bid: this.addBid
        }
        this.http
            .post(this.url + '/sysPhoneUserFunction/addPhoneBetterUser', condition)
            .subscribe(
                (
                    response => {
                        this.addPhoneBetterModal.hide();
                        this.glaobal_toaster(response);
                        this.search();
                    })
            );
    }

    public searchConditon = {
        username: '',
        bettername: '',
        status:-1
    }

    public betterUserList:any[];

    getBetterUserList() {
        this.http
            .post(this.url + '/sysPhoneUserFunction/betterUserList', '')
            .subscribe(
                (
                    response => {
                        this.betterUserList = response.json() as any[];
                    })
            );
    }

    public setPage(pageNo:number):void {
        this.currentPage = pageNo;
    }

    public pageChanged(event:any):void {
        //更新查询页数
        this.currentPage = event.page;
        this.search();
    }

    public phoneUser:any[];
    public search() {
        const conditioon = {
            username:this.searchConditon.username,
            status:this.searchConditon.status,
            bettername:this.searchConditon.bettername,
            page:this.currentPage,
            rows:this.pageSize
        }
        return this.http
            .post(this.url + '/sysPhoneUserFunction/searchPhoneUser', conditioon)
            .subscribe(
                (
                    response => {
                        this.phoneUser = response.json()['rows'] as any[];
                        this.totalItems = response.json()['total'] as number;
                    })
            );
    }

    //toaster共用的函数
    public glaobal_toaster(response:Response) {
        let ok = response.json()['ok'] as boolean;
        let message = response.json()['msg'] as string;
        if (ok) {
            this.toaster.pop('success', this._translate.instant('common.success'), message);
        } else {
            this.toaster.pop('error', this._translate.instant('common.error'), message);
        }
    }

}

