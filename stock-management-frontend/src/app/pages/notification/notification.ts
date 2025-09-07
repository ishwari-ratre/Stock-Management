import { Component } from '@angular/core';
import { CommonModule} from '@angular/common';
import { Notification, NotificationMessage } from '../../services/notification';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification.html',
  styleUrls: ['./notification.css']
})
export class NotificationComponent {
  messages: NotificationMessage[] = [];

  constructor(private notification: Notification) {
    this.notification.notifications$.subscribe((msg: any) => {
      this.addMessage(msg);
    });
  }

  addMessage(msg: NotificationMessage) {
    this.messages.push(msg);
    // Remove message after duration
    setTimeout(() => {
      this.messages = this.messages.filter(m => m !== msg);
    }, msg.duration || 3000);
  }
}
