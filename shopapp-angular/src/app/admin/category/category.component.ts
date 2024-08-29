import { Component, OnInit } from '@angular/core';
import { CategoryResponse } from '../../responses/category/category.respones';
import { CategoryService } from '../../service/category.service';
import { enviroment } from '../../enviroments/enviroment';
import { SweetAlertService } from '../../service/sweet-alert.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css',
})
export class CategoryComponent implements OnInit {
  categories: CategoryResponse[] = [];
  constructor(private categoryService: CategoryService,private alert: SweetAlertService) {}
  ngOnInit(): void {
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        this.categories = response;
        this.categories.map((item) => {
          if(item.thumbnail){
            item.thumbnail = `${enviroment.apiImage}/${item.thumbnail}`;
          }
          return item;
        });
        console.log(this.categories)
      },
      error:(respone)=> {
          this.alert.showError("Không thể lấy dữ liệu")
      },
    });
  }
}
