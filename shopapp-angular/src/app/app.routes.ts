import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { OrderComponent } from './components/order/order.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { OderConfirmComponent } from './components/oder-confirm/oder-detail.component';
import { UserDetailComponent } from './components/user.detail/user.detail.component';
import { ProductComponent } from './components/product/product.component';
import { OrderSuccessComponent } from './components/order.success/order.success.component';
import { AdminComponent } from './admin/admin.component';
import { SizeComponent } from './admin/size/size.component';
import { SizeEditComponent } from './admin/size/size.edit.component';
import { BannerComponent } from './admin/banner/banner.component';
import { BannerEditComponent } from './admin/banner/banner.edit.component';
import { CategoryComponent } from './admin/category/category.component';
import { CategoryEditComponent } from './admin/category/category.edit.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'san-pham',
    component: ProductComponent,
  },
  {
    path: 'gio-hang',
    component: OrderComponent,
  },
  {
    path: 'thong-tin-don-hang',
    component: OrderSuccessComponent,
  },
  {
    path: 'xac-nhan',
    component: OderConfirmComponent,
  },
  {
    path: 'dang-ki',
    component: RegisterComponent,
  },
  {
    path: 'dang-nhap',
    component: LoginComponent,
  },
  {
    path: 'chi-tiet-san-pham/:id',
    component: DetailProductComponent,
  },
  {
    path: 'thong-tin-ca-nhan',
    component: UserDetailComponent,
    // canActivate: [AuthGuardFn],
  },
  {
    path: 'quan-ly',
    component: AdminComponent,
    children: [
      {
        path: 'size',
        component: SizeComponent,
      },
      {
        path: 'size/edit',
        component: SizeEditComponent,
      },
      {
        path: 'banner',
        component: BannerComponent,
      },
      {
        path: 'banner/edit',
        component: BannerEditComponent,
      },
      {
        path: 'category',
        component: CategoryComponent,
      },
      {
        path: 'category/edit',
        component: CategoryEditComponent,
      },
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
