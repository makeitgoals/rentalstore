import { Component, ViewChild } from '@angular/core';
import { SidebarComponent } from '@syncfusion/ej2-angular-navigations';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class Sidebar2Component {
  @ViewChild('sideBar') sideBar!: SidebarComponent;
  public showBackdrop = false;
  public closeOnDocumentClick = false;
  public type = 'Push';

  public onCreated(args: any): void {
    // if (this.sideBar) {
    this.sideBar.element.style.visibility = '';
    this.sideBar.dataBind();
    // }
  }

  public onItemClick(args: any): void {
    // if (Browser.isDevice) {
    //   this.sideBar?.hide();
    // }
    const elements: HTMLElement[] = args.currentTarget.parentElement.querySelectorAll('.active-item');
    elements.forEach(element => {
      if (element.classList.contains('active-item')) {
        element.classList.remove('active-item');
      }
    });
    args.currentTarget.classList.add('active-item');
  }
}
