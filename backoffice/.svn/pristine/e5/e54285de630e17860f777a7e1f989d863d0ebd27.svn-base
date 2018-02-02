import {Component, OnInit, ViewChild} from '@angular/core';
import {ResourceService} from './resource.service';
import {ModalDirective} from 'ng2-bootstrap';
import {Resource} from "./resource";
import {Message} from "primeng/primeng";
import {CustomHttp} from "../components/customhttp";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "app/common/common";

@Component({
  styleUrls:['./resource.component.scss'],
  templateUrl:'./resource.component.html',
})

export class ResourceComponent  implements  OnInit{

  /**
   * 查询对象
   */
  public searchResourceModel:Resource=new Resource();
  /**
   * 操作对象
   * @type {Resource}
   */
  public useResourceModel:Resource=new Resource();

  private msgs: Message[] = [];


  /**
   * 分页属性
   */
  public currentPage: number = 1;
  public smallnumPages: number = 1;
  public totalItems: number = 0;
  public pageSize: number = 20;
  public maxSize: number = Common.MAX_PAGE_SIZE;
  public tableList: any[] = [];

  /**
   * 页面视图属性
   */
  @ViewChild('createResourceDialog')
  private createResourceDialog : ModalDirective;
  @ViewChild('updateResourceDialog')
  private updateResourceDialog : ModalDirective;

  constructor (private resourceService:ResourceService,private http:CustomHttp, private _translate: TranslateService){ }

  ngOnInit(): void {
    this.searchResourceModel.status="0";
    this.search();
  }


  search(){
    this.resourceService.search(this.searchResourceModel,this.currentPage,this.pageSize).subscribe(
      data => {
        this.totalItems = data.total;
        this.tableList = data.rows;
      },
      err => {
        console.log(err);
      });
  }

  add(){
    this.resourceService.add(this.useResourceModel).subscribe(
      res => {
        if(res.ok){
          this.createResourceDialog.hide();
          this.search();
        }else{
          this.showError(res.msg);
        }
      },
      err => {
        console.log(err);
      });
  }

  public readyedit(edit:any){
    this.useResourceModel.resourceID= edit.resourceid;
    this.useResourceModel.resourceName= edit.resourcename;
    this.useResourceModel.resourceDesc= edit.resourcedesc;
    this.useResourceModel.status= (edit.status==true?'1':'2');
    this.updateResourceDialog.show();
  }

  public update(){
    this.resourceService.update(this.useResourceModel).subscribe(res => {
        if(res.ok){
          this.updateResourceDialog.hide();
          this.search();
        }else{
          this.showError(res.msg);
        }
      },
      err => {
        console.log(err);
      });
  }
  public pageChanged(event: any): void {
    this.currentPage =  event.page;
    this.search();
  }

  showError(msg:string) {
    this.msgs = [];
    this.msgs.push({severity:'error', summary: this._translate.instant('common.error'), detail:msg});
  }

}
