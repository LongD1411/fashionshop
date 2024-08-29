import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule,RouterLink,RouterOutlet],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent {}
