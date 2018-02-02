/**
 * Created by Administrator
 * on 2017/4/3.
 */
import { Component, OnInit, ViewChild} from '@angular/core';
import {Http,Response,Headers} from '@angular/http';
import {CustomHttp} from '../../components/customhttp';
import { ModalDirective } from 'ng2-bootstrap/modal/modal.component';
import {ToasterService} from 'angular2-toaster/angular2-toaster';
import {TranslateService} from "@ngx-translate/core";
import {Message} from "primeng/primeng";
import {ConfirmationService} from "primeng/components/common/api";
import {Common} from "../../common/common";


@Component({
    selector: 'gameodds',
    templateUrl: 'gameodds.component.html',
    styleUrls: ['./gameodds.component.scss'],

})
export class GameOddsComponent implements OnInit {

    @ViewChild('createModal')
    public createModal:ModalDirective;
    @ViewChild('updateModal')
    public updateModal:ModalDirective;



    public currentPage:number = 1;
    public smallnumPages:number = 1;
    public totalItems:number = 0;
    public pageSize:number = 20;
    public maxSize:number = Common.MAX_PAGE_SIZE;


    public gameList:any[];
    public playList:any[];
    public dataList:any[];

    //游戏桌,游戏类型,比例名称,下注比例计算,下注下限,下注上限
    public pid = '';
    public gid = '';
    public en:string;
    public rate:number;
    public min:number;
    public max:number;

    //服务器的地址
    public url:string = Common.URL;

    constructor(private confirmDialog: ConfirmationService,public http:CustomHttp, private toaster:ToasterService, private _translate: TranslateService) {
    }

    ngOnInit() {
        this.getGameList();
        this.getPlayList();
        this.search();
    }

    public insertNewData = {
        gid: '',
        pid: '',
        en: '',
        rate: '',
        min: '',
        max: '',
        dis: ''
    }

    goDeleteGameConfig(data:any) {
      let msg = this._translate.instant('gameOdds.isDeleteThisMessahe');
      this.confirmDialog.confirm({
        message: msg,
        accept: () => {
          const conditon = {
            bettypeid: data.bettypeid
          }
          this.http
            .post(this.url + '/sysTradeItemFunction/deleteTradeItem', conditon)
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



    public updateGameOdds = {
        id: '',
        pid: '',
        gid: '',
        key: '',
        rate: '',
        min: '',
        max: '',
        dis: ''
    };

    goUpdateGameConfig(data:any) {
        this.updateGameOdds.id = data.id;
        this.updateGameOdds.pid = data.play_id;
        this.updateGameOdds.gid = data.game_id;
        this.updateGameOdds.key = data.key;
        this.updateGameOdds.rate = data.rate;
        this.updateGameOdds.min = data.min;
        this.updateGameOdds.max = data.max;
        this.updateGameOdds.dis = data.dis;
        if(this.updateGameOdds.pid == '0'){
            this.updateGameOdds.pid ='';
        }
        this.updateModal.show();
    }

    updateGameConfig() {
        this.http
            .post(this.url + '/sysTradeItemFunction/updateTradeItem', this.updateGameOdds)
            .subscribe(
                (
                    response => {
                        this.search();
                        this.updateModal.hide();
                        this.glaobal_toaster(response);
                    })
            );
    }

    createGameConfig() {
        //条件判断
        this.http
            .post(this.url + '/sysTradeItemFunction/createTradeItem', this.insertNewData)
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

    public setPage(pageNo:number):void {
        this.currentPage = pageNo;
    }

    public pageChanged(event:any):void {
        //更新查询页数
        this.currentPage = event.page;
        this.search();
    }

    public getGameList() {
        this.http
            .post(this.url + '/sysGameTableFunction/gameList', '')
            .subscribe(
                (
                    response => {
                        this.gameList = response.json() as any[];
                    })
            );
    }

    public getPlayList() {
        return this.http
            .post(this.url + '/sysGameTableFunction/playList', '')
            .subscribe(
                (
                    response => {
                        this.playList = response.json() as any[];
                    })
            );
    }

    public search() {
        const condition = {
            page: this.currentPage,
            rows: this.pageSize,
            pid: this.pid,
            gid: this.gid,
            rate: this.rate,
            en: this.en,
            min: this.min,
            max: this.max
        }
        return this.http
            .post(this.url + '/sysTradeItemFunction/searchTradeItem', condition)
            .subscribe(
                (
                    response => {
                        this.dataList = response.json()['rows'] as any[];
                        this.totalItems = response.json()['total'];
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

    //清除双向绑定对象的值
    public glaobal_clearObjectVal(obj:any) {
        let keys:any[] = Object.keys(obj);
        for (let key of keys) {
            obj[key] = '';
        }
    }

}

