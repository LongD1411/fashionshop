import { CommonModule } from '@angular/common';
import { Component, Renderer2 } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { UserResponse } from '../responses/user/user.response';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './admin.component.html',
})
export class AdminComponent {
  userResponse: UserResponse | undefined;
  constructor(
    private renderer: Renderer2,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadScript('/assets/js/sb-admin-2.min.js');
    this.loadScript('/vendor/chart.js/Chart.min.js');
    this.loadScript('/assets/js/demo/chart-area-demo.js');
    this.loadScript('/assets/js/demo/chart-pie-demo.js');

    
    const token = localStorage.getItem('access_token');
    if(token){
      this.userService.getUserDetail().subscribe({
        next: (response)=>{this.userResponse = response.result}
      })
    }
  }

  loadScript(src: string) {
    const script = this.renderer.createElement('script');
    script.src = src;
    script.type = 'text/javascript';
    script.async = true;
    this.renderer.appendChild(document.body, script);
  }
}
