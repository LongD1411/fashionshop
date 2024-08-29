import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-image-upload',
  standalone: true,
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css'],
  imports: [CommonModule],
})
export class ImageUploadComponent {
  selectedImage: string | ArrayBuffer | null = null;

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    console.log(file);
    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (e) => {
        this.selectedImage = e.target!.result;
        console.log(e.target?.result);
      };
     
    }
  }
}
