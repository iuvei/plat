import {
  Component, ComponentRef, OnInit, ViewChild, ViewContainerRef, ComponentFactoryResolver,
  ViewRef, OnDestroy
} from '@angular/core';

import {CalendarModule} from "primeng/primeng";
import {TranslateService} from "@ngx-translate/core";
import {forEach} from "@angular/router/src/utils/collection";
import {WinLostService} from "./winlost.service";
import {WinLostTableComponent} from "./winlost-table.component";
import {WinloseVo} from "app/model/winlosevo";
import {view_utils} from "@angular/compiler/src/private_import_core";
import {CommonUtil} from "../../common/CommonUtil";


@Component({
  templateUrl: 'winlost.component.html',
  styleUrls: ['./winlost.component.scss']
})
export class WinLostComponent implements  OnInit,OnDestroy {
  /**
   * 查询属性
   */
  public startTime: Date = new Date();
  public lastTime: Date = new Date();

  /**
   * 分页属性
   */
  public tableList: WinloseVo[] = [];

  @ViewChild('winLosePanel', {read: ViewContainerRef})
  private winLosePanel: ViewContainerRef;

  private listTableComponent : ComponentRef<WinLostTableComponent>[] = [];

  public tableindex : number = 0;



  /*en: any;*/

  constructor(private winLostService: WinLostService, private _translate: TranslateService,
              private _componentFactoryResolver: ComponentFactoryResolver) {
  }

  ngOnInit() {
    let obj = JSON.parse(sessionStorage.getItem("winlost-component"));
    if (obj) {
      this.startTime = new Date(obj.startTime);
      this.lastTime = new Date(obj.lastTime);
      this.tableList = obj.tableList;
      let tableListData = obj.tableListData;
      for(let i=0;i<tableListData.length;i++){
        this.createTableComponent(tableListData[i].tableList,tableListData[i].parentModel);
      }
    } else {
      this.startTime = CommonUtil.getStartDate();
      this.lastTime = CommonUtil.getEndDate();
      this.search();
    }
  }

  ngOnDestroy(): void {
    let data = {
      "startTime":this.startTime,
      "lastTime":this.lastTime,
      "tableList":this.tableList,
      // "listTableComponent":this.listTableComponent,
      // "tableindex":this.tableindex
      "tableListData":[]
    };

    for(let i=0;i<this.listTableComponent.length;i++){
      let temp = this.listTableComponent[i].instance;
      data.tableListData.push({"tableList":temp.tableList,"parentModel":temp.parentModel});
      console.info(`${temp.tableList}--${temp.parentModel}--${temp.index}`);
    }
    sessionStorage.setItem("winlost-component",JSON.stringify(data));
  }

  search(){
    this.clearAllTable();
    this.winLostService.search(CommonUtil.formatDate(this.startTime),CommonUtil.formatDate(this.lastTime),
      1,null).subscribe(
      sta => {
        this.tableList = sta.data;
        this.createTableComponent(this.tableList, null);
      },
      err => {
        console.log(err);
      });
  }

  searchChild(data) {
    let parentAgent : WinloseVo = data.model;
    let tableComponent :WinLostTableComponent = data.component;
    let isdel : boolean = false;
    let i = 0;
    for(let item of this.listTableComponent){
      if(item.instance.index>tableComponent.index){
        isdel = true;
      }
      if(isdel){
        item.destroy();
      }else{
        i++;
      }
    }
    this.listTableComponent.splice(i,this.listTableComponent.length-i);
    /*let length : number = this.listTableComponent.length;
    for(let i:number=0;i<length;i++){
      let item : ComponentRef<WinLostTableComponent> = this.listTableComponent[i];
      if(item.instance.index>tableComponent.index){
        isdel = true;
      }
      if(isdel){
        item.destroy();
        this.listTableComponent.splice(i,1);
      }
    }*/
      this.winLostService.search(CommonUtil.formatDate(this.startTime),CommonUtil.formatDate(this.lastTime),
        parentAgent.statisType,parentAgent.agentId).subscribe(
        p => {
          let agentStatisList : WinloseVo[]= [];
          let temps = p.data;
          if (temps) {
            for (let i = 0; i < temps.length; i++) {
              let item = temps[i];
              agentStatisList.push(item);
            }
            this.createTableComponent(agentStatisList, parentAgent);
          }
        }
      );
  }

  //清空所有表格组件。
  private clearAllTable() {
    let comp: ComponentRef<WinLostTableComponent>;
    do {
      comp = this.listTableComponent.pop();
      if (comp) {
        comp.destroy();
      }
    } while (comp);
  }

  private createTableComponent(agentStatisList: WinloseVo[], parentAgent: WinloseVo) {
    let componentFactory = this._componentFactoryResolver.resolveComponentFactory(WinLostTableComponent);
    let componentRef = this.winLosePanel.createComponent(componentFactory);
    this.listTableComponent.push(componentRef);

    let winloseTable = componentRef.instance;
    winloseTable.tableList = agentStatisList;
    winloseTable.parentModel = parentAgent;
    winloseTable.index = ++this.tableindex;
    let path="/";
    for (let item of this.listTableComponent){
      if(item.instance.parentModel!=null){
        if(path.length>1){
          path+="/";
        }
        path = path + item.instance.parentModel.loginName;
      }
    }
    if(parentAgent !=null && parentAgent.statisType ==3){
      winloseTable.path = path.substr(0,path.lastIndexOf("/"));
    }else{
      winloseTable.path = path;
    }
    scroll(0,document.body.scrollHeight);

    winloseTable.onExpend.subscribe(data => {
      this.searchChild(data);
    });

    winloseTable.onHandle.subscribe(data=>{
      if(data.type==1){
        this.search();
      }
    });

    winloseTable.onClose.subscribe(data => {
      do {
        let item = this.listTableComponent.pop();
        if (!item) break;
        if (item.instance.path.indexOf(data.path)>=0) {
          item.destroy();
        } else {
          this.listTableComponent.push(item);
          break;
        }
      } while (true);
    });
  }



}
