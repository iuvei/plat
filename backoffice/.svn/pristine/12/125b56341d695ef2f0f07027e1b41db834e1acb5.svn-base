import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";


/**
 * Created by Brent on 2017/5/14.
 */
@Component({
  selector: 'roomhedge-table',
  templateUrl: './roomhedge-table.component.html'
})

export class RoomHedgeTableComponent implements  OnInit{

  @Input() public tableList: any[] ;

  @Input() public parentModel:any ;

  @Input() public index : number = 0 ;

  @Output() onExpend = new EventEmitter<{}>();
  @Output() onClose = new EventEmitter<RoomHedgeTableComponent>();
  @Output() onHandle = new EventEmitter<{}>();

  public path:string='/';

  ngOnInit(): void {}

  expend(searchModel :any){
    this.onExpend.emit({"model":searchModel,"component":this});
  }

  close(){
    this.onClose.emit(this);
  }

  public handle(type : number,searchModel :any){
    this.onHandle.emit({"type":type,"model":searchModel,"component":this})
  }
}
