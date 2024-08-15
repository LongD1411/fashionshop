import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CategoryResponse } from '../../responses/category/category.respones';
import { CategoryService } from '../../service/category.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './header.component.html',
})
export class HeaderComponent {}
