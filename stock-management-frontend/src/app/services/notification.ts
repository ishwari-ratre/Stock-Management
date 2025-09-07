import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export type NotificationType = 'success'|'error'|'info';
export interface NotificationMessage{
  message:string;
  type:NotificationType;
  duration?:number; //milliseconds
}

@Injectable({
  providedIn: 'root'
})
export class Notification {
  private notificationSubject = new Subject<NotificationMessage>();
  notifications$ = this.notificationSubject.asObservable();

  constructor() { }

  show(message: string, type: NotificationType = 'info', duration: number = 3000) {
    this.notificationSubject.next({ message, type, duration });
  }

  success(message: string, duration?: number) {
    this.show(message, 'success', duration);
  }

  error(message: string, duration?: number) {
    this.show(message, 'error', duration);
  }

  info(message: string, duration?: number) {
    this.show(message, 'info', duration);
  }
}
