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
    path: 'product',
    component: DetailProductComponent,
  },
  {
    path: 'users/detail',
    component: UserDetailComponent,
  },
];
