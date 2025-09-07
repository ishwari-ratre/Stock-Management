import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';   // ✅ import Auth

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  user = { email: '', password: '' };

  constructor(private auth: AuthService) {}

  onSubmit() {
    this.auth.register(this.user).subscribe({
      next: (res) => console.log('Registered successfully!', res),
      error: (err) => console.error('Error registering:', err)
    });
  }
}
