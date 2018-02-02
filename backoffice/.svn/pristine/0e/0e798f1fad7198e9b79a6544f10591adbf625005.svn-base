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
    selector: 'gameconfig',
    templateUrl: 'gameconfig.component.html',
    styleUrls: ['./gameconfig.component.scss'],

})
//游戏配置管理
export class GameConfigComponent implements OnInit {


    @ViewChild('createModal')
    public createModal:ModalDirective;
    @ViewChild('updateModal')
    public updateModal:ModalDirective;



    public currentPage:number = 1;
    public smallnumPages:number = 1;
    public totalItems:number = 0;
    public pageSize:number = 20;
    public maxSize:number = 10;

    //加载游戏类型
    public gameTypeList:any[];
    //游戏数据
    public gameDataList:any[];
    //默认search初始化条件
    public gid:any = '';
    public keyname:string = '';
    public keyval:string = '';
    //游戏配置
    public insertNewData = {
        keyname: '',
        keyval: '',
        remarks: '',
        gid: ''
    }

    public updateData = {
        //当前的数据id
        id: null,
        key: null,
        value: null,
        remark: null,
        //游戏类型
        gameId: null
    }
    public updateGcn:string;

    public deleteGcid:number;

    //服务器的地址
    public url:string = Common.URL;

    constructor(private confirmDialog: ConfirmationService,public http:CustomHttp, private toaster:ToasterService, private _translate: TranslateService) {
    }

    ngOnInit() {
        //初始化游戏列表
        this.gameList();
        this.search();
    }

    public pageChanged(event:any):void {
        this.currentPage = event.page;
        this.search();
    }

    goDeleteGameConfig(data:any) {
      let msg = this._translate.instant("gameConfig.confirmDeleteGameConfig");
      this.confirmDialog.confirm({
        message: msg,
        accept: () => {
          const condition = {
            gcid: data.gcid
          }
          this.http
            .post(this.url + '/sysGameConfigFunction/deleteGameConfig', condition)
            .subscribe(
              (
                response => {
                  this.search();
                  this.glaobal_toaster(response);
                })
            );
        }
      });

    }

    goUpdateGameConfig(data:any) {
        this.glaobal_clearObjectVal(this.updateData);
        this.updateData.id = data.id;
        this.updateData.gameId = data.gameId;
        this.updateData.key = data.key;
        this.updateData.value = data.value;
        this.updateData.remark = data.remark;
        if(data.gameId == 1){
          this.updateGcn = this._translate.instant('gameTable.baccarat');
        }else if(data.gameId ==2){
          this.updateGcn = this._translate.instant('gameTable.dragonTiger');
        }else if(data.gameId ==3){
          this.updateGcn = this._translate.instant('gameTable.roulette');
        }else if(data.gameId ==4){
          this.updateGcn = this._translate.instant('gameTable.sicbo');
        }else{
          this.updateGcn = '--';
        }

        this.updateModal.show();
    }

    //修该相关信息
    updateGameConfig() {
        return this.http
            .post(this.url + '/gameConfigFunction/updateGameConfig', this.updateData)
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

    //创建新的用户数据
    createGameConfig() {
        return this.http
            .post(this.url + '/sysGameConfigFunction/createGameConfig', this.insertNewData)
            .subscribe(
                (
                    response => {
                        this.search();
                        this.createModal.hide();
                        this.glaobal_clearObjectVal(this.insertNewData);
                        this.glaobal_toaster(response);
                    })
            );
    }

    //获取数据类型
    gameList() {
        return this.http
            .post(this.url + '/gametable/getGameType', '')
            .subscribe(
                (
                    response => {
                        this.gameTypeList = response.json()['data'] as any[];
                    })
            );
    }

    //查询数据
    search() {
        const condition = {
            page: this.currentPage,
            rows: this.pageSize,
            gid: this.gid,
            keyname: this.keyname,
            keyval: this.keyval
        }
        return this.http
            .post(this.url + '/gameConfigFunction/searchGameConfig', condition)
            .subscribe(
                (
                    response => {
                        //this.totalItems = response.json()['data']['total'] as number;
                        this.gameDataList = response.json()['data'] as any[];
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

