import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[account-add-table]',
})
export class AccountAddTableDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}
