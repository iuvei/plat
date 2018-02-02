import { NgModule } from '@angular/core';
import { Routes,
     RouterModule } from '@angular/router';

import { AgentPageComponent } from './agentpage.component';

const routes: Routes = [
  {
    path: '',
    component: AgentPageComponent,
    data: {
      title: 'New Asia Agent'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AgentPageRoutingModule {}
