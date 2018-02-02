import {Component, OnInit} from "@angular/core";
import {BetService} from "./bet.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  templateUrl: './bet.component.html',
  styleUrls: ['./bet.component.scss']
})
export class BetComponent implements  OnInit {
  private data: any[] = [];
  public statusList = [
    {"name": this._translate.instant('common.all'), "value":null},
    {"name": this._translate.instant('status.round.new_boot'), "value":1},
    {"name": this._translate.instant('status.round.new_round'), "value":3},
    {"name": this._translate.instant('status.round.start_bet'), "value":4},
    {"name": this._translate.instant('status.round.end_bet'), "value":5}
    ];

  constructor(private betService:BetService,private _translate: TranslateService) { }

  ngOnInit() {
    this.search();
  }

  public search() {
    this.betService.search().subscribe(
      data => {
        this.data = data.data;
      });
  }

  public expend(item){
    item.isExpanded = !item.isExpanded;
  }
}
