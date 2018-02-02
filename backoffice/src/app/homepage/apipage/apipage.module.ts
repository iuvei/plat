import { NgModule } from '@angular/core';
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { DropdownModule } from 'ng2-bootstrap/dropdown';

import { ApiPageComponent } from './apipage.component';
import { ApiPageRoutingModule } from './apipage-routing.module';

@NgModule({
  imports: [
    ApiPageRoutingModule,
    ChartsModule,
    DropdownModule
  ],
  declarations: [ ApiPageComponent ]
})
export class ApiPageModule { }
