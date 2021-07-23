import { Route } from '@angular/router';

import { SchedulerComponent } from './scheduler.component';

export const SCHEDULER_ROUTE: Route = {
  path: '',
  component: SchedulerComponent,
  data: {
    pageTitle: 'scheduler.title',
  },
};
