import { CommonModule, NgOptimizedImage } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  NavigationEnd,
  Router,
  RouterLink,
  RouterOutlet,
} from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { PopupComponent } from './components/popup/popup.component';
import { CurrencyService } from './service/currency.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    NgOptimizedImage,
    RouterLink,
    FormsModule,
    ReactiveFormsModule,
    HeaderComponent,
    FooterComponent,
    PopupComponent,
    CommonModule,
  ],
  templateUrl: 'app.component.html',
})
export class AppComponent {
  showHeaderFooter: boolean = true;
  showPopup: boolean = true;
  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const currentUrl = this.router.url;
        this.showHeaderFooter = !(
          currentUrl === '/dang-ki' ||
          currentUrl === '/dang-nhap' ||
          currentUrl.startsWith('/quan-ly')
        );
        this.showPopup = !currentUrl.startsWith('/quan-ly');
      }
    });
  }
}
