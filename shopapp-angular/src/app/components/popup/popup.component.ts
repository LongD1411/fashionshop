import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-popup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './popup.component.html',
  styleUrl: './popup.component.css'
})
export class PopupComponent {
  isVisible = true;

  closeAdPopup() {
    this.isVisible = false;
  }
}
