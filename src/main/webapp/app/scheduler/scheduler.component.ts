import { Component, OnInit, OnDestroy, ViewEncapsulation, ViewChild, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import {
  EventSettingsModel,
  RenderCellEventArgs,
  EventRenderedArgs,
  DayService,
  WeekService,
  WorkWeekService,
  MonthService,
  YearService,
  AgendaService,
  MonthAgendaService,
  TimelineViewsService,
  TimelineMonthService,
  TimelineYearService,
  DragAndDropService,
  ResizeService,
  ExcelExportService,
  ICalendarExportService,
  ICalendarImportService,
  View,
  ScheduleComponent,
} from '@syncfusion/ej2-angular-schedule';
import { extend, Internationalization } from '@syncfusion/ej2-base';
import { webinarData, eventsData, scheduleData } from './data';

import { ChangeEventArgs } from '@syncfusion/ej2-buttons';

import { Event } from './scheduler.model';

@Component({
  selector: 'jhi-scheduler',
  providers: [
    DayService,
    WeekService,
    WorkWeekService,
    MonthService,
    YearService,
    AgendaService,
    MonthAgendaService,
    TimelineViewsService,
    TimelineMonthService,
    TimelineYearService,
    DragAndDropService,
    ResizeService,
    ExcelExportService,
    ICalendarExportService,
    ICalendarImportService,
  ],
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SchedulerComponent implements OnInit, OnDestroy {
  @ViewChild('scheduleObj')
  public scheduleObj?: ScheduleComponent;
  account: Account | null = null;

  // eslint-disable-next-line @typescript-eslint/ban-types
  public data: any[] = <any[]>extend([], webinarData, '', true);
  // eslint-disable-next-line @typescript-eslint/ban-types
  public eventsData: any[] = <any[]>extend([], eventsData, '', true);
  // eslint-disable-next-line @typescript-eslint/ban-types
  public scheduleData: any[] = <any[]>extend([], scheduleData, '', true);
  // public data: Event[] = [
  //   {
  //     id: 2,
  //     Subject: 'Tulsi Meeting',
  //     StartTime: new Date(2021, 7, 24, 10, 0),
  //     EndTime: new Date(2021, 7, 24, 12, 30),
  //     isAllDay: false,
  //   },
  //   {
  //     Id: 3,
  //     Subject: 'Tulsi Spanned Meeting',
  //     StartTime: new Date(2021, 7, 23, 10, 0),
  //     EndTime: new Date(2021, 7, 24, 12, 30),
  //     isAllDay: false,
  //   },
  //   {
  //     Id: 4,
  //     Subject: 'Tulsi AllDay Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 5,
  //     Subject: 'Tulsi AllDay 2nd Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 6,
  //     Subject: 'Tulsi AllDay 3rd Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 7,
  //     Subject: 'Tulsi AllDay 4th Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 8,
  //     Subject: 'Tulsi AllDay 5th Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 9,
  //     Subject: 'Tulsi AllDay 6th Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 10,
  //     Subject: 'Tulsi AllDay 7th Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 11,
  //     Subject: 'Tulsi AllDay 8th Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  //   {
  //     Id: 12,
  //     Subject: 'Tulsi AllDay 9th Meeting',
  //     StartTime: new Date(2021, 7, 24, 0, 0),
  //     EndTime: new Date(2021, 7, 24, 24, 0),
  //     isAllDay: true,
  //   },
  // ];

  public temp: string =
    '<div class="tooltip-wrap">' +
    '<div class="image ${EventType}"></div>' +
    '<div class="content-area"><div class="name">${Subject}</></div>' +
    '${if(City !== null && City !== undefined)}<div class="city">${City}</div>${/if}' +
    '<div class="time">From&nbsp;:&nbsp;${StartTime.toLocaleString()} </div>' +
    '<div class="time">To&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;${EndTime.toLocaleString()} </div></div></div>';
  // Date for webinarData
  // public selectedDate: Date = new Date(2018, 1, 15);
  // Date for scheduleData
  // public selectedDate: Date = new Date(2019, 0, 10);
  // Date for Cell data for month view
  public selectedDate: Date = new Date(2017, 11, 15);
  public eventSettings: EventSettingsModel = {
    dataSource: this.data,
    enableTooltip: true,
    tooltipTemplate: this.temp,
    // fields: {
    //   id: 'Id',
    //   subject: { name: 'Subject' },
    //   isAllDay: { name: 'isAllDay' },
    //   startTime: { name: 'StartTime' },
    //   endTime: { name: 'EndTime' },
    // },
  };

  public currentView: View = 'Day';
  public enableAllDayScroll = false;
  public showWeekend = true;

  private readonly destroy$ = new Subject<void>();

  private instance: Internationalization = new Internationalization();

  constructor(private accountService: AccountService, private router: Router) {
    // , @Inject('sourceFiles') private sourceFiles: any
    // sourceFiles.files = ['scheduler.component.css'];
  }

  getTimeString(value: Date): string {
    return this.instance.formatDate(value, { skeleton: 'hm' });
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onChange(args: ChangeEventArgs): void {
    if (this.scheduleObj) {
      if (args.checked) {
        this.scheduleObj.eventSettings.enableTooltip = true;
      } else {
        this.scheduleObj.eventSettings.enableTooltip = false;
      }
      this.scheduleObj.dataBind();
    }
  }

  onTemplateChange(args: ChangeEventArgs): void {
    if (this.scheduleObj) {
      if (args.checked) {
        this.scheduleObj.eventSettings.tooltipTemplate = this.temp;
      } else {
        this.scheduleObj.eventSettings.tooltipTemplate = '';
      }
      this.scheduleObj.dataBind();
    }
  }
  // constructor(@Inject('sourceFiles') private sourceFiles: any) {
  //   sourceFiles.files = ['tooltip.style.css'];
  // }

  getCellContent(date: Date): string {
    if (date.getMonth() === 10 && date.getDate() === 23 && this.currentView === 'Month') {
      return '<img src="../../content/images/thanksgiving-day.svg" /><div class="caption">Thanksgiving day</div>';
    } else if (date.getMonth() === 11 && date.getDate() === 9 && this.currentView === 'Month') {
      return '<img src="../../content/images/get-together.svg" /><div class="caption">Party time</div>';
    } else if (date.getMonth() === 11 && date.getDate() === 13 && this.currentView === 'Month') {
      return '<img src="../../content/images/get-together.svg" /><div class="caption">Party time</div>';
    } else if (date.getMonth() === 11 && date.getDate() === 22 && this.currentView === 'Month') {
      return '<img src="../../content/images/birthday.svg" /><div class="caption">Happy birthday</div>';
    } else if (date.getMonth() === 11 && date.getDate() === 24 && this.currentView === 'Month') {
      return '<img src="../../content/images/christmas-eve.svg" /><div class="caption">Christmas Eve</div>';
    } else if (date.getMonth() === 11 && date.getDate() === 25 && this.currentView === 'Month') {
      return '<img src="../../content/images/christmas.svg" /><div class="caption">Christmas Day</div>';
    } else if (date.getMonth() === 0 && date.getDate() === 1 && this.currentView === 'Month') {
      return '<img src="../../content/images/newyear.svg" /><div class="caption">New Year"s Day</div>';
    } else if (date.getMonth() === 0 && date.getDate() === 14 && this.currentView === 'Month') {
      return '<img src="../../content/images/get-together.svg" /><div class="caption">Get together</div>';
    }
    return '';
  }

  getWeatherImage(value: Date): string {
    if (
      this.currentView === 'Week' ||
      this.currentView === 'WorkWeek' ||
      this.currentView === 'TimelineWeek' ||
      this.currentView === 'TimelineWorkWeek'
    ) {
      switch (value.getDay()) {
        case 0:
          return '<img class="weather-image" src="../../content/images/weather-clear.svg"/><div class="weather-text">25°C</div>';
        case 1:
          return '<img class="weather-image" src="../../content/images/weather-clouds.svg"/><div class="weather-text">18°C</div>';
        case 2:
          return '<img class="weather-image" src="../../content/images/weather-rain.svg"/><div class="weather-text">10°C</div>';
        case 3:
          return '<img class="weather-image" src="../../content/images/weather-clouds.svg"/><div class="weather-text">16°C</div>';
        case 4:
          return '<img class="weather-image" src="../../content/images/weather-rain.svg"/><div class="weather-text">8°C</div>';
        case 5:
          return '<img class="weather-image" src="../../content/images/weather-clear.svg"/><div class="weather-text">27°C</div>';
        case 6:
          return '<img class="weather-image" src="../../content/images/weather-clouds.svg"/><div class="weather-text">17°C</div>';
        default:
          return '';
      }
    }
    return '';
  }

  getDateHeaderText(value: Date): string {
    return this.instance.formatDate(value, { skeleton: 'Ed' });
  }

  OnRenderCell(args: RenderCellEventArgs): void {
    if (args.elementType === 'monthCells' && this.currentView === 'Month') {
      const ele: Element = document.createElement('div');
      if (args.date) {
        ele.innerHTML = this.getWeatherImage(args.date);
        if (ele.firstChild) {
          args.element.appendChild(ele.firstChild);
        }
      }
    }
  }

  oneventRendered(args: EventRenderedArgs): void {
    const categoryColor: string = args.data.CategoryColor as string;
    // if (!args.element || !categoryColor) {
    //   return;
    // }
    if (this.scheduleObj) {
      if (this.scheduleObj.currentView === 'Agenda') {
        (args.element.firstChild as HTMLElement).style.borderLeftColor = categoryColor;
      } else {
        args.element.style.backgroundColor = categoryColor;
      }
    }
  }
}

// setTimeout(()=>{
//   console.log("Stop");
// },5000)
