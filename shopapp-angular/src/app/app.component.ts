import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { defer } from 'rxjs';
import { NgOptimizedImage } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  // template: `Hello {{ city }} {{1+1}} `,
  imports: [
    RouterOutlet,
    NgOptimizedImage,
    RouterLink,
    FormsModule,
    ReactiveFormsModule,
    HeaderComponent,
    FooterComponent,
  ],
  templateUrl: 'app.component.html',
})
export class AppComponent {}
