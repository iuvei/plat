import {Component, Input, OnInit} from "@angular/core";
import {BetService} from "./bet.service";

@Component({
  selector: 'top-bet-table',
  templateUrl: './top-bet-table.component.html',
  styleUrls: ['./top-bet-table.component.scss']
})
export class TopBetTableComponent implements  OnInit {
  private dataList: any[] = [];
  @Input() public parentObj = {};

  constructor(private betService:BetService) { }

  ngOnInit() {
    this.getTopBet();
  }

  public getTopBet(){
    this.betService.getTopBet(this.parentObj['roundId']).subscribe(data=>{
      this.dataList = data.data;
      // this.dataList = [
      //   {agentid:11,loginname:'ddddd',bettype:'ddd',betamount:1000,lastloginip:'192.168.1.1'},
      //   {agentid:13,loginname:'ddddd',bettype:'ddd',betamount:1000,lastloginip:'192.168.1.1'}
      // ]
    });
  }

}
