import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { SCHEDULER_ROUTE } from './scheduler.route';
import { SchedulerComponent } from './scheduler.component';

// Imported syncfusion modules
import { ScheduleModule } from '@syncfusion/ej2-angular-schedule';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([SCHEDULER_ROUTE]),
    // Registering EJ2 Schedule Module
    ScheduleModule,
  ],
  declarations: [SchedulerComponent],
  exports: [SchedulerComponent],
})
export class SchedulerModule {}
