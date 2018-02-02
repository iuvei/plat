import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {WinloseVo} from "../../model/winlosevo";


/**
 * Created by Brent on 2017/5/14.
 */
@Component({
  selector: 'winlost-table',
  templateUrl: './winlost-table.component.html',
  styleUrls: ['./winlost-table.component.scss']
})

export class WinLostTableComponent implements  OnInit{

  @Input() public tableList: WinloseVo[] = [];

  @Input() public parentModel : WinloseVo ;

  @Input() public index : number = 0 ;

  @Output() onExpend = new EventEmitter<{}>();
  @Output() onClose = new EventEmitter<WinLostTableComponent>();
  @Output() onHandle = new EventEmitter<{}>();

  public path:string='/';

  ngOnInit(): void {

  }


  expend(winlose : WinloseVo){
    this.onExpend.emit({"model":winlose,"component":this});
  }

  close(){
    this.onClose.emit(this);
  }

  public handle(type : number,winlose : WinloseVo){
    this.onHandle.emit({"type":type,"model":winlose,"component":this})
  }

}
