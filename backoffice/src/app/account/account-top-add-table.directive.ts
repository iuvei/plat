import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[account-top-add-table]',
})
export class AccountTopAddTableDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}
