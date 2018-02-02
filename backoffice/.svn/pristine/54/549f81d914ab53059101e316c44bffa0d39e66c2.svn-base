import {Component,OnInit} from '@angular/core';
import {Message,TreeNode,MenuItem} from "primeng/primeng";
import {Http, Response} from '@angular/http';
import {CustomHttp} from "../../components/customhttp";
import {TranslateService} from "@ngx-translate/core";
import {Common} from "../../common/common";

@Component({
    templateUrl: 'tigereport.component.html',
    styleUrls: ['./tigereport.component.scss']
})

export class TigerReportComponent implements OnInit {


  constructor(private http:CustomHttp, private _translate: TranslateService) { }

    ngOnInit() {
        this.startTime = new Date(this.endTime.getFullYear() - 1, this.endTime.getMonth(), this.endTime.getDay());
    }


    //服务器的地址
    public url:string = Common.URL;
    //时间控制
    public showStartDatePicker:boolean = false;
    public showEndDatePicker:boolean = false;

    //quert param
    public startTime:Date;
    public endTime:Date = new Date();
    public queryName:string = '';

    msgs:Message[];
    //输赢报表
    lazyFiles:TreeNode[];
    //代理平台分类
    lazyFiles2:TreeNode[];
    items:MenuItem[];

    lazyFilesStatus:boolean = false;
    lazyFiles2Status:boolean = false;
    //查看代理分类

    public treecondition = {
        id: "",
        nodeType: 0,
        agentName: "",
        startDay: "",
        endDay: ""
    }

    public queryDetail(agentId:number) {
        console.log(this.startTime.toLocaleDateString().replace('/', '-').replace('/', '-'));
        var condition = {
            agentId: agentId,
            startDay: this.startTime.toLocaleDateString().replace('/', '-').replace('/', '-'),
            endDay: this.endTime.toLocaleDateString().replace('/', '-').replace('/', '-')
        }
        this.http.post(this.url + '/agentReport/slotReport-platCat', condition)
            .toPromise()
            .then(response => {
                this.lazyFiles2 = response.json()['data'] as TreeNode[];
                this.lazyFiles2Status = true;
            });
    }

    //查询树的数据
    public queryTreeData() {
        this.treecondition.nodeType = 0;
        this.treecondition.agentName = this.queryName;
        this.treecondition.startDay = this.startTime.toLocaleDateString().replace('/', '-').replace('/', '-');
        this.treecondition.endDay = this.endTime.toLocaleDateString().replace('/', '-').replace('/', '-');
        //this.tigereportService.getLazyFilesystem(this.treecondition).then(files => {this.lazyFiles = files,console.log(files)});
        this.http.post(this.url + '/agentReport/slotReport-subAgent', this.treecondition)
            .toPromise()
            .then(response => {
                this.lazyFiles = response.json()['data'] as TreeNode[];
                this.lazyFilesStatus = true;
            });
    }

    nodeSelect(event) {
        if (event.node) {
            this.queryDetail(event.node.data.agentId)
        }
    }

    nodeExpand(event) {
        //查询相关树的信息
        if (event.node) {
            this.treecondition.id = event.node.data.id;
            this.treecondition.nodeType = 1;
            this.treecondition.agentName = this.queryName;
            this.treecondition.startDay = this.startTime.toLocaleDateString().replace('/', '-').replace('/', '-');
            this.treecondition.endDay = this.endTime.toLocaleDateString().replace('/', '-').replace('/', '-');
            this.http.post(this.url + '/agentReport/slotReport-subAgent', this.treecondition)
                .toPromise()
                .then(response => {
                    event.node.children = response.json()['data'] as TreeNode[];
                    //展示详情
                    this.queryDetail(event.node.data.agentId)
                });

            //in a real application, make a call to a remote url to load children of the current node and add the new nodes as children
            //this.tigereportService.getLazyFilesystem(this.treecondition).then(nodes => event.node.children = nodes);
        }
    }


    nodeExpand2(event) {
        if (event.node) {
            this.treecondition.id = event.node.data.id;
            this.treecondition.nodeType = 1;
            this.treecondition.agentName = this.queryName;
            this.treecondition.startDay = this.startTime.toLocaleDateString().replace('/', '-').replace('/', '-');
            this.treecondition.endDay = this.endTime.toLocaleDateString().replace('/', '-').replace('/', '-');
            this.http.post(this.url + '/agentReport/slotReport-subAgent', this.treecondition)
                .toPromise()
                .then(response => {
                    event.node.children = response.json()['data'] as TreeNode[];
                });
            //in a real application, make a call to a remote url to load children of the current node and add the new nodes as children
            //this.tigereportService.getLazyFilesystem(this.treecondition).then(nodes => event.node.children = nodes);
        }
    }

    public closeStart():void {
        this.showStartDatePicker = false;
    }

    public closeEnd():void {
        this.showEndDatePicker = false;
    }


}
